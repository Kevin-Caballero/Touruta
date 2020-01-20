package aplicacion.android.kvn.touruta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TourRecyclerAdapter extends RecyclerView.Adapter<TourRecyclerAdapter.TourHolder> {

    ArrayList<Tour> tourList = new ArrayList<>();

    public TourRecyclerAdapter(ArrayList<Tour> tourList) {
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public TourHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tourlist_cardlayout,null,false);
        return new TourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourHolder holder, int position) {
        holder.name.setText(tourList.get(position).getTourName());
        holder.description.setText(tourList.get(position).getTourDescription());
        holder.ubication.setText(tourList.get(position).getTourCountry());
        holder.duration.setText(tourList.get(position).getTourDuration());
        //holder.picture.setImageResource(tourList.get(position).getTourPicture());
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class TourHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView name,ubication,duration,description;
        Button btnViewMore;

        public TourHolder(@NonNull View itemView) {
            super(itemView);
            picture=itemView.findViewById(R.id.picture);
            name= itemView.findViewById(R.id.name);
            ubication=itemView.findViewById(R.id.ubication);
            duration=itemView.findViewById(R.id.duration);
            description=itemView.findViewById(R.id.description);
            btnViewMore=itemView.findViewById(R.id.btnViewMore);
        }
    }
}
