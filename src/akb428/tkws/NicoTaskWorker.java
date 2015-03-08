package akb428.tkws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import akb428.tkws.dao.IMediaUrlDao;
import akb428.tkws.dao.mariadb.MediaUrlDao;
import akb428.tkws.thread.MediaDownloderThread;

public class NicoTaskWorker {

	public static Properties applicationProperties = null;

	public static void main(String[] args) throws ClassNotFoundException, UnsupportedEncodingException, IOException, InterruptedException {

		String configFile = "./config/application.properties";

		if (args.length == 1) {
			configFile = args[0];
		}
		InputStream inStream = new FileInputStream(configFile);
		applicationProperties = new Properties();
		applicationProperties.load(new InputStreamReader(inStream, "UTF-8"));

		// TODO　設定ファイルでDB切り替え
		Class.forName("org.mariadb.jdbc.Driver");

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
			// TODO keywordを保存したいがここでは取得できないため一時的にtextをそのまま保存
			// idはインクリメントで自動払い出し
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
