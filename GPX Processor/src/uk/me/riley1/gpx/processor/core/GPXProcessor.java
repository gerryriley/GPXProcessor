package uk.me.riley1.gpx.processor.core;

import org.w3c.dom.*;

import uk.me.riley1.gpx.processor.ui.ConfigurationWindow.Configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class GPXProcessor {
	
	private Document gpxDoc;
	private List<GPXRoute> routes;
	private double markerInterval = 1;
	private Configuration config;
	private UnitConverter intervalUnit = UnitConverter.MILES;
	public static String ROUTE = "rte";
	public static String WAYPOINT = "rtept";
	public static String WP_NAME = "name";
	public static String WP_SYM = "sym";
	
	public GPXProcessor(Configuration config) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sFactory.newSchema(new URL("http://www.topografix.com/GPX/1/1/gpx.xsd"));
		factory.setSchema(schema);
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		this.config = config;
		gpxDoc = builder.parse(getFile());
		setRoutes(gpxDoc.getElementsByTagName(ROUTE));
	}

	public List<GPXRoute> getRoutes() {
		
		return routes;
	}
	
	private void setRoutes(NodeList nL) {
		
		routes = new ArrayList<GPXRoute>();
		for (int i = 0; i < nL.getLength(); i++) {
			GPXRoute route = new GPXRoute((Element) nL.item(i), isForward());
			routes.add(route);
		}
	}

	
	public void addMarkers() {
		
		for (GPXRoute route : getRoutes()) {
			
			route.addMarkers(getExplicitInterval());
		}

	}
	
	public void removeMarkers() {
		for (GPXRoute route : getRoutes()) {
			
			route.removeMarkers();
		}
	}

	
	public void generateOutput() throws  Exception {
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		GPXFile file = getFile();
		GPXFile original = file.renameFile();
		if (original == null)	 {
			throw new Exception("Unable to create a copy of the original GPX File before processing");
		}
		Result result = new StreamResult(original);
		Source source = new DOMSource(gpxDoc);
		transformer.transform(source, result);
	}

	public boolean isForward() {
		
		String s = (String) config.get(Configuration.DIRECTION);
		boolean direction = "forward".equalsIgnoreCase(s);
		return direction;
	}

	public GPXFile getFile() {

		return (GPXFile) config.get(Configuration.GPX_FILE);
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
	
	public boolean isStartFinish() {
		
		return (boolean) config.get(Configuration.START_FINISH);
	}


}
