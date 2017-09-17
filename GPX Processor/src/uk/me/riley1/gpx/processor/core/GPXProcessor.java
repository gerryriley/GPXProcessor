package uk.me.riley1.gpx.processor.core;

import org.w3c.dom.*;

import java.io.File;
import java.net.URL;
import java.util.Date;

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
	private String direction = "backward";
	private File file;
	private double markerInterval = 1;
	private UnitConverter intervalUnit = UnitConverter.MILES;
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
		setFile(file);
		gpxDoc = builder.parse(file);
		routes = gpxDoc.getElementsByTagName(ROUTE);
	}

	public NodeList getRoutes() {
		return routes;
	}

	
	public void addMarkers() {
		
		for (int i = 0; i < routes.getLength(); i++) {
			Element e = (Element) routes.item(i);
			GPXRoute route = new GPXRoute(e, isForward());
			
			route.addMarkers(getExplicitInterval());
		}

	}
	
	public void removeMarkers() {
		for (int i = 0; i < routes.getLength(); i++) {
			Element e = (Element) routes.item(i);
			GPXRoute route = new GPXRoute(e, true);
			route.removeMarkers();
		}
	}

	
	public void generateOutput() throws  TransformerException {
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		File file = getFile();
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
	

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public boolean isForward() {
		return "forward".equalsIgnoreCase(getDirection());
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public double getMarkerInterval() {
		return markerInterval;
	}

	public void setMarkerInterval(double markerInterval) {
		this.markerInterval = markerInterval;
	}

	public UnitConverter getIntervalUnit() {
		return intervalUnit;
	}

	public void setIntervalUnit(UnitConverter intervalUnit) {
		this.intervalUnit = intervalUnit;
	}
	private UnitConverter getExplicitInterval() {
		
		getIntervalUnit().normalize(getMarkerInterval());
		return getIntervalUnit();
	}

}
