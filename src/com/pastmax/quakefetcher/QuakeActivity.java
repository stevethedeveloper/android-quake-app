package com.pastmax.quakefetcher;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author Stephen Walton
 * 
 * QuakeActivity
 * Creates QuakeFragment and passes the selected record id
 *
 */
public class QuakeActivity extends SingleFragmentActivity {
	private String mUrl;
	
	@Override
	protected Fragment createFragment() {
		String quakeId = (String)getIntent().getSerializableExtra(QuakeFragment.EXTRA_QUAKE_ID);
		
		return QuakeFragment.newInstance(quakeId);
	}

	public void clickUrl(View v) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
		startActivity(browserIntent);
	}

	public void setUrl(String url) {
		mUrl = url;
	}

}
