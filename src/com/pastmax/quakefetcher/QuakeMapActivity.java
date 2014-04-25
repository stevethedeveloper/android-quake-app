package com.pastmax.quakefetcher;

import android.support.v4.app.Fragment;

/**
 * @author Stephen Walton
 * 
 * Set up the map fragment
 *
 */
public class QuakeMapActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new QuakeMapFragment();
	}

}
