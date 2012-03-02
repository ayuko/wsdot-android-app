/*
 * Copyright (c) 2012 Washington State Department of Transportation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package gov.wa.wsdot.android.wsdot.ui;

import gov.wa.wsdot.android.wsdot.R;
import gov.wa.wsdot.android.wsdot.R.drawable;
import gov.wa.wsdot.android.wsdot.R.id;
import gov.wa.wsdot.android.wsdot.R.layout;
import gov.wa.wsdot.android.wsdot.shared.CameraItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MountainPassItemMapFragment extends BaseActivity {
	
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	private static final String DEBUG_TAG = "MountainPassItemMap";	
	private ArrayList<CameraItem> cameraItems;
	private CamerasOverlay cameras = null;
	private MapView map = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        cameraItems = (ArrayList<CameraItem>)getIntent().getSerializableExtra("Cameras");
        setContentView(R.layout.map_tabs);
        Bundle b = getIntent().getExtras();
        Float latitude = new Float(b.getString("Latitude"));
        Float longitude = new Float(b.getString("Longitude"));

        map = (MapView) findViewById(R.id.mapview);
        map.setSatellite(false);
        map.getController().setZoom(11);
        map.setBuiltInZoomControls(true);
        map.setTraffic(true);
        GeoPoint newPoint = new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
        map.getController().setCenter(newPoint);
        
        new OverlayTask().execute();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1E6), (int)(lon*1E6)));
	}	
	
	private class CamerasOverlay extends ItemizedOverlay<CameraOverlayItem> {
		private ArrayList<CameraOverlayItem> cameraOverlayItems = new ArrayList<CameraOverlayItem>();

		public CamerasOverlay() {
			super(null);
			
			try {				
				for (int j=0; j < cameraItems.size(); j++) {
					cameraOverlayItems.add(new CameraOverlayItem(getPoint(cameraItems.get(j).getLatitude(), cameraItems.get(j).getLongitude()),
							cameraItems.get(j).getTitle(),
							cameraItems.get(j).getImageUrl(),
							getMarker(R.drawable.camera)));
				}
					
			 } catch (Exception e) {
				 Log.e(DEBUG_TAG, "Error in network call", e);
			 }
			 
			 populate();
		}
		
		@Override
		protected CameraOverlayItem createItem(int i) {
			return(cameraOverlayItems.get(i));
		}
		
		@Override
		protected boolean onTap(int i) {
			OverlayItem item = getItem(i);
			new GetCameraImage().execute(item.getSnippet());

			return true;
		} 
		 
		 @Override
		 public int size() {
			 return(cameraOverlayItems.size());
		 }
		 
		 private Drawable getMarker(int resource) {
			 Drawable marker = getResources().getDrawable(resource);
			 marker.setBounds(0, 0, marker.getIntrinsicWidth(),
			 marker.getIntrinsicHeight());
			 boundCenterBottom(marker);

			 return(marker);
		 }
	}	

	private class GetCameraImage extends AsyncTask<String, Void, Drawable> {
		private final ProgressDialog dialog = new ProgressDialog(MountainPassItemMapFragment.this);

		protected void onPreExecute() {
			this.dialog.setMessage("Retrieving camera image ...");
			this.dialog.setOnCancelListener(new OnCancelListener() {
	            public void onCancel(DialogInterface dialog) {
	                cancel(true);
	            }				
			});
			this.dialog.show();
		}

	    protected void onCancelled() {
	    	Toast.makeText(MountainPassItemMapFragment.this, "Cancelled", Toast.LENGTH_SHORT).show();
	    }	
		
		protected Drawable doInBackground(String... params) {
			return loadImageFromNetwork(params[0]);
		}
		
		protected void onPostExecute(Drawable result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(MountainPassItemMapFragment.this);
			LayoutInflater inflater = (LayoutInflater) MountainPassItemMapFragment.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.camera_dialog, null);
			ImageView image = (ImageView) layout.findViewById(R.id.image);
			
			if (image.equals(null)) {
				image.setImageResource(R.drawable.camera_offline);
			} else {
				image.setImageDrawable(result);				
			}	

			builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});

			builder.setView(layout);
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}
	}	
	
    private Drawable loadImageFromNetwork(String url) {
    	BufferedInputStream in;
        BufferedOutputStream out;
        
        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();
            final byte[] data = dataStream.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);                        
            Drawable image = new BitmapDrawable(bitmap);
            return image;
	    } catch (Exception e) {
	        Log.e(DEBUG_TAG, "Error retrieving camera images", e);
	    }
	    return null;	    
    }
    
    /**
     * Copy the content of the input stream into the output stream, using a
     * temporary byte array buffer whose size is defined by
     * {@link #IO_BUFFER_SIZE}.
     * 
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws IOException If any error occurs during the copy.
     */
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

	class CameraOverlayItem extends OverlayItem {
		 Drawable marker = null;
	
		 CameraOverlayItem(GeoPoint pt, String title, String description, Drawable marker) {
			 super(pt, title, description);
			 this.marker = marker;
		 }

		 @Override
		 public Drawable getMarker(int stateBitset) {
			 Drawable result = marker;
			 setState(result, stateBitset);

			 return result;
		 }
	}
	
	class OverlayTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		public void onPreExecute() {
			if (cameras != null) {
				map.getOverlays().remove(cameras);
				map.invalidate();
				cameras = null;
			}
		}

		@Override
		public Void doInBackground(Void... unused) {
			cameras = new CamerasOverlay();	 
			return null;
		}

		@Override
		public void onPostExecute(Void unused) {
			map.getOverlays().add(cameras);		
			map.invalidate();
		 }
	}    
	
}
