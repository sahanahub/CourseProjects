package com.example.homework04;
/*
a. Assignment #:Homework04
b. File Name:SortFragment.java
c. Full name of the Student :Sahana Srinivas
*/

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homework04.databinding.FragmentSortBinding;
import com.example.homework04.databinding.FragmentUsersBinding;

import java.util.ArrayList;

public class SortFragment extends Fragment {
    FragmentSortBinding sortBinding;
    SortRecyclerViewAdapter.ISortRecyclerView ilistener;
    ArrayList<String> sortTitleList;
    SortRecyclerViewAdapter adapter;
    RecyclerView sortRecyclerView;

    public SortFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sortBinding = FragmentSortBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_tag_sort_fragment));
        View view = inflater.inflate(R.layout.fragment_sort, container, false);
        sortRecyclerView = view.findViewById(R.id.recyclerViewSort);
        sortRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        sortTitleList = new ArrayList<>();
        sortTitleList.add(getString(R.string.label_tv_age));
        sortTitleList.add(getString(R.string.label_tv_name));
        sortTitleList.add(getString(R.string.label_tv_state));

        adapter = new SortRecyclerViewAdapter(sortTitleList,ilistener);
        adapter.notifyDataSetChanged();
        sortRecyclerView.setLayoutManager(linearLayoutManager);
        sortRecyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof SortRecyclerViewAdapter.ISortRecyclerView) {
                ilistener = (SortRecyclerViewAdapter.ISortRecyclerView) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }

}