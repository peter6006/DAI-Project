package es.uvigo.esei.dai.hybridserver;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.db.DocumentoDBDAO;
import es.uvigo.esei.dai.hybridserver.db.DocumentoDBXML;
import es.uvigo.esei.dai.hybridserver.db.DocumentoDBXSD;
import es.uvigo.esei.dai.hybridserver.db.DocumentoDBXSLT;

@WebService(endpointInterface = "es.uvigo.esei.dai.hybridserver")
public class WebServiceImplement implements ServerWebService{
	
	DocumentoDBXML xml;
	DocumentoDBXSD xsd;
	DocumentoDBXSLT xslt;
	DocumentoDBDAO html;
	
	public List<String> getHtmlUuid() throws Exception
	{
		String aux="";
		List<String> aDevolver = new ArrayList<String>();
		aDevolver=html.listarUUID();
				
		Iterator<String> iterador= aDevolver.iterator();
		while(iterador.hasNext())
		{
			aux=iterador.next();
			System.out.println(aux);
		}
		return aDevolver;
		
	}
	public List<String> getXmlUuid()
	{

		String aux="";
		List<String> aDevolver = new ArrayList<String>();
		aDevolver=xml.listarUUIDXML();
				
		Iterator<String> iterador= aDevolver.iterator();
		while(iterador.hasNext())
		{
			aux=iterador.next();
			System.out.println(aux);
		}
		return aDevolver;
	}
	
	
	public List<String> getXsdUuid()
	{
		String aux="";
		List<String> aDevolver = new ArrayList<String>();
		aDevolver=xsd.listarUUIDXSD();
		
		Iterator<String> iterador= aDevolver.iterator();
		while(iterador.hasNext())
		{
			aux=iterador.next();
			System.out.println(aux);
		}
		return aDevolver;
	}
	
	
	public List<String> getXsltUuid()
	{
		String aux="";
		List<String> aDevolver = new ArrayList<String>();
		aDevolver=xslt.listarUUIDXSLT();

		Iterator<String> iterador= aDevolver.iterator();
		while(iterador.hasNext())
		{
			aux=iterador.next();
			System.out.println(aux);
		}
		return aDevolver;
	}
	
	public String getHtmlContent(String uuid) throws Exception
	{
		String retorno=html.get(uuid);
		return retorno;
	}
	
	public String getXmlContent(String uuid) throws Exception
	{
		String retorno=xml.getXML(uuid);
		return retorno;
	}
	
	public String getXsdContent(String uuid) throws Exception
	{
		String retorno=xsd.getXSD(uuid);
		return retorno;
	}
	
	public String getXsltContent(String uuid) throws Exception
	{
		String retorno=xslt.getXSLT(uuid);
		return retorno;
	}

	public String getXSDasociadoXSLT(String uuid) throws Exception
	{
		String retorno=xslt.getXSDfromXSLT(uuid);
		return retorno;
	}
	
}
