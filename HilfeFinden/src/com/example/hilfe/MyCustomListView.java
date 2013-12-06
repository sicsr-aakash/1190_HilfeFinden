package com.example.hilfe;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hilfe.library.UserFunctions;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyCustomListView extends ListActivity {

	static final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_list_view);
		
		SimpleAdapter adapter = new SimpleAdapter(
		this,list,R.layout.custom_row_view, new String[] {"name","mobile","email","distance"},
		new int[] {R.id.text1,R.id.text2, R.id.text3, R.id.text4}

		);
		populateList();


		setListAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.custom_list_view, menu);
		return true;
	}
	
	private void populateList() {
		
		HashMap<String,String> temp = new HashMap<String,String>();
		   
	      try {
	    	  
	    	  UserFunctions userFunction = new UserFunctions();
	    	  SharedPreferences pref= getApplicationContext().getSharedPreferences("MyPref", 1);
	    	  String email = pref.getString("emailpref", null);
	    	  String interest = pref.getString("interestpref", null);
	    	  
	    	  String strurl = "http://54.225.23.238/android_login_api/jsonfinal.php?email="+email+"&interest="+interest;
	          URL url = new URL(strurl);
	          HttpURLConnection urlConnection = 
	              (HttpURLConnection) url.openConnection();
	          urlConnection.setRequestMethod("GET");
	          urlConnection.connect();
	                      // gets the server json data
	          BufferedReader bufferedReader = 
	              new BufferedReader(new InputStreamReader(
	                      urlConnection.getInputStream()));
	          String next;
	          while ((next = bufferedReader.readLine()) != null){
	              JSONArray ja = new JSONArray(next);

	              for (int i = 0; i < ja.length(); i++) {
	                  JSONObject jo = (JSONObject) ja.get(i);
	                  	temp.put("name",jo.getString("name"));
	          			temp.put("mobile", jo.getString("mobile"));
	          			temp.put("email", jo.getString("email"));
	          			temp.put("distance", jo.getString("distance"));
	                    list.add(temp);
	              }
	          }
	      } catch (MalformedURLException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	      } catch (IOException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	      } catch (JSONException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	      }
	     
	}
}
