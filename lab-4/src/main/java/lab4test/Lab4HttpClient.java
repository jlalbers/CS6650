package lab4test;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * HTTP client class for Lab 3.
 */
public class Lab4HttpClient {

    /**
     * Generates a new CloseableHttpClient for requests.
     * @return new instance of CloseableHttpClient
     */
    private CloseableHttpClient generateClient() {
        return HttpClients.createDefault();
    }

    /**
     * Closes a CloseableHttpClient with exception-handling.
     * @param client CloseableHttpClient to close
     */
    private boolean closeClient(CloseableHttpClient client) {
        try {
            client.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int runPostTest(CloseableHttpClient httpClient, String IP, ) {
        // Create POST request object
        String uri = IP + ":8080/"
        HttpGet httpPost = new HttpPost();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // Get response code
            int code = response.getCode();
            // Close request stream
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            // Return HTTP request conde on successful completion
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            // Return -1 if there was an error with the request
            return -1;
        }
    }

    /**
     * Records the start time, response code, and latency of a {@link #runGetTest test request},
     * then writes the result to a CSV file using the {@link #writeResults writeResults} method.
     * @param client client that will send the test request
     * @param start start time of the test calling this method
     * @param file CSV file where the test record will be appended
     */
    public void instrumentedRequest(CloseableHttpClient client, long start, File file, int ) {
        // Record request start time
        long startTime = System.nanoTime() - start;
        long beginTime = System.nanoTime();
        // Record response code of request
        int code = runGetTest(client);
        // Record latency
        long latency = System.nanoTime() - beginTime;
        // Write request results
        try {
            writeResults(file, startTime, code, latency);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Thread instrumentedThread(int numRequests, int startSkierID, int endSkierID, int liftRange,
                                          int startTime, int endTime, CountDownLatch latch,) {
        Runnable doPost = () -> {
            CloseableHttpClient client = generateClient();
            Random random = new Random();
            for (int i = 0; i < numRequests; i++) {

                instrumentedRequest(client);
            }
        };
    }

    public void instrumentedTest(int numThreads, int numSkiers, int numLifts, int numRuns, String IP) {
        // Phase 1 --------------------------------------------------------------------------------
        System.out.println("Beginning Phase 1\n");
        List<Thread> phase1Threads = new ArrayList<>(); // List to keep track of our threads for Phase 1
        int phase1ThreadCount = numThreads / 4; // Number of threads to spawn for Phase 1
        CountDownLatch phase1Gate = new CountDownLatch(phase1ThreadCount / 5); // Latch for when
        int phase1Skiers = numSkiers / phase1ThreadCount; // Number of skiers per thread
        int numRequests = (numRuns / 5) * phase1Skiers;

        for (int i = 0; i <  numThreads / 4; i++) {
            int skierStart = 1 + phase1Skiers * i;
            int skierEnd = Math.min(phase1Skiers * (i + 1), numSkiers);
            phase1Threads.add(instrumentedThread(

                    );
        }
        for (Thread thread : phase1Threads) {
            thread.start();
        }

    }

}
