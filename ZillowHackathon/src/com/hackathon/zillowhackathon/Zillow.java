package com.hackathon.zillowhackathon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.hackathon.zillowhackathon.Model.House;

import android.app.Activity;
import android.os.AsyncTask;

public class Zillow {
	public static final String zwsid = "X1-ZWz1e1igggcxl7_8qndf";
	
	public static Activity context;
	/*
	public static House getHouse(String address, String zip) {
		HouseData houseData = new HouseData(address, zip);
		HashMap<String, Double> priceBreakdown = new HashMap<String, Double>();
		priceBreakdown.put("School",getSchoolRate());
		priceBreakdown.put("Healthcare",getHealthcareRate());
		priceBreakdown.put("Transportation",getTransportationRate());
		priceBreakdown.put("Safety",getSafetyRate());
		
		JSONObject j;
		return null;
	}
	public static class HouseData {
		public double schoolRate = 0.0;
		public double healthcareRate = 0.0;
		public double transportationRate = 0.0;
		public double safetyRate = 0.0;
		public HouseData(final String address, final String zip) {
			context.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					final String urlForAddressSearch = "http://www.zillow.com/webservice/GetSearchResults.htm?zws-id=" + zwsid +"="+ address + "&citystatezip=" + zip ;
					new AsyncTask<String,String,String>(){

						@Override
						protected String doInBackground(String... params) {
							try{
							HttpClient httpclient = new DefaultHttpClient();
						    HttpResponse response = httpclient.execute(new HttpGet(urlForAddressSearch));
						    StatusLine statusLine = response.getStatusLine();
						    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
						        ByteArrayOutputStream out = new ByteArrayOutputStream();
						        response.getEntity().writeTo(out);
						        String responseString = out.toString();
						        out.close();
						        JSONObject jResult = new JSONObject(responseString);
						        jResult.get("response")
						        
						    } else{
						        response.getEntity().getContent().close();
						        throw new IOException(statusLine.getReasonPhrase());
						    }
							} catch(Exception e) {
								e.printStackTrace();
							}
							return null;
						}}.execute("");
				}});
		}
	}
	*/
}
