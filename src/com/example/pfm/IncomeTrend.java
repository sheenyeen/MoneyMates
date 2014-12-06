package com.example.pfm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class IncomeTrend extends Activity {
	private View mChart;
	private String[] mMonth = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	Calendar currenttime;
	int currentmonth;
	JSONObject trans;
	Calendar c;
	String transactionType;
	Double totalIncome = 0.00, totalExpense = 0.00, totalSaving = 0.00; 
	LinearLayout chartlayout;
	Button btnChart, btn6Months, btn30Days;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_chart);

		// Getting reference to the button btn_chart
		btnChart = (Button) findViewById(R.id.btn_chart);
		btn6Months = (Button) findViewById(R.id.btn_chart_sixMonths);
		btn30Days = (Button) findViewById(R.id.btn_chart_thirtyDays);
		chartlayout = (LinearLayout) findViewById(R.id.chart);
		
		currenttime = Calendar.getInstance();
		currentmonth = currenttime.get(Calendar.MONTH);
		
		//openChart();
		// Defining click event listener for the button btn_chart
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Draw the Income vs Expense Chart
				btnChart.setBackgroundResource(R.drawable.button_grey);
				btn6Months.setBackgroundResource(R.drawable.button_blue_square);
				btn30Days.setBackgroundResource(R.drawable.button_blue_square);
				chartlayout.removeAllViews();
				openChart();
			}
		};
		
		btn6Months.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnChart.setBackgroundResource(R.drawable.button_blue_square);
				btn6Months.setBackgroundResource(R.drawable.button_grey);
				btn30Days.setBackgroundResource(R.drawable.button_blue_square);
				chartlayout.removeAllViews();
				openChart6Months();			
			}
		});
		
		btn30Days.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnChart.setBackgroundResource(R.drawable.button_blue_square);
				btn6Months.setBackgroundResource(R.drawable.button_blue_square);
				btn30Days.setBackgroundResource(R.drawable.button_grey);
				chartlayout.removeAllViews();
				openChart30Days();
			}
		});
		

		// Setting event click listener for the button btn_chart of the
		// MainActivity layout
		btnChart.setOnClickListener(clickListener);

	}

	private void openChart() {
		int[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }; // x-axis month
		//int[] income = { 2000, 2500, 2700, 3000, 2800, 3500, 3700, 3800, 0, 0, 0, 0 }; //first line graph values
		//int[] expense = { 2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400, 0, 0, 0, 0 }; //second line graph values
		
		double[] expense12months = new double[12]; 
		double maxy = 0.0;
		currenttime = Calendar.getInstance();
		currenttime.add(Calendar.MONTH, 1);
		
		for(int i = 0; i < expense12months.length; i++){
			totalIncome = 0.0;
			currenttime.add(Calendar.MONTH, -1);
			for(int j = 0; j < MyService.transArray.length(); j++){
				try {
					trans = MyService.transArray.getJSONObject(j);
					String datetime = trans.getString("TransactionDateTime");
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					try {
						Date date = simpleDateFormat.parse(datetime);
						Log.d("date", date.toString());
						c = Calendar.getInstance();
						c.setTime(date);

						if ((c.get(Calendar.MONTH) == currenttime.get(Calendar.MONTH) && (c.get(Calendar.YEAR) == currenttime.get(Calendar.YEAR)))) {
							String amount = trans.getString("Amount");
							transactionType = trans.getString("TransactionTypeID");

							if(transactionType.equals("1")){
								Double parseDouble = Double.parseDouble(amount);
								totalIncome = totalIncome + parseDouble;		
							}else{
								Log.d("Not this month", date.toString());
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(totalIncome > maxy){
				maxy = totalIncome;
			}
			expense12months[11-i] = totalIncome;
			x[11-i] = (currenttime.get(Calendar.MONTH))+1;
			Log.d("i month", String.valueOf(i));
			Log.d("totalIncome", String.valueOf(totalIncome));	
		}

		// Creating an XYSeries for Income
		//XYSeries incomeSeries = new XYSeries("Income");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Expense");
		// Adding data to Income and Expense Series
		for (int i = 0; i < x.length; i++) {
			//incomeSeries.add(i, income[i]);
			expenseSeries.add(i, expense12months[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		//dataset.addSeries(incomeSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(expenseSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		//XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		//incomeRenderer.setColor(Color.CYAN); // color of the graph set to cyan
		//incomeRenderer.setFillPoints(true);
		//incomeRenderer.setLineWidth(2f);
		//incomeRenderer.setDisplayChartValues(true);
		// setting chart value distance
		//incomeRenderer.setDisplayChartValuesDistance(10);
		// setting line graph point style to circle
		//incomeRenderer.setPointStyle(PointStyle.CIRCLE);
		// setting stroke of the line chart to solid
		//incomeRenderer.setStroke(BasicStroke.SOLID);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.rgb(95, 156, 72));
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2f);
		expenseRenderer.setDisplayChartValues(true);
		// setting line graph point style to circle
		expenseRenderer.setPointStyle(PointStyle.SQUARE);
		// setting stroke of the line chart to solid
		expenseRenderer.setStroke(BasicStroke.SOLID);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Income Trend in 12 months");
		multiRenderer.setXTitle("Year " + currenttime.get(Calendar.YEAR));
		multiRenderer.setYTitle("Amount in RM");

		/***
		 * Customizing graphs
		 */
		// setting text size of the title
		multiRenderer.setChartTitleTextSize(28);
		// setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(24);
		// setting text size of the graph lable
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setLabelsColor(Color.BLACK);
		multiRenderer.setAxesColor(Color.BLACK);
		multiRenderer.setXLabelsColor(Color.BLACK);
		multiRenderer.setYLabelsColor(0, Color.BLACK);
		multiRenderer.setGridColor(Color.GRAY);
		// setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(false);
		// setting pan enablity which uses graph to move on both axis
		multiRenderer.setPanEnabled(true, true);
		// setting click false on graph
		multiRenderer.setClickEnabled(true);
		// setting zoom to false on both axis
		multiRenderer.setZoomEnabled(false, false);
		// setting lines to display on y axis
		multiRenderer.setShowGridY(true);
		// setting lines to display on x axis
		multiRenderer.setShowGridX(true);
		// setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
		// setting displaying line on grid
		multiRenderer.setShowGrid(true);
		// setting zoom to false
		multiRenderer.setZoomEnabled(false);
		// setting external zoom functions to false
		multiRenderer.setExternalZoomEnabled(false);
		// setting displaying lines on graph to be formatted(like using
		// graphics)
		multiRenderer.setAntialiasing(true);
		// setting to in scroll to false
		multiRenderer.setInScroll(true);
		// setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		// setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		// setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		// setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		// setting no of values to display in y axis
		multiRenderer.setYLabels(10);
		// setting y axis max value, Since i'm using static values inside the
		// graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMax(maxy*1.1);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-0.5);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMax(11);
		// setting bar size or space between two bars
		// multiRenderer.setBarSpacing(0.5);
		// Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		// Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(getResources().getColor(
				R.color.transparent_background));
		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setScale(2f);
		// setting x axis point size
		multiRenderer.setPointSize(4f);
		// setting the margin size for the graph in the order top, left, bottom,
		// right
		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

		currenttime = Calendar.getInstance();
		for (int i = 0; i < x.length; i++) {
			currenttime.get(Calendar.MONTH);
			String currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
			multiRenderer.addXTextLabel(11-i, currentMonth);
			currenttime.add(Calendar.MONTH, -1);
		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		//multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);

		// this part is used to display graph on the xml
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
		// remove any views before u paint the chart
		chartContainer.removeAllViews();
		// drawing bar chart
		mChart = ChartFactory.getLineChartView(IncomeTrend.this, dataset,
				multiRenderer);
		// adding the view to the linearlayout
		chartContainer.addView(mChart);

	}
	
	private void openChart6Months() {
		int[] x = { 0, 1, 2, 3, 4, 5}; // x-axis month
		
		double[] expense6months = new double[6]; 
		double maxy = 0;
		currenttime = Calendar.getInstance();
		currenttime.add(Calendar.MONTH, 1);
		
		for(int i = 0; i < expense6months.length; i++){
			totalIncome = 0.0;
			currenttime.add(Calendar.MONTH, -1);
			for(int j = 0; j < MyService.transArray.length(); j++){
				try {
					trans = MyService.transArray.getJSONObject(j);
					String datetime = trans.getString("TransactionDateTime");
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					try {
						Date date = simpleDateFormat.parse(datetime);
						Log.d("date", date.toString());
						c = Calendar.getInstance();
						c.setTime(date);

						if ((c.get(Calendar.MONTH) == currenttime.get(Calendar.MONTH) && (c.get(Calendar.YEAR) == currenttime.get(Calendar.YEAR)))) {
							String amount = trans.getString("Amount");
							transactionType = trans.getString("TransactionTypeID");

							if(transactionType.equals("1")){
								Double parseDouble = Double.parseDouble(amount);
								totalIncome = totalIncome + parseDouble;		
							}else{
								Log.d("Not this month", date.toString());
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(totalIncome > maxy){
				maxy = totalIncome;
			}
			expense6months[5-i] = totalIncome;
			x[5-i] = (currenttime.get(Calendar.MONTH))+1;
			Log.d("i month", String.valueOf(i));
			Log.d("totalIncome", String.valueOf(totalIncome));	
		}

		// Creating an XYSeries for Income
		//XYSeries incomeSeries = new XYSeries("Income");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Expense");
		// Adding data to Income and Expense Series
		for (int i = 0; i < x.length; i++) {
			//incomeSeries.add(i, income[i]);
			expenseSeries.add(i, expense6months[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		//dataset.addSeries(incomeSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(expenseSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		//XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		//incomeRenderer.setColor(Color.CYAN); // color of the graph set to cyan
		//incomeRenderer.setFillPoints(true);
		//incomeRenderer.setLineWidth(2f);
		//incomeRenderer.setDisplayChartValues(true);
		// setting chart value distance
		//incomeRenderer.setDisplayChartValuesDistance(10);
		// setting line graph point style to circle
		//incomeRenderer.setPointStyle(PointStyle.CIRCLE);
		// setting stroke of the line chart to solid
		//incomeRenderer.setStroke(BasicStroke.SOLID);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.rgb(95, 156, 72));
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2f);
		expenseRenderer.setDisplayChartValues(true);
		// setting line graph point style to circle
		expenseRenderer.setPointStyle(PointStyle.SQUARE);
		// setting stroke of the line chart to solid
		expenseRenderer.setStroke(BasicStroke.SOLID);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Income Trend in 6 months");
		multiRenderer.setXTitle("Year " + currenttime.get(Calendar.YEAR));
		multiRenderer.setYTitle("Amount in RM");

		/***
		 * Customizing graphs
		 */
		// setting text size of the title
		multiRenderer.setChartTitleTextSize(28);
		// setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(24);
		// setting text size of the graph lable
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setLabelsColor(Color.BLACK);
		multiRenderer.setAxesColor(Color.BLACK);
		multiRenderer.setXLabelsColor(Color.BLACK);
		multiRenderer.setYLabelsColor(0, Color.BLACK);
		multiRenderer.setGridColor(Color.GRAY);
		// setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(false);
		// setting pan enablity which uses graph to move on both axis
		multiRenderer.setPanEnabled(true, true);
		// setting click false on graph
		multiRenderer.setClickEnabled(true);
		// setting zoom to false on both axis
		multiRenderer.setZoomEnabled(false, false);
		// setting lines to display on y axis
		multiRenderer.setShowGridY(true);
		// setting lines to display on x axis
		multiRenderer.setShowGridX(true);
		// setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
		// setting displaying line on grid
		multiRenderer.setShowGrid(true);
		// setting zoom to false
		multiRenderer.setZoomEnabled(false);
		// setting external zoom functions to false
		multiRenderer.setExternalZoomEnabled(false);
		// setting displaying lines on graph to be formatted(like using
		// graphics)
		multiRenderer.setAntialiasing(true);
		// setting to in scroll to false
		multiRenderer.setInScroll(true);
		// setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		// setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		// setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		// setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		// setting no of values to display in y axis
		multiRenderer.setYLabels(10);
		// setting y axis max value, Since i'm using static values inside the
		// graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMax(maxy*1.1);
		multiRenderer.setYAxisMin(0);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-0.5);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMax(5);
		// setting bar size or space between two bars
		// multiRenderer.setBarSpacing(0.5);
		// Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		// Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(getResources().getColor(
				R.color.transparent_background));
		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setScale(2f);
		// setting x axis point size
		multiRenderer.setPointSize(4f);
		// setting the margin size for the graph in the order top, left, bottom,
		// right
		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

		currenttime = Calendar.getInstance();
		for (int i = 0; i < x.length; i++) {
			currenttime.get(Calendar.MONTH);
			String currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
			multiRenderer.addXTextLabel(5-i, currentMonth);
			currenttime.add(Calendar.MONTH, -1);
		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		//multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);

		// this part is used to display graph on the xml
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
		// remove any views before u paint the chart
		chartContainer.removeAllViews();
		// drawing bar chart
		mChart = ChartFactory.getLineChartView(IncomeTrend.this, dataset,
				multiRenderer);
		// adding the view to the linearlayout
		chartContainer.addView(mChart);
	}
	
	private void openChart30Days() {
		int[] x = new int[30]; // x-axis month
		
		double[] expense30days = new double[30]; 
		double maxy = 0;
		currenttime = Calendar.getInstance();
		currenttime.add(Calendar.DATE, 1);
		
		for(int i = 0; i < expense30days.length; i++){
			totalIncome = 0.0;
			currenttime.add(Calendar.DATE, -1);
			for(int j = 0; j < MyService.transArray.length(); j++){
				try {
					trans = MyService.transArray.getJSONObject(j);
					String datetime = trans.getString("TransactionDateTime");
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					try {
						Date date = simpleDateFormat.parse(datetime);
						Log.d("date", date.toString());
						c = Calendar.getInstance();
						c.setTime(date);

						if (c.get(Calendar.DATE) == currenttime.get(Calendar.DATE) && c.get(Calendar.MONTH) == currenttime.get(Calendar.MONTH) && (c.get(Calendar.YEAR) == currenttime.get(Calendar.YEAR))) {
							String amount = trans.getString("Amount");
							transactionType = trans.getString("TransactionTypeID");

							if(transactionType.equals("1")){
								Double parseDouble = Double.parseDouble(amount);
								totalIncome = totalIncome + parseDouble;		
							}else{
								Log.d("Not 30 days", date.toString());
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(totalIncome > maxy){
				maxy = totalIncome;
			}
			expense30days[29-i] = totalIncome;
			x[29-i] = (currenttime.get(Calendar.DATE))+1;
			Log.d("i month", String.valueOf(i));
			Log.d("totalIncome", String.valueOf(totalIncome));	
		}

		// Creating an XYSeries for Income
		//XYSeries incomeSeries = new XYSeries("Income");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Expense");
		// Adding data to Income and Expense Series
		for (int i = 0; i < x.length; i++) {
			//incomeSeries.add(i, income[i]);
			expenseSeries.add(i, expense30days[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		//dataset.addSeries(incomeSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(expenseSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		//XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		//incomeRenderer.setColor(Color.CYAN); // color of the graph set to cyan
		//incomeRenderer.setFillPoints(true);
		//incomeRenderer.setLineWidth(2f);
		//incomeRenderer.setDisplayChartValues(true);
		// setting chart value distance
		//incomeRenderer.setDisplayChartValuesDistance(10);
		// setting line graph point style to circle
		//incomeRenderer.setPointStyle(PointStyle.CIRCLE);
		// setting stroke of the line chart to solid
		//incomeRenderer.setStroke(BasicStroke.SOLID);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.rgb(95, 156, 72));
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2f);
		expenseRenderer.setDisplayChartValues(true);
		// setting line graph point style to circle
		expenseRenderer.setPointStyle(PointStyle.SQUARE);
		// setting stroke of the line chart to solid
		expenseRenderer.setStroke(BasicStroke.SOLID);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Income Trend in 30 days");
		//multiRenderer.setXTitle("Year " + currenttime.get(Calendar.YEAR));
		multiRenderer.setYTitle("Amount in RM");

		/***
		 * Customizing graphs
		 */
		// setting text size of the title
		multiRenderer.setChartTitleTextSize(28);
		// setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(24);
		// setting text size of the graph lable
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setLabelsColor(Color.BLACK);
		multiRenderer.setAxesColor(Color.BLACK);
		multiRenderer.setXLabelsColor(Color.BLACK);
		multiRenderer.setYLabelsColor(0, Color.BLACK);
		multiRenderer.setGridColor(Color.GRAY);
		// setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(false);
		// setting pan enablity which uses graph to move on both axis
		multiRenderer.setPanEnabled(true, true);
		// setting click false on graph
		multiRenderer.setClickEnabled(true);
		// setting zoom to false on both axis
		multiRenderer.setZoomEnabled(false, false);
		// setting lines to display on y axis
		multiRenderer.setShowGridY(true);
		// setting lines to display on x axis
		multiRenderer.setShowGridX(true);
		// setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
		// setting displaying line on grid
		multiRenderer.setShowGrid(true);
		// setting zoom to false
		multiRenderer.setZoomEnabled(false);
		// setting external zoom functions to false
		multiRenderer.setExternalZoomEnabled(false);
		// setting displaying lines on graph to be formatted(like using
		// graphics)
		multiRenderer.setAntialiasing(true);
		// setting to in scroll to false
		multiRenderer.setInScroll(true);
		// setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		// setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		// setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		// setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		// setting no of values to display in y axis
		multiRenderer.setYLabels(10);
		// setting y axis max value, Since i'm using static values inside the
		// graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMax(maxy*1.1);
		multiRenderer.setYAxisMin(0);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-0.5);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMax(30);
		// setting bar size or space between two bars
		// multiRenderer.setBarSpacing(0.5);
		// Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		// Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(getResources().getColor(
				R.color.transparent_background));
		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setScale(2f);
		// setting x axis point size
		multiRenderer.setPointSize(4f);
		// setting the margin size for the graph in the order top, left, bottom,
		// right
		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

		int counter = 0;
		currenttime = Calendar.getInstance();
		for (int i = 0; i < x.length; i++) {
			currenttime.get(Calendar.DATE);
			String currentMonth = new SimpleDateFormat("MMM").format(currenttime.getTime());
			if(counter%3==0){
				multiRenderer.addXTextLabel(29-i, currenttime.get(Calendar.DATE) + "\n" + currentMonth);
			}
			currenttime.add(Calendar.DATE, -1);
			counter++;
		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		//multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);

		// this part is used to display graph on the xml
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
		// remove any views before u paint the chart
		chartContainer.removeAllViews();
		// drawing bar chart
		mChart = ChartFactory.getLineChartView(IncomeTrend.this, dataset,
				multiRenderer);
		// adding the view to the linearlayout
		chartContainer.addView(mChart);
	}

}
