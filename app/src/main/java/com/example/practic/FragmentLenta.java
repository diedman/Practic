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
        rvStaffs.setLayoutManager(new LinearLayoutManager(thisView.getContext()));


        //TODO: Подавать значения из базы сюда
        ArrayList<CoworkerSpace> testArray = new ArrayList<>();
        testArray.add(new CoworkerSpace(0,"Coworking1",1,1));
        testArray.add(new CoworkerSpace(1,"Coworking2",1,1));

        //TODO: Если есть идеи, как лучше подавать строку в адаптер, то поправьте
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Все коворкинги");
        for (CoworkerSpace space : testArray){
            titles.add(space.getTitle());
        }

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, titles);
        menuCoworking.setAdapter(menuAdapter);

        return thisView;
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
