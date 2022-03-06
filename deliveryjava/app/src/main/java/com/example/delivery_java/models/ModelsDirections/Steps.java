package com.example.delivery_java.models.ModelsDirections;

import android.location.Location;

public class Steps {
    private android.location.Location start_location;
    private android.location.Location end_location;
    private OverviewPolyLine polyline;

    public android.location.Location getStart_location() {
        return start_location;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public OverviewPolyLine getPolyline() {
        return polyline;
    }
}