package uk.me.riley1.gpx.processor;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.naming.directory.InvalidAttributesException;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class GPXProcessor {
	
	private Document gpxDoc;
	private NodeList routes;
	public static String ROUTE = "rte";
	public static String WAYPOINT = "rtept";
	public static String WP_NAME = "name";
	public static String WP_SYM = "sym";
	
	public GPXProcessor(File file) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sFactory.newSchema(new URL("http://www.topografix.com/GPX/1/1/gpx.xsd"));
		factory.setSchema(schema);
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		gpxDoc = builder.parse(file);
		routes = gpxDoc.getElementsByTagName(ROUTE);
		routes = gpxDoc.getElementsByTagName(ROUTE);
	}

	public NodeList getRoutes() {
		return routes;
	}

	public GPXProcessor addMarkers() throws InvalidAttributesException {
		
		return addMarkers(true);
	}
	
	public GPXProcessor addMarkers(boolean forward) throws InvalidAttributesException {
		addMarkers(UnitConverter.MILES, forward);
		return this;
	}
	
	public void addMarkers(UnitConverter converter, boolean forward) {
		
		for (int i = 0; i < routes.getLength(); i++) {
			Element e = (Element) routes.item(i);
			GPXRoute route = new GPXRoute(e, forward);
			route.addMarkers(converter);
		}

	}
	
	public void removeMarkers() {
		for (int i = 0; i < routes.getLength(); i++) {
			Element e = (Element) routes.item(i);
			GPXRoute route = new GPXRoute(e, true);
			route.removeMarkers();
		}
	}

	
	public void generateOutput(File file) throws  TransformerException {
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		if (file.exists()) {
			String path = file.getAbsolutePath();
			File ren = new File(path + String.valueOf(new Date().getTime()));
			file.renameTo(ren);
			file = new File(path);
		}
				
		Result result = new StreamResult(file);
		Source source = new DOMSource(gpxDoc);
		transformer.transform(source, result);
	}
}
