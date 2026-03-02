// App.java
public class App {
    public static void main(String[] args) {
        X.bar();

    }

}

class X {
    private int a;

    private static int b;

    public void foo(int i) {
        a = i;
        new Z().print();

    }
    public static void bar() {

        a = 0;
        new Z().print();

    }
}