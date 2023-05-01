package com.example.myweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    View view;
    MainActivity mainActivity;
    GoogleMap gMap;
    FrameLayout map;
    Location currentLocation;
    RelativeLayout relativeLayout;
    FusedLocationProviderClient fusedClient;
    LottieAnimationView lottieAnimationView;
    Marker marker;
    SearchView searchView;
    String marker_name;
    private static final int REQUEST_CODE =101;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_maps, container, false);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        mainActivity = (MainActivity) getActivity();
        mainActivity.binding.fab.setVisibility(View.GONE);

        map = view.findViewById(R.id.google_map);
        searchView = view.findViewById(R.id.searchViewMaps);
        lottieAnimationView = view.findViewById(R.id.erruers);
        relativeLayout = view.findViewById(R.id.mapsrelative);
        if (!internetIsConnected()){
            lottieAnimationView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        else{
            lottieAnimationView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        searchView.clearFocus();


        fusedClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLocation();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = searchView.getQuery().toString();
                if(loc == null){
                    Toast.makeText(getContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
                }else {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(loc,1);
                        Log.d("ADDRESS", String.valueOf(addressList));
                        if(addressList.size() > 0){
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
                            if(marker != null){
                                marker.remove();
                            }
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(loc);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            gMap.clear();
                            marker = gMap.addMarker(markerOptions);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query updates
                return false;
            }
        });


        SupportMapFragment supportMapFragment =(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }


        return view;
    }

    private boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
                getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;

        }

        Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    SupportMapFragment supportMapFragment =(SupportMapFragment)
                            getChildFragmentManager().findFragmentById(R.id.google_map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsFragment.this);
                }
            }
        });
    }

    private String CityName(LatLng latLng)
    {
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Button button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLngs = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLngs).title(CityName(latLngs));
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngs));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs,10));
                googleMap.clear();
                googleMap.addMarker(markerOptions);
            }
        });


        this.gMap = googleMap;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : "+ latLng.longitude );
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        latLng,10
                ));
                markerOptions.title(CityName(latLng));
                googleMap.clear();
                googleMap.addMarker(markerOptions);
            }

        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                marker_name = marker.getTitle();
                Bundle bundle =new Bundle();
                bundle.putString("City",marker_name);
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                mainActivity.replaceFragment(homeFragment);

                return false;
            }
        });

    }

}



