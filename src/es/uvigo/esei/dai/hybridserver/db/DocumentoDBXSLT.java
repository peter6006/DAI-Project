package es.uvigo.esei.dai.hybridserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import es.uvigo.esei.dai.hybridserver.HybridServer;
import es.uvigo.esei.dai.hybridserver.http.*;

public class DocumentoDBXSLT implements HtmlDAOXSLT {
	protected final String TABLE_NAME = "HTML";
	protected final String UUID_NAME = "uuid";
	protected final String CONTENT_NAME = "content";
	private Properties proper;

	// en construccion
	public DocumentoDBXSLT(Properties p) {
		proper = p;
	}

	protected String getContentName() {
		return CONTENT_NAME;
	}

	protected String getTableName() {
		return TABLE_NAME;
	}

	protected String getUUIDName() {
		return UUID_NAME;
	}

	@Override
	public boolean existsXSLT(String uuid) {
		HybridServer d = new HybridServer(proper);
		boolean estado = true;
		try (PreparedStatement statement = d.getConnection().prepareStatement(
				"SELECT * FROM XSLT WHERE uuid=?")) {
			statement.setString(1, uuid);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					estado = true;
				} else {
					estado = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return estado;
	}

	@Override
	public String getXSLT(String uuid) throws Exception {
		HybridServer d = new HybridServer(proper);
		try (PreparedStatement statement = d.getConnection().prepareStatement(
				"SELECT * FROM XSLT WHERE uuid=?")) {
			statement.setString(1, uuid);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return result.getString("content");
				} else {
					throw new Exception(
							"No existe el elemento solicitado en la base de datos.");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getXSDfromXSLT(String xslt) throws Exception {
		HybridServer d = new HybridServer(proper);
		try (PreparedStatement statement = d.getConnection().prepareStatement(
				"SELECT xsd FROM XSLT WHERE uuid=?")) {
			statement.setString(1, xslt);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return result.getString("xsd");
				} else {
					throw new Exception(
							"No existe el elemento solicitado en la base de datos.");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public String createXSLTwXSD(String content, String xsd)
			throws SQLException {
		String id;
		HybridServer d = new HybridServer(proper);
		try (PreparedStatement statement = d.getConnection().prepareStatement(
				"INSERT INTO XSLT (uuid,content,xsd) VALUES (?, ?, ?)")) {
			id = UUID.randomUUID().toString();
			statement.setString(1, id);
			statement.setString(2, content);
			statement.setString(3, xsd);
			int result = statement.executeUpdate();
			if (result != 1) {
				throw new SQLException("Error insertando elemento");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return id;
	}

	public String createXSLT(String content) throws SQLException {
		String id;
		HybridServer d = new HybridServer(proper);
		try (PreparedStatement statement = d.getConnection().prepareStatement(
				"INSERT INTO XSLT (uuid,content,xsd) VALUES (?, ?)")) {
			id = UUID.randomUUID().toString();
			statement.setString(1, id);
			statement.setString(2, content);
			int result = statement.executeUpdate();
			if (result != 1) {
				throw new SQLException("Error insertando elemento");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return id;
	}

	@Override
	public boolean updateXSLT(String uuid, String content) {
		HybridServer d = new HybridServer(proper);
		try (PreparedStatement statement = d.getConnection().prepareStatement(
				"INSERT INTO XSLT WHERE uuid=?",
				Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(2, content);
			if (statement.executeUpdate() != 1) {
				throw new SQLException("Error actualizando elemento");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	@Override
	public boolean deleteXSLT(String uuid) {
		boolean estado = true;
		HybridServer d = new HybridServer(proper);
		try (PreparedStatement statement = d.getConnection().prepareStatement(
				"DELETE FROM XSLT WHERE uuid=?")) {
			try {
				statement.setString(1, uuid);
			} catch (NumberFormatException ex) {
				estado = false;
			}
			if (statement.executeUpdate() != 1) {
				System.out.println("Se ha producido un error");
				estado = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return estado;
	}

	@SuppressWarnings("null")
	@Override
	public Map<String, String> getMapaXSLT() {
		HybridServer d = new HybridServer(proper);
		Map<String, String> map = null;
		try (PreparedStatement statement = d.getConnection().prepareStatement(
				"SELECT * FROM XSLT")) {
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					map.put(result.getString("uuid"),
							result.getString("content"));
				}
				return map;
			} catch (SQLException e) {
				throw new SQLException("Error al convertir a mapa");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> listarContenidoXSLT() {
		HybridServer d = new HybridServer(proper);
		try (Statement statement = d.getConnection().prepareStatement(
				"SELECT * FROM XSLT")) {
			try (ResultSet result = statement
					.executeQuery("SELECT * FROM XSLT")) {
				final List<String> contenido = new ArrayList<>();
				while (result.next()) {
					contenido.add(result.getString("content"));
				}
				return contenido;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> listarUUIDXSLT() {
		HybridServer d = new HybridServer(proper);
		try (Statement statement = d.getConnection().prepareStatement(
				"SELECT * FROM XSLT")) {
			try (ResultSet result = statement
					.executeQuery("SELECT * FROM XSLT")) {
				final List<String> contenido = new ArrayList<>();

				while (result.next()) {
					contenido.add(result.getString("uuid"));
				}
				return contenido;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}