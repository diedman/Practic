package com.example.practic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentLenta extends Fragment {

    private RecyclerView rvStaffs;
    private AutoCompleteTextView menuCoworking;

    public static FragmentLenta newInstance() {
        return new FragmentLenta();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_lenta, container, false);

        initFields(thisView);
        initAdapters(thisView);


        return thisView;
    }

    private void initFields(View thisView) {
        rvStaffs = thisView.findViewById(R.id.rvLents);
        menuCoworking = thisView.findViewById(R.id.autoCompleteTextView_Coworking);
    }

    private void initAdapters(View thisView) {
        List<LentaItem> posts = getPosts();//new ArrayList<>();
        //posts.add( new LentaItem(R.drawable.noavatar, new EventData(1,"Title", "desc", new Date(1641996794), "no speaker")));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));
//        posts.add( new LentaItem(R.drawable.noavatar, "Tittle", "13.08.2022"));


        LentaAdapter lentaAdapter = new LentaAdapter(posts);
        rvStaffs.setAdapter(lentaAdapter);
        rvStaffs.setLayoutManager(new LinearLayoutManager(thisView.getContext()));

        ArrayList<CoworkingSpace> spaces = (ArrayList<CoworkingSpace>) DBCommunication.getCoworkingSpaces();
//                new ArrayList<>();
//        spaces.add(new CoworkingSpace(0,"Coworking1",1,1));
//        spaces.add(new CoworkingSpace(1,"Coworking2",1,1));

        //TODO: Если есть идеи, как лучше подавать строку в адаптер, то поправьте
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Все коворкинги");
        for (CoworkingSpace space : spaces){
            titles.add(space.getTitle());
        }

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, titles);
        menuCoworking.setAdapter(menuAdapter);
        menuCoworking.setSelection(0);

        menuCoworking.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i == 0) {
                lentaAdapter.setItems(posts);
                return;
            }
            int spaceId = spaces.get(i-1).getId();
            List<LentaItem> coworkingPosts = posts.stream()
                    .filter(v -> v.getEventData().getSpaceId() == spaceId)
                    .collect(Collectors.toList());

            lentaAdapter.setItems(coworkingPosts);
        });
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
