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
        View view = LayoutInflater.from(this.parent).inflate(R.layout.commentlist_cardlayout,null);

        dbHandler = new MyDBHandler(parent.getContext(), MyDBHandler.DATABASE_NAME, null, 1);
        db=dbHandler.getReadableDatabase();

        TextView commentUser,commentContent;

        commentUser=view.findViewById(R.id.txtCommentUser);
        commentContent=view.findViewById(R.id.txtCommentContent);

        return new CommentHolder(view,commentUser,commentContent);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {


        Cursor c= db.query(MyDBHandler.TABLE_USERS,new String[]{MyDBHandler.COLUMN_USER_NICK_NAME},MyDBHandler.COLUMN_USER_ID + " = ? ",new String[] {Integer.toString(commentList.get(position).getCommentUserId())},null,null,null);
        //Cursor c = db.rawQuery("SELECT " + MyDBHandler.COLUMN_USER_NICK_NAME + " FROM " + MyDBHandler.TABLE_USERS + " WHERE " +  MyDBHandler.COLUMN_USER_ID + " =? " + commentList.get(position).getCommentUserId(),null );
        if(c.moveToFirst()){
            String userNickname = c.getString(0);
            holder.setCommentUser(userNickname);
            holder.setCommentContent(commentList.get(position).getCommentContent());
        }

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
