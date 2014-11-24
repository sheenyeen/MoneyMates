package com.example.pfm;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class BillAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<String> bills;
    private static LayoutInflater inflater=null;
    
    public BillAdapter(Activity a, ArrayList<String> b) {
        activity = a;
        bills=b;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }

    public int getCount() {
        return bills.size();
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
            vi = inflater.inflate(R.layout.bill_list, null);

        TextView billTV= (TextView)vi.findViewById(R.id.billTV); 
        Button payBtn = (Button)vi.findViewById(R.id.payBtn);
        
        
        // Setting all values in listview
        billTV.setText(bills.get(position));
		
		
        return vi;    
    }
}
