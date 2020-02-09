package aplicacion.android.kvn.touruta.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import aplicacion.android.kvn.touruta.MyDBHandler;
import aplicacion.android.kvn.touruta.OBJECTS.Checkpoint;
import aplicacion.android.kvn.touruta.R;
import aplicacion.android.kvn.touruta.OBJECTS.Tour;
import aplicacion.android.kvn.touruta.ADAPTERS.TourRecyclerAdapter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ToursACT extends AppCompatActivity {

    MyDBHandler dbHandler;
    SQLiteDatabase db;
    Tour newTour;
    Checkpoint newCheckpoint;
    RecyclerView recyclerView;
    ArrayList<Tour> tourList;
    String[] pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tours);

        dbHandler = new MyDBHandler(this, MyDBHandler.DATABASE_NAME, null, 1);

        FillToursTable();
        FillCheckpointsTable();


        tourList = new ArrayList<>();
        pictures = getResources().getStringArray(R.array.pictures);
        recyclerView = findViewById(R.id.recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TourListQuery();

        TourRecyclerAdapter tourAdapter = new TourRecyclerAdapter(this, R.layout.tourlist_cardlayout, tourList);

        tourAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), tourList.get(recyclerView.getChildAdapterPosition(view)).getTourName(), Toast.LENGTH_SHORT).show();
                Tour selectedTour = tourList.get(recyclerView.getChildAdapterPosition(view));

                Intent TourDetailsIntetn = new Intent(getApplicationContext(), TourDetailsACT.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedTour", selectedTour);
                TourDetailsIntetn.putExtras(bundle);
                startActivity(TourDetailsIntetn);
            }
        });

        recyclerView.setAdapter(tourAdapter);
    }

    /**Funcion que llena un arraylist de tours para posteriormente pasarselo a un adaptador.
     * Lanzamos una consulta contra la base de datos que nos devolvera todos los tours en un cursor.
     * Recorremos dicho cursos creando instancias de la clase tour para asi añadirlos al arraylist.*/
    private void TourListQuery() {
        db = dbHandler.getReadableDatabase();
        Tour tour;
        Cursor c = db.rawQuery("SELECT * FROM " + dbHandler.TABLE_TOURS, null);
        int i = 0;

        while (c.moveToNext()) {
            //tour con id de imagen
            tour = new Tour();
            tour.setTourId(c.getInt(0));
            tour.setTourName(c.getString(1));
            tour.setTourDescription(c.getString(2));
            tour.setTourCountry(c.getString(3));
            tour.setTourDistance(c.getString(4));
            tour.setTourDuration(c.getString(5));
            tour.setTourNumCheckpoints(c.getString(6));

            int pictureID = getResources().getIdentifier(pictures[i], "drawable", this.getPackageName());
            tour.setPictureId(pictureID);

            tourList.add(tour);
            i++;
        }
    }

    /**Si la tabla de tours esta vacia, llenamos al array tours con el array de strigns
     * que nos devuelve la funcion ReadArchive.
     * Recorremos dicho array y volcamos sobre el array de strings line el contenido de
     * cada linea de cp pero separado por ";".
     * Finalmente recorremos line y creamos instancias de la clase tour con los valores obtenidos
     * y añadimos el contenido de dichos objetos a la base de datos*/
    private void FillToursTable() {
        if (RecordsQuantity(MyDBHandler.TABLE_TOURS) == 0) {
            String[] tours = ReadArchive(R.raw.tours);

            db = dbHandler.getWritableDatabase();
            for (int i = 0; i < tours.length; i++) {
                String[] line = tours[i].split(";");

                //tour con nombre de imagen
                newTour = new Tour(line[0], line[1], line[2], line[3], line[4], line[5], line[6]);

                dbHandler.AddTour(newTour);
            }
            //Toast.makeText(this, "REG=" + tours.length, Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    /**Si la tabla de checkpoints esta vacia, llenamos al array cp con el array de  strigns
     * que nos devuelve la funcion ReadArchive.
     * Recorremos dicho array y volcamos sobre el array de strings line el contenido de
     * cada linea de cp pero separado por ";".
     * Finalmente recorremos line y creamos instancias de la clase Checkpoint con los valores obtenidos
     * y añadimos el contenido de dichos objetos a la base de datos*/
    private void FillCheckpointsTable() {
        if (RecordsQuantity(MyDBHandler.TABLE_CHECKPOINTS) == 0) {
            String[] cp = ReadArchive(R.raw.checkpoints);

            db = dbHandler.getWritableDatabase();
            for (int i = 0; i < cp.length-1; i++) {
                String[] line = cp[i].split(";");
                for (int j = 0; j < line.length; j+=4) {
                    newCheckpoint = new Checkpoint( Integer.parseInt(line[j]),Double.parseDouble(line[j+1]) ,Double.parseDouble(line[j+2]),line[j+3]);
                    dbHandler.AddCheckpoint(newCheckpoint);
                }
            }
            db.close();
        }
    }

    /**Funcion para conocer el numero de registros existentes en una tabla*/
    private long RecordsQuantity(String tablename) {
        db = dbHandler.getReadableDatabase();
        long qnt = DatabaseUtils.queryNumEntries(db, tablename);
        db.close();
        return qnt;
    }

    /**Funcion que nos devuelve un array de strings con el contenido de un txt.
     * Primero volvamos sobre un stream de entrada el contenido del archivo.
     * Mientras haya contenido lo vamos pasando a un array de bytes de salida.
     * Finalmente devolvemos dicho array parseado a String y separado por saltos de linea*/
    private String[] ReadArchive(int ref) {
        InputStream inputStream = getResources().openRawResource(ref);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return byteArrayOutputStream.toString().split("\n");
    }
}
