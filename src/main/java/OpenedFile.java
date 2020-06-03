public class OpenedFile {
    private int fileDescriptor;
    private LowLevelFileSystem lowLevelFileSystem;

    public OpenedFile(int fileDescriptor, LowLevelFileSystem lowLevelFileSystem) {
        this.fileDescriptor = fileDescriptor;
        this.lowLevelFileSystem = lowLevelFileSystem;
    }

    public void close() {
        lowLevelFileSystem.closeFile(fileDescriptor);
    }

    public void readSync(Buffer buffer) {
        int bytesRead = lowLevelFileSystem.syncReadFile(fileDescriptor, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
        validateBytesRead(bytesRead);
    }

    public void writeSync(Buffer buffer) {
        lowLevelFileSystem.syncWriteFile(fileDescriptor, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
    }

    public void readAsync(Buffer buffer) {
        lowLevelFileSystem.asyncReadFile(fileDescriptor,
                                        buffer.getBytes(),
                                        buffer.getStart(),
                                        buffer.getEnd(), bytesRead -> {
                                        validateBytesRead(bytesRead);
                                        });
    }

    public void readAndWriteComplete(Buffer buffer) {
        int bytesRead = lowLevelFileSystem.syncReadFile(fileDescriptor, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
        Buffer bufferToWrite = new Buffer(0, bytesRead);
        writeSync(bufferToWrite);

        while (buffer.getEnd() == bytesRead) {
            bytesRead = lowLevelFileSystem.syncReadFile(fileDescriptor, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
            writeSync(bufferToWrite);
        }
    }

    private void validateBytesRead(int bytesRead) {
        if (bytesRead <= 0) {
            throw new ReadFileException();
        }
    }
}
