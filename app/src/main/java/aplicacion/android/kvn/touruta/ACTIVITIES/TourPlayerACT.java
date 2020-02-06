package aplicacion.android.kvn.touruta.ACTIVITIES;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import aplicacion.android.kvn.touruta.R;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.BitSet;

public class TourPlayerACT extends FragmentActivity implements OnMapReadyCallback {
    double lat,lon;
    private GoogleMap mMap;
    int permissionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Al crearse la actividad lo primero es comprobar los permisos para acceder a la ubicacion.
        * Si no tiene los permisos aceptados se solicitaran.
        * Luego se comprueba el acceso a los servicios de google,
        * en caso afirmativo se carga el mapa de lo contrario mostrara un error.*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_player_act);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        permissionCheck= ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);

        //SOLICITAR PERMISO SI NO ESTA ACTIVADO
        if (permissionCheck== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if(status== ConnectionResult.SUCCESS){ //si nos conseguimos conectar a los servicios de google play
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }else{
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,(Activity)getApplicationContext(),10);
            dialog.show();
        }
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true); //habilitar ubicacion actual dentro del mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings=mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setAllGesturesEnabled(true);

        LocationManager locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat=location.getLatitude();
                lon=location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };

        LatLng marcador = new LatLng(43.3125271,-1.8986133);
        LatLng you = new LatLng(lat, lon);

        mMap.addMarker(new MarkerOptions().position(marcador).title("marcador"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marcador,16));
    }
}
