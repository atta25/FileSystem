public class HighLevelFileSystem {
    private LowLevelFileSystem lowLevelFileSystem;

    public HighLevelFileSystem(LowLevelFileSystem lowLevelFileSystem) {
        this.lowLevelFileSystem = lowLevelFileSystem;
    }

    public OpenedFile open(String path) {
        int fileDescriptor = lowLevelFileSystem.openFile(path);
        return new OpenedFile(fileDescriptor, lowLevelFileSystem);
    }
}
