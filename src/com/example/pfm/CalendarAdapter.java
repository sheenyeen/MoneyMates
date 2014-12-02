package com.example.pfm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	private Context mContext;

	private java.util.Calendar month;
	public GregorianCalendar pmonth; // calendar instance for previous month
	/**
	 * calendar instance for previous month for getting complete view
	 */
	public GregorianCalendar pmonthmaxset;
	private GregorianCalendar selectedDate;
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	DateFormat df;

	private ArrayList<String> items;
    private Map<String, List<String>> list_items;
    private ArrayList<String> dateList;
    public static List<String> dayString;
	private View previousView;

	public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
		CalendarAdapter.dayString = new ArrayList<String>();
		Locale.setDefault( Locale.US );
		month = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = c;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
        this.list_items = new HashMap<String, List<String>>();
        this.dateList = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());
		refreshDays();
	}

	public void setItems(ArrayList<String> items) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;
	}

    public void setListItems(Map<String, List<String>> list_items) {

        for(Map.Entry<String,List<String>> list_map : list_items.entrySet()){
            String tempkey = list_map.getKey();
            if (tempkey.length() == 1) {
                tempkey = "0" + tempkey;
            }
            List<String> test = list_map.getValue();
            this.list_items.put(tempkey, list_map.getValue());
        }


        //this.list_items = list_items;
        for(Map.Entry<String,List<String>> map : this.list_items.entrySet()){
            this.dateList.add(map.getKey());
        }

    }

	public int getCount() {
		return dayString.size();
	}

	public Object getItem(int position) {
		return dayString.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView calendarBillTV;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);

		}
		calendarBillTV = (TextView) v.findViewById(R.id.calendarBillTV);
		// separates daystring into parts.
		String[] separatedTime = dayString.get(position).split("-");
		// taking last part of date. ie; 2 from 2012-12-02
		String gridvalue = separatedTime[2].replaceFirst("^0*", "");
		// checking whether the day is in current month or not.
		if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
			// setting offdays to white color.
			calendarBillTV.setTextColor(Color.WHITE);
			calendarBillTV.setClickable(false);
			calendarBillTV.setFocusable(false);
		} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
			calendarBillTV.setTextColor(Color.WHITE);
			calendarBillTV.setClickable(false);
			calendarBillTV.setFocusable(false);
		} else {
			// setting curent month's days in blue color.
			calendarBillTV.setTextColor(Color.BLUE);
		}

		if (dayString.get(position).equals(curentDateString)) {
			setSelected(v);
			previousView = v;
		} else {
			v.setBackgroundResource(R.drawable.list_item_background);
		}
		calendarBillTV.setText(gridvalue);

		// create date string for comparison
		String date = dayString.get(position);

		//if (date.length() == 1) {
		//	date = "0" + date;
		//}
		String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}

		/*
		// show icon if date is not empty and it exists in the items array
		ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
		if (date.length() > 0 && items != null && items.contains(date)) {
			iw.setVisibility(View.VISIBLE);
		} else {
			iw.setVisibility(View.INVISIBLE);
		}
		*/

        /*
        //Set the summary after the calendar number
        TextView event_detail = (TextView) v.findViewById(R.id.event_summary);
        if (date.length() > 0 && items != null && items.contains(date)) {
            String temp_str = date;
            temp_str += items.toString();
            event_detail.setText(temp_str);
        } else {
            event_detail.setText("");
        }
        */

        /* Other info:
        for(String s : team1.keySet()){
        if(team1.get(s).equals(value)) return s;
        }
         */


        //Set the summary after the calendar number
		TextView selectedDayBill = (TextView) v.findViewById(R.id.calendarBillTV);
        if (date.length() > 0 && dateList != null && dateList.contains(date)) {
            String temp_str = "";
            temp_str = date.toString().substring(8);
            temp_str += "\n";
            //temp_str += this.list_items.get(date).get(0);
            //event_detail.setText(temp_str);
            selectedDayBill.setText(temp_str);
            //selectedDayBill.setTextSize(20);
            selectedDayBill.setTextColor(Color.BLACK);
            selectedDayBill.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
        	selectedDayBill.setText(date.toString().substring(8)+"\n");
        }


		return v;
	}

	public View setSelected(View view) {
		if (previousView != null) {
			previousView.setBackgroundResource(R.drawable.list_item_background);
		}
		previousView = view;
		view.setBackgroundResource(R.drawable.calendar_selected_day);

        //Add to event_detail if clicked

        return view;
	}

	public void refreshDays() {
		// clear items
		items.clear();
		dayString.clear();
		Locale.setDefault( Locale.US );
		pmonth = (GregorianCalendar) month.clone();
		// month start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			dayString.add(itemvalue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			pmonth.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

}