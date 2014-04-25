package com.pastmax.quakefetcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stephen Walton
 *
 *	This is the Quake model
 *	Mostly setters and getters
 */
public class Quake {
	private String mTitle;
	private String mPlace;
	private String mId;
	private double mMagnitude;
	private long mDate;
	private String mTz;
	private String mEventType;
	private String mStatus;
	private String mUrl;
	private double mLongitude;
	private double mLatitude;
	private double mDepth;

	// Format Place
	public String getPlace() {
		String mNewPlace = "";
		int i = mPlace.indexOf(' ');
		String first = mPlace.substring(0, i);
		String remainder = mPlace.substring(i);
		
        if (first.length() > 0) {
			// Grab the km
        	Matcher matcher = Pattern.compile("\\d+").matcher(first);

        	//Convert km to miles and prepend
	        if (matcher.find()) {
	        	mNewPlace = String.valueOf(Math.round(Integer.parseInt(matcher.group()) * 0.62137)) + "mi (" + first + ")" + remainder; 
	        }
        }
        
        return mNewPlace;

	}

	public void setPlace(String place) {
		mPlace = place;
	}

	public String toString() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public double getMagnitude() {
		return mMagnitude;
	}

	public void setMagnitude(double magnitude) {
		mMagnitude = magnitude;
	}

	public long getDate() {
		return mDate;
	}

	public void setDate(long date) {
		mDate = date;
	}

	public String getTz() {
		return mTz;
	}

	public void setTz(String tz) {
		mTz = tz;
	}

	public String getEventType() {
		return mEventType;
	}

	public void setEventType(String eventType) {
		mEventType = eventType;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String status) {
		mStatus = status;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getDepth() {
		// Convert km to miles
    	String depth = Math.round(mDepth * 0.62137) + "mi"; 
		return depth;
	}

	public void setDepth(double depth) {
		mDepth = depth;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

}
