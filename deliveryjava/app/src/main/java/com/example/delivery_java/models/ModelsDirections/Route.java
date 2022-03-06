package com.example.delivery_java.models.ModelsDirections;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {
    @SerializedName("overview_polyline")
    private OverviewPolyLine overview_polyline;

    private List<Legs> legs;

    public OverviewPolyLine getOverviewPolyLine() {
        return overview_polyline;
    }

    public List<Legs> getLegs() {
        return legs;
    }

    public void setOverviewPolyLine(OverviewPolyLine overviewPolyLine) {
        this.overview_polyline = overviewPolyLine;
    }

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }
}

