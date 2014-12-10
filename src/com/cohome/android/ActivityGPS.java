package com.cohome.android;

import java.util.Date;

import com.example.androidspike.R;
import com.example.androidspike.R.layout;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ActivityGPS extends Activity {
	private String providerId = LocationManager.GPS_PROVIDER;
	TextView available,enabled,provider,timestamp,latitude,longitude,speed,altitude;
	
	private LocationListener myLocationListener = new LocationListener() { 
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			available = (TextView) findViewById(R.id.available);
			if (status == LocationProvider.AVAILABLE) {
			    available.setText("TRUE");
			} 
			else {
				available.setText("FALSE"); 
			}
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			enabled = (TextView) findViewById(R.id.enabled);
			enabled.setText("TRUE"); 
		} 
 
		@Override 
		public void onProviderDisabled(String provider) {
			enabled = (TextView) findViewById(R.id.enabled);
			enabled.setText("FALSE");
		}
		 
		@Override
		public void onLocationChanged(Location location) { 
			updateLocationData(location);
		}

	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_gps);
	}
	
	protected void onResume() {	 
		super.onResume();
		available = (TextView) findViewById(R.id.available);
		enabled = (TextView) findViewById(R.id.enabled);
		provider = (TextView) findViewById(R.id.provider);

		provider.setText(providerId);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); 
		LocationProvider provider = locationManager.getProvider(providerId);
		if (provider == null) { 
			available.setText("FALSE"); 
		} 
		else {
			available.setText("TRUE"); 
			boolean gpsEnabled = locationManager.isProviderEnabled(providerId);
			if (gpsEnabled) { 
				enabled.setText("TRUE");
			} 
			else {
				enabled.setText("FALSE");
			}
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
			if (location != null) {
				updateLocationData(location);
			} 
			locationManager.requestLocationUpdates(providerId, 15, 1, myLocationListener);
		}
	 }
	
	@Override 
	protected void onPause() {
		super.onPause(); 
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.removeUpdates(myLocationListener);
	}
	
	private void setTextViewValue(int textViewId, String value) { 
		  TextView textView = (TextView) findViewById(textViewId);
		  textView.setText(value);
	}
	
	private void updateLocationData(Location location) {
		timestamp = (TextView) findViewById(R.id.timestamp);
		latitude = (TextView) findViewById(R.id.latitude);
		longitude = (TextView) findViewById(R.id.longitude);
		altitude = (TextView) findViewById(R.id.altitude);
		speed = (TextView) findViewById(R.id.speed);

		Date time = new Date(location.getTime()); 
		timestamp.setText(time.toString());
		 
		double lat = location.getLatitude(); 
		latitude.setText(String.valueOf(lat));
		
		double lng = location.getLongitude(); 
		longitude.setText(String.valueOf(lng));
		 
		if (location.hasAltitude()) { 
			double alt = location.getAltitude();
			altitude.setText(String.valueOf(alt));
		} 
		 
		if (location.hasSpeed()) {
		   float s = location.getSpeed();
		   speed.setText(String.valueOf(s)); 
		}
	 }

}
