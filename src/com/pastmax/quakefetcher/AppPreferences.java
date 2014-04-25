package com.pastmax.quakefetcher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author Stephen Walton
 * 
 * This is the default implementation of PreferenceActivity
 * This is the old way to handle preferences, the new way uses fragments, which isn't compatible with the support library and older devices
 *
 */
public class AppPreferences extends PreferenceActivity {

	//Suppressing this warning because the alternative is to use PreferenceFragment, which is not compatible with older devices
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

}
