package aplicacion.android.kvn.touruta.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import aplicacion.android.kvn.touruta.ADAPTERS.CommentRecyclerAdapter;
import aplicacion.android.kvn.touruta.MyDBHandler;
import aplicacion.android.kvn.touruta.OBJECTS.Comment;
import aplicacion.android.kvn.touruta.R;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class AllCommentsACT extends AppCompatActivity {
    RecyclerView commentRecyclerView;
    MyDBHandler dbHandler;
    SQLiteDatabase db;
    ArrayList<Comment> commentLongList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments_act);

        commentRecyclerView = findViewById(R.id.commentsLongRecyclerView);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHandler = new MyDBHandler(this, MyDBHandler.DATABASE_NAME, null, 1);
        CommentLongListQuery();
        CommentRecyclerAdapter commentAdapter = new CommentRecyclerAdapter(this,R.layout.commentlist_cardlayout,commentLongList);
        commentRecyclerView.setAdapter(commentAdapter);

    }

    private void CommentLongListQuery() {
        commentLongList = new ArrayList<>();
        db = dbHandler.getReadableDatabase();
        Comment comment;

        Cursor cC = db.rawQuery("SELECT *  FROM " + MyDBHandler.TABLE_COMMENTS + " WHERE " + MyDBHandler.COLUMN_COMMENT_TOUR_ID + " = ?"  + " ORDER BY " + MyDBHandler.COLUMN_COMMENT_ID + " DESC ", new String[]{Integer.toString(TourDetailsACT.selectedTour.getTourId())});
        if (cC.moveToFirst()) {
            Toast.makeText(this, cC.getString(0) + " " + cC.getString(3), Toast.LENGTH_SHORT).show();
            do {
                comment = new Comment();
                comment.setCommentTourId(cC.getInt(1));
                comment.setCommentUserId(cC.getInt(2));
                comment.setCommentContent(cC.getString(3));

                commentLongList.add(comment);
            } while (cC.moveToNext());
        }


    }
}