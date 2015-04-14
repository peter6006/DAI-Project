package es.uvigo.esei.dai.hybridserver.http;

import java.util.Map;

public class HTMLController {

	private HtmlDAO dao;
	private HTTPResponse response;

	String extension = "";
	String contenido = "";

	public HTMLController(HtmlDAO dao) {
		this.dao = dao;
	}

	public HTTPResponse process(HTTPRequest request) throws Exception {
		response = new HTTPResponse();

		extension = request.getResourceName();
		response.setVersion(request.getHttpVersion());
		response.setStatus(HTTPResponseStatus.S200);
		response.setContent(request.getContent());

		if (request.getMethod() == HTTPRequestMethod.GET) {
			if (request.getResourceChain().equals("/")) {
				HTTPResponse pagina = new HTTPResponse();
				pagina.setContent("<h1>DAI | Hybrid Server</h1>\n\nCibran y Pedro \n\n Aqui se meteran todos los link de las paginas existentes:");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else if (request.getResourceChain().equals("/html")) {
				HTTPResponse pagina = new HTTPResponse();
				Map<String, String> aux = dao.getMapa();
				String cadena = "";
				while (!aux.isEmpty()) {
					Object[] aux2 = aux.keySet().toArray();
					cadena = cadena.concat("<a href=\"html?uuid=" + aux2[0]
							+ "\">" + aux2[0] + "</a>");
					aux.remove(aux2[0].toString());
				}
				pagina.setContent(cadena);
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			}
			if (!request.getResourceChain().contains("/html")) {
				HTTPResponse pagina = new HTTPResponse();
				pagina.setContent("400");
				pagina.setStatus(HTTPResponseStatus.S400);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			}

			if (request.getResourceChain().contains("uuid=")) {
				HTTPResponse pagina = new HTTPResponse();
				String[] key = request.getResourceChain().split("=");
				if (dao.exists(key[1])) {
					pagina.setContent(dao.get(key[1]));
					pagina.setStatus(HTTPResponseStatus.S200);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				}
				if (!dao.exists(key[1])) {

					pagina.setContent("404");
					pagina.setStatus(HTTPResponseStatus.S404);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				}
			}
		} else if (request.getMethod() == HTTPRequestMethod.POST) {
			if (!request.completo().contains("xxx")) {
				HTTPResponse pagina = new HTTPResponse();
				String uuid = dao.create(request.completo());
				pagina.setContent("<a href=\"html?uuid=" + uuid + "\">" + uuid
						+ "</a>");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else {
				HTTPResponse pagina = new HTTPResponse();
				pagina.setStatus(HTTPResponseStatus.S400);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			}
		} else if (request.getMethod() == HTTPRequestMethod.DELETE) {
			String[] key = request.getResourceChain().split("=");
			HTTPResponse pagina = new HTTPResponse();
			if (!dao.exists(key[1])) {
				pagina.setContent("404");
				pagina.setStatus(HTTPResponseStatus.S404);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else if (dao.exists(key[1])) {
				boolean estado = dao.delete(key[1]);
				pagina.setContent("Eliminacion de la pagina asociada a: "
						+ key[1] + ",estado: " + estado);
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else {
				pagina.setContent("The page couldn't be deleted");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				if (dao.exists(key[1])) {
					pagina.setContent("The page already exists");
					pagina.setStatus(HTTPResponseStatus.S404);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				}
				return pagina;
			}
		} else {
			HTTPResponse pagina = new HTTPResponse();
			pagina.setContent("500");
			pagina.setStatus(HTTPResponseStatus.S500);
			pagina.setVersion("HTTP/1.1");
			return pagina;
		}
		return response;
	}
}