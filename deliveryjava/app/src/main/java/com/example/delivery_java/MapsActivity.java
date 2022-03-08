package com.example.delivery_java;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.delivery_java.databinding.ActivityMapsBinding;
import com.example.delivery_java.interfaces.MapsDirectionAPI;
import com.example.delivery_java.models.ModelsDirections.DirectionResults;
import com.example.delivery_java.models.ModelsDirections.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST = 500;

    private ActivityMapsBinding binding;
    private MapsDirectionAPI mapsDirectionAPI;
    private List<LatLng> polylinelist;
    private PolylineOptions polylineOptions;
    private LatLng origion, destino;
    private LatLng locationFinal;
    private Marker marker;

    // Mapa de Google
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        String destino = (String) extras.get("direccion");

        locationFinal = getLocationFromAddress(this, destino);


        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/").build();
        mapsDirectionAPI = retrofit.create(MapsDirectionAPI.class);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);

            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Esto es por que tipo de mapa quieres que aparezca, si normal, satelite, terrestre
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Esto es para que te aparezcan lienas o verdes o rojas o amarillas dependiendo del trafico
        // lo desactive por que era confuso con mi linea
        mMap.setTrafficEnabled(false);

        origion = new LatLng(latitude, longitude);
        String myLatitudeFinal = Double.valueOf(latitude).toString();
        String myLongitudeFinal = Double.valueOf(longitude).toString();

        agregarMarcador(locationFinal.latitude, locationFinal.longitude);
        destino = locationFinal;
        String destinationLongitudeFinal = Double.valueOf(locationFinal.longitude).toString();
        String destinationLatitudeFinal = Double.valueOf(locationFinal.latitude).toString();

        getDirection(myLatitudeFinal + "," + myLongitudeFinal, destinationLatitudeFinal + ", " + destinationLongitudeFinal);

        /* Esto te genera una linea recta
         Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(42.23842, -8.7191752), new LatLng(42.2283124, -8.7336103))
                .width(10).color(Color.RED));*/
    }

    /**
     * Funcion que agrega un marcador, en este caso destino, al mapa, pasandole una latitud y una longitud
     *
     * @param lat @double Latitud
     * @param lng @double Longitud
     */
    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions()
                /* .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue)) */
                .position(coordenadas)
                .title("Destino"));
    }


    /**
     * Funcion que hace la llamada a la api de google maps y devuelve un
     * diccionario con las rutas y genera la linea a raiz de ese array
     * @param origin @String el origen de la ruta
     * @param destination @String el destino de la ruta
     */
    private void getDirection(String origin, String destination) {

        mapsDirectionAPI.getDirection("driving", "less_driving", origin, destination,
                getString(R.string.google_maps_key)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DirectionResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(DirectionResults directionResults) {
                        polylinelist = new ArrayList<>();
                        List<Route> routeList = directionResults.getRoutes();
                        for (Route route : routeList) {
                            String polyline = route.getOverviewPolyLine().getPoints();
                            polylinelist.addAll(decodePoly(polyline));
                        }
                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.BLUE);
                        polylineOptions.width(15);
                        polylineOptions.startCap(new ButtCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polylinelist);
                        mMap.addPolyline(polylineOptions);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(origion);
                        builder.include(destino);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    /**
     * Funcion para generar un array de LatLng para generar las lineas
     * @param encoded @string polilinea
     * @return @array[LatLng] devuelve un array con las coordenadas para trazar la ruta
     */
    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }
        return poly;
    }


    /**
     * Funcion en la que le paso el contexto y una string con la direccion y me devuelve un
     * LatLng con la longitud y latitud para generar el marker y la polyline en el mapa
     *
     * @param context    @Context
     * @param strAddress @String con la direccion que deseamos convertir
     * @return p1 @LatLng diccionario con la latitud y logitud de la direccion
     */
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}
