package com.example.pfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pfm.MainActivity.connectDB;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity{
	
	EditText firstname, lastname, emailAdd, password, confirmPassword;
	Button registerButton, backButton;
	String failMsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		firstname = (EditText) findViewById(R.id.firstname);
		lastname = (EditText) findViewById(R.id.lastname);
		emailAdd = (EditText) findViewById(R.id.emailAddress);
		password = (EditText) findViewById(R.id.password);
		confirmPassword = (EditText) findViewById(R.id.confirmPassword);
		registerButton = (Button) findViewById(R.id.registerButton);
		backButton = (Button) findViewById(R.id.backButton);
 		
		registerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(firstname.getText().toString().equals("") || lastname.getText().toString().equals("") || emailAdd.getText().toString().equals("") || password.getText().toString().equals("") || confirmPassword.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(), "Please fill up all fields.", Toast.LENGTH_SHORT);
					toast.show();	
				}else if(password.getText().toString().length()<6){
					Toast toast = Toast.makeText(getApplicationContext(), "Password must be of 6 or more characters.", Toast.LENGTH_LONG);
					toast.show();
				}
				else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
					Toast toast = Toast.makeText(getApplicationContext(), "Passwords do not match. Please re-enter password.", Toast.LENGTH_LONG);
					toast.show();
					password.setText("");
					confirmPassword.setText("");
				}else{
					connectDB connect = new connectDB();
					connect.execute();
				}				
			}
		});
		
		backButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mainIntent();			
			}		
		});
	}
	
	public void mainIntent(){
		Intent mainIntent = new Intent(this, MainActivity.class);
		startActivity(mainIntent);
	}

	public void registerSuccessful(){
		Toast toast = Toast.makeText(getApplicationContext(), "Successfully registered.", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public void registerFailed(){
		//Toast toast = Toast.makeText(getApplicationContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT);
		//toast.show();
		firstname.setText("");
		lastname.setText("");
		emailAdd.setText("");
		password.setText("");
		confirmPassword.setText("");
	}
	
	class connectDB extends AsyncTask<Void, Void, Void>{

		boolean registrationFlag = false;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONParser jsonparser = new JSONParser();
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("firstname",firstname.getText().toString()));
			list.add(new BasicNameValuePair("lastname",lastname.getText().toString()));
			list.add(new BasicNameValuePair("email",emailAdd.getText().toString()));
			list.add(new BasicNameValuePair("password",password.getText().toString()));
			//JSONObject jObject = jsonparser.makeHttpRequest("http://10.0.2.2/login/register.php", "GET", list);
			JSONObject jObject = jsonparser.makeHttpRequest(MyService.URL+"register.php", "GET", list);
			
			try {
				Log.d("JSON", jObject.toString());
				if(jObject.getString("status").equals("success")){
					registrationFlag = true;
				}else{
					failMsg = jObject.getString("msg");
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
			if(registrationFlag==true){
				registerSuccessful();
				mainIntent();
			}else{
				Toast toast = Toast.makeText(getApplicationContext(), failMsg, Toast.LENGTH_SHORT);
				toast.show();
				registerFailed();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
	}

}
