package aplicacion.android.kvn.touruta;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ToursACT extends AppCompatActivity {

    MyDBHandler dbHandler;
    SQLiteDatabase db;
    Tour newTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);

        dbHandler=new MyDBHandler(this,MyDBHandler.DATABASE_NAME,null,1);
        FillToursTable();

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
