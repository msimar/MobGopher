package com.univ.helsinki.app.sensor;

import java.util.ArrayList;
import java.util.List;

import android.text.format.DateFormat;

public class SensorStream {
	private List<SensorUnit> sensors;
	private StreamSource sensorDevice;
	private String timestamp = "";

	public SensorStream() {
		// TODO should read from string.xml file
		final String dateFormat = "dd-MM-yyyy hh:mm:ss";

		setTimestamp((DateFormat.format(dateFormat, new java.util.Date())
				.toString()));
		setSensorDevice(new StreamSource());
		setSensors(new ArrayList<SensorUnit>());
	}

	public List<SensorUnit> getSensors() {
		return sensors;
	}

	public void setSensors(List<SensorUnit> sensors) {
		this.sensors = sensors;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public StreamSource getSensorDevice() {
		return sensorDevice;
	}

	public void setSensorDevice(StreamSource sensorDevice) {
		this.sensorDevice = sensorDevice;
	}
}
