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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HTTPResponse {

	private HTTPResponse response;
	private HTTPResponseStatus estado;
	private String version;
	private String contenido;
	private LinkedHashMap<String, String> headers;

	public HTTPResponse() throws IOException {
		headers = new LinkedHashMap<String, String>();
	}

	public HTTPResponseStatus getStatus() {
		return estado;
	}

	public void setStatus(HTTPResponseStatus status) {
		estado = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return contenido;
	}

	public void setContent(String content) {
		contenido = content;
	}

	public Map<String, String> getParameters() {
		return headers;
	}

	public String putParameter(String name, String value) {
		headers.put(name, value);
		return name;
	}

	public boolean containsParameter(String name) {
		if (response.listParameters().contains(name)) {
			return true;
		} else
			return false;
	}

	public String removeParameter(String name) {

		String aux = "";
		if (response.listParameters().contains(name)) {
			aux = response.getParameters().get(name);
			response.getParameters().remove(name);
		} else {
			System.out
					.println("no se ha encontrado ningun parametro con esa clave");
		}
		return aux;
	}

	public void clearParameters() {
		headers.clear();
	}

	public List<String> listParameters() {
		List<String> listaParametros = new LinkedList<String>();
		for (String clave : headers.keySet()) {
			listaParametros.add(headers.get(clave));
		}
		return listaParametros;
	}

	public void print(Writer writer) throws IOException {
		PrintWriter salida = new PrintWriter(writer);
		salida.print(version + " " + estado.getCode() + " " + estado.getStatus() + "\n");
		for (final String header : headers.keySet()) {
			salida.print(header + ": " + headers.get(header) + "\n");
		}
		if (contenido != null) {
			salida.print("Content-Length: " + contenido.length() + "\n");
			salida.print("\n");
			salida.write(contenido.toCharArray());
		} else {
			salida.print("\n");
		}
		salida.flush();
	}

	@Override
	public String toString() {
		final StringWriter writer = new StringWriter();
		try {
			this.print(writer);
		} catch (IOException e) {
		}
		return writer.toString();
	}
}
