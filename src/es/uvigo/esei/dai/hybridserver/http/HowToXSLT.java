package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

public class HowToXSLT {
	// segun pienso yo, esta clase deberia de tener 2 funciones,
	// una que puede ser muy similar a la que hay, que devuelva 0 o -1 si el XML
	// se puede transformar o no,
	// y otra que devuelva el contenido transformado mediante un String

	public static int transformacion() throws FileNotFoundException,
			TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();

		Source xslDoc = new StreamSource("configuration.xslt");
		Source xmlDoc = new StreamSource("configuration.xml");

		String outputFileName = "configuration.html";

		FileOutputStream htmlFile = new FileOutputStream(outputFileName);
		Transformer trasform = tFactory.newTransformer(xslDoc);
		trasform.transform(xmlDoc, new StreamResult(htmlFile));

		if (!tFactory.equals(null)) {
			System.out.println("Transformacion realizada con exito");
			return 0;
		} else {
			return -1;
		}

	}
	
	static boolean validateAgainstXSD(String xml, String xsl) throws IOException, ParserConfigurationException, SAXException
	{
		PrintWriter writer;
		PrintWriter writer2;
		
			writer = new PrintWriter("aux1.xsl", "UTF-8");
			writer.println(xsl);
			writer.close();
			writer2 = new PrintWriter("aux2.xml", "UTF-8");
			writer2.println(xml);
			writer2.close();
		
		
//	    try
//	    {
//	        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
//	        Schema schema = factory.newSchema(new StreamSource("aux1.xsl"));
//	        Validator validator = schema.newValidator();
//	        validator.validate(new StreamSource("aux2.xml"));
//	        return true;
//	    }
//	    catch(Exception ex)
//	    {
//	        return false;
//	    }
		
//		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		Schema schema;
//		
//			schema = schemaFactory.newSchema(new File("aux1.xsl"));
//
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//		factory.setNamespaceAware(true);
//		factory.setSchema(schema);
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		builder.parse(new File("aux2.xml"));
//		if(factory.isValidating()){
//			return true;
//			
//		}else{
//			return false;
//			
//		}
			
			   
	        try {
	            SchemaFactory factory = 
	                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	            Schema schema = factory.newSchema(new File("aux1.xsl"));
	            Validator validator = schema.newValidator();
	            validator.validate(new StreamSource(new File("aux2.xml")));
	        } catch (IOException | SAXException e) {
	            System.out.println("Exception: "+e.getMessage());
	            return false;
	        }
	        return true;
		
	}

	public static String transformacion2(String xml, String xsl) throws FileNotFoundException,
			TransformerException, UnsupportedEncodingException {
		TransformerFactory tFactory = TransformerFactory.newInstance();

		PrintWriter writer;
		PrintWriter writer2;
		
			writer = new PrintWriter("test1.xsl", "UTF-8");
			writer.println(xsl);
			writer.close();
			writer2 = new PrintWriter("test2.xml", "UTF-8");
			writer2.println(xml);
			writer2.close();
		System.out.println("asdasd" + xml + "qweqweqwe" + xsl);
		
		Source xslDoc = new StreamSource("test1.xsl");
		Source xmlDoc = new StreamSource("test2.xml");

		String outputFileName = "sample1.html";

		FileOutputStream htmlFile = new FileOutputStream(outputFileName);
		Transformer trasform = tFactory.newTransformer(xslDoc);
		trasform.transform(xmlDoc, new StreamResult(htmlFile));
		String ret = null;
		try {
			System.out.println("asd" + readFile(outputFileName));
			ret = readFile(outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;

	}
	
	private static String readFile(String filename) throws IOException {
		   String lineSep = System.getProperty("line.separator");
		   BufferedReader br = new BufferedReader(new FileReader(filename));
		   String nextLine = "";
		   StringBuffer sb = new StringBuffer();
		   while ((nextLine = br.readLine()) != null) {
		     sb.append(nextLine);
		     sb.append(lineSep);
		   }
		   return sb.toString();
		}

	/*
	 * package es.uvigo.esei.dai.hybridserver.http;
	 * 
	 * import java.io.File; import java.io.FileNotFoundException; import
	 * java.io.FileOutputStream; import java.io.IOException; import
	 * java.io.StringWriter;
	 * 
	 * import javax.xml.XMLConstants; import javax.xml.parsers.DocumentBuilder;
	 * import javax.xml.parsers.DocumentBuilderFactory; import
	 * javax.xml.parsers.ParserConfigurationException; import
	 * javax.xml.transform.OutputKeys; import javax.xml.transform.Transformer;
	 * import javax.xml.transform.TransformerConfigurationException; import
	 * javax.xml.transform.TransformerException; import
	 * javax.xml.transform.TransformerFactory; import
	 * javax.xml.transform.dom.DOMSource; import
	 * javax.xml.transform.stream.StreamResult;
	 * 
	 * import org.w3c.dom.Document; import org.xml.sax.SAXException;
	 * 
	 * public class HowToXSLT { // segun pienso yo, esta clase deberia de tener
	 * 2 funciones, // una que puede ser muy similar a la que hay, que devuelva
	 * 0 o -1 si el XML // se puede transformar o no, // y otra que devuelva el
	 * contenido transformado mediante un String
	 * 
	 * public static int transformacion() throws
	 * TransformerConfigurationException {
	 * 
	 * TransformerFactory tFactory = TransformerFactory.newInstance();
	 * 
	 * Transformer transformer = tFactory .newTransformer(new
	 * javax.xml.transform.stream.StreamSource( "configuracion.xsl"));
	 * 
	 * try { transformer.transform(new javax.xml.transform.stream.StreamSource(
	 * "configuracion.xml"), new javax.xml.transform.stream.StreamResult(new
	 * FileOutputStream("configuracion.html"))); transformer.toString();
	 * 
	 * System.out.println("Transformacion realizada con exito"); return 0; }
	 * catch (Exception e) { return -1; } }
	 * 
	 * public static String toXML(Document document) throws
	 * TransformerException//como le pasamos el XML? { TransformerFactory
	 * factory=TransformerFactory.newInstance();
	 * factory.setAttribute("indent-number", 3);
	 * 
	 * Transformer transformer = factory.newTransformer();
	 * transformer.setOutputProperty(OutputKeys.INDENT, "yes"); StringWriter
	 * writer = new StringWriter();
	 * 
	 * transformer.transform(new DOMSource(document), new StreamResult(writer));
	 * 
	 * 
	 * return writer.toString(); }
	 * 
	 * 
	 * public static Document loadAndValidate(String documentPath) throws
	 * ParserConfigurationException, SAXException, IOException {
	 * 
	 * 
	 * 
	 * DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
	 * factory.setValidating(true); factory.setNamespaceAware(true);
	 * factory.setAttribute
	 * ("http://java.sun.com/xml/jaxp/properties/schemaLanguage"
	 * ,XMLConstants.W3C_XML_SCHEMA_NS_URI);
	 * 
	 * 
	 * DocumentBuilder builder= factory.newDocumentBuilder();
	 * 
	 * 
	 * return builder.parse(new File(documentPath)); }
	 * 
	 * 
	 * }
	 */

}
