package com.example.pfm;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PieChartActivity extends Activity {
	private GraphicalView ChartView1;
	private GraphicalView mChartView2;
	int count = 0;
	int[] Mycolors = new int[] { 
			Color.parseColor("#01B18B"), Color.parseColor("#F16C5B"),
			Color.parseColor("#8489EB"), Color.parseColor("#119913"),
			Color.parseColor("#D569F6"), Color.parseColor("#A17A10"), 
			Color.parseColor("#DB5AB9"), Color.parseColor("#FA9B16"), 
			Color.parseColor("#00FFBF"), Color.parseColor("#EBEB63"), 
			Color.parseColor("#082DFF"), Color.parseColor("#AD0C0C"),
			Color.parseColor("#00EBF7"), Color.parseColor("#FBA6FF"),
			Color.parseColor("#A1EDA6"), Color.parseColor("#EA4DFF")}; //16 colours

	public Intent execute(Context context, RelativeLayout parent) {
		// public Intent execute(Context context,RelativeLayout parent) {
		
		int[] colors = new int[count];
		for (int i = 0; i < count; i++) {
			colors[i] = Mycolors[i];
		}
		DefaultRenderer renderer = buildCategoryRenderer(colors);
		renderer.setPanEnabled(false);// Disable User Interaction
		renderer.setLabelsColor(Color.BLACK);
		renderer.setLegendTextSize(30);
		renderer.setZoomButtonsVisible(false);
		renderer.setShowLabels(true);
		renderer.setChartTitle("");
		renderer.setLabelsTextSize(12);
		renderer.setDisplayValues(true);
		renderer.setLegendTextSize(20);

		renderer.setChartTitleTextSize(30);
		CategorySeries categorySeries = new CategorySeries("Fruits");
		for(int i = 0; i < labels.size(); i++)
		{
			categorySeries.add(labels.get(i), marks.get(i));
		}
		ChartView1 = ChartFactory.getPieChartView(context, categorySeries,
				renderer);
		parent.removeAllViews();
		parent.addView(ChartView1);
		return ChartFactory.getPieChartIntent(context, categorySeries,
				renderer, null);
	}

	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();

			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	public void setResource(ArrayList<String> ar, ArrayList<Double> arr) {
		labels = ar;
		marks = arr;
		count = arr.size();
	}
	ArrayList<String> labels;
	ArrayList<Double> marks;
}
