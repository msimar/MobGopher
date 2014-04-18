package com.univ.helsinki.app.core;

import java.util.Map;

import com.univ.helsinki.app.db.FeedResource;

public final class SensorFeed {
	
	private String name;
	private String vendor;
	
	private int typeValue;
	private String typeName;

	public int getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(int typeValue) {
		this.typeValue = typeValue;
		this.setTypeName(FeedResource.getInstance().mAllSensorMap.get(typeValue));
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	private Map<String, String> valueMap;
	private boolean isAllowed;

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Map<String, String> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, String> valueMap) {
		this.valueMap = valueMap;
	}
}
