package com.example.practic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class FragmentLenta extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_lenta, container, false);
        ArrayList<Lenta> posts = new ArrayList<>();
        posts.add( new Lenta(R.drawable.noavatar, "Post1", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Post2", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Post3", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Post4", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Post5", "13.08.2022"));
        posts.add( new Lenta(R.drawable.noavatar, "Post6", "13.08.2022"));

        RecyclerView rvStaffs = thisView.findViewById(R.id.rvLents);
        AutoCompleteTextView menuCoworking = thisView.findViewById(R.id.autoCompleteTextView_Coworking);

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

}
