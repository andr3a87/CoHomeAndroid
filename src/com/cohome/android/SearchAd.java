package com.cohome.android;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.cohome.util.JsonRequest;
import com.cohome.util.PlacesAutoCompleteAdapter;
import com.example.androidspike.R;





//import com.example.androidspike.ViewAd;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchAd extends Activity implements OnClickListener,	OnItemClickListener {
    private static final String LOG_TAG = "CoHomeAndroid";
    public final static String EXTRA_MESSAGE = "annunci";
    public static final String URL = "http://192.168.1.100:8080/CoHome-war/JSONServlet?op=cercaAnnunci&location=Torino,TO,Italia";
	// Widget GUI
	TextView txtDateStart;
	ImageView imgCalendarStart;
	TextView txtDateEnd;
	ImageView imgCalendarEnd;
	
	TextView txtDayStart;
	TextView txtMonthStart;
	TextView txtYearStart;
	TextView txtDayOfWeekStart;
	
	TextView txtDayEnd;
	TextView txtMonthEnd;
	TextView txtYearEnd;
	TextView txtDayOfWeekEnd;
	
	ImageView imgAdd;
	ImageView imgLess;
	TextView nOspiti;
	
	ImageView cerca;
	String[] strDays = new String[]{
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thusday",
            "Friday",
            "Saturday"
          };
	String[] strMonth = new String[]{
            "January",
            "Febraury",
            "March",
            "April",
            "May",
            "June",
            "Jule",
            "August",
            "September",
            "October",
            "November",
            "December"
          };
	// Variable for storing current date and time
	private int mYear, mMonth, mDay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_ad);

		AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
		autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
		autoCompView.setOnItemClickListener(this);


		//btnCalendar = (Button)findViewById(R.id.button1);
		//System.out.println(btnCalendar);
		txtDateStart = (TextView) findViewById(R.id.textDateStart);
		txtDayStart =(TextView) findViewById(R.id.textDayStart);
		txtMonthStart =(TextView) findViewById(R.id.textMonthStart);
		txtYearStart =(TextView) findViewById(R.id.textYearStart);
		txtDayOfWeekStart= (TextView) findViewById(R.id.textDayOfWeekStart);
		imgCalendarStart = (ImageView) findViewById(R.id.boxCalendarStart);
		
		Calendar c = Calendar.getInstance();
		txtDateStart.setText(c.get(Calendar.DAY_OF_MONTH) + "-"+ (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
		txtDayStart.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
		txtMonthStart.setText(strMonth[c.get(Calendar.MONTH) - 1]);
		txtYearStart.setText(String.valueOf(c.get(Calendar.YEAR)));
		txtDayOfWeekStart.setText(strDays[c.get(Calendar.DAY_OF_WEEK) - 1]);
		
		imgCalendarStart.setOnClickListener(this);
		
		txtDateEnd = (TextView) findViewById(R.id.textDateEnd);
		txtDayEnd =(TextView) findViewById(R.id.textDayEnd);
		txtMonthEnd =(TextView) findViewById(R.id.textMonthEnd);
		txtYearEnd =(TextView) findViewById(R.id.textYearEnd);
		txtDayOfWeekEnd= (TextView) findViewById(R.id.textDayOfWeekEnd);
		imgCalendarEnd = (ImageView) findViewById(R.id.boxCalendarEnd);
		
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) +1);
		txtDateEnd.setText(c.get(Calendar.DAY_OF_MONTH) + "-"+ (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
		txtDayEnd.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
		txtMonthEnd.setText(strMonth[c.get(Calendar.MONTH) - 1]);
		txtYearEnd.setText(String.valueOf(c.get(Calendar.YEAR)));
		txtDayOfWeekEnd.setText(strDays[c.get(Calendar.DAY_OF_WEEK) - 1]);
		imgCalendarEnd.setOnClickListener(this);
		
		imgAdd = (ImageView) findViewById(R.id.imageAdd);
		imgLess = (ImageView) findViewById(R.id.imageLess);
		nOspiti =(TextView) findViewById(R.id.editTextOspiti);
		imgAdd.setOnClickListener(this);
		imgLess.setOnClickListener(this);
		
		cerca = (ImageView) findViewById(R.id.imageCerca);
		cerca.setOnClickListener(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_ad, menu);
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

	
	@Override
	public void onClick(View v) {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		// Launch Date Picker Dialog
		if(v == imgCalendarStart){
			DatePickerDialog dpd = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
							// Display Selected date in textbox
							txtDateStart.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
							txtDayStart.setText(String.valueOf(dayOfMonth));
							txtMonthStart.setText(strMonth[monthOfYear - 1]);
							txtYearStart.setText(String.valueOf(year));
							c.set(year, monthOfYear, dayOfMonth);
							txtDayOfWeekStart.setText(strDays[c.get(Calendar.DAY_OF_WEEK) - 1]);
						}
					}, mYear, mMonth, mDay);
			dpd.show();
		}
		if(v == imgCalendarEnd){
			DatePickerDialog dpd = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
							// Display Selected date in textbox
							txtDateEnd.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
							txtDayEnd.setText(String.valueOf(dayOfMonth));
							txtMonthEnd.setText(strMonth[monthOfYear - 1]);
							txtYearEnd.setText(String.valueOf(year));
							c.set(year, monthOfYear, dayOfMonth);
							txtDayOfWeekEnd.setText(strDays[c.get(Calendar.DAY_OF_WEEK) - 1]);
						}
					}, mYear, mMonth, mDay);
			dpd.show();
		}
		if(v == imgAdd){
			int n = Integer.parseInt(nOspiti.getText().toString());
			n++;
			nOspiti.setText(String.valueOf(n));
		}
		if(v == imgLess){
			int n = Integer.parseInt(nOspiti.getText().toString());
			n--;
			if(n>0)
			nOspiti.setText(String.valueOf(n));
		}
		if(v == cerca){
			JsonRequest a = new JsonRequest();
			a.execute(new String[] { URL });
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		 String str = (String) parent.getItemAtPosition(position);
	     Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	private class JsonRequest extends AsyncTask<String, Void, String> {
	    private static final String LOG_TAG = "CoHomeAndroid";
		
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
	    	Intent intent = new Intent(getApplicationContext(),ViewAd.class);
	    	intent.putExtra(EXTRA_MESSAGE, output);
	    	startActivity(intent);
	    }
	 
	}
	
	
		
}

