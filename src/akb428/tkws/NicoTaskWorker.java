package akb428.tkws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import akb428.tkws.config.Application;
import akb428.tkws.dao.FactoryMediaUrlDao;
import akb428.tkws.dao.IMediaUrlDao;
import akb428.tkws.thread.MediaDownloderThread;

public class NicoTaskWorker {

	public static void main(String[] args) throws ClassNotFoundException, UnsupportedEncodingException, IOException, InterruptedException, SQLException {

		if (args.length == 1) {
			new Application(args[0]);
		} else {
			new Application();
		}

		IMediaUrlDao dao = FactoryMediaUrlDao.create();

		MediaDownloderThread mediaDownloderThread = new MediaDownloderThread();
		mediaDownloderThread.start();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(SendTaskToWorkerStatusAdapter.QUEUE_NAME, false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);

		channel.basicConsume(SendTaskToWorkerStatusAdapter.QUEUE_NAME, true, consumer);

		String path = null;
		String url = null;
		String text = null;
		String userName = null;
		MessagePack msgpack = new MessagePack();
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			List<String> byteData = msgpack.read(delivery.getBody(), Templates.tList(Templates.TString));

			path = byteData.get(0);
			url = byteData.get(1);
			text = byteData.get(2);
			userName = byteData.get(3);
			System.out.println(url);
			TaskModel taskModel = new TaskModel(path, url, text, userName);
			doWork(dao, taskModel);
		}

	}

	private static void doWork(IMediaUrlDao dao, TaskModel taskModel) throws InterruptedException {
		if (!dao.isExistUrl(taskModel.url)) {
			System.out.println("DB regist");
			dao.registUrl(taskModel.path, taskModel.url, taskModel.text, taskModel.userName);
		} else {
			System.out.println("duplicate media url");
		}
	}
}

class TaskModel {

	String path = null;
	String url = null;
	String text = null;
	String userName = null;

	public TaskModel(String path, String url, String text, String userName) {
		this.path = path;
		this.url = url;
		this.text = text;
		this.userName = userName;
	}
}
