package com.hackathon.zillowhackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	ArrayList<SeekBar> sb = new ArrayList<SeekBar>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent("com.hackathon.zillowhackathon.map");
			this.startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();

		if(Model.housesJson == null) {
			StringBuilder buf=new StringBuilder();
			InputStream json;
			try {
				json = getAssets().open("input.txt");
				BufferedReader in=
						new BufferedReader(new InputStreamReader(json));
				String str;

				while ((str=in.readLine()) != null) {
					buf.append(str);
				}
				Model.housesJson = buf.toString();
				Log.e("HOUSE_JSON ", "" + Model.housesJson);
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Zillow.context = this;
		new Model.UserPreference(60, (SeekBar) findViewById(R.id.seekBar1), "School", seekBarCallback);
		new Model.UserPreference(60, (SeekBar) findViewById(R.id.seekBar2), "Healthcare", seekBarCallback);
		new Model.UserPreference(40, (SeekBar) findViewById(R.id.seekBar3), "Transportation", seekBarCallback);
		new Model.UserPreference(40, (SeekBar) findViewById(R.id.seekBar4), "Safety", seekBarCallback);

		if(Model.currentHouse == null) createHouse();
		refresh();
	}

	Callback seekBarCallback = new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			refresh();
			return false;
		}
	};

	public void refresh() {
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				((TextView) findViewById(R.id.listed_price)).setText("$" + Model.currentHouse.listedPrice);
				((TextView) findViewById(R.id.zestimate_value)).setText("$" + Model.currentHouse.zestimate);
				((TextView) findViewById(R.id.your_value)).setText("$" + Model.currentHouse.getYourValue());

				((TextView) findViewById(R.id.zestimate_value_details)).setText(Model.currentHouse.getBreakdownZestimateValue());
				((TextView) findViewById(R.id.your_value_details)).setText(Model.currentHouse.getBreakdownYourValue());

			}});
	}

	public void onButtonClick(View button) {
		if(button.getId() == R.id.button1) {
			setSeekBar(new int[]{30,50,80,40});
		} else if(button.getId() == R.id.button2) {
			setSeekBar(new int[]{50,20,30,70});
		} else if(button.getId() == R.id.button3) {
			setSeekBar(new int[]{0,80,80,80});
		} else if(button.getId() == R.id.button4) {
			setSeekBar(new int[]{80,50,20,60});
		}
	}

	public void onExpandClick(View button) {
		View zestimate = findViewById(R.id.zestimate_value_details);
		View yourValue = findViewById(R.id.your_value_details);
		View zestimateBar = findViewById(R.id.zestimate_value_details_bar);
		View yourvalueBar = findViewById(R.id.your_value_details_bar);
		View zestimateItem = findViewById(R.id.zestimate_value_details_items);
		View yourValueItem = findViewById(R.id.your_value_details_items);
		Button expand = (Button) findViewById(R.id.button5);
		if(View.GONE == findViewById(R.id.zestimate_value_details).getVisibility()) {
			zestimate.setVisibility(View.VISIBLE);
			yourValue.setVisibility(View.VISIBLE);
			zestimateItem.setVisibility(View.VISIBLE);
			yourValueItem.setVisibility(View.VISIBLE);
			zestimateBar.setVisibility(View.VISIBLE);
			yourvalueBar.setVisibility(View.VISIBLE);
			expand.setBackground(getResources().getDrawable(R.drawable.minus));
		} else {
			zestimate.setVisibility(View.GONE);
			yourValue.setVisibility(View.GONE);
			zestimateItem.setVisibility(View.GONE);
			yourValueItem.setVisibility(View.GONE);
			zestimateBar.setVisibility(View.GONE);
			yourvalueBar.setVisibility(View.GONE);
			expand.setBackground(getResources().getDrawable(R.drawable.plus));
		}
	}

	public void setSeekBar(int[] preference) {
		( (SeekBar) findViewById(R.id.seekBar1)).setProgress(preference[0]);
		( (SeekBar) findViewById(R.id.seekBar2)).setProgress(preference[1]);
		( (SeekBar) findViewById(R.id.seekBar3)).setProgress(preference[2]);
		( (SeekBar) findViewById(R.id.seekBar4)).setProgress(preference[3]);
	}

	// Testing Stub
	void createHouse() {
		HashMap<String, Double> priceBreakDown = new HashMap<String, Double>();
		priceBreakDown.put(Model.Preferences[0], 0.1);
		priceBreakDown.put(Model.Preferences[1], -0.05);
		priceBreakDown.put(Model.Preferences[2], -0.02);
		priceBreakDown.put(Model.Preferences[3], 0.05);
		Model.currentHouse = new Model.House(300000,320000,priceBreakDown);
	}
}
