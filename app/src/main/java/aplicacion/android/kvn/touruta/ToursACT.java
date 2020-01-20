package aplicacion.android.kvn.touruta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ToursACT extends AppCompatActivity {

    MyDBHandler dbHandler;
    SQLiteDatabase db;
    Tour newTour;
    RecyclerView recyclerView;
    ArrayList<Tour> tourList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);

        dbHandler=new MyDBHandler(this,MyDBHandler.DATABASE_NAME,null,1);
        FillToursTable();

        tourList=new ArrayList<>();

        recyclerView=findViewById(R.id.recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TourListQuery();

        TourRecyclerAdapter tourAdapter = new TourRecyclerAdapter(tourList);
        recyclerView.setAdapter(tourAdapter);

    }

    private void TourListQuery() {
        db=dbHandler.getReadableDatabase();
        Tour tour;
        Cursor c=db.rawQuery("SELECT * FROM "+dbHandler.TABLE_TOURS,null);

        while(c.moveToNext()){
            tour=new Tour();
            tour.setTourName(c.getString(1));
            tour.setTourDescription(c.getString(2));
            tour.setTourCountry(c.getString(3));
            tour.setTourDistance(c.getString(4));
            tour.setTourDuration(c.getString(5));
            tour.setTourNumCheckpoints(c.getString(6));
            tour.setTourPicture(c.getString(7));

            tourList.add(tour);
        }
    }

    private void FillToursTable(){
        if(RecordsQuantity()==0){
            String[] tours = ReadArchive();

            db = dbHandler.getWritableDatabase();
            for (int i = 0; i < tours.length ; i++) {
                String[] line = tours[i].split(";");

                newTour=new Tour(line[0],line[1],line[2],line[3],line[4],line[5],line[6]);

                dbHandler.AddTour(newTour);
            }
            Toast.makeText(this,"REG="+tours.length,Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    private long RecordsQuantity(){
        db = dbHandler.getReadableDatabase();
        long qnt = DatabaseUtils.queryNumEntries(db,MyDBHandler.TABLE_TOURS);
        db.close();
        return qnt;
    }

    private String[] ReadArchive(){
        InputStream inputStream = getResources().openRawResource(R.raw.tours);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            int i= inputStream.read();
            while(i!=-1){
                byteArrayOutputStream.write(i);
                i=inputStream.read();
            }
            inputStream.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return byteArrayOutputStream.toString().split("\n");
    }
}
