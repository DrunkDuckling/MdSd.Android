package com.example.mdsdproject.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mdsdproject.R;
import com.example.mdsdproject.models.Places;
import com.example.mdsdproject.services.PlacesService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapsFragment extends Fragment implements LocationListener {

    private static final String TAG = "MapsFragment";

    private GoogleMap mMap;
    private String[] placeNames;
    private String[] imageUrl;
    private List<Places> findPlaces;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private String latitude;
    private String longitude;
    // DSL Params
    String filterQuery = "Hotels";
    double distanceThreshold = 5000;
    Location startPoint = new Location("locationA"); // Used for distance measuring
    Location endPoint = new Location("locationB"); // Used for distance measuring

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            try {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            Log.i(TAG, "onMapReady: ");
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        latitude = Double.toString(location.getLatitude());
        longitude = Double.toString(location.getLongitude());
        Log.v(TAG, "IN ON LOCATION CHANGE, lat=" + latitude + ", lon=" + longitude);

        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
            new GetPlacesAsyncTask(location).execute();
            //findNearLocation(location);
            System.out.println("HERE");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {    }

    @Override
    public void onProviderEnabled(String provider) {    }

    @Override
    public void onProviderDisabled(String provider) {    }

    class GetPlacesAsyncTask extends AsyncTask<Void, Void, Void>{

        private Location asyncLoc;

        public GetPlacesAsyncTask(Location location) {
            this.asyncLoc = location;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            findNearLocation(asyncLoc);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void findNearLocation(Location location)   {
        Log.i(TAG, "findNearLocation: ");
        if (getActivity() != null) {
            PlacesService service = new PlacesService(getActivity().getResources().getString(R.string.google_maps_key));
            findPlaces = service.findPlaces(location.getLatitude(), location.getLongitude(), filterQuery, distanceThreshold);
            Log.i(TAG, "findNearLocation: " + findPlaces);
            placeNames = new String[findPlaces.size()];
            imageUrl = new String[findPlaces.size()];

            for (int i = 0; i < findPlaces.size(); i++) {
                if (findPlaces.get(i) != null) {
                    Places placeDetail = findPlaces.get(i);
                    endPoint.setLatitude(placeDetail.getLatitude());
                    endPoint.setLongitude(placeDetail.getLongitude());

                    if (startPoint.distanceTo(endPoint) < distanceThreshold) {
                        System.out.println(placeDetail.getName());
                        placeNames[i] = placeDetail.getName();
                        imageUrl[i] = placeDetail.getName();
                    }
                }
            }
        }
    }
}