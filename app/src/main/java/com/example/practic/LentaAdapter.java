package com.example.practic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class LentaAdapter extends
        RecyclerView.Adapter<LentaAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView tvTitle;
        public TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageview_lentimage);
            tvTitle = itemView.findViewById(R.id.text_title);
            tvDate = itemView.findViewById(R.id.text_date);
        }
    }

    private final List<LentaItem> mLentaItem;
    public LentaAdapter(List<LentaItem> posts) {
        mLentaItem = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View lentaView = inflater.inflate(R.layout.recyclerview_lenta_item, parent, false);
        return new ViewHolder(lentaView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LentaItem lentaItem = mLentaItem.get(position);

        ImageView image = holder.image;
        TextView tittle = holder.tvTitle;
        TextView date = holder.tvDate;

        image.setImageResource(lentaItem.getIdPicture());
        tittle.setText(lentaItem.getTittle());
        date.setText(lentaItem.getDate());

        holder.image.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment fragment = new FragmentRegistrationEvent(lentaItem.getEventData());
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit();

        });
    }

    @Override
    public int getItemCount() {
        return mLentaItem.size();
    }
}
