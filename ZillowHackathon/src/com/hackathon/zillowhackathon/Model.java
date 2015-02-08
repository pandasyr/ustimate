package com.hackathon.zillowhackathon;

import java.util.HashMap;

import android.os.Handler.Callback;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Model {
	public static House currentHouse;
	public static String housesJson;
	
	public static final String[] Preferences = {
		"School",
		"Healthcare",
		"Transportation",
		"Safety"
	};
	
	public static HashMap<String, UserPreference> userPreferences = new HashMap<String, UserPreference>();
	
	public static class House {
		public int listedPrice;
		public int zestimate;
		public HashMap<String, Double> priceBreakdown;
		
		public House(int listedPrice, int zestimate, HashMap<String, Double> priceBreakdown) {
			this.listedPrice = listedPrice;
			this.zestimate = zestimate;
			this.priceBreakdown = priceBreakdown;
		}
		
		public int getYourValue() {
			int yourValue = zestimate;
			for(String key : Preferences) {
				UserPreference up = userPreferences.get(key);
				double breakdown = priceBreakdown.get(key);
				yourValue += breakdown * zestimate * (up.score * 0.02 - 1.0);
			}
			return yourValue;
		}
		
		public String getBreakdownZestimateValue() {
			String result = "";
			int base = zestimate;
			for(String key : Preferences) {
				double breakdown = priceBreakdown.get(key);
				String percent = " (" + (breakdown>0?"+":"") + (int)(breakdown * 100) + "%) ";
				result +="$" + (int) (breakdown * zestimate) + percent + "\n";
				base -= breakdown * zestimate;
			}
			result = "$" + (int)base + "\n" + result;
			return result;
		}
		
		public String getBreakdownYourValue() {
			String result = "";
			int base = zestimate;
			for(String key : Preferences) {
				UserPreference up = userPreferences.get(key);
				double breakdown = priceBreakdown.get(key);
				String percent = " (" + (breakdown>0?"+":"") + (int)(breakdown * 100 * (up.score*0.02)) + "%)";
				result += "$" + (int) (breakdown * zestimate * (up.score*0.02)) + percent + "\n";
				base -= breakdown * zestimate;
			}
			result = "$" + (int)base + "\n" + result;
			return result;
		}
	}
	
	public static class UserPreference {
		public String key;
		public SeekBar seekBar;
		public int score;
		public Callback callback;
		public UserPreference(int score, SeekBar seekBar, String key, Callback callback) {
			this.score = score;
			this.key = key;
			this.seekBar = seekBar;
			this.callback = callback;
			userPreferences.put(key, this);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					UserPreference.this.score = progress;
					UserPreference.this.callback.handleMessage(null);
					// TODO Auto-generated method stub
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}});
		}
		
	};
	
	
}
