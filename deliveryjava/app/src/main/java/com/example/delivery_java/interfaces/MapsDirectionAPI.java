package com.example.delivery_java.interfaces;

import com.example.delivery_java.models.ModelsDirections.DirectionResults;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapsDirectionAPI {
    @GET("/maps/api/directions/json")
    Single<DirectionResults> getDirection (@Query("mode") String mode,
                                           @Query("transit_route_preferance") String preferance,
                                           @Query("origin") String origin,
                                           @Query("destination") String destination,
                                           @Query("key") String key);
}

