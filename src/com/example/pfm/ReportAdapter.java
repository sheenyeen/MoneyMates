package com.example.pfm;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportAdapter extends BaseAdapter{
	private Activity activity;
	private ArrayList<HashMap<String, String>> report;
    private static LayoutInflater inflater=null;
    
    public ReportAdapter(Activity a, ArrayList<HashMap<String, String>> b) {
        activity = a;
        report=b;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }

    public int getCount() {
        return report.size();
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
            vi = inflater.inflate(R.layout.setting_list, null);

        ImageView settingIcon= (ImageView)vi.findViewById(R.id.settingIcon); 
        TextView settingTV = (TextView)vi.findViewById(R.id.settingTV); 
        
        HashMap<String, String> reports = new HashMap<String, String>();
		reports = report.get(position);

		// Setting all values in listview
		settingTV.setText(reports.get("name"));

		Context context = settingIcon.getContext();
		int id = context.getResources().getIdentifier(reports.get("imagename"), "drawable", context.getPackageName());
        
         Bitmap j=decodeSampledBitmapFromResource(context.getResources(),id, 50, 50);
		
		settingIcon.setImageBitmap(j);

		return vi;  
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
            int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
