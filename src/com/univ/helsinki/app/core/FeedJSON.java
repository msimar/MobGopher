package com.univ.helsinki.app.core;


public final class FeedJSON {
	private String latitude;
	private String longitude;
	
	private String address;
	
	private String pressure = "NA";
	private String height = "NA"; 
	
	private String temperature = "NA";
	
	private String accelerometerX = "";
	private String accelerometerY = "";
	private String accelerometerZ = "";
	
	public void setPressure(String _pressure){
		this.pressure = _pressure;
	}
	
	public void setHeight(String _height){
		this.height = _height;
	}
	
	public void setTemperature(String _temperature){
		this.temperature = _temperature;
	}
	
	public void setLastKnowLocation(Double _latitude, Double _longitude){
		this.latitude = _latitude.toString();
		this.longitude = _longitude.toString();
	}
	
	public void setLastKnowLocation(String _latitude, String _longitude){
		this.latitude = _latitude;
		this.longitude = _longitude;
	}
	
	public void setAddres(String _address){
		this.address = _address;
	}
	
	public void  setAccelerometer(float x, float y, float z){
		this.accelerometerX = Float.toString(x) ;
		this.accelerometerY = Float.toString(y);
		this.accelerometerZ = Float.toString(z);
	}
	
	public void  setAccelerometer(String x, String y, String z){
		this.accelerometerX = x ;
		this.accelerometerY = y;
		this.accelerometerZ = z;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return " Accelerometer(" + this.accelerometerX 	+ ", " + this.accelerometerY + ", " + this.accelerometerZ + "), " +
				"\n\n Geo(" + this.latitude 	+ ", " + this.longitude + "), " +
				"\n\n Address : " 	+  this.address + 
				"\n\n Pressume : " 	+ this.pressure +
				"\n\n Height : " 	+ this.height +
				"\n\n Temperature : " 	+ this.temperature +
				"\n\n" 	+ new DeviceFeed() ;
		
	}
}
