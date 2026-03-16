class Constants {
    public static final int BITS_OF_10KB = 8 * 10 * 1024;
    public static final int BITS_OF_5KB = calcBitsOfKb(5);

    public static int calcBitsOfKb(int words)  {
        return words * 8 * 1024;
    }
}