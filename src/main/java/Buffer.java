public class Buffer {
    private byte[] bytes;
    private int start;
    private int end;

    public Buffer(int start, int end, int size) {
        bytes = new byte[size];
        this.start = start;
        this.end = end;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void limit(int bytesRead) {
        end = start + bytesRead - 1;
    }
}
