package es.uvigo.esei.dai.hybridserver.http;

import java.sql.SQLException;
import java.util.Map;

public interface HtmlDAOXSLT {

	public abstract boolean existsXSLT(String uuid);

	public abstract String getXSLT(String uuid) throws Exception;

	public abstract String createXSLT(String content) throws SQLException;

	public abstract boolean deleteXSLT(String uuid);

	public Map<String, String> getMapaXSLT();

	public boolean updateXSLT(String uuid, String content);

	String getXSDfromXSLT(String xslt) throws Exception;

}
