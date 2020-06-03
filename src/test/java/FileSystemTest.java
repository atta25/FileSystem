import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class FileSystemTest {
    private LowLevelFileSystem lowLevelFileSystem;
    private OpenedFile openedFile;
    private static final int fileDescriptor = 10001;

    @Before
    public void setup() {
        lowLevelFileSystem = mock(LowLevelFileSystem.class);
        openedFile = new OpenedFile(fileDescriptor, lowLevelFileSystem);
    }

    @Test
    public void close() {
        openedFile.close();
        verify(lowLevelFileSystem, times(1)).closeFile(fileDescriptor);
    }

    @Test(expected = ReadFileException.class)
    public void readFields() {
        when(lowLevelFileSystem.syncReadFile(eq(fileDescriptor), any(byte[].class), eq(0), eq(4))).thenReturn(3);
        when(lowLevelFileSystem.syncReadFile(eq(fileDescriptor), any(byte[].class), eq(4), eq(5))).thenReturn(1);
        when(lowLevelFileSystem.syncReadFile(eq(fileDescriptor), any(byte[].class), eq(4), eq(5))).thenReturn(0);

        Buffer bufferForFirstField = new Buffer(0, 4);
        openedFile.readSync(bufferForFirstField);

        Buffer bufferForSecondField = new Buffer(4, 5);
        openedFile.readSync(bufferForSecondField);

        Buffer bufferForThirdField = new Buffer(5, 10);
        openedFile.readSync(bufferForThirdField);
    }

    @Test
    public void writeFields() {
        Buffer bufferForFirstField = new Buffer(0, 4);
        openedFile.writeSync(bufferForFirstField);

        Buffer bufferForSecondField = new Buffer(4, 5);
        openedFile.writeSync(bufferForSecondField);

        Buffer bufferForThirdField = new Buffer(5, 10);
        openedFile.writeSync(bufferForThirdField);

        verify(lowLevelFileSystem, times(3)).syncWriteFile(eq(fileDescriptor), any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void whenIWantToReadAn80BytesFileInBlocksOf30ShouldReadAndWrite3Times() {
        Buffer buffer = new Buffer(0, 29);
        when(lowLevelFileSystem.syncReadFile(eq(fileDescriptor), any(byte[].class), anyInt(), anyInt())).thenReturn(29,29, 19);

        openedFile.readAndWriteComplete(buffer);

        verify(lowLevelFileSystem, times(3)).syncReadFile(eq(fileDescriptor), any(byte[].class), anyInt(), anyInt());
        verify(lowLevelFileSystem, times(3)).syncWriteFile(eq(fileDescriptor), any(byte[].class), anyInt(), anyInt());
    }
}
