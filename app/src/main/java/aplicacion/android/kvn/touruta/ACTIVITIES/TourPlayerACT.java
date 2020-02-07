package aplicacion.android.kvn.touruta.ACTIVITIES;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import aplicacion.android.kvn.touruta.R;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.BitSet;
import java.util.List;
import java.util.Locale;

public class TourPlayerACT extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marker;
    double lat = 0.0;
    double lng = 0.0;
    String dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_player_act);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /**Cuando obtenemos el mapa asincronicamente, definimos el tipo de mapa, asi como la configuracion
    * de la interfaz del mismo y llamamos a la funcion que obtiene nuestra ubicacion*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings=mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
        myUbication();
    }

    /**Comprobar si el gps del dispositivo esta activo. Si no lo esta se lanza un intent implicito
    * a la configuracion del gps para que el usuario lo active.
    * Tambien se comprueba si el permiso de acceso a la ubicacion esta aceptado para que en caso
    * contrario se le vuelva a solicitar.*/
    private void LocationStart() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
    }

    /** Obtener la direccion de la calle gracias a la longitud y latitud del dispositivo.
     * El metodo getFromLocation de la clase geocoder devuelve un array de direcciones
     * que se sabe que describen el área que rodea inmediatamente a la latitud y longitud dadas.
     * En caso de que la lista contenga alguna direccion, le asignamos dicha direccion a la variable
     * global dir para mas tarde trabajar con dicha variable*/
    public void SetLocation(Location location) {
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    dir = (DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Este segmento de codigo nos permite agregar un marcador al mapa estableciendo una posicion
     * un titulo para dicho marcador y se le puede establecer un icono para mostrar.*/
    private void AddMarker(double lat, double lng) {
        LatLng coord = new LatLng(lat, lng);
        CameraUpdate myUbication = CameraUpdateFactory.newLatLngZoom(coord, 16);
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions()
                .position(coord)
                .title("YOU")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_you)));

        mMap.animateCamera(myUbication);
    }

    /** Funcion mediante la cual gracias a un escuchador actualizamos el marcador que nos representa
     * sobre el mapa*/
    private void UpdateUbication(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            AddMarker(lat, lng);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            UpdateUbication(location);
            SetLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(TourPlayerACT.this, "GPS ACTIVADO", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(TourPlayerACT.this, "GPS DESACTIVADO", Toast.LENGTH_SHORT).show();
            LocationStart();
        }

    };

    /**Obtener mi ubicacion gracias a las funciones declaradas anteriormente en caso de que tengamos
     * permiso.
     * Se obtiene la ubicacion gracias al metodo requestLocationUpdates de la clase LocationManager.
     * Dicho metodo acutaliza nuestra posicion en base a un tiempo y a una distancia minima y al
     * escuchador utilizado anteriormente*/
    private void myUbication() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
            return;
        }else{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            UpdateUbication(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,10,locationListener);
        }
    }
}
