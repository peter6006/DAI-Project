package es.uvigo.esei.dai.hybridserver.http;

import java.util.Map;
import java.util.UUID;

public class InMemoryDAO implements HtmlDAO {

	Map<String, String> uid;
	String uuid;

	public InMemoryDAO(Map<String, String> m) {
		uid = m;
	}

	public Map<String, String> getMapa() {
		return uid;
	}

	@Override
	public boolean exists(String uuid) {
		return this.uid.containsKey(uuid);
	}

	@Override
	public String get(String uuid) {
		String aux = "";
		if (uid.containsKey(uuid)) {
			return uid.get(uuid);
		}
		return aux;
	}

	@Override
	public String create(String content) {
		UUID key = UUID.randomUUID();
		while (uid.containsKey(key)) {
			key = UUID.randomUUID();
		}
		content = content.replaceFirst("html", "");
		uid.put(key.toString(), content);
		return key.toString();
	}

	@Override
	public boolean delete(String uuid) {
		boolean estado = true;
		if (uid.containsKey(uuid)) {
			uid.remove(uuid);
			estado = true;
		} else {
			estado = false;
		}
		return estado;
	}

	@Override
	public boolean update(String uuid, String content) {
		if (uid.containsKey(uuid)) {
			uid.put(uuid, content);
			return true;
		} else {
			return false;
		}
	}
}
