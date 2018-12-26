package com.yz.model;

import java.util.List;

public class BccCheckRoute {

	private String routeId;
	private String routeType;
	private String headRoute;
	private String bodyRoute;
	
	private List<BccFieldCheck> fieldCheckList;
	
	@Override
	public String toString() {
		return "BccCheckRoute:{routeId:" + routeId
				+ ",routeType:" + routeType
				+ ",headRoute:" + headRoute
				+ ",bodyRoute:" + bodyRoute
				+ "}";
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String getHeadRoute() {
		return headRoute;
	}

	public void setHeadRoute(String headRoute) {
		this.headRoute = headRoute;
	}

	public String getBodyRoute() {
		return bodyRoute;
	}

	public void setBodyRoute(String bodyRoute) {
		this.bodyRoute = bodyRoute;
	}
	
	public List<BccFieldCheck> getFieldCheckList() {
		return fieldCheckList;
	}

	public void setFieldCheckList(List<BccFieldCheck> fieldCheckList) {
		this.fieldCheckList = fieldCheckList;
	}
	
	
}
