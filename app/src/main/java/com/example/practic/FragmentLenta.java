package com.example.practic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentLenta extends Fragment {

    public FragmentLenta() {
        // Required empty public constructor
    }

    public static FragmentLenta newInstance() {
        return new FragmentLenta();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lenta, null);
        ArrayList<Lenta> posts = new ArrayList<>();
        posts.add( new Lenta(R.drawable.noavatar, "Tittle", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Tittle", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Tittle", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Tittle", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Tittle", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Tittle", "13.08.2022"));
        RecyclerView rvStaffs = (RecyclerView) v.findViewById(R.id.rvLents);

        LentaAdapter adapter = new LentaAdapter(posts);
        rvStaffs.setAdapter(adapter);
        rvStaffs.setLayoutManager(new LinearLayoutManager(v.getContext()));
        // Inflate the layout for this fragment
        return v;
    }

}
