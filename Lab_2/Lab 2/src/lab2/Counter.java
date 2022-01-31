package lab2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * lab2.Counter class for Lab 2 Step 1.
 */

public class Counter {
    private int n;

    /**
     * Constructor for lab2.Counter object.
     * @param n The number of threads to w
     */
    public Counter(int n) {
        this.n = n;
    }

    /**
     * Constructor method with default of 1000 threads.
     */
    public Counter() {
        this(1000);
    }

    public void execute() {
        AtomicInteger count = new AtomicInteger();

        Runnable runnable = () -> {
                IntStream.range(0, 10)
                        .forEach(i -> count.incrementAndGet());
        };

        ExecutorService executor = Executors.newFixedThreadPool(this.n);

        long start = System.nanoTime();
        try {
            IntStream.range(0, this.n)
                    .forEach(i -> executor.submit(runnable));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        executor.shutdown();

        double runtime = (double)(System.nanoTime() - start) / 1000000.0;
        System.out.printf("Executor Thread Pool Runtime: %f ms\n", runtime);
        System.out.printf("Count: %d\n\n", count.get());
    }

    public void executeSimple() {
        AtomicInteger count = new AtomicInteger();

        Runnable runnable = () -> {
            IntStream.range(0, 10)
                    .forEach(i -> count.incrementAndGet());
        };

        long start = System.nanoTime();

        IntStream.range(0, this.n)
                .forEach(i -> new Thread(runnable).start());

        while (count.get() < this.n * 10) {
            continue;
        }

        double runtime = (double)(System.nanoTime() - start) / 1000000.0;
        System.out.printf("Sequential Thread Spawn Runtime: %f ms\n", runtime);
        System.out.printf("Count: %d\n\n", count.get());
    }
}
