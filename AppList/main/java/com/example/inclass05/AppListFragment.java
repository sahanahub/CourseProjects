package com.example.inclass05;

/*
a. Assignment #:InCLass05
b. File Name:AppListFragment.java
c. Full name of the Student 1:Krithika Kasaragod
d. Full name of the Student 2:Sahana Srinivas
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
import android.widget.TextView;

import com.example.inclass05.databinding.FragmentAppListBinding;

import java.util.ArrayList;
import java.util.List;


public class AppListFragment extends Fragment {

    FragmentAppListBinding appListBinding;
    ArrayAdapter<DataServices.App> AppListAdapter;
    ArrayList<DataServices.App> AppList;
    IListviewInterface mListener;
    private static final String ARG_PARAM_CATEGORY = "ARG_PARAM_CATEGORY";
    private String mCategory;

    public AppListFragment() {
        // Required empty public constructor
    }

    public static AppListFragment newInstance(String mCategory) {
        AppListFragment fragment = new AppListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_CATEGORY, mCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(ARG_PARAM_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appListBinding = FragmentAppListBinding.inflate(inflater, container, false);
        getActivity().setTitle(mCategory);
        return appListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppList = DataServices.getAppsByCategory(mCategory);
        AppListAdapter = new AppListAdapter(getContext(),R.layout.custom_app_list_layout,AppList);
        appListBinding.listViewAppCategory.setAdapter(AppListAdapter);
        appListBinding.listViewAppCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListener.gotoAppDetailsFragment(AppList.get(position));
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IListviewInterface) {
                mListener = (IListviewInterface) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }

    public class AppListAdapter extends ArrayAdapter<DataServices.App>{
        IListviewInterface listenerObj;
        public AppListAdapter(@NonNull Context context, int resource, @NonNull List<DataServices.App> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.custom_app_list_layout,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.tvAppName = convertView.findViewById(R.id.textViewAppName);
                viewHolder.tvArtistName = convertView.findViewById(R.id.textViewArtistName);
                viewHolder.tvReleasedDate = convertView.findViewById(R.id.textViewReleasedDate);
                convertView.setTag(viewHolder);

            }

            DataServices.App app = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tvAppName .setText(app.name);
            viewHolder.tvArtistName .setText(app.artistName);
            viewHolder.tvReleasedDate .setText(app.releaseDate);

            return convertView;
        }
        public class ViewHolder{
            TextView tvAppName,tvArtistName,tvReleasedDate;

        }
    }


}