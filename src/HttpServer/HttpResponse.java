package HttpServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/*
  HTTP Response = Status-Line
    *(( general-header | response-header | entity-header ) CRLF)
    CRLF
    [ message-body ]
    Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
*/

public class HttpResponse {
	final private static String WWWROOT = System.getProperty("user.dir") + File.separator + "WWWROOT";

	HttpRequest request;
	OutputStream output;

	public HttpResponse(OutputStream output, HttpRequest request) throws IOException {
		this.output = output;
		this.request = request;
		makeResponse();
	}

	/**
	 * 生成响应数据
	 */
	private void makeResponse() {
		String url = request.getUrl();

		// 文件不存在的第一种情况
		if (url == null) {
			make404Response();
			return;
		}

		File f = new File(url);

		if (!f.exists()) {
			// 文件不存在的第二种情况
			make404Response();
			return;
		}

		// 以下是文件存在的情况，根据文件后缀名判断文件类型，根据不同的文件类型构造响应数据
		int dotPos = url.lastIndexOf("."); // 小数点出现的最后位置
		String suffixName = url.substring(dotPos + 1);
		suffixName = suffixName.toLowerCase(); // 将后缀名转换成小写

		try {
			switch (suffixName) {
			case "html":
			case "htm":
			case "shm":
				makeGeneralResponse(url, "text/html");
				break;
			case "css":
				makeGeneralResponse(url, "text/css");
				break;
			case "js":
				makeGeneralResponse(url, "application/x-javascript");
				break;
			case "png":
				makeGeneralResponse(url, "image/png");
				break;
			case "bmp":
				makeGeneralResponse(url, "image/bmp");
				break;
			case "jpg":
			case "jpeg":
				makeGeneralResponse(url, "image/jpeg");
				break;
			case "gif":
				makeGeneralResponse(url, "image/gif");
				break;
			default:
				makeGeneralResponse(url, "text/plain");// 其他文件都当成纯文本文件处理
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 生成404错误响应数据
	 */
	private void make404Response() {
		// file not found
		String response = "HTTP/1.1 404 File Not Found\r\nContent-Type: text/html\r\nServer: LR-HTTP-SERVER\r\nConnection: close\\r\\nContent-Length: 22\r\n\r\n<h1>404 Not Found</h1>";
		try {
			output.write(response.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通用生成响应消息的方法
	 * 
	 * @param path
	 *            本地文件的绝对路径
	 * @param mime
	 *            文件类型
	 * @throws IOException
	 */
	private void makeGeneralResponse(String path, String mime) throws IOException {
		File f = new File(path);

		String ResponseHeader = "HTTP/1.1 200 OK\r\nContent-Type: " + mime
				+ "\r\nServer: LR-HTTP-SERVER\r\nConnection: close\r\nContent-Length: " + f.length() + "\r\n\r\n";

		FileInputStream fis = new FileInputStream(f);
		byte[] buffer = new byte[8192];
		int len;

		output.write(ResponseHeader.getBytes("utf-8"));// 发送响应头

		System.out.println(getThreadName() + " 正在发送文件 " + path);
		// 发送响应体
		while ((len = fis.read(buffer)) >= 0) {
			output.write(buffer, 0, len);
			System.out.println(getThreadName() + " 本次发送的数据长度：" + len);
		}
		fis.close();
		System.out.println(getThreadName() + " 文件发送完毕");
	}

	private String getThreadName() {
		return Thread.currentThread().getName();
	}
}