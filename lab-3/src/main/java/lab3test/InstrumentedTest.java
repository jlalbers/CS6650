package lab3test;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class InstrumentedTest {
    public static void main(String[] args) {
        // Validate IP address
        if (args.length == 1) {
            String ip = args[0];
            // Split IP from dotted decimal
            String[] validateIp = ip.split("\\D");
            for (String s : validateIp) {
                // Parse each byte of IP
                int num = Integer.parseInt(s);
                if (num > 255 || num < 0) {
                    System.out.println("Invalid target IP address. Terminating test.");
                    return;
                }
            }
            // GET URL
            String url = "http://" + ip + ":8080/lab-3_war/skiers/1/seasons/2019/days/366/skiers/123";
            System.out.println("Beginning instrumented test of target " + url + "\n");
            // Generate new client
            Lab3HttpClient client = new Lab3HttpClient(url);
            CloseableHttpClient testClient = client.generateClient();
            // Send test request to ensure good connection
            int testCode = client.runGetTest(testClient);
            // Accept response code of 200
            if (testCode != 200) {
                System.out.println("Invalid response from target IP. Terminating test.");
                return;
            }

            System.out.println("IP validated.\n");
            System.out.println("Testing baseline single-thread throughput:\n");

            // Get baseline throughput for Little's Law
            int littleRequests = 100;
            long littleStart = System.nanoTime();
            client.runMultipleGet(client.generateClient(),littleRequests, false);
            double littleRuntime = (double)(System.nanoTime() - littleStart) / 1000000;
            System.out.println("Total time for "+littleRequests+" requests on one thread: " + littleRuntime + " ms");
            System.out.println("Average latency per request: " + littleRuntime / littleRequests + " ms");
            System.out.println("Average throughput: " + (double)littleRequests / littleRuntime * 1000 + " reqs/s");

            // Begin multithreaded tests
            System.out.println("\nStarting multithreaded testing.\n");
            for (int i = 32; i < 257; i *= 2) {
                System.out.println("\nRunning multithreaded test with " + i + " threads:");
                client.instrumentedTest(i, 1000);
                System.out.println("Test complete.");
            }
            System.out.println("\nCalculating test metrics:");
            String cwd = System.getProperty("user.dir");
            ProcessBuilder processBuilder = new ProcessBuilder("python3", cwd + "/res/stats_script.py");
            processBuilder.redirectErrorStream(true);
            try {
                Process process = processBuilder.inheritIO().start();
                process.waitFor(5, TimeUnit.SECONDS);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (args.length > 1){
            System.out.println("Program only accepts one argument. Please retry with IP address as argument. " +
                    "Terminating test.");
        } else {
            System.out.println("Program must accept an IP address as an input. Terminating test.");
        }
    }
}
