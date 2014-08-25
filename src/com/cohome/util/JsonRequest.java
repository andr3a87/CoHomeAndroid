package com.cohome.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.cohome.android.SearchAd;
import com.cohome.android.ViewAd;
import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class JsonRequest extends AsyncTask<String, Void, String> {
    private static final String LOG_TAG = "CoHomeAndroid";

	JSONObject json=null;
	String jsons=null;
    
	
	@Override
    protected String doInBackground(String... urls) {
        String output = null;
        for (String url : urls) {
            output = getOutputFromUrl(url);
        }
        return output;
    }
	
	public JSONObject getInfo(){
		return json;
	}
	public String getInfo2(){
		return jsons;
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
    	
    }
 
}
