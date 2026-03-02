class RectangleJava {
    private final int height;
    private int width;

    public RectangleJava(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

}