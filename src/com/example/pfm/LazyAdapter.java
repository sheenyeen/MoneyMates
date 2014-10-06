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
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter{

	private Activity activity;
	private ArrayList<HashMap<String, String>> budget;
	private ArrayList<String> category;
    private static LayoutInflater inflater=null;
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> b) {
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
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.budget_list, null);

        TextView category= (TextView)vi.findViewById(R.id.categoryTV); 
        TextView amount = (TextView)vi.findViewById(R.id.amountTV); 
        Button addBudgetBtn=(Button)vi.findViewById(R.id.addBudgetBtn);
        
        HashMap<String, String> budgets = new HashMap<String, String>();
        budgets = budget.get(position);
        
        // Setting all values in listview
        category.setText(budgets.get("category"));
		amount.setText(budgets.get("amount"));
		addBudgetBtn.setTag(budgets.get("categoryID"));
		
        return vi;    
    }

}
