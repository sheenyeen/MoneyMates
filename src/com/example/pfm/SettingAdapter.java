package com.example.pfm;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter{
	private Activity activity;
	private ArrayList<HashMap<String, String>> goals;
    private static LayoutInflater inflater=null;
    
    public SettingAdapter(Activity a, ArrayList<HashMap<String, String>> b) {
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

        ImageView settingIcon= (ImageView)vi.findViewById(R.id.settingIcon); 
        TextView settingTV = (TextView)vi.findViewById(R.id.settingTV); 
        
        // Setting all values in listview
       
		
        return vi;    
    }
}
