package es.uvigo.esei.dai.hybridserver.http;

import java.sql.SQLException;
import java.util.Map;

public interface HtmlDAOXML {

	public abstract boolean existsXML(String uuid);

	public abstract String getXML(String uuid) throws Exception;

	public abstract String createXML(String content) throws SQLException;

	public abstract boolean deleteXML(String uuid);

	public Map<String, String> getMapaXML();

	public boolean updateXML(String uuid, String content);

}
