package uk.me.riley1.gpx.processor;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GPXWayPoints {
	
	private GPXRoute route;
	private int index;
	private NodeList nodeList;

	public GPXWayPoints(GPXRoute route) {
		this.route = route;
		route.getElement();
		nodeList = route.getElement().getElementsByTagName(GPXProcessor.WAYPOINT);
	}
	
	public GPXWayPoint getFirstWayPoint() {
		
		index = route.isForward() ? 0 : nodeList.getLength() - 1;
		return getNextWayPoint();
	}
	
	public GPXWayPoint getNextWayPoint() {
		
		GPXWayPoint wayPoint = null;
		if(index < nodeList.getLength() && index > -1) {
			Element wp = (Element) nodeList.item(index);
			wayPoint = new GPXWayPoint(wp, route.isForward());
			index = route.isForward() ? ++index : --index;
		}
		return wayPoint;
	}
	

}
