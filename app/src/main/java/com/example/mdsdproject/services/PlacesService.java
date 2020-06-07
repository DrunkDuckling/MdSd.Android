package com.example.mdsdproject.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.mdsdproject.models.Places;

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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlacesService {

    private String API_KEY;
    private static final String TAG = "PlacesService";
    public PlacesService(String apikey) {
        this.API_KEY = apikey;
        Log.i(TAG, "PlacesService: " + API_KEY);
    }

    public List<Places> findPlaces(double latitude, double longitude, String placeSpacification, double radius) {
        String urlString = makeUrl(latitude, longitude,placeSpacification, radius);
        Log.i(TAG, "findPlaces: " + urlString);
        try {
            String json = getJSON(urlString);

            //System.out.println(json);
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("results");


            ArrayList<Places> arrayList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    Places place = Places.JSONToResult((JSONObject) array.get(i));
                    place.setBitmap(convertURLToBitmap(place.getIcon()));
                    //Log.v("Places Services ", "" + place);
                    arrayList.add(place);
                } catch (Exception e) {
                }
            }
            return arrayList;
        } catch (JSONException ex) {
            Logger.getLogger(com.example.mdsdproject.services.PlacesService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String makeUrl(double latitude, double longitude,String place, double radius) {
        StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");

        if (place.equals("")) {
            urlString.append("&location=");
            urlString.append(latitude);
            urlString.append(",");
            urlString.append(longitude);
            urlString.append("&radius=" + radius);
            urlString.append("&sensor=false&key=" + API_KEY);
        } else {
            urlString.append("&location=");
            urlString.append(latitude);
            urlString.append(",");
            urlString.append(longitude);
            urlString.append("&radius=" + radius);
            urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
        }

        return urlString.toString();
    }

    protected String getJSON(String url) {
        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl)
    {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public Bitmap convertURLToBitmap(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}