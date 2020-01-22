package aplicacion.android.kvn.touruta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    RecyclerView recyclerView;
    ArrayList<Tour> tourList;
    String[] pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);

        dbHandler=new MyDBHandler(this,MyDBHandler.DATABASE_NAME,null,1);
        FillToursTable();

        tourList=new ArrayList<>();
        pictures=getResources().getStringArray(R.array.pictures);
        recyclerView=findViewById(R.id.recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TourListQuery();

        TourRecyclerAdapter tourAdapter = new TourRecyclerAdapter(this,R.layout.tourlist_cardlayout,tourList);

        tourAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), tourList.get(recyclerView.getChildAdapterPosition(view)).getTourName(), Toast.LENGTH_SHORT).show();
                Tour tour = new Tour();
                tour.setTourName(tourList.get(recyclerView.getChildAdapterPosition(view)).getTourName());
                tour.setTourDescription(tourList.get(recyclerView.getChildAdapterPosition(view)).getTourDescription());
                tour.setTourCountry(tourList.get(recyclerView.getChildAdapterPosition(view)).getTourCountry());
                tour.setTourDistance(tourList.get(recyclerView.getChildAdapterPosition(view)).getTourDistance());
                tour.setTourDuration(tourList.get(recyclerView.getChildAdapterPosition(view)).getTourDuration());
                tour.setTourNumCheckpoints(tourList.get(recyclerView.getChildAdapterPosition(view)).getTourNumCheckpoints());
                tour.setTourPicture(tourList.get(recyclerView.getChildAdapterPosition(view)).getTourPicture());
                tour.setPictureId(tourList.get(recyclerView.getChildAdapterPosition(view)).getPictureId());

                Intent TourDetailsIntetn = new Intent(getApplicationContext(),TourDetailsACT.class);
                startActivity(TourDetailsIntetn);
            }
        });

        recyclerView.setAdapter(tourAdapter);

    }

    private void TourListQuery() {
        db=dbHandler.getReadableDatabase();
        Tour tour;
        Cursor c=db.rawQuery("SELECT * FROM "+dbHandler.TABLE_TOURS,null);
        int i=0;

        while(c.moveToNext()){
            //tour con id de imagen
            tour=new Tour();
            tour.setTourName(c.getString(1));
            tour.setTourDescription(c.getString(2));
            tour.setTourCountry(c.getString(3));
            tour.setTourDistance(c.getString(4));
            tour.setTourDuration(c.getString(5));
            tour.setTourNumCheckpoints(c.getString(6));

            int pictureID= getResources().getIdentifier(pictures[i],"drawable",this.getPackageName());
            tour.setPictureId(pictureID);

            tourList.add(tour);
            i++;
        }
    }

    private void FillToursTable(){
        if(RecordsQuantity()==0){
            String[] tours = ReadArchive();

            db = dbHandler.getWritableDatabase();
            for (int i = 0; i < tours.length ; i++) {
                String[] line = tours[i].split(";");

                //tour con nombre de imagen
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
