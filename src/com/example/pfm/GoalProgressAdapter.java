package com.example.pfm;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GoalProgressAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> goals;
    private static LayoutInflater inflater=null;
    
    public GoalProgressAdapter(Activity a, ArrayList<HashMap<String, String>> b) {
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
            vi = inflater.inflate(R.layout.goal_saving_progress, null);

        TextView goalName= (TextView)vi.findViewById(R.id.goalNameTV); 
        TextView priority = (TextView)vi.findViewById(R.id.priorityTV); 
        ProgressBar goalProgress =(ProgressBar)vi.findViewById(R.id.progressBar);
        Drawable myProgressBar;
        
        HashMap<String, String> goal = new HashMap<String, String>();
        goal = goals.get(position);
        
        // Setting all values in listview
        goalProgress.setProgress(0);
        goalName.setText(goal.get("GoalName"));
		priority.setText(goal.get("Priority"));
		int progress = (int)(Double.parseDouble(goal.get("Saving"))/Double.parseDouble(goal.get("Amount"))*100);
		if(Double.parseDouble(goal.get("Amount"))>0)
			goalProgress.setProgress(progress);
		
		myProgressBar = activity.getResources().getDrawable(R.drawable.progressbar_green);
		
		goalProgress.setProgressDrawable(myProgressBar);
		goalProgress.setProgress(0);
		goalProgress.setProgress(progress);
		goalProgress.invalidate();
		
        return vi;    
    }
}