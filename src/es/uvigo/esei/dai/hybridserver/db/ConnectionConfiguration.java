package es.uvigo.esei.dai.hybridserver.db;

import java.util.Properties;

public interface ConnectionConfiguration {
	public String getConnectionString();

	public Properties getConnectionProperties();

}
