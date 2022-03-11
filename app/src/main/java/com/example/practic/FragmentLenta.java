package com.example.practic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentLenta extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lenta, container, false);
        List<LentaItem> posts = getPosts();
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
        RecyclerView rvStaffs = v.findViewById(R.id.rvLents);

        LentaAdapter adapter = new LentaAdapter(posts);
        rvStaffs.setAdapter(adapter);
        rvStaffs.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }

    private List<LentaItem> getPosts() {
        List<LentaItem> posts = new ArrayList<>();

        List<EventData> events = DBCommunication.getEvents();

        for (EventData event: events) {
            posts.add(new LentaItem(R.drawable.noavatar, event));
        }

        return posts;
    }

}
