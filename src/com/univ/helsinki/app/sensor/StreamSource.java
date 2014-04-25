package com.univ.helsinki.app.sensor;

public class StreamSource {

	private String osVersion = System.getProperty("os.version");
	private String osRelease = android.os.Build.VERSION.RELEASE;
	private String device = android.os.Build.DEVICE;
	private String model = android.os.Build.MODEL;
	private String product = android.os.Build.PRODUCT;
	private String brand = android.os.Build.BRAND;
//	private String display = android.os.Build.DISPLAY;
	private String id = android.os.Build.ID;
	private String manufacturer = android.os.Build.MANUFACTURER;
//	private String serial = android.os.Build.SERIAL;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "OS Version :" + osVersion 
				+ "\nOS Release :" + osRelease 
				+ "\nDevice :" + device 
				+ "\nModel :" + model 
				+ "\nProduct :" + product;
	}
}
