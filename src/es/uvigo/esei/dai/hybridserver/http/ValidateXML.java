package es.uvigo.esei.dai.hybridserver.http;

import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import javax.xml.validation.Validator;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
public class ValidateXML {
  private static int errorCount = 0;
  public static void main(String[] a) {
    if (a.length<2) {
      System.out.println("Usage:");
      System.out.println("java XsdSchemaValidator schema_file_name "
        + "xml_file_name");
    } else {
      String schemaName = a[0];
      String xmlName = a[1];
      Schema schema = loadSchema(schemaName);
      Document document = parseXmlDom(xmlName);
      validateXml(schema, document);
    }
  }
  public static int validateXml(Schema schema, Document document) {
	  int retorno=0;
	  try {
    	
      // creating a Validator instance
      Validator validator = schema.newValidator();
      System.out.println();
      System.out.println("Validator Class: "
        + validator.getClass().getName());

      // setting my own error handler
      validator.setErrorHandler(new MyErrorHandler());

      // validating the document against the schema
      validator.validate(new DOMSource(document));
      System.out.println();
      if (errorCount>0) {
    	  retorno= -1;
        System.out.println("Failed with errors: "+errorCount);
      } else {
    	  retorno=0;
        System.out.println("Passed.");
      }
      
      

    } catch (Exception e) {
      // catching all validation exceptions
      System.out.println();
      System.out.println(e.toString());
    }
	  
	  return retorno;
  }
  public static Schema loadSchema(String name) {
    Schema schema = null;
    try {
      String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      SchemaFactory factory = SchemaFactory.newInstance(language);
      schema = factory.newSchema(new File(name));
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return schema;
  }
  public static Document parseXmlDom(String name) {
    Document document = null;
    try {
      DocumentBuilderFactory factory 
         = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.parse(new File(name));
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return document;
  }
  static class MyErrorHandler implements ErrorHandler {
    public void fatalError( SAXParseException e )
       throws SAXException {
      System.out.println(e.toString());
      throw e;
    }
    public void error( SAXParseException e ) throws SAXException {
      System.out.println(e.toString());
      errorCount++;
      // continue with validatin process
      // throw e;
    }
    public void warning( SAXParseException e ) throws SAXException {
      System.out.println(e.toString());
    }
  }
}