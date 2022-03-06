package com.example.delivery_java.models.ModelsDirections;

import com.google.gson.annotations.SerializedName;

public class OverviewPolyLine {

    @SerializedName("points")
    public String points;

    public String getPoints() {
        return points;
    }
    public void setPoints(String points) {
        this.points = points;
    }

}

