package HttpServer;

import java.io.InputStream;
import java.io.File;
import java.io.IOException;

public class HttpRequest {
	final private static String WWWROOT = System.getProperty("user.dir") + File.separator + "WWWROOT";// 网站根目录

	private InputStream input;
	private String url;

	public HttpRequest(InputStream input) {
		this.input = input;
		parse();// 解析HTTP请求
	}

	/**
	 * 解析HTTP请求，获取URL并转换成服务器上的绝对文件路径
	 */
	private void parse() {
		StringBuffer request = new StringBuffer(2048);
		int i;
		byte[] buffer = new byte[2048];
		try {
			i = input.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;
		}
		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}

		String[] splited = request.toString().split(" ");
		String realPath = WWWROOT + splited[1];
		// 在Windows上将/替换成\
		realPath = realPath.replace("/", File.separator);
		// 将\ 替换成\\ 防止被转义
		realPath = realPath.replace("\\", "\\\\");

		// 判断类型
		File f = new File(realPath);
		if (f.isDirectory()) {
			// 是目录，添加默认文件名
			url = new File(realPath, "index.html").getAbsolutePath();
		} else if (f.isFile()) {
			// 是文件，不处理
			url = realPath;
		} else {
			// 不是路径，也不是文件
			url = null;
		}

	}

	public String getUrl() {
		return url;

	}

}