package HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpProcessor extends Thread {
	private Socket socket;

	public HttpProcessor(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		InputStream input = null;
		OutputStream output = null;

		try {
			input = socket.getInputStream();
			output = socket.getOutputStream();

			HttpRequest request = new HttpRequest(input);// 创建一个对象来处理HTTP请求
			new HttpResponse(output, request);// 创建一个对象来生成HTTP响应信息并发送给浏览器
			input.close();
			output.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
