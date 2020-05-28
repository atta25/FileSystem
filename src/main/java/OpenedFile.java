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
        buffer.limit(bytesRead);
    }

    public void writeSync(Buffer buffer) {
        lowLevelFileSystem.syncWriteFile(fileDescriptor, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
    }

    public void readAsync(Buffer buffer) {
        lowLevelFileSystem.asyncReadFile(fileDescriptor,
                                        buffer.getBytes(),
                                        buffer.getStart(),
                                        buffer.getEnd(), bytesRead -> {
                                        buffer.limit(bytesRead);
                                        });
    }
}
