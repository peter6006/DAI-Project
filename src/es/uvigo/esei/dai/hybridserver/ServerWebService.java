package es.uvigo.esei.dai.hybridserver;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServerWebService {

	@WebMethod public List<String> getHtmlUuid() throws Exception;
	@WebMethod public List<String> getXmlUuid();
	@WebMethod public List<String> getXsdUuid();
	@WebMethod public List<String> getXsltUuid();
	
	@WebMethod public String getHtmlContent(String uuid) throws Exception;
	@WebMethod public String getXmlContent(String uuid) throws Exception;
	@WebMethod public String getXsdContent(String uuid) throws Exception;
	@WebMethod public String getXsltContent(String uuid) throws Exception;
	@WebMethod public String getXSDasociadoXSLT(String uuid) throws Exception;
	
}
