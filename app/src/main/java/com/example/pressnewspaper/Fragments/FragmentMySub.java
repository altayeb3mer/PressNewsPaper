package com.example.pressnewspaper.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pressnewspaper.Adapter.AdapterMySubscriptions;
import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelMySub;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;

import java.util.ArrayList;


public class FragmentMySub extends Fragment {


    public FragmentMySub() {
        // Required empty public constructor
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_my_sub, container, false);
        testAdapter();
        return view;
    }
    RecyclerView recyclerView;
    AdapterMySubscriptions adapterMySubscriptions;
    ArrayList<ModelMySub> mySubArrayList;
    private void testAdapter(){
        recyclerView = view.findViewById(R.id.spc_recycler);
        mySubArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ModelMySub modelMySub = new ModelMySub();
            if (i % 2 == 0){
                modelMySub.setNewsPaperName("صحيفة الصيحة");
                modelMySub.setSubscriptionsType("شهري");
                modelMySub.setSubscriptionsStartDate("2-4-2020");
                modelMySub.setSubscriptionsEndDate("2-5-2020");
            }else{
                modelMySub.setNewsPaperName("صحيفة الصيحة");
                modelMySub.setSubscriptionsType("شهري");
                modelMySub.setSubscriptionsStartDate("2-4-2020");
                modelMySub.setSubscriptionsEndDate("2-5-2020");
            }
            mySubArrayList.add(modelMySub);
            adapterMySubscriptions = new AdapterMySubscriptions(getActivity(),mySubArrayList);
            recyclerView.setAdapter(adapterMySubscriptions);
        }

        //end of test fun
    }


}
