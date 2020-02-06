package aplicacion.android.kvn.touruta.ACTIVITIES;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import aplicacion.android.kvn.touruta.ADAPTERS.CommentRecyclerAdapter;
import aplicacion.android.kvn.touruta.OBJECTS.Comment;
import aplicacion.android.kvn.touruta.MyDBHandler;
import aplicacion.android.kvn.touruta.R;
import aplicacion.android.kvn.touruta.OBJECTS.Tour;

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
    ArrayList<Comment> commentShortList;
    public static Tour selectedTour;
    Comment newComment;

    public static String tourId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details_act);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnVerMas = findViewById(R.id.btnVerMas);
        btnVerMas.setOnClickListener(this);
        CommentRecyclerView = findViewById(R.id.commentsShortRecyclerView);
        CommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                if (!charSequence.toString().isEmpty()) { //TODO comprobar que no sean espacios
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    btnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Bundle receivedBundle = getIntent().getExtras();

        selectedTour = (Tour) receivedBundle.getSerializable("selectedTour");

        name.setText(selectedTour.getTourName());
        description.setText(selectedTour.getTourDescription());
        duration.setText("DURATION: " + selectedTour.getTourDuration());
        distance.setText("DISTANCE: " + selectedTour.getTourDistance() + "km");
        checkpoints.setText("CHECKPOINTS: " + selectedTour.getTourNumCheckpoints());
        picture.setBackground(getDrawable(selectedTour.getPictureId()));

        dbHandler = new MyDBHandler(this, MyDBHandler.DATABASE_NAME, null, 1);

        commentShortList = new ArrayList<>();


        CommentShortListQuery();
        CommentRecyclerAdapter commentAdapter = new CommentRecyclerAdapter(this, R.layout.commentlist_cardlayout, commentShortList);
        CommentRecyclerView.setAdapter(commentAdapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TourPlayerIntent = new Intent(view.getContext(),TourPlayerACT.class);
                startActivity(TourPlayerIntent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnVerMas) {
            Intent allCommentsIntent = new Intent(this,AllCommentsACT.class);
            startActivity(allCommentsIntent);
        } else if (view == btnSend) {
            //Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show();
            db = dbHandler.getReadableDatabase();

            Cursor cU = db.query(MyDBHandler.TABLE_USERS, null, MyDBHandler.COLUMN_USER_EMAIL + " = ?", new String[]{LogInACT.logedUser.getEmail()}, null, null, null);
            Cursor cT = db.query(MyDBHandler.TABLE_TOURS, null, MyDBHandler.COLUMN_TOUR_NAME + " = ?", new String[]{selectedTour.getTourName()}, null, null, null);

            if (cU.moveToFirst() && cT.moveToFirst()) {
                tourId = cT.getString(0);

                newComment = new Comment();
                newComment.setCommentTourId(cT.getInt(0));
                newComment.setCommentUserId(cU.getInt(0));
                newComment.setCommentContent(commentBox.getText().toString());
            }

            if (dbHandler.AddComment(newComment) == 1) {
                commentBox.setText("");
                Toast.makeText(this, "COMMENT ADDED", Toast.LENGTH_SHORT).show();
                CommentShortListQuery();
                CommentRecyclerAdapter commentAdapter = new CommentRecyclerAdapter(this, R.layout.commentlist_cardlayout, commentShortList);
                CommentRecyclerView.setAdapter(commentAdapter);
            } else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void CommentShortListQuery() {
        commentShortList=new ArrayList<>();
        db = dbHandler.getReadableDatabase();
        Comment comment;

        Cursor cC=db.rawQuery("SELECT *  FROM "+ MyDBHandler.TABLE_COMMENTS  + " WHERE " + MyDBHandler.COLUMN_COMMENT_TOUR_ID + " = ?" + " ORDER BY " + MyDBHandler.COLUMN_COMMENT_ID + " DESC " + " LIMIT 5",new String[]{Integer.toString(selectedTour.getTourId())});
        if(cC.moveToFirst()){
            //Toast.makeText(this, cC.getString(0)+ " " + cC.getString(3), Toast.LENGTH_SHORT).show();
            do {
                comment = new Comment();
                comment.setCommentTourId(cC.getInt(1));
                comment.setCommentUserId(cC.getInt(2));
                comment.setCommentContent(cC.getString(3));

                commentShortList.add(comment);
            }while(cC.moveToNext());
        }


    }
}
