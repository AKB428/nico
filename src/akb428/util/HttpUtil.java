package akb428.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class HttpUtil {

	public static void download(String url, String path) throws IOException {
		URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos =
            new FileOutputStream(FileUtil.createPath(path, urlLastElement(url)));
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
	}

	/**
	 * URLの最後の要素を返します。
	 * http://www.gooo.co/uploder/01.jpg の場合 01.jpg
	 * 空の場合(例えば http://google.co.jp/)、index.htmlを返します。
	 * @param url URLアドレス
	 * @return URLアドレスの最後の要素
	 */
	public static String urlLastElement(String url) {

		// index.html対策
		int lashSlashIndex = url.lastIndexOf("/");
		if (lashSlashIndex == url.length() - 1) {
			return "index.html";
		}

		String[] strings = url.split("/");
		return strings[strings.length - 1];
	}
}
