package com.pastmax.quakefetcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * @author Stephen Walton
 *
 *	The app's main activity's fragment.
 *
 *	Contains an AsyncTask and custom adapter.
 *
 */
public class QuakeListFragment extends ListFragment {
	private static final String TAG = "QuakeListFragment";

	private static final String DIALOG_FILTER = "filter";
	private static final int REQUEST_FILTER = 0;
	private Callbacks mCallbacks;

	ArrayList<Quake> mItems;
	Toast updateToast;
	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy h:ma");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	String mMagnitude;
	String mTime;
	String selectedTime;
	String mDistance;

	
	/* Sets up the original list view
	 * Reads initial values from SharedPreferences
	 * Calls AsyncTask to fetch records from USGS
	 * 
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		//Set up options menu
		setHasOptionsMenu(true);
		
		getActivity().setTitle(R.string.quake_title_label);

		//Call the EULA only on first load and on updates.
		new EulaDisplay(getActivity()).show();
		
		//For storing default values
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity().getBaseContext());

		// set initial magnitude
		mMagnitude = sp.getString("magnitudeDefault", "1");

		// set initial time
		selectedTime = sp.getString("timeDefault", "-24");

		TimeZone tz = TimeZone.getTimeZone("UTC");
		df.setTimeZone(tz);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(selectedTime));
		Date date = calendar.getTime();
		mTime = df.format(date);

		// set initial distance
		mDistance = sp.getString("distanceDefault", "0");

		
		// Call the AsyncTask to get initial records.  Pass initial parameters.
		new FetchItemsTask().execute(mMagnitude, mTime, mDistance);
	}

	/* Get selected quake
	 * 
	 * (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Get a quake from the adapter
		Quake q = (Quake) (getListAdapter()).getItem(position);

		// Quake details
		mCallbacks.onQuakeSelected(q);
	}

	/* Inflate the options menu
	 * 
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_quake_list, menu);
	}

	/* Handle options menu selections
	 * 
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Fetch new items from USGS using AsyncTask
		// Display a toast
		case R.id.menu_item_refresh:
			updateToast = Toast.makeText(getActivity(),
					"Getting new quakes...", Toast.LENGTH_SHORT);
			updateToast.show();
			new FetchItemsTask().execute(mMagnitude, mTime, mDistance);
			return true;
		// Start the QuakeMapActivity.  Uses already fetched records.
		case R.id.menu_item_map:
			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());
			if(status == ConnectionResult.SUCCESS) {
				Intent i = new Intent(getActivity(), QuakeMapActivity.class);
				startActivity(i);
			}
			return true;
		// Call the filter dialog
		case R.id.menu_item_filter:
			FragmentManager fm = getActivity().getSupportFragmentManager();
			// Pass the current magnitude, time and distance
			FilterFragment dialog = FilterFragment.newInstance(mMagnitude,
					selectedTime, mDistance);
			// Set this as the target fragment to receive selected values in dialog
			dialog.setTargetFragment(QuakeListFragment.this, REQUEST_FILTER);
			dialog.show(fm, DIALOG_FILTER);
			return true;
		// Show the settings activity to set default values.  Updates the adapter.
		case R.id.menu_item_settings:
			Intent s = new Intent(getActivity(), AppPreferences.class);
			startActivity(s);
			((QuakeAdapter)getListAdapter()).notifyDataSetChanged();
			return true;
		// Return home
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(getActivity());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* Handle result from filter fragment
	 * Fetch new data using AsyncTask
	 * 
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == REQUEST_FILTER) {
			mMagnitude = data.getSerializableExtra(
					FilterFragment.EXTRA_MAGNITUDE).toString();
			selectedTime = data.getSerializableExtra(FilterFragment.EXTRA_TIME)
					.toString();
			TimeZone tz = TimeZone.getTimeZone("UTC");
			df.setTimeZone(tz);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(selectedTime));
			Date date = calendar.getTime();

			mTime = df.format(date);

			mDistance = data
					.getSerializableExtra(FilterFragment.EXTRA_DISTANCE)
					.toString();

			new FetchItemsTask().execute(mMagnitude, mTime, mDistance);
		}
	}
	
	/**
	 * 
	 * Required interface for hosting activities
	 *
	 */
	
	public interface Callbacks {
		void onQuakeSelected(Quake quake);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	
	/**
	 * Inner class for the custom adapter
	 *
	 */
	private class QuakeAdapter extends ArrayAdapter<Quake> {

		// Pass in the current records
		public QuakeAdapter(ArrayList<Quake> quakes) {
			super(getActivity(), 0, quakes);
			getListView().setBackgroundResource(R.drawable.listview_bg);
		}

		//Override the default list item view
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_quake, null);
			}

			Quake c = getItem(position);

			TextView placeTextView = (TextView) convertView
					.findViewById(R.id.quake_list_item_placeTextView);
			placeTextView.setText(c.getPlace());

			TextView dateTextView = (TextView) convertView
					.findViewById(R.id.quake_list_item_dateTextView);
			dateTextView.setText(formatter.format(c.getDate()));

			TextView magnitudeTextView = (TextView) convertView
					.findViewById(R.id.quake_list_item_magnitudeTextView);
			if (c.getMagnitude() >= 4.5) {
				magnitudeTextView.setTextColor(Color.parseColor("#FF0000"));
			} else if (c.getMagnitude() >= 2.5) {
				magnitudeTextView.setTextColor(Color.parseColor("#009999"));
			} else {
				magnitudeTextView.setTextColor(Color.BLACK);
			}
			magnitudeTextView.setText(String.valueOf(c.getMagnitude()));

			return convertView;
		}

	}

	/**
	 * This is a convenience method to set up the adapter when new records are fetched
	 * Cancels the update toast
	 */
	void setupAdapter() {
		if (getActivity() == null) {
			return;
		}

		if (mItems != null) {
			setListAdapter(new QuakeAdapter(mItems));
		} else {
			setListAdapter(null);
		}

		if (updateToast != null && updateToast.getView().isShown()) {
			updateToast.cancel();
		}
	}

	/**
	 * Inner class for the AsyncTask
	 * Called by execute(magnitude, time, distance)
	 * 
	 * @param magnitude
	 * @param time
	 * @param distance
	 *
	 */
	private class FetchItemsTask extends
			AsyncTask<String, Void, ArrayList<Quake>> {
		
		/* Starts a background thread
		 * 
		 * Takes the magnitude, time and distance params from execute()
		 * 
		 * Calls class QuakeFetcher, which does the work of retrieving JSON and parsing
		 * 
		 * @return the list of quakes
		 * 
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected ArrayList<Quake> doInBackground(String... params) {
			String magnitude = params[0];
			String time = params[1];
			String distance = params[2];

			ArrayList<Quake> i = new QuakeFetcher().fetchItems(getActivity(),
					magnitude, time, distance);

			return i;
		}

		/* Takes the items and sets the global variable mItems
		 * Calls setupAdapter() to update records
		 * 
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(ArrayList<Quake> items) {
			mItems = items;
			setupAdapter();
		}

	}

}
