package com.example.hilfe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hilfe.library.UserFunctions;

public class InterestSelectActivity extends ListActivity {

	UserFunctions userFunctions;
	Button btnLogout;
	public ListView lv;
	ArrayAdapter<String> adapter;
	EditText inputSearch;

	private static String KEY_INTEREST = "interest";
	
	@SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /**
         * Dashboard Screen for the application
         * */        
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
        	setContentView(R.layout.dashboard);
        	
        	
        	
        	setListAdapter(new ArrayAdapter(
                    this,android.R.layout.simple_list_item_1,
                    this.populate()));
            
        	ArrayList<String> interests=populate();
        	
        	lv = getListView();
        	
        	inputSearch = (EditText) findViewById(R.id.inputSearch);
            adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.interest_name, interests);
            lv.setAdapter(adapter);
            
            lv.setOnItemClickListener(new OnItemClickListener()
            {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					 String selectedFromList =(String) (lv.getItemAtPosition(arg2));
					 Log.v("*",selectedFromList);
					
					 UserFunctions userFunction = new UserFunctions();
					 SharedPreferences pref= getApplicationContext().getSharedPreferences("MyPref", 1);
	    			 String email=pref.getString("emailpref", null);
	       		     JSONObject json = userFunction.sendSearchInterest(email, selectedFromList);
//	       		     
//	       		     Intent search = new Intent(getApplicationContext(), DashboardActivity.class);
//	       		     search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//  	        	     startActivity(search);
//  	        	     finish();
	    				
				}
               
            });
            

            
            inputSearch.addTextChangedListener(new TextWatcher() {

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
					
				}

				public void onTextChanged(CharSequence cs, int start,
						int before, int count) {
					
					InterestSelectActivity.this.adapter.getFilter().filter(cs);
				}
                
               
            });
        	
            
        	// The checkbox for the each item is specified by the layout android.R.layout.simple_list_item_multiple_choice
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, interests);
     
            // Getting the reference to the listview object of the layout
            /*ListView listView = (ListView) findViewById(R.id.listInterest);*/
     
            // Setting adapter to the listview
            lv.setAdapter(adapter);
     				            
        	btnLogout = (Button) findViewById(R.id.btnLogout);
        	btnLogout.setText("Add Details");
        	
        	btnLogout.setOnClickListener(new View.OnClickListener() {
    			
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				
    	        	
    				   				
    				SparseBooleanArray checked = lv.getCheckedItemPositions();
    				int size = checked.size(); // number of name-value pairs in the array
    				
    				String checkedValues = "";
    				UserFunctions userFunction = new UserFunctions();
    				for (int i = 0; i < size; i++) {
    				    int key = checked.keyAt(i);
    				    boolean value = checked.get(key);
    				    if (value)
    				        System.out.println(checked.get(key));
    				    	Log.v("Checked :", lv.getItemAtPosition(key).toString());
    				    	checkedValues = checkedValues + lv.getItemAtPosition(key).toString() + ",";
    				    	Log.v("CheckedValues : ", checkedValues);	
    				}
    				SharedPreferences pref= getApplicationContext().getSharedPreferences("MyPref", 1);
    				String email=pref.getString("emailpref", null);
    			//	String email = userFunctions.getEmailLogin(getApplicationContext());
    				JSONObject json = userFunction.addSelectedInterest(email, checkedValues);
    				

    	           // userFunctions.logoutUser(getApplicationContext());
    				Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
    	        	dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	        	startActivity(dashboard);
    	        	// Closing dashboard screen
    	        	finish();
    			}
    		});
        	
        }else{
        	// user is not logged in show login screen
        	Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(login);
        	// Closing dashboard screen
        	finish();
        }
        
        
        
        
    }
    
    public ArrayList<String> populate() {
        ArrayList<String> items = new ArrayList<String>();
     
        try {
            URL url = new URL
            ("http://54.225.23.238/android_login_api/json.php");
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
