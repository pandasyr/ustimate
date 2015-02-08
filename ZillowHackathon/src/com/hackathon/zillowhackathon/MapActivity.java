package com.hackathon.zillowhackathon;

import java.util.HashMap;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MapActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent("com.hackathon.zillowhackathon.main");
			this.startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	WebView wv = null;
	@Override
	public void onResume(){
		super.onResume();
		if(wv != null) return;
		wv = (WebView) findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebChromeClient(new WebChromeClient() {
			 public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
			    callback.invoke(origin, true, false);
			 }
			});
		WebView.setWebContentsDebuggingEnabled(true);
		wv.getSettings().setAllowUniversalAccessFromFileURLs(true);
		wv.addJavascriptInterface(ji, "ji");
		wv.loadUrl("file:///android_asset/" + "Search.html");
	}
	
	class JsInterface {
		@JavascriptInterface
	    public void onLabelClicked(int price, int zestimate, double safety, double transportation, double school, double healthcare){
			HashMap<String, Double> priceBreakDown = new HashMap<String, Double>();
			priceBreakDown.put(Model.Preferences[0], school);
			priceBreakDown.put(Model.Preferences[1], healthcare);
			priceBreakDown.put(Model.Preferences[2], transportation);
			priceBreakDown.put(Model.Preferences[3], safety);
			Model.currentHouse = new Model.House(price, zestimate,priceBreakDown);
			Intent intent = new Intent("com.hackathon.zillowhackathon.main");
			MapActivity.this.startActivity(intent);
		}
		
		@JavascriptInterface
		public int addHouse(int price, int zestimate, double p1, double p2, double p3, double p4){
			HashMap<String, Double> priceBreakDown = new HashMap<String, Double>();
			priceBreakDown.put(Model.Preferences[0], p1);
			priceBreakDown.put(Model.Preferences[1], p2);
			priceBreakDown.put(Model.Preferences[2], p3);
			priceBreakDown.put(Model.Preferences[3], p4);
			Model.House house = new Model.House(price, zestimate,priceBreakDown);
			return house.getYourValue();
		}
		
		@JavascriptInterface
		public void onPageReady() {
			MapActivity.this.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					wv.loadUrl("javascript:loadHouses(" + Model.housesJson + ")" );
				}});
		}
	}
	
	JsInterface ji = new JsInterface();
	
	
}
