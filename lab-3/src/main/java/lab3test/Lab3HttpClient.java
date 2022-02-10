package lab3test;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * HTTP client class for Lab 3.
 */
public class Lab3HttpClient {
    private final String url;

    /**
     * Constructor for lab3test.Lab3HttpClient.
     * @param url request target URL
     */
    public Lab3HttpClient(String url) {
        this.url = url;
    }

    /**
     * Default constructor. Defaults to retrieving public IP address.
     */
    public Lab3HttpClient() {
        this("ifconfig.me");
    }

    /**
     * Generates a new CloseableHttpClient for requests.
     * @return new instance of CloseableHttpClient
     */
    public CloseableHttpClient generateClient() {
        return HttpClients.createDefault();
    }

    /**
     * Closes a CloseableHttpClient with exception-handling.
     * @param client CloseableHttpClient to close
     */
    public void closeClient(CloseableHttpClient client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends multiple GET requests from an initialized CloseableHttpClient. Can only send requests to the URL
     * specified by the lab3test.Lab3HttpClient invoker. Attempts to close the client on completion.
     * @param client CloseableHttpClient with which to send requests. This method will attempt to close the client
     *               upon completion.
     * @param n number of requests that will be sent before the client is closed.
     * @param verbose determines whether the response code and message body will be printed. If true, the response
     *                will be printed. If false, this information will be suppressed.
     */
    public void runMultipleGet(CloseableHttpClient client, int n, boolean verbose) {
        // Create GET request object
        HttpGet httpGet = new HttpGet(url);
        // Send one request per loop iteration
        for (int i = 0; i < n; i++) {
            // Execute GET request
            try (CloseableHttpResponse response1 = client.execute(httpGet)) {
                // Print response code if verbose
                if (verbose) System.out.println(response1.getCode());
                // Get message body
                HttpEntity entity1 = response1.getEntity();
                // Print message body if verbose
                if (verbose) System.out.println(EntityUtils.toString(entity1));
                // Close stream
                EntityUtils.consume(entity1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Close client
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs one GET request and returns the response code. Used for instrumented testing where a code is needed for
     * each request. Client will not be closed upon completion.
     * @param httpClient client from which to send requests. Client will remain open upon completion.
     * @return the HTTP response code of the response. -1 is returned if an error is encountered and a response code
     * is not sent.
     */
    public int runGetTest(CloseableHttpClient httpClient) {
        // Create GET request object
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response1 = httpClient.execute(httpGet)) {
            // Get response code
            int code = response1.getCode();
            // Close request stream
            HttpEntity entity1 = response1.getEntity();
            EntityUtils.consume(entity1);
            // Return HTTP request conde on successful completion
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            // Return -1 if there was an error with the request
            return -1;
        }
    }

    /**
     * Synchronized, private method to append a request test result to a CSV log. Results are in the format <i>Start
     * Time (ns),Request Type,Latency (ns),Response Code</i>. Synchronization of this method ensures each response
     * will be recorded without race conditions while ensuring that any imposed waits will not factor into the latency.
     * @param csvfile CSV File object onto which the results are appended
     * @param start start time, as measured from the beginning of the test in nanoseconds
     * @param code HTTP response code received by the client
     * @param latency time between when the request is sent and a response is received
     * @throws IOException if an error is encountered while closing the FileWriter
     */
    synchronized private void writeResults(File csvfile, long start, int code, long latency) throws IOException {
        // Open writer to append record to end of file
        FileWriter fstream = new FileWriter(csvfile, true);
        PrintWriter csvwriter = new PrintWriter(fstream);
        // Condense record as a single CSV line
        String line = start + ",Get," + latency + "," + code;
        // Write line to file
        csvwriter.println(line);
        // Close writers
        csvwriter.close();
        try {
            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Records the start time, response code, and latency of a {@link #runGetTest test request},
     * then writes the result to a CSV file using the {@link #writeResults writeResults} method.
     * @param client client that will send the test request
     * @param start start time of the test calling this method
     * @param file CSV file where the test record will be appended
     */
    public void instrumentedRequest(CloseableHttpClient client, long start, File file) {
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

    /**
     * Executes a test of the latency and response codes of requests sent to the servlet. Each thread will send
     * requests sequentially. Test record will be saved as "threadlog[#threads].csv.
     * @param numThreads number of threads to use for the test
     * @param numRequests number of requests to send per thread
     */
    public void instrumentedTest(int numThreads, int numRequests) {
        // Write new record file/overwrite existing record file
        File csv = new File("res/threadlog" + numThreads + ".csv");
        try {
            FileWriter overWrite = new FileWriter(csv);
            // Write CSV header row
            overWrite.write("Start,Type,Latency,Code\n");
            overWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // Create countdown latch matching the number of threads
        CountDownLatch latch = new CountDownLatch(numThreads);
        // Record test start time
        final long start = System.nanoTime();
        // Create runnable that will execute the test on each thread
        Runnable client = () -> {
            // Generate a new client per thread
            CloseableHttpClient httpClient = generateClient();
            // Generate the requests
            for (int i = 0; i < numRequests; i++) {
                instrumentedRequest(httpClient, start, csv);
            }
            try {
                // Close the client
                httpClient.close();
                // Decrement latch
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
                // Decrement latch if error is encountered closing the client
                latch.countDown();
            }
        };
        // Spawn threads
        for (int i = 0; i < numThreads; i++) {
            new Thread(client).start();
        }
        // Wait for latch to reach 0 before terminating
        try {
            latch.await(600, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
