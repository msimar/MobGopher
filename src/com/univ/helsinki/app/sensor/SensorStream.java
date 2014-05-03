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
/**
 * {"sensorDevice":{"brand":"samsung","device":"crater","id":"JDQ39",
 * "manufacturer":"samsung","model":"GT-I9152","osRelease":"4.2.2","osVersion":
 * "3.0.31-1128812"
 * ,"product":"craterxx"},"sensors":[{"name":"Sensor.TYPE_LOCATION"
 * ,"vendor":"","typeValue"
 * :"-111","values":[{"longitude":24.900528,"latitude":60.230667
 * }],"status":true}
 * ,{"name":"Sensor.TYPE_ACCELEROMETER","vendor":"","typeValue":
 * "1","values":[{"accelerometer_z"
 * :9.876082,"accelerometer_y":0.6907272,"accelerometer_x"
 * :-0.29568392}],"status"
 * :true},{"name":"Sensor.TYPE_ACCELEROMETER","vendor":"",
 * "typeValue":"1","values"
 * :[{"accelerometer_z":9.85214,"accelerometer_y":0.6619968
 * ,"accelerometer_x":-0.29209262
 * }],"status":true}],"timestamp":"26-04-2014 01:25:08"}
 */
