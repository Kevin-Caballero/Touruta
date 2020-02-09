package aplicacion.android.kvn.touruta.ACTIVITIES;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import aplicacion.android.kvn.touruta.MyDBHandler;
import aplicacion.android.kvn.touruta.OBJECTS.Checkpoint;
import aplicacion.android.kvn.touruta.R;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TourPlayerACT extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, Runnable {
    private GoogleMap mMap;
    private Marker marker;
    double lat = 0.0;
    double lng = 0.0;
    String dir;
    MyDBHandler dbHandler;
    SQLiteDatabase db;
    ArrayList<Checkpoint> checkpoints;
    Button btnPlayPause, btnPrevious, btnNext;
    SeekBar seekBar;
    //TAMAÑO DEL ARRAY = NUM CHECKPOINTS (PONGO TRES CANCIONES COMO EJEMPLO)
    MediaPlayer[] explanations = new MediaPlayer[3];
    int pos = 0;
    Handler handler;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_player_act);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbHandler = new MyDBHandler(this, MyDBHandler.DATABASE_NAME, null, 1);

        CheckpointListQuery();

        //TODO AUTOMATIZAR LA REPRODUCCION DE AUDIOS AL ACERCARSE A LOS CHECKPOINTS
        seekBar = findViewById(R.id.seekBar);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnPlayPause.setOnClickListener(this);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        explanations[0] = MediaPlayer.create(this, R.raw.ilomilo);
        explanations[1] = MediaPlayer.create(this, R.raw.badguy);
        explanations[2] = MediaPlayer.create(this, R.raw.buryafriend);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    float ratio = explanations[pos].getCurrentPosition() / (float) explanations[pos].getDuration();
                    seekBar.setProgress((int) (ratio * seekBar.getMax()), true);
                }
            }
        };
    }


    /**MAPA*/
    /**
     * Funcion para llenar un array list con los checkpoints de cada tour.
     * El primer paso es realizar la consulta que nos devolvera la informacion sobre un cursor.
     * Recorreremos dicho cursor mientras creamos objetos de tipo checkpoint con los datos de linea
     * para mas adelante llenar el arraylist de checkpoints
     */
    private void CheckpointListQuery() {
        db = dbHandler.getReadableDatabase();
        Cursor c = db.query(MyDBHandler.TABLE_CHECKPOINTS, null, MyDBHandler.COLUMN_CHECKPOINT_TOUR_ID + " = ? ", new String[]{Integer.toString(TourDetailsACT.selectedTour.getTourId())}, null, null, null);
        checkpoints = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                int tourid = c.getInt(1);
                double lat = c.getDouble(2);
                double lon = c.getDouble(3);
                String name = c.getString(4);
                Checkpoint checkpoint = new Checkpoint(tourid, lat, lon, name);
                checkpoints.add(checkpoint);
            } while (c.moveToNext());
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

    /**
     * Cuando obtenemos el mapa asincronicamente, definimos el tipo de mapa, asi como la configuracion
     * de la interfaz del mismo y llamamos a la funcion que obtiene nuestra ubicacion
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setMyLocationEnabled(true);


        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setAllGesturesEnabled(true);

        AddCheckpointMarkers();
        myUbication();
    }

    /**
     * Comprobar si el gps del dispositivo esta activo. Si no lo esta se lanza un intent implicito
     * a la configuracion del gps para que el usuario lo active.
     * Tambien se comprueba si el permiso de acceso a la ubicacion esta aceptado para que en caso
     * contrario se le vuelva a solicitar.
     */
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

    /**
     * Obtener la direccion de la calle gracias a la longitud y latitud del dispositivo.
     * El metodo getFromLocation de la clase geocoder devuelve un array de direcciones
     * que se sabe que describen el área que rodea inmediatamente a la latitud y longitud dadas.
     * En caso de que la lista contenga alguna direccion, le asignamos dicha direccion a la variable
     * global dir para mas tarde trabajar con dicha variable
     */
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

    /**
     * Este segmento de codigo nos permite agregar un marcador al mapa estableciendo una posicion
     * un titulo para dicho marcador y se le puede establecer un icono para mostrar.
     */
    private void AddCheckpointMarkers() {
        for (int i = 0; i < checkpoints.size(); i++) {

            LatLng cp = new LatLng(checkpoints.get(i).getLat(), checkpoints.get(i).getLon());
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(cp)
                    .title(checkpoints.get(i).getName()));
        }
    }

    private void AddYourMarker(double lat, double lng) {
        LatLng you = new LatLng(lat, lng);
        CameraUpdate myUbication = CameraUpdateFactory.newLatLngZoom(you, 15);
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions()
                .position(you)
                .title("YOU")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_you)));

        mMap.animateCamera(myUbication);
    }

    /**
     * Funcion mediante la cual gracias a un escuchador actualizamos el marcador que nos representa
     * sobre el mapa
     */
    private void UpdateUbication(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            AddYourMarker(lat, lng);
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

    /**
     * Obtener mi ubicacion gracias a las funciones declaradas anteriormente en caso de que tengamos
     * permiso.
     * Se obtiene la ubicacion gracias al metodo requestLocationUpdates de la clase LocationManager.
     * Dicho metodo acutaliza nuestra posicion en base a un tiempo y a una distancia minima y al
     * escuchador utilizado anteriormente
     */
    private void myUbication() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 25, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            UpdateUbication(location);
        }
    }


    /**
     * REPRODUCTOR
     */
    @Override
    public void onClick(View view) {
        if (view == btnPlayPause) {
            PlayPause();
        } else if (view == btnPrevious) {
            Previous();
        } else if (view == btnNext) {
            Next();
        }
        thread = new Thread(this);
        thread.start();
    }

    private void PlayPause() {

        if (explanations[pos].isPlaying()) {
            explanations[pos].pause();
            btnPlayPause.setBackgroundResource(R.drawable.play);
        } else {
            explanations[pos].start();
            btnPlayPause.setBackgroundResource(R.drawable.pause);
        }
    }

    /**
     * Si el indice es menor que la longitud del array y ademas se esta reproduciondo una pista,
     * debemos parar la pista actual, incrementar en uno el indice e inicial la nueva pista.
     * En caso de que no hubiese nada reproduciendose simplemete actualizamos el indice
     */
    private void Next() {
        if (pos < explanations.length - 1) {
            if (explanations[pos].isPlaying()) {
                explanations[pos].stop();
            }
            pos++;
            explanations[pos].start();
        } else if (pos == explanations.length - 1) {
            explanations[explanations.length - 1].stop();
            pos = 0;
            btnPlayPause.setBackgroundResource(R.drawable.play);
            btnPlayPause.setEnabled(false);
            btnNext.setEnabled(false);
            btnPrevious.setEnabled(false);
            Toast.makeText(this, "TOUR FINALIZADO", Toast.LENGTH_SHORT).show();
        }
    }

    private void Previous() {
        if (pos >= 1) {
            if (explanations[pos].isPlaying()) {
                explanations[pos].stop();
                explanations[0] = MediaPlayer.create(this, R.raw.ilomilo);
                explanations[1] = MediaPlayer.create(this, R.raw.badguy);
                explanations[2] = MediaPlayer.create(this, R.raw.buryafriend);
                pos--;
                explanations[pos].start();
            } else {
                pos--;
            }
        } else {
            pos = explanations.length - 1;
            Toast.makeText(this, "TOUR FINALIZADO", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void run() {
        long actual;
        actual = System.currentTimeMillis();
        while (explanations[pos].isPlaying()) {
            if ((System.currentTimeMillis() - actual) >= 1) {
                //Actualizar barra de progreso.
                handler.obtainMessage(1).sendToTarget();
                actual = System.currentTimeMillis();
            }
        }
    }
}
