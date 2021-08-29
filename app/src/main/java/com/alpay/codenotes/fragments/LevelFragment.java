package com.alpay.codenotes.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.LevelBlockAdapter;
import com.alpay.codenotes.models.Level;
import com.alpay.codenotes.models.LevelBlock;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LevelFragment extends Fragment {

    private View view;
    private Unbinder unbinder;

    LevelBlockAdapter adapter;

    @BindView(R.id.level_blocks)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_level, container, false);
        unbinder = ButterKnife.bind(this, view);
        setSpinner();
        Level.populateTurtleLevels();
        setupRecyclerView();
        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }


    public void setupRecyclerView(){
        adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setSpinner(){
        Spinner spinner = (Spinner) view.findViewById(R.id.levels_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.levels_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                openLevel(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void openLevel(int level){
        switch (level){
            case 1:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels1);
                break;

            case 2:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels2);
                break;

            case 3:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels3);
                break;

            case 4:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels4);
                break;

            case 5:
                adapter = new LevelBlockAdapter((AppCompatActivity) getActivity(), Level.turtleLevels5);
            default:
                break;
        }
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}