package es.uvigo.esei.dai.hybridserver.http;

import java.sql.SQLException;
import java.util.Map;

public interface HtmlDAOXSD {

	public abstract boolean existsXSD(String uuid);

	public abstract String getXSD(String uuid) throws Exception;

	public abstract String createXSD(String content) throws SQLException;

	public abstract boolean deleteXSD(String uuid);

	public Map<String, String> getMapaXSD();

	public boolean updateXSD(String uuid, String content);

}
