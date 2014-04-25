package com.pastmax.quakefetcher;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

/**
 * This is the main activity for the app.  It creates the fragment QuakeListFragment()
 * 
 * Extends the abstract class SingleFragmentActivity, which simply sets up fragments
 *
 */
public class QuakeListActivity extends SingleFragmentActivity implements QuakeListFragment.Callbacks {
	private String mUrl;
	
	/* Creates an instance of QuakeListFragment.
	 * 
	 * (non-Javadoc)
	 * @see com.pastmax.quakefetcher.SingleFragmentActivity#createFragment()
	 */
	@Override
	protected Fragment createFragment() {
		return new QuakeListFragment();
	}

	/* 
	 * Override abstract class method
	 * 
	 * (non-Javadoc)
	 * @see com.pastmax.quakefetcher.SingleFragmentActivity#getLayoutResId()
	 */
	@Override
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	}

	/* Display a quake detail view
	 * 
	 * (non-Javadoc)
	 * @see com.pastmax.quakefetcher.QuakeListFragment.Callbacks#onQuakeSelected(com.pastmax.quakefetcher.Quake)
	 */
	@Override
	public void onQuakeSelected(Quake quake) {
		if (findViewById(R.id.detailFragmentContainer) == null) {
			//Start QuakeActivity
			Intent i = new Intent(this, QuakeActivity.class);
			i.putExtra(QuakeFragment.EXTRA_QUAKE_ID, quake.getId());
			startActivity(i);
		} else {
			FragmentManager fm  = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
			Fragment newDetail = QuakeFragment.newInstance(quake.getId());
			
			if (oldDetail != null) {
				ft.remove(oldDetail);
			}
			
			ft.add(R.id.detailFragmentContainer, newDetail);
			ft.commit();
		}
	}
	
	/**
	 * Open a url.  Uses global mUrl.
	 * 
	 * @param v
	 */
	public void clickUrl(View v) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
		startActivity(browserIntent);
	}

	/**
	 * Set global variable mUrl
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		mUrl = url;
	}


}
