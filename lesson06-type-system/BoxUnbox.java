

class BoxUnbox {
    public static void main() {
        int x = 0;
        Integer i = Integer.valueOf(x);  // Box
        int y = i.intValue();            // Unbox

    }
}