package com.example.pfm;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GoalAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> goals;
    private static LayoutInflater inflater=null;
    
    public GoalAdapter(Activity a, ArrayList<HashMap<String, String>> b) {
        activity = a;
        goals=b;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }

    public int getCount() {
        return goals.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.goal_list, null);

        TextView goalName= (TextView)vi.findViewById(R.id.goalNameTV); 
        TextView duration = (TextView)vi.findViewById(R.id.durationTV); 
        TextView amount=(TextView)vi.findViewById(R.id.amountTV);
        
        HashMap<String, String> goal = new HashMap<String, String>();
        goal = goals.get(position);
        
        // Setting all values in listview
        goalName.setText(goal.get("GoalName"));
		duration.setText(goal.get("PeriodName"));
		amount.setText(goal.get("Amount"));
		//set tag??
		
        return vi;    
    }
}
