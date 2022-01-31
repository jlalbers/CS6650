import lab2.CollectionsTest;
import lab2.Counter;

public class Main {
    public static void main(String[] args) {
        for (int i = 1; i < 10001; i *= 10) {
            System.out.println("Number of Threads: " +  i + "\n");
            Counter counter = new Counter(i);
            counter.executeSimple();
            counter.execute();
        }

        for (int i = 1; i < 1000001; i *= 100) {
            System.out.println("Number of Threads: " +  i + "\n");
            CollectionsTest collections = new CollectionsTest(i);
            collections.test1();
            collections.test2();
        }
    }
}
