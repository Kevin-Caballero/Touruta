package aplicacion.android.kvn.touruta;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TourDetailsACT extends AppCompatActivity implements View.OnClickListener {
    Button btnVerMas, btnSend;
    RecyclerView CommentRecyclerView;
    TextView name, description, duration, distance, checkpoints;
    ImageView picture;
    EditText commentBox;
    MyDBHandler dbHandler;
    SQLiteDatabase db;
    List<Comment> commentShortList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details_act);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnVerMas = findViewById(R.id.btnVerMas);
        btnVerMas.setOnClickListener(this);
        CommentRecyclerView = findViewById(R.id.commentsShortRecyclerView);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        duration = findViewById(R.id.duration);
        distance = findViewById(R.id.distance);
        checkpoints = findViewById(R.id.checkpoints);
        picture = findViewById(R.id.picture);
        picture.setOnClickListener(this);
        commentBox = findViewById(R.id.commentBox);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnSend.setVisibility(View.GONE);
        commentBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){ //TODO comprobar que no sean espacios
                    btnSend.setVisibility(View.VISIBLE);
                }else{
                    btnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Bundle receivedBundle = getIntent().getExtras();

        Tour selectedTour = (Tour) receivedBundle.getSerializable("selectedTour");

        commentShortList= new ArrayList<>();

        name.setText(selectedTour.getTourName());
        description.setText(selectedTour.getTourDescription());
        duration.setText("DURATION: " + selectedTour.getTourDuration());
        distance.setText("DISTANCE: " + selectedTour.getTourDistance() + "km");
        checkpoints.setText("CHECKPOINTS: " + selectedTour.getTourNumCheckpoints());
        picture.setBackground(getDrawable(selectedTour.getPictureId()));

        dbHandler = new MyDBHandler(this, MyDBHandler.DATABASE_NAME, null, 1);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnVerMas) {
            Toast.makeText(this, "nueva activ", Toast.LENGTH_SHORT).show();
        } else if (view == btnSend) {
            Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show();
        }
    }

    private void CommentShortListQuery(){
        db=dbHandler.getReadableDatabase();
        Comment comment;
        Cursor c = db.rawQuery("SELECT * FROM "+ MyDBHandler.TABLE_COMMENTS +" LIMIT 5",null);

        while(c.moveToFirst()){
            comment = new Comment();
            //TODO datos reales!!!!!
            comment.setCommentUserId(1);
            comment.setCommentTourId(1);
            comment.setCommentContent("sdas");

            commentShortList.add(comment);
        }
    }
}
