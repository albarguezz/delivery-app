package com.example.delivery_java.interfaces;

import com.example.delivery_java.models.ModelsDirections.DirectionResults;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RoutesAPI {

    @GET("/maps/api/directions/json")
    public void getJson(@Query("origin") String origin, @Query("destination") String destination, Callback<DirectionResults> callback);
}

