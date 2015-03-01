package akb428.util;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;


public class RabbitMQ {

	private final static String QUEUE_NAME = "development.send_object_strage";

	public static void send(String dest_path, String src_path) throws java.io.IOException {
		 ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    
		    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		    
		    //'{"dest_path":"/test", "src_path":"./private/EBjHCeH.jpg"}'
		    String message = String.format("{\"dest_path\":\"%s\", \"src_path\":\"%s\"}", dest_path, src_path);
		    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		    System.out.println(" [x] Sent '" + message + "'");
		    
		    channel.close();
		    connection.close();

	}

}
