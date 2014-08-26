package com.cohome.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cohome.util.JsonRequest;
import com.example.androidspike.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class ViewAd extends Activity {
    private static final String LOG_TAG = "CoHomeAndroid";
    private GoogleMap mMap;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_view_ad);
		JSONObject j;
		Intent intent = getIntent();
		String response = intent.getStringExtra(SearchAd.EXTRA_MESSAGE);
		//System.out.println(message);
		try {
			j = new JSONObject(response);
		
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			CameraPosition cameraPosition = new CameraPosition.Builder()
		    .target(new LatLng(Double.parseDouble(j.getJSONObject("coordinate").getString("lat")),Double.parseDouble(j.getJSONObject("coordinate").getString("lng"))))  // Sets the center of the map to Mountain View
		    .zoom(13)                   // Sets the zoom
		    .build();                   // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			JSONArray a = j.getJSONArray("annunci");
			for(int index=0; index<a.length()-1;index++)
				addMarker((JSONObject)a.get(index));	
		} catch (JSONException e) {
			e.printStackTrace();
		}
 	}
	public void addMarker(JSONObject j){
		try {
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.addMarker(new MarkerOptions()
			        .position(new LatLng(Double.parseDouble(j.getString("lat")),Double.parseDouble(j.getString("lng"))))
			        .title(j.getString("titolo")) );
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_ad, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
