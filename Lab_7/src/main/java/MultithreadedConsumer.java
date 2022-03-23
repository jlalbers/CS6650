import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class MultithreadedConsumer {

    public static void main(String[] args) {
        int numThreads = 1;
        for (String arg : args) {
            if (arg.startsWith("--numThreads=")) {
                numThreads = Integer.parseInt(arg.substring(13));
            }
        }
        LiftRideDao dbDao = new LiftRideDao();
        dbDao.createLiftRideTable();
        System.out.println(" [x] DB connection successful, initialized LiftRides table");
        System.out.println(" [x] Running consumer with " + numThreads + " threads");
        List<Thread> threads = new ArrayList<>();
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            for (int i = 0; i < numThreads; i++) {
                Runnable runnable = () -> {
                    try {
                        Channel channel = connection.createChannel();
                        channel.queueDeclare("test", false, false, false, null);

                        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                            String message = new String(delivery.getBody(), "UTF-8");
                            try {
                                LiftRide liftRide = new LiftRide(message);
                                dbDao.createLiftRide(liftRide);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            System.out.println(" [x] Received '" + message + "'");
                        };
                        channel.basicConsume("", true, deliverCallback, consumerTag -> { });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                Thread thread = new Thread(runnable);
                threads.add(thread);
                thread.start();
            }
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
