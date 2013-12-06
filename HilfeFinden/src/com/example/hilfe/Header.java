package com.example.hilfe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.example.hilfe.R;
import com.example.hilfe.library.UserFunctions;

public class Header extends Activity {
	
	Button btnLogout;
	UserFunctions userFunctions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_header);
		
		
		btnLogout = (Button) findViewById(R.id.btnLogout);
		
		/* Code for generation of button text dynamically 
		Button subject = new Button(this);
        subject.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        subject.setText("A");
        setContentView(subject); */
        
		btnLogout.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
							
				// TODO Auto-generated method stub
				userFunctions.logoutUser(getApplicationContext());
				Intent login = new Intent(getApplicationContext(), LoginActivity.class);
	        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(login);
	        	// Closing dashboard screen
	        	finish();
			}
		});
    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_header, menu);
		return true;
	}

}
