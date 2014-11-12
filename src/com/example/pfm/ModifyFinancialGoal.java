package com.example.pfm;

import android.app.Activity;
import android.os.Bundle;

public class ModifyFinancialGoal extends Activity{

	String userid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		userid = MyService.userid;
		
	}
}
