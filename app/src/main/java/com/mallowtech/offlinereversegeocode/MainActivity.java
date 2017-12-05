package com.mallowtech.offlinereversegeocode;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mallowtech.offlinereversegeocodelibrary.geocode.ReverseGeoCode;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private ReverseGeoCode reverseGeoCode = null;
    private InputStream file = null;
    private ProgressDialog dialog;
    TextView address, location;
    Button getLocation;
    LocationTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(MainActivity.this);
        address = (TextView) findViewById(R.id.address);
        location = (TextView) findViewById(R.id.location);
        getLocation = (Button) findViewById(R.id.getlocation);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //create LocationTracker Object
//                tracker = new LocationTracker(MainActivity.this);
//                // check if location is available
//                if (tracker.isLocationEnabled) {
//                    double latitude = 46.81685;
//                    double longitude = -64.06542;
//                    location.setText("Your Location is Latitude= " + latitude + " Longitude= " + longitude);
//
//                } else {
//                    // show dialog box to user to enable location
//                    tracker.askToOnLocation();
//                }
                getCompleteAddressString(39.47174,-104.89482);
            }
        });
    }

    private void getCompleteAddressString(double latitude, double longitude) {
        try {
            new BackGroundOperation(latitude, longitude).execute("");
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(" location address", "Cannot get Address!");
        }
    }

    private class BackGroundOperation extends AsyncTask<String, Void, String> {
        double latitude, longitude;

        public BackGroundOperation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                reverseGeoCode = new ReverseGeoCode(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            address.setText("Nearest address is " + reverseGeoCode.nearestPlace(latitude, longitude));
            System.out.println("Nearest address is " + reverseGeoCode.nearestPlace(latitude, longitude));
        }

        @Override
        protected void onPreExecute() {
            try {
                dialog.setMessage("Doing something, please wait.");
                dialog.show();
                file = getAssets().open("cities.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}