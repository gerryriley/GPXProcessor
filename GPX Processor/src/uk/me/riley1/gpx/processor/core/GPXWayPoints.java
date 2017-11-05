package uk.me.riley1.gpx.processor.core;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GPXWayPoints {
	
	private GPXRoute route;
	private int index;
	private NodeList nodeList;

	public GPXWayPoints(GPXRoute route) {
		this.route = route;
		nodeList = route.getElement().getElementsByTagName(GPXProcessor.WAYPOINT);
		index = route.isForward() ? 0 : nodeList.getLength() - 1;
	}
	
	public GPXWayPoint getFirstWayPoint(boolean direction) {
		
		index = direction ? 0 : nodeList.getLength() - 1;
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

	public GPXWayPoint getLastWayPoint(boolean direction) {
		
		index = direction ? nodeList.getLength() - 1 : 0;
		return getNextWayPoint();
	}
	
	public void insertWaypoint(GPXWayPoint newPt, GPXWayPoint refPt) {
	
		route.getElement().insertBefore(newPt.getElement(), refPt.getElement());
		// backtrack on index to support the new way point.
		//index = route.isForward() ? --index : --index;
	}
}
