package com.pastmax.quakefetcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * @author Stephen Walton
 * 
 * QuakeFetcher is the class the AsyncTask calls to get new quakes
 * The resulting JSON is parsed and stored in a singleton QuakeStore
 *
 */
public class QuakeFetcher {
	public static final String TAG = "QuakeFetcher";
	
	// The base URL to the USGS API
	private static final String BASE_URL = "http://comcat.cr.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time";
	
	// Connect to the URL
	// Return the results
	byte[] getUrlBytes(String urlSpec) throws IOException {
		URL url = new URL(urlSpec);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = connection.getInputStream();
			
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
			
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			return out.toByteArray();
		} finally {
			connection.disconnect();
		}
	}
	
	public String getUrl(String urlSpec) throws IOException {
		return new String(getUrlBytes(urlSpec));
	}
	
	/**
	 * Gets, parses and stores quakes based on params
	 * 
	 * @param c - LocationManager needs a context
	 * @param magnitude - minimum magnitude
	 * @param time - timeframe
	 * @param distance - distance from location or all
	 * @return quakes
	 */
	public ArrayList<Quake> fetchItems(Context c, String magnitude, String time, String distance) {
		ArrayList<Quake> mQuakes = new ArrayList<Quake>();

		try {
			
			// Pass params
			String urlParams = "&minmagnitude=" + magnitude + "&starttime=" + time;

			if (!distance.equals("0")) {
				LocationManager locationManager = (LocationManager) 
						c.getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
	
				// Get current location
				Location location = locationManager
						.getLastKnownLocation(locationManager.getBestProvider(criteria,
								false));
				
				String mLatitude = "0";
				String mLongitude = "0";
				mLatitude = String.valueOf(location.getLatitude());
				mLongitude = String.valueOf(location.getLongitude());
				 
				// More params
				urlParams = urlParams + "&maxradiuskm=" + distance + "&latitude=" + mLatitude + "&longitude=" + mLongitude;
			}
			
			// Create the URL
			String mUrl = BASE_URL + urlParams;
			// Fetch the data
			String jsonString = getUrl(mUrl);
			
			// Set up JSON object
			JSONObject jObject = new JSONObject(jsonString);
			// Get the relevant part of the object
			JSONArray jArray = jObject.getJSONArray("features");
			
			// Loop through the results and add to quake array
			for (int i = 0; i < jArray.length(); i++) {
				try {
					JSONObject quake = jArray.getJSONObject(i);
					JSONObject properties = quake.getJSONObject("properties");
					JSONObject geometry = quake.getJSONObject("geometry");
					JSONArray gArray = geometry.getJSONArray("coordinates");
					double longitude = gArray.getDouble(0);
					double latitude = gArray.getDouble(1);
					double depth = gArray.getDouble(2);
					
					
					Quake q = new Quake();
					q.setTitle(properties.getString("title"));
					q.setPlace(properties.getString("place"));
					q.setId(quake.getString("id"));
					q.setDate(properties.getLong("time"));
					q.setMagnitude(properties.getDouble("mag"));
					q.setTz(properties.getString("tz"));
					q.setEventType(properties.getString("type"));
					q.setStatus(properties.getString("status"));
					q.setUrl(properties.getString("url"));
					q.setLongitude(longitude);
					q.setLatitude(latitude);
					q.setDepth(depth);

					mQuakes.add(q);
				} catch (JSONException e) {
					Log.e(TAG, "JSON Exception in QuakeFetcher");
				}
			}
			
		} catch (IOException ioe) {
			Log.e(TAG, "Failed to fetch items", ioe);
		} catch (JSONException e) {
			Log.e(TAG, "JSON object exception", e);
		}
		
		// Store quakes in singleton QuakeStore
		QuakeStore.get(c.getApplicationContext()).setQuakes(mQuakes);
		// Return quakes
		return mQuakes; 
	}
}
