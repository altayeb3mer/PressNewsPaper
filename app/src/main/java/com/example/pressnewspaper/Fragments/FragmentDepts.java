package com.example.pressnewspaper.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pressnewspaper.R;

public class FragmentDepts extends Fragment {



    public FragmentDepts() {
        // Required empty public constructor
    }

    

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_depts, container, false);
        init();
        return view;
    }

    private void init() {
    }
}