package com.example.inclass05;

/*
a. Assignment #:InCLass05
b. File Name:AppDetailsFragment.java
c. Full name of the Student 1:Krithika Kasaragod
d. Full name of the Student 2:Sahana Srinivas
*/

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.inclass05.databinding.FragmentAppDetailsBinding;

import java.util.ArrayList;

public class AppDetailsFragment extends Fragment {

    FragmentAppDetailsBinding appDetailsBinding;
    private static final String ARG_PARAM_DETAIL = "ARG_PARAM_DETAIL";
    private DataServices.App appObj;
    ArrayAdapter<String> listViewAdapter;
    ArrayList<String> genrelist;


    public AppDetailsFragment() {
        // Required empty public constructor
    }

    public static AppDetailsFragment newInstance(DataServices.App object) {
        AppDetailsFragment fragment = new AppDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_DETAIL, object);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appObj = (DataServices.App) getArguments().getSerializable(ARG_PARAM_DETAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appDetailsBinding = FragmentAppDetailsBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_app_details_title));
        return appDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genrelist = appObj.genres;
        listViewAdapter= new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1,genrelist);
        appDetailsBinding.listCategory.setAdapter(listViewAdapter);
        appDetailsBinding.tvAppName.setText(appObj.name);
        appDetailsBinding.tvArtistName.setText(appObj.artistName);
        appDetailsBinding.tvReleasedDate.setText(appObj.releaseDate);
    }
}