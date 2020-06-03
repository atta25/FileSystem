public class Buffer {
    private byte[] bytes;
    private int start;
    private int end;

    public Buffer(int start, int end) {
        bytes = new byte[end - start];
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
}
