package com.example.homework04;
/*
a. Assignment #:Homework04
b. File Name:UserFragment.java
c. Full name of the Student :Sahana Srinivas
*/
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.homework04.databinding.FragmentUsersBinding;

import java.util.ArrayList;
import java.util.Collections;

public class UsersFragment extends Fragment {

    FragmentUsersBinding usersBinding;
    FragmentInterface mListener;

    private static final String ARG_PARAM_USER_LIST = "ARG_PARAM_USER_LIST";
    private static final String ARG_PARAM_FILTER_NAME = "ARG_PARAM_FILTER_NAME";
    private static final String ARG_PARAM_SORT_ATTRIBUTE = "ARG_PARAM_SORT_ATTRIBUTE";
    private static final String ARG_PARAM_SORT_CRITERIA = "ARG_PARAM_SORT_CRITERIA";
    UserRecyclerViewAdapter adapter;
   static private ArrayList<DataServices.User> newUserList;
    String filterName, sortAttribute;
    int sortCriteria;
    RecyclerView usersRecyclerView;

    public UsersFragment() {
        // Required empty public constructor
    }

    public static UsersFragment newInstance( ArrayList<DataServices.User> mUserList,String filterName,String sortAttribute, int sortCriteria) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        fragment.newUserList = mUserList;
        args.putSerializable(ARG_PARAM_USER_LIST, mUserList);
        args.putString(ARG_PARAM_FILTER_NAME,filterName);
        args.putString(ARG_PARAM_SORT_ATTRIBUTE, sortAttribute);
        args.putInt(ARG_PARAM_SORT_CRITERIA, sortCriteria);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            newUserList = (ArrayList<DataServices.User>) getArguments().getSerializable(ARG_PARAM_USER_LIST);
            this.filterName = getArguments().getString(ARG_PARAM_FILTER_NAME);
            this.sortAttribute = getArguments().getString(ARG_PARAM_SORT_ATTRIBUTE);
            this.sortCriteria = getArguments().getInt(ARG_PARAM_SORT_CRITERIA);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(newUserList == null) {
            newUserList = DataServices.getAllUsers();
            filterName = getString(R.string.label_all_states);
            sortAttribute = getString(R.string.label_tv_state);
            sortCriteria = 0;

        }
            newUserList = filterListMethod(filterName);
            newUserList = sortListMethod(newUserList,sortAttribute,sortCriteria);

        usersBinding = FragmentUsersBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_tag_users_fragment));
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        usersRecyclerView = view.findViewById(R.id.recyclerViewUsers);
        usersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new UserRecyclerViewAdapter(newUserList);
        usersRecyclerView.setLayoutManager(linearLayoutManager);
        usersRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonFilter = view.findViewById(R.id.buttonFilter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.goToFilterFragment();
            }
        });

        Button buttonSort = view.findViewById(R.id.buttonSort);
        buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToSortFragment();
            }
        });

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

    public ArrayList<DataServices.User> filterListMethod(String filterState){

        ArrayList<DataServices.User> filteredUserList = new ArrayList<DataServices.User>();
        ArrayList<DataServices.User> userList =  DataServices.getAllUsers();
        if(filterState.equalsIgnoreCase(getString(R.string.label_all_states))) {
            filteredUserList = DataServices.getAllUsers();

        }
        else if(!filterState.equalsIgnoreCase(getString(R.string.label_all_states))){
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).state.equalsIgnoreCase(filterState)) {

                    filteredUserList.add(userList.get(i));
                }
            }
        }
        return filteredUserList;
    }

    public ArrayList<DataServices.User> sortListMethod(ArrayList<DataServices.User> userList, String sortAttribute, int sortCriteria){

        ArrayList<DataServices.User> sortedUserList = new ArrayList<DataServices.User>();
        if (sortCriteria == 0) {
                if (sortAttribute.equalsIgnoreCase(getString(R.string.label_tv_age)))
                    Collections.sort(userList, SortClass.ageComparitor());
                else if (sortAttribute.equalsIgnoreCase(getString(R.string.label_tv_state)))
                    Collections.sort(userList, SortClass.stateComparitor());
                else if (sortAttribute.equalsIgnoreCase(getString(R.string.label_tv_name)))
                    Collections.sort(userList, SortClass.nameComparitor());
            } else if (sortCriteria == 1) {
                if (sortAttribute.equalsIgnoreCase(getString(R.string.label_tv_age)))
                    Collections.sort(userList, Collections.reverseOrder(SortClass.ageComparitor()));
                else if (sortAttribute.equalsIgnoreCase(getString(R.string.label_tv_state)))
                    Collections.sort(userList, Collections.reverseOrder(SortClass.stateComparitor()));
                else if (sortAttribute.equalsIgnoreCase(getString(R.string.label_tv_name)))
                    Collections.sort(userList, Collections.reverseOrder(SortClass.nameComparitor()));
            }
        sortedUserList = userList;
        return sortedUserList;
    }
}