package es.uvigo.esei.dai.hybridserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import com.mysql.jdbc.Connection;

import es.uvigo.esei.dai.hybridserver.http.HowToXSLT;
import es.uvigo.esei.dai.hybridserver.http.HtmlDAO;
import es.uvigo.esei.dai.hybridserver.http.InMemoryDAO;
import es.uvigo.esei.dai.hybridserver.http.ServiceThread;

public class HybridServer {
	private static int SERVICE_PORT = 8888;
	private Thread serverThread;
	private boolean stop;
	private HtmlDAO dao;
	private int numClientes = 50;
	private Properties properties;
	private Connection connection;
	private String dburl = "jdbc:mysql://localhost/hybridserverdb";
	private String dbuser = "dai";
	private String dbpassword = "daipassword";
	private boolean esDB = false;

	public HybridServer() {
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void close() throws SQLException {
		this.connection.close();
	}

	public HybridServer(Properties props) {
		this.properties = props;
		SERVICE_PORT = Integer.parseInt(props.getProperty("port"));
		numClientes = Integer.parseInt(props.getProperty("numClients"));
		dburl = props.getProperty("db.url");
		dbuser = props.getProperty("db.user");
		dbpassword = props.getProperty("db.password");
		esDB = true;
		try {
			connection = (Connection) DriverManager.getConnection(dburl,
					dbuser, dbpassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HybridServer(Configuration conf) throws Exception {
		if(validarFicheroConfiguracion(conf)){
		SERVICE_PORT = conf.getHttpPort();
		numClientes = conf.getNumClients();
		dburl = conf.getDbURL();
		dbuser = conf.getDbUser();
		dbpassword = conf.getDbPassword();
		
		properties = new Properties();
		properties.setProperty("port", Integer.toString(conf.getHttpPort()));
		properties.setProperty("numClients", Integer.toString(conf.getNumClients()));
		properties.setProperty("db.url", conf.getDbURL());
		properties.setProperty("db.user", conf.getDbUser());
		properties.setProperty("db.password", conf.getDbPassword());
		esDB = true;
		}else{throw new Exception("-FICHERO MAL CARGADO- "); }
		try {
			connection = (Connection) DriverManager.getConnection(dburl,
					dbuser, dbpassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean validarFicheroConfiguracion(Configuration conf) throws FileNotFoundException, TransformerException {
		if(HowToXSLT.transformacion() == 0){
			return true;
		}else{
			return false;
		}
	}

	public HybridServer(Map<String, String> p) {
		dao = new InMemoryDAO(p);
	}

	public int getPort() {
		return SERVICE_PORT;
	}

	public void start() {
		if (!esDB) {
			this.serverThread = new Thread() {

				@Override
				public void run() {
					try (final ServerSocket serverSocket = new ServerSocket(
							getPort())) {
						ExecutorService threadPool = Executors
								.newFixedThreadPool(numClientes);
						while (true) {
							Socket socket = serverSocket.accept();
							if (stop)
								break;
							threadPool.execute(new ServiceThread(socket, dao));
						}
					} catch (IOException e) {
						System.err
								.println("Se ha producido un error en el servidor"
										+ e.getMessage());
					}
				}
			};
		} else {
			this.serverThread = new Thread() {

				@Override
				public void run() {
					try (final ServerSocket serverSocket = new ServerSocket(
							getPort())) {
						ExecutorService threadPool = Executors
								.newFixedThreadPool(numClientes);
						while (true) {
							Socket socket = serverSocket.accept();
							if (stop)
								break;
							threadPool.execute(new ServiceThread(socket,
									properties));
						}
					} catch (IOException e) {
						System.err
								.println("Se ha producido un error en el servidor"
										+ e.getMessage());
					}
				}
			};
		}

		this.stop = false;
		this.serverThread.start();
	}

	public void stop() {
		this.stop = true;

		try (Socket socket = new Socket("localhost", getPort())) {
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			this.serverThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		this.serverThread = null;
	}
}
