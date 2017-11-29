package com.a3x3conect.welhamboys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.a3x3conect.welhamboys.Modules.Getlocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import a3x3conect.com.mycampus365.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Double latt, lngg;
    JSONArray jArray;
    JSONObject json_data;
    String usrnm;
    private GoogleMap mMap;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        usrnm = sp.getString("username", null);


        String Urlr = "http://183.82.106.77:8080/welham/webservices/transport.php";
//                    Intent intent = new Intent(getApplicationContext(), process.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("name",Urlr); //Here you will add the data into intent to pass bw activites
//                    startActivity(intent);
        new JsonAsync().execute(Urlr);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Getting Location...");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

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


    public class JsonAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = " ";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finaljson = buffer.toString();


                //   return (routeid+status+userid).toString();

                return finaljson;
                //

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String finaljson) {
            super.onPostExecute(finaljson);
            dialog.dismiss();

            try {

                jArray = new JSONArray(finaljson);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    //   JSONArray jsonObject = sys.getJSONArray(i);
                    json_data = jArray.getJSONObject(i);


                    String username = json_data.getString("username");

                    if (username.equalsIgnoreCase(usrnm)) {

                        lngg = json_data.getDouble("longitude");
                        latt = json_data.getDouble("latitude");

                        Log.e("lat log", String.valueOf(lngg) + "   " + String.valueOf(lngg));
                    }
                }


            } catch (JSONException e) {
                Toast.makeText(MapsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }


            try {
//                paren = new JSONObject(finaljson);
//                latt = paren.getDouble("lat");
                //              lngg = paren.getDouble("lon");

                LatLng sydney = new LatLng(latt, lngg);


                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                List<android.location.Address> addresses = geocoder.getFromLocation(latt, lngg, 2);

                String city = addresses.get(0).getAddressLine(0);
                String city2 = addresses.get(0).getAddressLine(1);//.getLocality();
                String state = addresses.get(0).getAdminArea();
                String zip = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();
                //   mMap.icon(BitmapDescriptorFactory.fromResource(R.drawable.pushbuspin));
                mMap.addMarker(new MarkerOptions().position(sydney).title(city + "," + city2).icon(BitmapDescriptorFactory.fromResource(R.drawable.buslog)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                Button getdir = (Button) findViewById(R.id.getdir);
                getdir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MapsActivity.this, Getlocation.class);
                        Bundle b = new Bundle();
                        b.putDouble("lat", latt);
                        b.putDouble("lng", lngg);
                        intent.putExtras(b);
                        startActivity(intent);

                    }
                });
            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }

            catch (IOException e) {
                e.printStackTrace();
            }


            //  double value = Double.parseDouble(result);

        }


    }
}