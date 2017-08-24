package uk.me.riley1.gpx.processor;

import org.w3c.dom.Element;

public class GPXRoute {
	
	private Element thisElement;
	private boolean forward;
	private GPXWayPoints wayPoints;
	

	public GPXRoute(Element route, boolean forward) {
		thisElement = route;
		this.forward = forward;
		wayPoints = new GPXWayPoints(this);
	}
	
	public void addMarkers(UnitConverter converter) {
		
		int miles = 0;
		double aMile = 0L;
		GPXWayPoints wayPoints = getWayPoints();
		GPXWayPoint fromPt = wayPoints.getFirstWayPoint();
		GPXWayPoint toPt = wayPoints.getNextWayPoint();
		while (fromPt != null && toPt != null) {
			aMile += fromPt.getDBP(toPt);
			if (aMile > 1) {
				//We have a mile so lets change previous point
				miles++;
				aMile-= 1;
				fromPt.addMarker(miles);
			}
			fromPt = toPt;
			toPt = wayPoints.getNextWayPoint();
		}
	}

	public void removeMarkers() {
		
		GPXWayPoints wayPoints = getWayPoints();
		GPXWayPoint fromPt = wayPoints.getFirstWayPoint();
		while (fromPt != null) {
			fromPt.removeMarker();
			fromPt = wayPoints.getNextWayPoint();
		}

	}
	
	public Element getElement() {
		return thisElement;
	}

	public boolean isForward() {
		return forward;
	}

	public GPXWayPoints getWayPoints() {
		return wayPoints;
	}


}
