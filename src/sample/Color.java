package sample;

/**
 * Represents a color channel, i.e. red , green blue or alpha.
 */
public enum Color {
    ALPHA(24, 0xff000000),
    RED  (16, 0x00ff0000),
    GREEN( 8, 0x0000ff00),
    BLUE ( 0, 0x000000ff);

    private int bits;
    private int bitmask;

    Color(int bits, int bitmask) {
        this.bits = bits;
        this.bitmask = bitmask;
    }

    /** Returns the number of bits to shift to get the value of the corresponding color component */
    public int getBits() {
        return this.bits;
    }

    /** Return the bit mask that zeros all other color components */
    public int getBitmask() {
        return bitmask;
    }
}