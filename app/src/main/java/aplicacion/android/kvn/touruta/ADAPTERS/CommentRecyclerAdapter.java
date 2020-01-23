package aplicacion.android.kvn.touruta.ADAPTERS;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder> {
    public CommentRecyclerAdapter() {
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
