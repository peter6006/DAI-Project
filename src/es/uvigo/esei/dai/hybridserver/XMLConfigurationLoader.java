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
package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class XMLConfigurationLoader {

	public Configuration load(File xmlFile) throws Exception {
		Configuration conf = new Configuration();

		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc;
		doc = db.parse(xmlFile);
		doc.getDocumentElement().normalize();
		NodeList con;

		con = doc.getElementsByTagName("http");
		Element htp = (Element) con.item(0);
		if (!htp.getTextContent().isEmpty()) {
			if (htp.getTextContent().contains("[a-zA-Z]")) {
				throw new Exception("Invalid http port number");
			} else {
				conf.setHttpPort(Integer.parseInt(htp.getTextContent()));
			}
		} else {
			throw new Exception("Missing http parameter");
		}

		NodeList con2 = doc.getElementsByTagName("webservice");
		Element ws = (Element) con2.item(0);
		conf.setWebServiceURL(ws.getTextContent());

		NodeList con3 = doc.getElementsByTagName("numClients");
		Element nc = (Element) con3.item(0);
		if (nc.getTextContent().contains("[a-zA-Z]")) {
			throw new Exception("Invalid number of clients");
		} else {
			conf.setNumClients(Integer.parseInt(nc.getTextContent()));
		}

		NodeList con4 = doc.getElementsByTagName("user");
		Element user = (Element) con4.item(0);
		if (user.getTextContent().isEmpty()) {
			throw new Exception("Missing database configuration");
		} else {
			conf.setDbUser(user.getTextContent());
		}

		NodeList con5 = doc.getElementsByTagName("password");
		Element password = (Element) con5.item(0);
		if (password.getTextContent().isEmpty()) {
			throw new Exception("Missing database configuration");
		} else {
			conf.setDbPassword(password.getTextContent());
		}

		NodeList con6 = doc.getElementsByTagName("url");
		Element url = (Element) con6.item(0);
		if (url.getTextContent().isEmpty()) {
			throw new Exception("Missing database configuration");
		} else {
			conf.setDbURL(url.getTextContent());
		}

		NodeList serversl = doc.getElementsByTagName("server");
		List<ServerConfiguration> l = new ArrayList<ServerConfiguration>();
		for (int i = 0; i < serversl.getLength(); i++) {
			ServerConfiguration servers;

			servers = new ServerConfiguration();
			Element lista = (Element) serversl.item(i);
			if (lista.getAttribute("httpAddress").isEmpty()
					|| lista.getAttribute("name").isEmpty()
					|| lista.getAttribute("namespace").isEmpty()
					|| lista.getAttribute("service").isEmpty()
					|| lista.getAttribute("wsdl").isEmpty()) {
						throw new Exception("Missing attributes in server");
			}else{
			servers.setHttpAddress(lista.getAttribute("httpAddress"));
			servers.setName(lista.getAttribute("name"));
			servers.setNamespace(lista.getAttribute("namespace"));
			servers.setService(lista.getAttribute("service"));
			servers.setWsdl(lista.getAttribute("wsdl"));
			l.add(servers);
			}
		}
		conf.setServers(l);
		return conf;

	}
}
