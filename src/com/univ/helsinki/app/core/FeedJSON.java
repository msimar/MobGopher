package com.univ.helsinki.app.core;


public final class FeedJSON {
	private String latitude;
	private String longitude;
	
	private String address;
	
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "(" + this.latitude + ", " + this.longitude + "), \n Address : " +  this.address;
	}
}
