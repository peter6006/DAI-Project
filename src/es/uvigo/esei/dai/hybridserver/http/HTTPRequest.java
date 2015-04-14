/**
 *  HybridServer
 *  Copyright (C) 2014 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.lang.Integer;
import java.net.URLDecoder;

public class HTTPRequest {
	private String completo;
	private String resourceChain;
	private String resourceName;
	private String httpVersion;
	private String content;
	private String contentEntero;
	private int contentLength;//
	String[] x;
	public HTTPRequestMethod method;//
	private Map<String, String> ResourceParameters = new LinkedHashMap<String, String>();
	private Map<String, String> HeaderParameters = new LinkedHashMap<String, String>();
	private boolean codificado;

	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {
		BufferedReader i = new BufferedReader(reader);
		String cadena = i.readLine();
		String[] nuevaCadena = cadena.split("\\s+");
		try {
			this.method = HTTPRequestMethod.valueOf(nuevaCadena[0]);
		} catch (Exception e) {
			throw new HTTPParseException("Missing method");
		}
		try {
			resourceChain = nuevaCadena[1];
		} catch (Exception e1) {
			throw new HTTPParseException("Missing Resource");
		}
		try {
			httpVersion = nuevaCadena[2];
		} catch (Exception e) {
			throw new HTTPParseException("Missing version");
		}
		if (nuevaCadena[1].contains("?")) {
			String[] aux2 = nuevaCadena[1].split("\\?");
			resourceName = aux2[0].substring(1);
			String[] parametros = aux2[1].split("&");
			for (int j = 0; j < parametros.length; j++) {
				String[] aux3 = parametros[j].split("=");
				ResourceParameters.put(aux3[0], aux3[1]);
			}
		} else {
			resourceName = nuevaCadena[1].substring(1);
		}
		String linea = i.readLine();
		while (!linea.matches("")) {
			String[] elementos = linea.split(":\\s+");
			try {
				HeaderParameters.put(elementos[0], elementos[1]);
			} catch (Exception e) {
				throw new HTTPParseException("Missing new line after header");
			}
			if (elementos[0].startsWith("Content-Length")) {
				contentLength = Integer.parseInt(elementos[1]);
			}
			if (elementos[0].startsWith("Content-Type")) {
				codificado = true;
			}
			linea = i.readLine();
		}
		if (method == HTTPRequestMethod.POST
				&& HeaderParameters.containsKey("Content-Length")) {
			char[] contentArray = new char[Integer.valueOf(HeaderParameters
					.get("Content-Length"))];
			i.read(contentArray);
			content = new String(contentArray);
			if (codificado == true) {
				content = URLDecoder.decode(content, "UTF-8");
			}
			if (content.contains("&")) {
				String[] p = content.split("&");
				for (int j = 0; j < p.length; j++) {
					String[] aux3 = p[j].split("=");
					ResourceParameters.put(aux3[0], aux3[1]);
				}
			} else {
				String[] param = content.split("=");
				ResourceParameters.put(param[0], param[1]);
			}
			contentEntero = content;
			x = content.split("=");
			content = x[1];
			completo = x[0].concat(x[1]);
		}
	}

	public String completo() {
		return completo;
	}

	public HTTPRequestMethod getMethod() {
		return method;
	}

	public String getResourceChain() {
		return resourceChain;
	}

	public String getResourceName() {
		return resourceName;
	}

	public Map<String, String> getResourceParameters() {
		return ResourceParameters;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public Map<String, String> getHeaderParameters() {
		return HeaderParameters;
	}

	public String getContent() {
		return contentEntero;
	}

	public int getContentLength() {
		return contentLength;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getMethod().name())
				.append(' ').append(this.getResourceChain()).append(' ')
				.append(this.getHttpVersion()).append('\n');

		for (Map.Entry<String, String> param : this.getHeaderParameters()
				.entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue())
					.append('\n');
		}

		if (this.content != null) {
			sb.append('\n').append(this.getContent());
		}

		return sb.toString();
	}
}
