package akb428.tkws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import akb428.tkws.config.Application;
import akb428.tkws.dao.IMediaUrlDao;
import akb428.tkws.dao.mariadb.MediaUrlDao;
import akb428.tkws.thread.MediaDownloderThread;

public class NicoTaskWorker {

	public static void main(String[] args) throws ClassNotFoundException, UnsupportedEncodingException, IOException, InterruptedException {

		if (args.length == 1) {
			new Application(args[0]);
		} else {
			new Application();
		}

		MediaDownloderThread mediaDownloderThread = new MediaDownloderThread();
		mediaDownloderThread.start();

		IMediaUrlDao dao = new MediaUrlDao();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(SendTaskToWorkerStatusAdapter.QUEUE_NAME, false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);

		channel.basicConsume(SendTaskToWorkerStatusAdapter.QUEUE_NAME, true, consumer);

		String url = null;
		String text = null;
		String userName = null;
		MessagePack msgpack = new MessagePack();
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			List<String> byteData = msgpack.read(delivery.getBody(), Templates.tList(Templates.TString));

			url = byteData.get(1);
			text = byteData.get(2);
			userName = byteData.get(3);
			System.out.println(url);
			TaskModel taskModel = new TaskModel(url, text, userName);
			doWork(dao, taskModel);
		}

	}

	private static void doWork(IMediaUrlDao dao, TaskModel taskModel) throws InterruptedException {

		// DAO Start
		if (!dao.isExistUrl(taskModel.url)) {
			System.out.println("DB regist");
			dao.registUrl(taskModel.url, taskModel.text, taskModel.userName);
		}
	}
}

class TaskModel {

	String url = null;
	String text = null;
	String userName = null;

	public TaskModel(String url, String text, String userName) {
		this.url = url;
		this.text = text;
		this.userName = userName;
	}
}
