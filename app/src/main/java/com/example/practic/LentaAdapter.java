package com.example.practic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class LentaAdapter extends
        RecyclerView.Adapter<LentaAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView titleTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageview_lentimage);
            titleTextView = (TextView) itemView.findViewById(R.id.text_title);
            dateTextView = (TextView) itemView.findViewById(R.id.text_date);

        }
    }

    private List<Lenta> mLenta;
    public LentaAdapter(List<Lenta> posts){mLenta = posts;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View lentaView = inflater.inflate(R.layout.recyclerview_lenta_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(lentaView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lenta lenta = mLenta.get(position);

        ImageView image = holder.image;
        TextView tittle = holder.titleTextView;
        TextView date = holder.dateTextView;

        image.setImageResource(lenta.getId_picture());
        tittle.setText(lenta.getTittle());
        date.setText(lenta.getDate());

        holder.image.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            FragmentRegistrationEvent myFragment = new FragmentRegistrationEvent();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, myFragment).addToBackStack(null).commit();

        });
    }

    @Override
    public int getItemCount() {
        return mLenta.size();
    }
}
