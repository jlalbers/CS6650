package lab2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CollectionsTest {
    private int n;

    public CollectionsTest(int n) {
        this.n = n;
    }

    public CollectionsTest() {
        this(1000);
    }

    private double threadTest(Runnable r) {
        long start = System.nanoTime();
        Thread thread = new Thread(r);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (double)(System.nanoTime() - start) / 1000000.0;
    }

    private double concurrentThreadTest(Runnable r) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        long start = System.nanoTime();
        IntStream.range(0, this.n)
                        .forEach(i -> executor.submit(r));
        executor.shutdown();
        while (!executor.isTerminated()) {
            continue;
        }
        return (double)(System.nanoTime() - start) / 1000000.0;
    }

    public void test1() {
        Vector<Integer> vector = new Vector<>();
        ArrayList<Integer> arrayList = new ArrayList<>();

        Runnable runOnVector = () -> IntStream.range(0, this.n)
                .forEach(vector::add);
        Runnable runOnArrayList = () -> IntStream.range(0, this.n)
                .forEach(arrayList::add);

        System.out.printf("Synchronized Vector\nRuntime: %f\nElements Added: %d\n\n",
                threadTest(runOnVector), vector.size());
        System.out.printf("Unsynchronized ArrayList\nRuntime: %f\nElements Added: %d\n\n",
                threadTest(runOnArrayList), arrayList.size());
    }

    public void test2() {
        Hashtable<Integer,Integer> hashTable = new Hashtable<>();
        HashMap<Integer,Integer> hashMap = new HashMap<>();
        ConcurrentHashMap<Integer,Integer> concurrentHashMap = new ConcurrentHashMap<>();

        Runnable runHashtable = () -> IntStream.range(0, this.n)
                .forEach(i -> hashTable.put(i, i));
        Runnable runHashMap = () -> IntStream.range(0, this.n)
                .forEach(i -> hashMap.put(i, i));
        Runnable runConcurrentHashMap = () -> IntStream.range(0, this.n)
                .forEach(i -> concurrentHashMap.put(i, i));

        System.out.printf("Synchronized Hashtable\nRuntime: %f\nElements Added: %d\n\n",
                threadTest(runHashtable), hashTable.size());
        System.out.printf("Unsynchronized HashMap\nRuntime: %f\nElements Added: %d\n\n",
                threadTest(runHashMap), hashMap.size());
        System.out.printf("Synchronized ConcurrentHashMap\nRuntime: %f\nElements Added: %d\n\n",
                threadTest(runConcurrentHashMap), concurrentHashMap.size());

        hashTable.clear();
        concurrentHashMap.clear();

        Map<Integer,Integer> safeHashMap = Collections
                .synchronizedMap(new HashMap<>());

        Random random = new Random();

        Runnable runMultiHashtable = () -> hashTable.put(random.nextInt(), 1);
        Runnable runMultiHashMap = () -> safeHashMap.put(random.nextInt(), 1);
        Runnable runMultiConcurrentHashMap = () -> concurrentHashMap.put(random.nextInt(), 1);

        double result1 = concurrentThreadTest(runMultiHashtable);
        double result2 = concurrentThreadTest(runMultiHashMap);
        double result3 = concurrentThreadTest(runMultiConcurrentHashMap);

        System.out.printf("Synchronized Multithread Hashtable\nRuntime: %f\nElements Added: %d\n\n",
                result1, hashTable.size());
        System.out.printf("Unsynchronized Multithread HashMap\nRuntime: %f\nElements Added: %d\n\n",
                result2, safeHashMap.size());
        System.out.printf("Synchronized Multithread ConcurrentHashMap\nRuntime: %f\nElements Added: %d\n\n",
                result3, concurrentHashMap.size());

    }
}
