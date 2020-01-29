package aplicacion.android.kvn.touruta.ADAPTERS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aplicacion.android.kvn.touruta.MyDBHandler;
import aplicacion.android.kvn.touruta.OBJECTS.Comment;
import aplicacion.android.kvn.touruta.OBJECTS.Tour;
import aplicacion.android.kvn.touruta.R;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder> {
    MyDBHandler dbHandler;
    SQLiteDatabase db;

    ArrayList<Comment> commentList = new ArrayList<>();
    Context parent;
    int resource;
    public CommentRecyclerAdapter(Context parent, int resource, ArrayList<Comment> commentList) {
        this.commentList = commentList;
        this.parent = parent;
        this.resource = resource;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlist_cardlayout,null);

        TextView commentUser,commentContent;

        commentUser=view.findViewById(R.id.txtCommentUser);
        commentContent=view.findViewById(R.id.txtCommentContent);

        return new CommentHolder(view,commentUser,commentContent);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        dbHandler = new MyDBHandler(parent, MyDBHandler.DATABASE_NAME, null, 1);
        db=dbHandler.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT " + MyDBHandler.COLUMN_USER_NICK_NAME + " FROM " + MyDBHandler.TABLE_USERS + " WHERE " +  MyDBHandler.COLUMN_USER_ID + " =? " +Integer.toString(commentList.get(position).getCommentUserId()),null );

        String userNickname = c.getString(0);
        holder.setCommentUser(userNickname);
        holder.setCommentContent(commentList.get(position).getCommentContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        TextView commentUser,commentContent;
        public CommentHolder(@NonNull View itemView,TextView commentUser,TextView commentContent) {
            super(itemView);

            this.commentContent=commentContent;
            this.commentUser=commentUser;
        }

        public void setCommentUser(String commentUser) {
            this.commentUser.setText(commentUser);
        }

        public void setCommentContent(String commentContent) {
            this.commentContent.setText(commentContent);
        }
    }
}
