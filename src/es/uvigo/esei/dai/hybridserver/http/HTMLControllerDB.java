package es.uvigo.esei.dai.hybridserver.http;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;



import es.uvigo.esei.dai.hybridserver.db.DocumentoDBDAO;
import es.uvigo.esei.dai.hybridserver.db.DocumentoDBXML;
import es.uvigo.esei.dai.hybridserver.db.DocumentoDBXSD;
import es.uvigo.esei.dai.hybridserver.db.DocumentoDBXSLT;

public class HTMLControllerDB {

	private HTTPResponse response;
	String extension = "";
	String contenido = "";
	private Properties propiedades;

	public HTMLControllerDB(Properties p) {
		propiedades = p;
	}

	public HTTPResponse processDB(HTTPRequest request) throws Exception {
		response = new HTTPResponse();
		extension = request.getResourceName();
		response.setVersion(request.getHttpVersion());
		response.setStatus(HTTPResponseStatus.S200);
		response.setContent(request.getContent());
		DocumentoDBDAO db = new DocumentoDBDAO(propiedades);
		DocumentoDBXML dbxml = new DocumentoDBXML(propiedades);
		DocumentoDBXSLT dbxslt = new DocumentoDBXSLT(propiedades);
		DocumentoDBXSD dbxsd = new DocumentoDBXSD(propiedades);

		if (request.getMethod() == HTTPRequestMethod.GET) {
			// si es un recurso html
			if (request.getResourceChain().equals("/")) {
				HTTPResponse pagina = new HTTPResponse();
				pagina.setContent("<h1>DAI | Hybrid Server</h1>\n\nCibran y Pedro \n\n Aqui se meteran todos los link de las paginas existentes:");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				pagina.putParameter("Content-type", "text/html");
				return pagina;
			} else if (request.getResourceName().equals("html")) {
				HTTPResponse pagina = new HTTPResponse();
				List<String> l = db.listarUUID();
				String cadena = "";
				Iterator<String> iterador = l.iterator();
				while (iterador.hasNext()) {
					String auxiliar = iterador.next();
					cadena = cadena.concat("<a href=\"html?uuid=" + auxiliar
							+ "\">" + auxiliar + "</a>");
				}
				pagina.setContent(cadena);
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				pagina.putParameter("Content-type", "text/html");
				if (request.getResourceChain().contains("uuid=")) {
					Map<String, String> key = request.getResourceParameters();
					if (db.exists(key.get("uuid"))) {
						pagina.setContent(db.get(key.get("uuid")));
						pagina.setStatus(HTTPResponseStatus.S200);
						pagina.setVersion("HTTP/1.1");
						pagina.putParameter("Content-type", "text/html");
						return pagina;
					} else {
						HTTPResponse po = new HTTPResponse();
						po.setContent("404");
						po.setStatus(HTTPResponseStatus.S404);
						po.setVersion("HTTP/1.1");
						return po;
					}
				}
				return pagina;
				// si es un recurso XML
			} else if (request.getResourceName().equals("xml")) {
				String xslt = null;
				if (request.getResourceChain().equals("/xml")) {
					HTTPResponse pagina = new HTTPResponse();
					List<String> l = dbxml.listarUUIDXML();
					String cadena = "";
					Iterator<String> iterador = l.iterator();
					while (iterador.hasNext()) {
						String auxiliar = iterador.next();
						cadena = cadena.concat("<a href=\"html?uuid="
								+ auxiliar + "\">" + auxiliar + "</a>");
					}
					pagina.setContent(cadena);
					pagina.setStatus(HTTPResponseStatus.S200);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else {

					HTTPResponse pagina = new HTTPResponse();
					Map<String, String> key = request.getResourceParameters();
					if (dbxml.existsXML(key.get("uuid"))) {
						String conten = dbxml.getXML(key.get("uuid"));
						if (request.getResourceParameters().containsKey("xslt")) {
							xslt = request.getResourceParameters().get("xslt");
							if (dbxslt.existsXSLT(xslt)) {
								String con = dbxslt.getXSLT(xslt);
								String xsd = dbxslt.getXSDfromXSLT(xslt);
								if (dbxsd.existsXSD(xsd)) {
									if(HowToXSLT.validateAgainstXSD(conten, con)){
										pagina.setContent(HowToXSLT.transformacion2(conten, con));
										 
										pagina.setStatus(HTTPResponseStatus.S200);
										pagina.setVersion("HTTP/1.1");
										pagina.putParameter("Content-type",
												"text/html");
										return pagina;
									}else{
										HTTPResponse p2 = new HTTPResponse();
										p2.setContent("400");
										p2.setStatus(HTTPResponseStatus.S400);
										p2.setVersion("HTTP/1.1");
										return p2;
									}
								} else {
									HTTPResponse po = new HTTPResponse();
									po.setContent("404");
									po.setStatus(HTTPResponseStatus.S404);
									po.setVersion("HTTP/1.1");
									return po;
								}
							} else {
								HTTPResponse po = new HTTPResponse();
								po.setContent("404");
								po.setStatus(HTTPResponseStatus.S404);
								po.setVersion("HTTP/1.1");
								return po;
							}
						} else {
							pagina.setContent(dbxml.getXML(key.get("uuid")));
							pagina.setStatus(HTTPResponseStatus.S200);
							pagina.setVersion("HTTP/1.1");
							pagina.putParameter("Content-type",
									"application/xml");
							return pagina;
						}
					} else {
						HTTPResponse po = new HTTPResponse();
						po.setContent("404");
						po.setStatus(HTTPResponseStatus.S404);
						po.setVersion("HTTP/1.1");
						return po;
					}

				}// si el recurso es XSLT
			} else if (request.getResourceName().equals("xslt")) {
				if (request.getResourceChain().equals("/xslt")) {
					HTTPResponse pagina = new HTTPResponse();
					List<String> l = dbxslt.listarUUIDXSLT();
					String cadena = "";
					Iterator<String> iterador = l.iterator();
					while (iterador.hasNext()) {
						String auxiliar = iterador.next();
						cadena = cadena.concat("<a href=\"html?uuid="
								+ auxiliar + "\">" + auxiliar + "</a>");
					}
					pagina.setContent(cadena);
					pagina.setStatus(HTTPResponseStatus.S200);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else {
					HTTPResponse pagina = new HTTPResponse();
					Map<String, String> key = request.getResourceParameters();
					if (dbxslt.existsXSLT(key.get("uuid"))) {
						// comprobar si el XSLT tiene un XSD - 400
						pagina.setContent(dbxslt.getXSLT(key.get("uuid")));
						pagina.setStatus(HTTPResponseStatus.S200);
						pagina.setVersion("HTTP/1.1");
						pagina.putParameter("Content-type", "application/xml");
						return pagina;
					} else {
						HTTPResponse po = new HTTPResponse();
						po.setContent("404");
						po.setStatus(HTTPResponseStatus.S404);
						po.setVersion("HTTP/1.1");
						return po;
					}
				}// si el recurso es XSD
			} else if (request.getResourceName().equals("xsd")) {
				if (request.getResourceChain().equals("/xsd")) {
					HTTPResponse pagina = new HTTPResponse();
					List<String> l = dbxsd.listarUUIDXSD();
					String cadena = "";
					Iterator<String> iterador = l.iterator();
					while (iterador.hasNext()) {
						String auxiliar = iterador.next();
						cadena = cadena.concat("<a href=\"html?uuid="
								+ auxiliar + "\">" + auxiliar + "</a>");
					}
					pagina.setContent(cadena);
					pagina.setStatus(HTTPResponseStatus.S200);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else {
					HTTPResponse pagina = new HTTPResponse();
					Map<String, String> key = request.getResourceParameters();
					if (dbxsd.existsXSD(key.get("uuid"))) {

						pagina.setContent(dbxsd.getXSD(key.get("uuid")));
						pagina.setStatus(HTTPResponseStatus.S200);
						pagina.setVersion("HTTP/1.1");
						pagina.putParameter("Content-type", "application/xml");
						return pagina;
					} else {
						HTTPResponse po = new HTTPResponse();
						po.setContent("404");
						po.setStatus(HTTPResponseStatus.S404);
						po.setVersion("HTTP/1.1");
						return po;
					}
				}
			} else if (!request.getResourceChain().contains("/html")
					&& !request.getResourceChain().contains("/xml")
					&& !request.getResourceChain().contains("/xslt")
					&& !request.getResourceChain().contains("/xsd")) {
				HTTPResponse pagina = new HTTPResponse();
				pagina.setContent("400");
				pagina.setStatus(HTTPResponseStatus.S400);
				pagina.setVersion("HTTP/1.1");
				return pagina;

			} else {
				HTTPResponse pagina = new HTTPResponse();
				pagina.setContent("404");
				pagina.setStatus(HTTPResponseStatus.S404);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			}

		} else if (request.getMethod() == HTTPRequestMethod.POST) {
			// si es un recurso html
			if (request.completo().contains("html")) {
				HTTPResponse pagina = new HTTPResponse();
				if (request.getResourceParameters().containsKey("html")) {
					String uuid = db.create(request.getResourceParameters()
							.get("html"));
					pagina.setContent("<a href=\"html?uuid=" + uuid + "\">"
							+ uuid + "</a>");
					pagina.setStatus(HTTPResponseStatus.S200);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else {
					HTTPResponse pagina1 = new HTTPResponse();
					pagina1.setContent("400");
					pagina1.setStatus(HTTPResponseStatus.S400);
					pagina1.setVersion("HTTP/1.1");
					return pagina1;
				}
				// si es un recurso xml
			} else if (request.getResourceName().equals("xml")) {
				HTTPResponse pagina = new HTTPResponse();
				if (request.getResourceParameters().containsKey("xml")) {
					String uuid = dbxml.createXML(request
							.getResourceParameters().get("xml"));
					pagina.setContent("<a href=\"xml?uuid=" + uuid + "\">"
							+ uuid + "</a>");
					pagina.setStatus(HTTPResponseStatus.S200);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else {
					HTTPResponse pagina1 = new HTTPResponse();
					pagina1.setContent("400");
					pagina1.setStatus(HTTPResponseStatus.S400);
					pagina1.setVersion("HTTP/1.1");
					return pagina1;
				}
				// si es un recurso xslt, hay que completarlo
				// si no tiene contenido o si no tiene un xsd asociado - 400
				// si su xsd no existe - 404
				// usar resourceParameters
			} else if (request.getResourceName().equals("xslt")) {
				HTTPResponse pagina = new HTTPResponse();
				if (request.getResourceParameters().containsKey("xslt")
						&& request.getResourceParameters().containsKey("xsd")
						&& dbxsd.existsXSD(request.getResourceParameters().get(
								"xsd"))) {
					String uuid = dbxslt.createXSLTwXSD(request
							.getResourceParameters().get("xslt"), request
							.getResourceParameters().get("xsd"));
					pagina.setContent("<a href=\"xslt?uuid=" + uuid + "\">"
							+ uuid + "</a>");
					pagina.setStatus(HTTPResponseStatus.S200);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else if (request.getResourceParameters().containsKey("xslt")
						&& !request.getResourceParameters().containsKey("xsd")) {
					pagina.setContent("400");
					pagina.setStatus(HTTPResponseStatus.S400);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else if (request.getResourceParameters().containsKey("xslt")
						&& request.getResourceParameters().containsKey("xsd")
						&& !dbxsd.existsXSD(request.getResourceParameters()
								.get("xsd"))) {
					pagina.setContent("404");
					pagina.setStatus(HTTPResponseStatus.S404);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else {
					HTTPResponse pagina1 = new HTTPResponse();
					pagina1.setContent("400");
					pagina1.setStatus(HTTPResponseStatus.S400);
					pagina1.setVersion("HTTP/1.1");
					return pagina1;
				}
				// si es un recurso xsd, hay que completarlo
			} else if (request.getResourceName().equals("xsd")) {
				HTTPResponse pagina = new HTTPResponse();
				if (request.getResourceParameters().containsKey("xsd")) {
					String uuid = dbxsd.createXSD(request
							.getResourceParameters().get("xsd"));
					pagina.setContent("<a href=\"xsd?uuid=" + uuid + "\">"
							+ uuid + "</a>");
					pagina.setStatus(HTTPResponseStatus.S200);
					pagina.setVersion("HTTP/1.1");
					return pagina;
				} else {
					HTTPResponse pagina1 = new HTTPResponse();
					pagina1.setContent("400");
					pagina1.setStatus(HTTPResponseStatus.S400);
					pagina1.setVersion("HTTP/1.1");
					return pagina1;
				}
			} else {
				HTTPResponse pagina = new HTTPResponse();
				pagina.setContent("400");
				pagina.setStatus(HTTPResponseStatus.S400);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			}
		} else if (request.getMethod() == HTTPRequestMethod.DELETE) {
			Map<String, String> key = request.getResourceParameters();
			HTTPResponse pagina = new HTTPResponse();
			if (db.exists(key.get("uuid"))) {
				db.delete(key.get("uuid"));
				pagina.setContent("200");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else if (dbxml.existsXML(key.get("uuid"))) {
				dbxml.deleteXML(key.get("uuid"));
				pagina.setContent("200");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else if (dbxslt.existsXSLT(key.get("uuid"))) {
				dbxslt.deleteXSLT(key.get("uuid"));
				pagina.setContent("200");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else if (dbxsd.existsXSD(key.get("uuid"))) {
				dbxsd.deleteXSD(key.get("uuid"));
				pagina.setContent("200");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else if (!db.exists(key.get("uuid"))) {
				pagina.setContent("404");
				pagina.setStatus(HTTPResponseStatus.S404);
				pagina.setVersion("HTTP/1.1");
				return pagina;
			} else {
				pagina.setContent("The page couldn't be deleted");
				pagina.setStatus(HTTPResponseStatus.S200);
				pagina.setVersion("HTTP/1.1");
				if (db.exists(key.get("uuid"))) {
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
	}
}