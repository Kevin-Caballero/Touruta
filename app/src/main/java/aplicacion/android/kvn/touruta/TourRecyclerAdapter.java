package aplicacion.android.kvn.touruta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TourRecyclerAdapter extends RecyclerView.Adapter<TourRecyclerAdapter.TourHolder> implements View.OnClickListener {

    ArrayList<Tour> tourList = new ArrayList<>();
    Context parent;
    int resource;
    private View.OnClickListener listener;


    public TourRecyclerAdapter(Context parent, int resource, ArrayList<Tour> tourList) {
        this.tourList = tourList;
        this.parent = parent;
        this.resource = resource;
    }

    @NonNull
    @Override
    public TourHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tourlist_cardlayout, null, false);

        view.setOnClickListener(this);

        ImageView picture;
        TextView name, ubication, duration, description;
        Button btnViewMore;

        picture = view.findViewById(R.id.picture);
        name = view.findViewById(R.id.name);
        ubication = view.findViewById(R.id.ubication);
        duration = view.findViewById(R.id.duration);
        description = view.findViewById(R.id.description);
        btnViewMore = view.findViewById(R.id.btnViewMore);

        return new TourHolder(view, name, ubication, duration, description, picture);
    }

    @Override
    public void onBindViewHolder(@NonNull TourHolder holder, int position) {
        holder.setName(tourList.get(position).getTourName());
        holder.setDuration(tourList.get(position).getTourDescription());
        holder.setUbication(tourList.get(position).getTourCountry());
        holder.setDescription(tourList.get(position).getTourDuration());
        holder.setPicture(tourList.get(position).getPictureId());
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class TourHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView name, ubication, duration, description;

        public TourHolder(@NonNull View itemView, TextView name, TextView ubication, TextView duration, TextView description, ImageView picture) {
            super(itemView);

            this.name = name;
            this.ubication = ubication;
            this.duration = duration;
            this.description = description;
            this.picture = picture;
        }

        public void setPicture(int picture) {
            this.picture.setBackground(parent.getDrawable(picture));
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setUbication(String ubication) {
            this.ubication.setText(ubication);
        }

        public void setDuration(String duration) {
            this.duration.setText(duration);
        }

        public void setDescription(String description) {
            this.description.setText(description);
        }
    }
}
