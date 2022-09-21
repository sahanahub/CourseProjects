package com.example.homework04;
/*
a. Assignment #:Homework04
b. File Name:FilterFragment.java
c. Full name of the Student :Sahana Srinivas
*/

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.homework04.databinding.FragmentFilterBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterFragment extends Fragment {

    ArrayList<DataServices.User> userObjList;
    ArrayList<String> stateList = new ArrayList<>();
    List<String> newStateList;
    FragmentFilterBinding filterBinding;
    ArrayAdapter<String> stateListAdapter;
    FragmentInterface mListener;


    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userObjList = DataServices.getAllUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        filterBinding = FragmentFilterBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_tag_filter_fragment));
        return filterBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stateList.clear();
        if (userObjList != null) {
            for (int i = 0; i < userObjList.size(); i++) {
                stateList.add(userObjList.get(i).state);

            }
            Set<String> set = new HashSet<>(stateList);
            newStateList = new ArrayList<String>(set);
           Collections.sort(newStateList);
           newStateList.add(0,getString(R.string.label_all_states));
        }


        stateListAdapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1,newStateList);
        filterBinding.listViewFilter.setAdapter(stateListAdapter);
        filterBinding.listViewFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListener.goToUserFragmentWithFilter(newStateList.get(position));

            }
        });
        stateListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof FragmentInterface) {
                mListener = (FragmentInterface) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }

}