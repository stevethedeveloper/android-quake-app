package com.pastmax.quakefetcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Spinner;

/**
 * @author Stephen Walton
 *
 */
public class FilterFragment extends DialogFragment {
	private static final String TAG = "FilterFragment";

	public static final String EXTRA_MAGNITUDE = "com.pastmax.quakefetcher.magnitude";
	public static final String EXTRA_TIME = "com.pastmax.quakefetcher.time";
	public static final String EXTRA_DISTANCE = "com.pastmax.quakefetcher.distance";

	private Spinner magnitudesSpinner;
	private Spinner timeSpinner;
	private Spinner distanceSpinner;

	private String[] magnitudeValues;
	private String[] timeValues;
	private String[] distanceValues;

	private String mMagnitude;
	private String mTime;
	private String mDistance;

	
	/**
	 * Get the current values, set the args
	 * 
	 * @param magnitude
	 * @param time
	 * @param distance
	 * @return a fragment instance
	 */
	public static FilterFragment newInstance(String magnitude, String time,
			String distance) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_MAGNITUDE, magnitude);
		args.putSerializable(EXTRA_TIME, time);
		args.putSerializable(EXTRA_DISTANCE, distance);

		FilterFragment fragment = new FilterFragment();
		fragment.setArguments(args);

		return fragment;
	}

	
	/* Grab the args, set the views on the dialog to the current values
	 * 
	 * (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mMagnitude = (String) getArguments().getSerializable(EXTRA_MAGNITUDE);
		mTime = (String) getArguments().getSerializable(EXTRA_TIME);
		mDistance = (String) getArguments().getSerializable(EXTRA_DISTANCE);

		magnitudeValues = getResources().getStringArray(
				R.array.magnitudes_values);
		timeValues = getResources().getStringArray(R.array.times_values);
		distanceValues = getResources().getStringArray(R.array.distance_values);

		View v = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_filter, null);

		// Magnitudes spinner
		magnitudesSpinner = (Spinner) v
				.findViewById(R.id.filter_magnitudeSpinner);
		magnitudesSpinner.setSelection(getIndex(magnitudeValues, mMagnitude));

		// Times spinner
		timeSpinner = (Spinner) v.findViewById(R.id.filter_timeSpinner);
		timeSpinner.setSelection(getIndex(timeValues, mTime));

		// Distance spinner
		distanceSpinner = (Spinner) v.findViewById(R.id.filter_distanceSpinner);
		distanceSpinner.setSelection(getIndex(distanceValues, mDistance));

		// return the dialog with buttons
		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.dialog_filter_title)
				.setNegativeButton(android.R.string.cancel, null)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sendResult(Activity.RESULT_OK);
							}
						}).create();
	}

	/**
	 * Send the results back to QuakeListFragment
	 * 
	 * @param resultCode
	 */
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}

		Intent i = new Intent();

		String selectedMagnitude = magnitudeValues[magnitudesSpinner
				.getSelectedItemPosition()];
		i.putExtra(EXTRA_MAGNITUDE, selectedMagnitude);

		String selectedTime = timeValues[timeSpinner.getSelectedItemPosition()];
		i.putExtra(EXTRA_TIME, selectedTime);

		String selectedDistance = distanceValues[distanceSpinner
				.getSelectedItemPosition()];
		i.putExtra(EXTRA_DISTANCE, selectedDistance);

		getTargetFragment().onActivityResult(getTargetRequestCode(),
				resultCode, i);
	}

	private int getIndex(String[] arr, String val) {

		int index = 0;

		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(val)) {
				index = i;
			}
		}
		return index;
	}
}
