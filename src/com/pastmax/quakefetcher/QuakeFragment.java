package com.pastmax.quakefetcher;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Stephen Walton
 * 
 * This fragment gets the current Quake and displays it
 *
 */
public class QuakeFragment extends Fragment {
	private static final String TAG = "QuakeFragment";
	public static final String EXTRA_QUAKE_ID = "com.pastmax.quakefetcher.quake_id";
	
	private View v;
	private Quake mQuake;
	private String mQuakeId;

	// For compatibility with all devices
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		getActivity().setTitle(R.string.quake_title_label);
		
		//mQuakeId = (String)getActivity().getIntent().getSerializableExtra(EXTRA_QUAKE_ID);
		String mQuakeId = (String)getArguments().getSerializable(EXTRA_QUAKE_ID);
		
		mQuake = QuakeStore.get(getActivity()).getQuake(mQuakeId);
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_quake, parent, false);

		if (mQuake != null) {
	        TextView titleTextView = (TextView) v
					.findViewById(R.id.quake_titleTextView);
			
	        if (mQuake.getMagnitude() >= 4.5) {
	        	titleTextView.setTextColor(Color.parseColor("#FF0000"));
			} else if (mQuake.getMagnitude() >= 2.5) {
				titleTextView.setTextColor(Color.parseColor("#009999"));
			} else {
				titleTextView.setTextColor(Color.BLACK);
			}

			titleTextView.setText(mQuake.getMagnitude() + " magnitude " + mQuake.getPlace());

	        TextView magnitudeTextView = (TextView) v
					.findViewById(R.id.quake_magnitudeTextView);
	        String magnitude = new Double(mQuake.getMagnitude()).toString();
	        magnitudeTextView.setText(magnitude);

	        TextView placeTextView = (TextView) v
					.findViewById(R.id.quake_placeTextView);
	        placeTextView.setText(mQuake.getPlace());
		
	        TextView longitudeTextView = (TextView) v
					.findViewById(R.id.quake_longitudeTextView);
	        String longitude = new Double(mQuake.getLongitude()).toString();
	        longitudeTextView.setText(longitude);

	        TextView latitudeTextView = (TextView) v
					.findViewById(R.id.quake_latitudeTextView);
	        String latitude = new Double(mQuake.getLatitude()).toString();
	        latitudeTextView.setText(latitude);

	        TextView depthTextView = (TextView) v
					.findViewById(R.id.quake_depthTextView);
	        depthTextView.setText(mQuake.getDepth());

	        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy h:ma");
	        
	        TextView dateTextView = (TextView) v
					.findViewById(R.id.quake_dateTextView);
	        dateTextView.setText(formatter.format(mQuake.getDate()));

	        TextView tzTextView = (TextView) v
					.findViewById(R.id.quake_tzTextView);
	        tzTextView.setText(mQuake.getTz());

	        TextView eventTypeTextView = (TextView) v
					.findViewById(R.id.quake_eventTypeTextView);
	        eventTypeTextView.setText(mQuake.getEventType());

	        TextView statusTextView = (TextView) v
					.findViewById(R.id.quake_statusTextView);
	        statusTextView.setText(mQuake.getStatus());
	        
	        if (getActivity().findViewById(R.id.detailFragmentContainer) == null) {
		        ((QuakeActivity)getActivity()).setUrl(mQuake.getUrl());
	        } else {
		        ((QuakeListActivity)getActivity()).setUrl(mQuake.getUrl());
	        }
	        
		}

		return v;
	}
	
	public static QuakeFragment newInstance(String quakeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_QUAKE_ID, quakeId);
		
		QuakeFragment fragment = new QuakeFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
		
}
