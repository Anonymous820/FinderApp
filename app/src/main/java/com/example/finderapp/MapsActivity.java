package com.example.finderapp;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.finderapp.model.SuitcasePlace;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "Tokes";

    EditText edt;
    private GoogleMap mMap;
    int flag=5;
    int temp=0;

    double latlo;
    double longlo;

    Button btnok;

    ArrayList<SuitcasePlace> arrayList=new ArrayList<>();

    String url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        btnok=findViewById(R.id.okbtn);
        edt=findViewById(R.id.edt);
        mapFragment.getMapAsync(this);


       getloc();

       btnok.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mMap.clear();
               getloc();
           }
       });





        }

    private void getloc() {

        Log.d(TAG, "CLICKED ");
        SmartLocation.with(this).location()
                .continuous()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(  Location location) {

                        Log.d(TAG, "CLICKEDAGAIN ");

                        latlo=location.getLatitude();

                        longlo=location.getLatitude();




                                Geocoder geocoder=new Geocoder(MapsActivity.this);

                                ArrayList<Address> addresses=new ArrayList<>();

                                try {
                                    addresses= (ArrayList<Address>) geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&radius=1500&type="+edt.getText().toString()+" &key=AIzaSyB2dj7bdFVDQ9V8w1v-C87W-XLxSo7ERhc";

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(location.getLatitude(), location.getLongitude())).title(addresses.get(0).getAddressLine(0)));
                                mMap.animateCamera(CameraUpdateFactory
                                        .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14f));

                                AndroidNetworking.initialize(MapsActivity.this);
                                AndroidNetworking.get(url)
                                        .setPriority(Priority.HIGH)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                try {
                                                    JSONArray results=response.getJSONArray("results");

                                                    for (int i = 0; i < results.length(); i++) {

                                                        SuitcasePlace place=new SuitcasePlace();

                                                        JSONObject obj=results.getJSONObject(i);
                                                        JSONObject geometry=obj.getJSONObject("geometry");
                                                        JSONObject location=geometry.getJSONObject("location");

                                                        double lat=location.getDouble("lat");
                                                        double lng=location.getDouble("lng");

//                                                        Log.d(TAG, "onResponse: LAT: "+lat+" LNG: "+lng);

                                                        String name=obj.getString("name");

                                                        place.setLat(lat);
                                                        place.setLng(lng);
                                                        place.setName(name);

                                                        arrayList.add(place);


                                                    }


                                                    for (SuitcasePlace place : arrayList) {

                                                        mMap.addMarker(new MarkerOptions()
                                                                .position(new LatLng(place.getLat(),place.getLng())).title(place.getName())
                                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                                    }


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });
                        mMap.addCircle(new CircleOptions()
                                .radius(1500)
                                .center(new LatLng(location.getLatitude(),location.getLongitude()))
                                .fillColor(Color.BLUE));


                            }
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }
}
