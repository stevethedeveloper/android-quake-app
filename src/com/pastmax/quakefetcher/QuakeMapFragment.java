package com.pastmax.quakefetcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author Stephen Walton
 * 
 * Fragment to display the map
 * Adds to back stack
 * Plots whatever is in QuakeStore
 *
 */
public class QuakeMapFragment extends SupportMapFragment {
	private static final String TAG = "QuakeMapFragment";

	private GoogleMap mGoogleMap;

	private static final String DIALOG_FILTER = "filter";

	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy h:ma");
	ArrayList<Quake> mQuakes;
	float pinHue;

	public static QuakeMapFragment newInstance() {
		QuakeMapFragment qf = new QuakeMapFragment();
		return qf;
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		getActivity().setTitle(R.string.quake_title_label);

		// Navigation back home
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);

		mGoogleMap = getMap();

		mGoogleMap.setMyLocationEnabled(true);

		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		//Get last known location
		Location location = locationManager
				.getLastKnownLocation(locationManager.getBestProvider(criteria,
						true));
		//If a location is found, center on it, if not just plot the quakes
		if (location != null) {
			LatLng current = new LatLng(location.getLatitude(),
					location.getLongitude());

			mGoogleMap
					.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 0));

		} else {
			LatLng current = new LatLng(0, 0);
			mGoogleMap
					.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 0));
		}

		//Grab quakes from the singleton
		mQuakes = QuakeStore.get(getActivity().getApplicationContext()).getQuakes();
		//Add quakes to the map
		for (Quake q : mQuakes) {
			addMarker(q);
		}

		return v;
	}

	/**
	 * @param q - pass in a Quake object
	 */
	public void addMarker(Quake q) {
		//Get the point
		LatLng p = new LatLng(q.getLatitude(),
				q.getLongitude());

		//Blue markers for smaller quakes, red for larger ones
		if (q.getMagnitude() >= 4.5) {
			pinHue = BitmapDescriptorFactory.HUE_RED;
		} else {
			pinHue = BitmapDescriptorFactory.HUE_AZURE;
		}
		
		//Add the marker with a simple snippet displaying general information
		mGoogleMap.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory.defaultMarker(pinHue))
				.title(q.getTitle())
				.snippet("Time: " + formatter.format(q.getDate()) + " - Depth: " + q.getDepth())
				.position(p)
				);
	}
}
