package uk.me.riley1.gpx.processor;

import javax.naming.directory.InvalidAttributesException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class GPXWayPoint {
	
	private Element Element;
	private boolean forward;

	public GPXWayPoint(Element wayPoint, boolean forward) {
		
		this.Element = wayPoint;
		this.forward = forward;				
	}

	public void addMarker(int miles) {
		
		Element wpName = (Element) getElement().getElementsByTagName(GPXProcessor.WP_NAME).item(0);
		Node text = wpName.getFirstChild();
		String name = text.getNodeValue();
		name = " Mile";
		if (miles > 1) {
			name+= "s";
		}
		text.setNodeValue(String.valueOf(miles) + name);
		Element sym = (Element) getElement().getElementsByTagName(GPXProcessor.WP_SYM).item(0);
		text = sym.getFirstChild();
		text.setNodeValue("Pin");
	}

	public Element getElement() {
		return Element;
	}

	public boolean isForward() {
		return forward;
	}

	public double getDBP(GPXWayPoint otherPoint) {
		
		Element fromPt = getElement();
		Element toPt = otherPoint.getElement();
		double distance = 0;
		double lat1 = Double.parseDouble(fromPt.getAttribute("lat"));
		double lat2 = Double.parseDouble(toPt.getAttribute("lat"));
		double lon1 = Double.parseDouble(fromPt.getAttribute("lon"));
		double lon2 = Double.parseDouble(toPt.getAttribute("lon"));
		double lat1r = Math.toRadians(lat1);
		double lat2r = Math.toRadians(lat2);
		double deltaLat = Math.toRadians(lat2 - lat1);
		double deltaLon = Math.toRadians(lon2 - lon1);
		
		// This calculation takes two latitudes in radians and the differences between latitudes and longitudes
		// and calculates the straight line great circle distance using the haversine formula see
		// http://www.movable-type.co.uk/scripts/latlong.html
		
		double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
		        Math.cos(lat1r) * Math.cos(lat2r) *
		        Math.sin(deltaLon/2) * Math.sin(deltaLon)/2;
		
		distance = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)) * 6371e3;
	
		try {
			UnitConverter conv = new UnitConverter(UnitConverter.METERS, distance);
			conv.setUnit(UnitConverter.MILES);
			distance = conv.convert();
			
		} catch (InvalidAttributesException e) {

			e.printStackTrace();
		}
		return distance;
	}

	public void removeMarker() {
		
		Element sym = (Element) getElement().getElementsByTagName(GPXProcessor.WP_SYM).item(0);
		Node text = sym.getFirstChild();
		String s = "Pin";
		if (s.equalsIgnoreCase(text.getNodeValue())) {
			text.setNodeValue("TinyDot");
		
			Element wpName = (Element) getElement().getElementsByTagName(GPXProcessor.WP_NAME).item(0);
			text = wpName.getFirstChild();
			text.setNodeValue("XXX");
		}
		
	}

}
