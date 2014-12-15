package com.cohome.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.androidspike.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ActivityGPS extends Activity {
	private static final String LOG_TAG = "CoHomeAndroid";
	
	private String providerId = LocationManager.GPS_PROVIDER;
	TextView available,enabled,provider,timestamp,latitude,longitude,speed,altitude;
	private GoogleMap mMapView;
	
	private String annunci;
    public static String URL = "http://192.168.1.107:8080/CoHome-war/JSONServlet?op=cercaAnnunciFromGPS";
	//public static String URL = "http://172.16.126.219:8080/CoHome-war/JSONServlet?op=cercaAnnunciFromGPS";
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
			updateMapView(location);
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
			locationManager.requestLocationUpdates(providerId, 12000, 0, myLocationListener);
		}
		mMapView = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();
	 }
	
	@Override 
	protected void onPause() {
		super.onPause(); 
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.removeUpdates(myLocationListener);
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
	
	private void updateMapView(Location location) {
		double lat = location.getLatitude(); 
		double lng = location.getLongitude(); 
		JSONObject j;
		String URL2 = "";
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
	    .target(new LatLng(lat,lng))  	// Sets the center of the map to Mountain View
	    .zoom(13)
	    .build();                   	// Creates a CameraPosition from the builder
		mMapView.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		//try {
			JsonRequest a = new JsonRequest(mMapView);
			URL2 += URL+"&latitude="+lat+"&longitude="+lng;
			a.execute(new String[] { URL2.replace(" ", "") });
			
			/*j = new JSONObject();
			JSONArray arr = j.getJSONArray("annunci");
			for(int index=0; index<arr.length()-1;index++)
				addMarker((JSONObject)arr.get(index));	
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}	
	
	public void addMarker(JSONObject j){
		try {
			mMapView = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			mMapView.addMarker(new MarkerOptions()
			        .position(new LatLng(Double.parseDouble(j.getString("lat")),Double.parseDouble(j.getString("lng"))))
			        .title(j.getString("titolo")) );
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	private class JsonRequest extends AsyncTask<String, Void, String> {
		private GoogleMap mMapView;
		
		public JsonRequest(GoogleMap mMapView){
			this.mMapView=mMapView;
		}
		@Override
	    protected String doInBackground(String... urls) {
	        String output = null;
	        for (String url : urls) {
	            output = getOutputFromUrl(url);
	        }
	        return output;
	    }
		
	    private String getOutputFromUrl(String url) {
	        String output = null;
	        try {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpGet httpGet = new HttpGet(url);
	            HttpResponse httpResponse = httpClient.execute(httpGet);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            output = EntityUtils.toString(httpEntity);
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } 
	        return output;
	    }
	    
	    @Override
	    protected void onPostExecute(String output) {
	    	try {
		    	JSONObject j = new JSONObject(output);
				JSONArray arr;
				arr = j.getJSONArray("annunci");
				for(int index=0; index<arr.length()-1;index++){
					JSONObject x = (JSONObject)arr.get(index);
					mMapView.addMarker(new MarkerOptions()
					.position(new LatLng(Double.parseDouble(x.getString("lat")),Double.parseDouble(x.getString("lng"))))
			        .title(x.getString("titolo")) );
					Log.e(LOG_TAG, "Lat-Lng: "+x.getString("lat")+"--"+x.getString("lng"));
				}	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	}

}
