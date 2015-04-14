package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Properties;

public class ServiceThread implements Runnable {
	private Socket socket;
	HtmlDAO dao;
	Properties pr;
	private boolean BD = false;

	public ServiceThread(Socket clientSocket, Properties p) throws IOException {
		this.socket = clientSocket;
		pr = p;
	}

	public ServiceThread(Socket clientSocket, HtmlDAO d) throws IOException {
		this.socket = clientSocket;
		dao = d;
		BD = true;
	}

	public void run() {
		try (Socket socket = this.socket) {
			if (BD) {
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));

				HTTPRequest request = new HTTPRequest(lector);
				HTMLController controlador = new HTMLController(dao);
				HTTPResponse response = controlador.process(request);
				response.print(new OutputStreamWriter(socket.getOutputStream()));
			} else {
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));

				HTTPRequest request = new HTTPRequest(lector);
				HTMLControllerDB controlador = new HTMLControllerDB(pr);
				HTTPResponse response = controlador.processDB(request);
				response.print(new OutputStreamWriter(socket.getOutputStream()));
			}

		} catch (IOException | HTTPParseException excepcion) {
			excepcion.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
