package es.uvigo.esei.dai.hybridserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.derby.impl.tools.sysinfo.Main;

public class Launcher {

	public void main(String[] args) throws IOException, ClassNotFoundException,
			SQLException {
		InputStream input = Main.class.getClassLoader().getResourceAsStream(
				args[0]);

		File fichero = new File(args[0]);
		if (input == null) {
			HybridServer miServidor = new HybridServer();
			miServidor.start();
		} else {
			Properties prop = new Properties();

			prop.setProperty("db.url", "jdbc:mysql://localhost/hybridserverdb");
			prop.setProperty("db.user", "dai");
			prop.setProperty("db.password", "daipassword");
			prop.setProperty("port", "8888");
			prop.setProperty("numClients", "50");

			Properties properties = new Properties(prop);

			if (fichero.exists()) {
				properties
						.load(new BufferedReader(new InputStreamReader(input)));
				if (!properties.containsKey("db.url")
						|| !properties.containsKey("db.user")
						|| !properties.containsKey("db.password")
						|| !properties.containsKey("port")
						|| !properties.containsKey("numClients"))
					;
				{
					System.out
							.println("Archivo corrupto, se ha producido un problema");
				}
			} else {
				System.out.println("El fichero solicitado no existe");
			}
			HybridServer miServidor = new HybridServer(properties);
			miServidor.start();
		}
	}
}
