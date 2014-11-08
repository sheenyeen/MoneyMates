package com.example.pfm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProgressAdapter extends BaseAdapter{

		private Activity activity;
		private ArrayList<HashMap<String, String>> budget;
		private ArrayList<String> category;
	    private static LayoutInflater inflater=null;
	    
	    public ProgressAdapter(Activity a, ArrayList<HashMap<String, String>> b) {
	        activity = a;
	        budget=b;
	        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    }

	    public int getCount() {
	        return budget.size();
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	    
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        View vi=convertView;
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.budget_progress, null);

	        TextView category= (TextView)vi.findViewById(R.id.categoryTV); 
	        ProgressBar progressBar = (ProgressBar)vi.findViewById(R.id.progressBar); 
	        Button addBudgetBtn=(Button)vi.findViewById(R.id.addBudgetBtn);
	        Drawable myProgressBar;

	        HashMap<String, String> budgets = new HashMap<String, String>();
	        budgets = budget.get(position);
	        
	        // Setting all values in listview
	        category.setText(budgets.get("category"));
			progressBar.setProgress(0);
			((View) addBudgetBtn.getParent()).setTag(budgets.get("amount"));
			int progress = (int)(Double.parseDouble(budgets.get("transSum"))/Double.parseDouble(budgets.get("amount"))*100);
			if(Double.parseDouble(budgets.get("amount"))>0)
				progressBar.setProgress(progress);
			addBudgetBtn.setTag(""+budgets.get("budgetObjIndex"));
			
			if(progress<100){
				myProgressBar = activity.getResources().getDrawable(R.drawable.progressbar_green);
			}
			else{
				myProgressBar = activity.getResources().getDrawable(R.drawable.progressbar_red);
			}
			progressBar.setProgressDrawable(myProgressBar);
			
			progressBar.setProgress(0);
			progressBar.setProgress(progress);
			progressBar.invalidate();
			
			final String progressString = ""+ progress + "%";
			
			progressBar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
					Toast toast = Toast.makeText(activity, progressString, Toast.LENGTH_SHORT);
					toast.show();
					/*Toast toast = Toast.makeText(activity,
							budget.get(position).get("transSum") + " / " + budget.get(position).get("amount"), Toast.LENGTH_SHORT);
					toast.show();*/
					
				}
			});
			
	        return vi;    
	    }

}
