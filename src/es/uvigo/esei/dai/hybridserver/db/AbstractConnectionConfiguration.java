package es.uvigo.esei.dai.hybridserver.db;

import java.io.File;
import java.util.Properties;

public abstract class AbstractConnectionConfiguration {
	public final static File DIR_SQL = new File("sql");
	private String userName;
	private String password;
	private String url;

	public AbstractConnectionConfiguration() {
		super();
	}

	public AbstractConnectionConfiguration(String userName, String password,
			String urldb) {
		this.setUserName(userName);
		this.setPassword(password);
		this.setURL(urldb);
	}

	public String getUserName() {
		return userName;
	}

	public void setURL(String urldb) {
		this.url = urldb;
	}

	public String getURL() {
		return url;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getConnectionProperties() {
		final Properties properties = new Properties();

		if (this.userName != null)
			properties.put("user", this.userName);

		if (this.password != null)
			properties.put("password", this.password);

		return properties;
	}

}