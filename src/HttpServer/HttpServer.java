package HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	private ServerSocket welcomeSocket;

	public HttpServer(int port) {
		try {
			welcomeSocket = new ServerSocket(port);
			System.out.println("Web服务器已启动，测试地址 http://localhost:" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				Socket connSocket = welcomeSocket.accept();
				new HttpProcessor(connSocket).start();// 创建一个线程来处理具体的HTTP请求

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
