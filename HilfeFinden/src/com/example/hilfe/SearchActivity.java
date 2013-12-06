package com.example.hilfe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hilfe.library.UserFunctions;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    try
    {
    	  ArrayList<String> interests=populate();
	      JSONArray jsonArray = new JSONArray(interests);
	      int length = jsonArray.length();
	      List<String> listContents = new ArrayList<String>(length);
	      for (int i = 0; i < length; i++)
	      {
	        listContents.add(jsonArray.getString(i));
	      }

      ListView myListView = (ListView) findViewById(R.id.listView1);
      myListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContents));
    }
    catch (Exception e)
    {
      // this is just an example
    }
  }
  
  public ArrayList<String> populate() {
      ArrayList<String> items = new ArrayList<String>();
   
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
                  items.add(jo.getString("name"));
                  items.add(jo.getString("email"));
                  items.add(jo.getString("mobile"));
                  items.add(jo.getString("distance"));
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
      return items;
  }
  
}