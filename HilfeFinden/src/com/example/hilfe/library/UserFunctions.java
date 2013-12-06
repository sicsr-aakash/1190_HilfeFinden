/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.example.hilfe.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	private static String loginURL = "http://54.225.23.238/android_login_api/";
	private static String registerURL = "http://54.225.23.238/android_login_api/";
	
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String interest_tag = "interest";
	private static String search_tag = "search";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
		
	}
	
	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password){
		// Building Parameters
	
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
	
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function make Login Request
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password, String mobNo, Double latitude, Double longitude){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("mobNo", mobNo));
		params.add(new BasicNameValuePair("latitude", latitude.toString()));
		params.add(new BasicNameValuePair("longitude", longitude.toString()));
		
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}
	
	public JSONObject addSelectedInterest(String email, String interest){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", interest_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("interest", interest));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}
	
	public JSONObject sendSearchInterest(String email, String selectedFromList){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", search_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("selectedFromList", selectedFromList));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}
	
	
	
	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	public String getEmailLogin(Context context)
	{
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		String email ="";
		
		if(count > 0){
		
			email = db.getEmailDb();
			return email;
		}
		return "";
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
	
}
