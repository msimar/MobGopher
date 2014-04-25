package com.univ.helsinki.app.sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SensorUnit {

	private boolean status;

	private String name;

	private String vendor;

	private String typeValue;

	private List<Map<String, Float>> values = new ArrayList<Map<String, Float>>();

	public SensorUnit() {
		// TODO Auto-generated constructor stub
	}

	public SensorUnit(String name, String vendor, String typeValue) {
		this.name = name;
		this.vendor = vendor;
		this.typeValue = typeValue;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

	public List<Map<String, Float>> getValues() {
		return values;
	}

	public void setValues(List<Map<String, Float>> values) {
		this.values = values;
	}
}
