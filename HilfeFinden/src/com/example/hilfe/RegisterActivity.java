package com.example.hilfe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hilfe.R;
import com.example.hilfe.library.DatabaseHandler;
import com.example.hilfe.library.UserFunctions;

public class RegisterActivity extends Activity {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText inputFullName;
	EditText inputEmail;
	EditText inputPassword;
	EditText inputMobileNo;
	TextView registerErrorMsg;
	
	GPSLocation getloc;
	
	
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		
		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.registerName);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputPassword = (EditText) findViewById(R.id.registerPassword);
		inputMobileNo = (EditText) findViewById(R.id.registerMobileNumber);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);
			
		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				String mobNo = inputMobileNo.getText().toString();
				Double latitude = 0.0, longitude = 0.0;
				UserFunctions userFunction = new UserFunctions();
				SharedPreferences pref=getApplicationContext().getSharedPreferences("MyPref",1);
				Editor editor=pref.edit();
				editor.putString("emailpref", email);
				editor.commit();
				
				if(name.equalsIgnoreCase("")|| email.equalsIgnoreCase("") || password.equalsIgnoreCase("")|| mobNo.equalsIgnoreCase("") )
						{
						    Toast.makeText(RegisterActivity.this, "All Fields Required.",Toast.LENGTH_LONG).show();
						}
			/*	
				final String MobNo=mobNo.trim();
				final String mobPattern="[0-9]";
				inputMobileNo.addTextChangedListener(new TextWatcher() {
					
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						
					}
					
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						// TODO Auto-generated method stub
						
					}
					
					public void afterTextChanged(Editable s) {
						 if (MobNo.matches(mobPattern))
					        { 
					            //Toast.makeText(getApplicationContext(),"Valid Number",Toast.LENGTH_SHORT).show();
					            // or
					            //registerErrorMsg.setText("valid email");
					        }
					        else
					        {
					             Toast.makeText(getApplicationContext(),"Invalid number",Toast.LENGTH_SHORT).show();
					            //or
					             registerErrorMsg.setText("Invalid number");
					        }
						}
				});
				*/
				
			final String Email = email.trim();
				final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
				
				inputEmail.addTextChangedListener(new TextWatcher() { 
				    public void afterTextChanged(Editable s) { 

				    if (Email.matches(emailPattern))
				        { 
				            //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
				            // or
				            //registerErrorMsg.setText("valid email");
				        }
				        else
				        {
				             Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
				            //or
				             registerErrorMsg.setText("Invalid email");
				        }
				    }

					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub
						
					}

					public void onTextChanged(CharSequence arg0, int arg1, int arg2,
							int arg3) {
						// TODO Auto-generated method stub
						
					}
				});

				
				
				try
				{
				getloc = new GPSLocation(RegisterActivity.this);
				if(getloc.canGetLocation())
				{
					latitude = getloc.getLatitude();
					longitude = getloc.getLongitude();
					//Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
					
					Toast.makeText(getApplicationContext(),"Your location is being recorded",Toast.LENGTH_SHORT).show();
					Toast.makeText(getApplicationContext(), latitude.toString(), Toast.LENGTH_SHORT).show();
					Toast.makeText(getApplicationContext(), longitude.toString(), Toast.LENGTH_SHORT).show();	
					
				}
				}
				catch(Exception e)
				{
					
				}

				JSONObject json = userFunction.registerUser(name, email, password, mobNo, latitude, longitude);
				
				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						registerErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){
							// user successfully registred
							// Store user details in SQLite Database
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");
							
							// Clear all previous data in database
							userFunction.logoutUser(getApplicationContext());
							db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
							// Launch Dashboard Screen
							Intent dashboard = new Intent(getApplicationContext(), InterestSelectActivity.class);
							// Close all views before launching Dashboard
							dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(dashboard);
							// Close Registration Screen
							finish();
						}else{
							// Error in registration
							registerErrorMsg.setText("Error occured in registration");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});
	}
}
