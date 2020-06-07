package com.example.mdsdproject.models;

import android.graphics.Bitmap;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class Places {
    private String id;
    private String icon;
    private String name;
    private Double latitude;
    private Double longitude;
    private Bitmap bitmap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static Places JSONToResult(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
			    Places result = new Places();
                JSONObject geometry = (JSONObject) jsonObject.get("geometry");
                JSONObject location = (JSONObject) geometry.get("location");
                result.setLatitude((Double) location.get("lat"));
                result.setLongitude((Double) location.get("lng"));
                result.setIcon(jsonObject.getString("icon"));
                result.setName(jsonObject.getString("name"));
                result.setId(jsonObject.getString("id"));
                return result;
            } catch (JSONException ex) {
                Logger.getLogger(Places.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "«model.name»{" + "id=" + id + ", icon=" + icon + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }
}
