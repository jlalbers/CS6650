/**
 * Skeleton socket client.
 * Accepts host/port on command line or defaults to localhost/12031
 * Then starts MAX_Threads and waits for them all to terminate before terminating main()
 * @author Ian Gorton
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientMultithreaded {

    static CyclicBarrier barrier;

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException  {
        String hostName;
        final int MAX_THREADS = 50 ;
        int port;

        if (args.length == 2) {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            hostName= null;
            port = 12031;  // default port in SocketServer
        }
        //initialization of barrier for the threads
        barrier = new CyclicBarrier(MAX_THREADS+1);
        long start = System.nanoTime();
        //create and start MAX_THREADS SocketClientThread
        for (int i=0; i< MAX_THREADS; i++){
            new SocketClientThread(hostName, port, barrier).start();
        }

        //wait for all threads to complete
        barrier.await();
        System.out.println("Wall time: for " + MAX_THREADS + ": " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - start,
                TimeUnit.NANOSECONDS) + " ms");

        System.out.println("Terminating ....");

    }
}

