package com.univ.helsinki.app.core;

import java.util.Map;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.univ.helsinki.app.db.FeedResource;
import com.univ.helsinki.app.util.Constant;

public final class SensorFeed {

	private String name;
	
	private String vendor;

	private int typeValue;
	
	private String typeName;

	private String sensorKey;

	private Map<String, String> valueMap;
	
	public String getSensorKey() {
		return sensorKey;
	}

	public void setSensorKey(String sensorKey) {
		this.sensorKey = sensorKey;
	}

	public int getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(int typeValue) {
		this.typeValue = typeValue;
		this.setTypeName(FeedResource.getInstance().mAllSensorMap
				.get(this.typeValue));
		this.setSensorKey(Constant.sAllSensorPreferenceKeyMap
				.get(this.typeValue));
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
