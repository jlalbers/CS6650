package tutorial.workqueues2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Random;

public class NewTask {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            boolean durable = true;
            channel.queueDeclare("task_queue", durable, false, false, null);
            String message = "Message: ";
            int randPeriods = random.nextInt(10) + 1;
            for (int i = 0; i < randPeriods; i++) {
                message += ".";
            }

            channel.basicPublish("", "task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

    }
}
