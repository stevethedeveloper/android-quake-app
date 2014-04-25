package com.pastmax.quakefetcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * @author Stephen Walton
 *
 * Abstract class for creating fragments in activity classes.
 *
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
	/**
	 * Required by any class extending this abstract class.  Usually returns a fragment to be used in onCreate().
	 * 
	 * @return
	 */
	protected abstract Fragment createFragment();
	
	/**
	 * Get the generic fragment.  This app does not define fragments in XML for flexibility.
	 * 
	 * @return activity_fragment
	 */
	protected int getLayoutResId() {
		return R.layout.activity_fragment;
	}
	
	/* This is where a fragment is created.  
	 * 
	 * The call to createFragment() is an abstract method that must be implemented by any activity extending this class.
	 * 
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResId());
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction()
			.add(R.id.fragmentContainer, fragment)
			.commit();
		}
	}
	

}
