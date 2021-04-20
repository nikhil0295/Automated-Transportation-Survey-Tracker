package com.example.ATS.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ATS.myapplication.Interfaces.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener{

    private GoogleMap mMap;
    double stoplat=0,stoplong=0;
    long time;
    double posstoplat2=0;
    double posstoplong2=0;
    double posstoplat=0;
    double posstoplong=0;

    private int measurement_index = Constants.INDEX_KM;
    ArrayList<LatLng> points = new ArrayList<>();
    Location start,next;
    ArrayList<Location> coordList = new ArrayList<Location>();
    public float distance;
    private double speed = 0.0;
    LocationManager locationManager ;
    long startTime;


    Marker marker;
    Button b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mapsdesign);
        b=(Button)findViewById(R.id.button2);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        /*b.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {


        mapFragment.getMapAsync(this);                         }

                             });*/
        mapFragment.getMapAsync(this);
        startTime=System.currentTimeMillis();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    long tEnd = System.currentTimeMillis();
                    long tDelta = tEnd - startTime;
                    Log.e("Difference",""+tDelta);
                    double elapsedSeconds = tDelta / 1000.0;
                    Log.e("Difference2",""+elapsedSeconds);
                    Intent intent1=getIntent();
                    String email=intent1.getStringExtra("Email");

                    Intent intent = new Intent(MapsActivity.this, Database.class);
                    //Log.e("Mail111",""+email);
                    intent.putExtra("First", points);
                    if (stoplat != 0 && stoplong != 0)
                    {
                        intent.putExtra("StopLat", stoplat);
                        intent.putExtra("StopLong", stoplong);
                    }
                    intent.putExtra("Time",elapsedSeconds);
                    intent.putExtra("Distance",distance);
                    intent.putExtra("Email",email);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("ERROR",e.toString());
                    Toast.makeText(getBaseContext(), "Error opening maps", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);

        if (isNetworkAvailable()) {
            // Execution here

            mMap = googleMap;
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates(bestProvider, 25, 25, this);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                start = location;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                coordList.add(location);
                LatLng latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                onLocationChanged(location);

            }
            else
                locationManager.requestLocationUpdates(bestProvider, 30, 30, this);

        }
    }


    /**
     * Service that receives ActivityRecognition updates. It receives updates
     * in the background, even if the main Activity is not visible.
     */


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onLocationChanged(Location location) {
        if (isNetworkAvailable()&& location!=null)
        {
            time =location.getElapsedRealtimeNanos();


            speed=location.getSpeed();
            String speedString = "" + roundDecimal(convertSpeed(speed), 2);
            //String unitString = measurementUnitString(measurement_index);
            next=location;
            coordList.add(location);

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double postoplat=roundDecimal(latitude,4);
            double posstoplong=roundDecimal(longitude,4);
            if(start!=null) {
                posstoplat2 = roundDecimal(start.getLatitude(), 4);
                posstoplong2 = roundDecimal(start.getLongitude(), 4);
            }

            if(postoplat==posstoplat2 && posstoplong==posstoplong2)
            {

                stoplat=latitude;
                stoplong=longitude;
                Log.e("Stop Locations:","stoplat :"+stoplat+"stoplong :"+stoplong);

            }

            LatLng dest = new LatLng(latitude, longitude);
            double latitudes=start.getLatitude();
            double longitudes=start.getLongitude();
            LatLng origin=new LatLng(latitudes,longitudes);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
            String displaylat="" + roundDecimal(latitude, 5);
            String displaylong="" + roundDecimal(longitude,5);
            updateUI(displaylat,displaylong, distance,speedString,time);
            start=location;
        }

    }
    private double roundDecimal(double value, final int decimalPlace)
    {
        BigDecimal bd = new BigDecimal(value);

        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        value = bd.doubleValue();

        return value;
    }
    private double convertSpeed(double speed){
        return ((speed * Constants.HOUR_MULTIPLIER) * Constants.UNIT_MULTIPLIERS[measurement_index]);
    }

    /*   private String measurementUnitString(int unitIndex){
           String string = "";

           switch(unitIndex)
           {
               case Constants.INDEX_KM:		string = "km/h";	break;
               case Constants.INDEX_MILES:	string = "mi/h";	break;
           }

           return string;
       }*/
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"
                +output+"?"+parameters+"&key=AIzaSyCu5ZSlcd5KYQje6TPNQ9aVYKdaUElPYP8";


        return url;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String LOG_TAG="ERRORCHECK";
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();
            Log.v(LOG_TAG, "JSON Data:" + data);

        }catch(Exception e){
            Log.e("Exception urldw", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(25);
                lineOptions.color(Color.RED);

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null)
            {
                mMap.addPolyline(lineOptions);
            }

        }
    }

    void updateUI(String latitude,String longitude,float distance,String speed,long time)
    {
        TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
        locationTv.setText("Lat:" + latitude + " Long:" + longitude);
        drawPrimaryLinePath(coordList);
    }
    ;
    private void drawPrimaryLinePath( ArrayList<Location> coordList )
    {
        if ( mMap == null )
        {
            return;
        }

        if ( coordList.size() < 2 )
        {
            return;
        }

        PolylineOptions options = new PolylineOptions();


        options.color(Color.parseColor("#CC0000FF"));
        options.width(20);
        options.visible(true);
        for ( Location locRecorded : coordList)
        {
            options.add( new LatLng( locRecorded.getLatitude(),
                    locRecorded.getLongitude() ) );
        }

        for(int i=0;i<coordList.size()-1;i++)
        {
            distance=coordList.get(i).distanceTo(coordList.get(i+1));
        }
        Log.v("Distance",""+distance);

        //mMap.addPolyline( options );

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // Add a marker in Sydney and move the camera




}