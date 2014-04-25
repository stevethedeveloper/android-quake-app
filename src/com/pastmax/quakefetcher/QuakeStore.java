package com.pastmax.quakefetcher;

import java.util.ArrayList;

import android.content.Context;

/**
 * @author Stephen Walton
 * 
 * Simpleton to store all quakes fetched
 *
 */
public class QuakeStore {
	private ArrayList<Quake> mQuakes;
	private static QuakeStore sQuakeStore;
	private Context mAppContext;
	
	private QuakeStore(Context appContext) {
		mAppContext = appContext;
		mQuakes = new ArrayList<Quake>();
	}
	
	//If a QuakeStore already exists, return it, otherwise create one and return it
	public static QuakeStore get(Context c) {
		if (sQuakeStore == null) {
			sQuakeStore = new QuakeStore(c.getApplicationContext());
		}
		return sQuakeStore;
	}
	
	//Return all quakes
	public ArrayList<Quake> getQuakes() {
		return mQuakes;
	}
	
	//Get one quake
	//TODO - maybe more of a hash
	public Quake getQuake(String id) {
		for (Quake q : mQuakes) {
			if (q.getId().equals(id)) {
				return q;
			}
		}
		return null;
	}
	
	//Add a Quake object to the QuakeStore
	public void addQuake(Quake q) {
		mQuakes.add(q);
	}
	
	//Add an array of Quake objects all at once
	public void setQuakes(ArrayList<Quake> quakes) {
		mQuakes = quakes;
	}

}
