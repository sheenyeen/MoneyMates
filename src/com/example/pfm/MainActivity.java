package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText username, password;
	Button login, register;
	ProgressBar spin;
	String userID;
	TextView loggingIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.loginBtn);
		register = (Button) findViewById(R.id.registerBtn);
		loggingIn = (TextView) findViewById(R.id.logginginTV);
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spin = (ProgressBar) findViewById(R.id.progressBar);
				spin.setVisibility(View.VISIBLE);
				login = (Button) findViewById(R.id.loginBtn);
				login.setVisibility(View.INVISIBLE);
				loggingIn.setVisibility(View.VISIBLE);
				if(username.getText().toString().equals("") || password.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(), "Please insert username and password.", Toast.LENGTH_SHORT);
					toast.show();
					spin.setVisibility(View.INVISIBLE);
					login.setVisibility(View.VISIBLE);	
					loggingIn.setVisibility(View.INVISIBLE);
				}else{
					connectDB connect = new connectDB();
					connect.execute();
				}
			}
		});		
		
		register.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registerIntent();
			}	
		});
	}
	
	public void registerIntent(){
		Intent registerIntent = new Intent(this, Register.class);
		startActivity(registerIntent);
		finish();
	}
	
	public void newIntent(){
		Intent intent = new Intent(this, Dashboard.class);
		Bundle b = new Bundle();
		b.putString("userid", userID);
		intent.putExtras(b);
		//Log.d("bundlestring", b.toString());
		startActivity(intent);
		finish();
	}
	
	public void loginFailed(){
		Toast toast = Toast.makeText(getApplicationContext(), "Username and password incorrect.", Toast.LENGTH_SHORT);
		toast.show();
		spin.setVisibility(View.INVISIBLE);
		login.setVisibility(View.VISIBLE);	
		loggingIn.setVisibility(View.INVISIBLE);
		username.setText("");
		password.setText("");
	}
	
	class connectDB extends AsyncTask<Void, Void, Void>{
		
		boolean loginFlag = false;

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("username",username.getText().toString()));
			list.add(new BasicNameValuePair("password",password.getText().toString()));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/login.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest("http://moneymatespfms.net46.net/login.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					loginFlag = true;
					userID = jObject.getString("userid");
				}else{
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(loginFlag==true){
				startService(new Intent(getApplicationContext(), MyService.class));
				Log.d("Call to start", "call to start");
				MyService.userid = userID;
				newIntent();
			}
			else{
				loginFailed();
				Log.d("Message", "Fail to login");
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
	}


}
