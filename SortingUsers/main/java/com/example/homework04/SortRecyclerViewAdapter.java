package com.example.homework04;
/*
a. Assignment #:Homework04
b. File Name:SortRecyclerViewAdapter.java
c. Full name of the Student :Sahana Srinivas
*/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SortRecyclerViewAdapter extends RecyclerView.Adapter<SortRecyclerViewAdapter.SortViewHolder>{

    ArrayList<String> sortTitleList;
    ISortRecyclerView mlistener;

    public SortRecyclerViewAdapter(ArrayList<String> sortTitleList, ISortRecyclerView mlistener) {
        super();
        this.sortTitleList = sortTitleList;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public SortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_recycler_view_layout,parent,false);
        SortRecyclerViewAdapter.SortViewHolder sortViewHolder = new SortRecyclerViewAdapter.SortViewHolder(view,mlistener);
        return sortViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SortViewHolder holder, int position) {

        holder.textViewSortTitle.setText(this.sortTitleList.get(position));
        holder.buttonAscend.setBackgroundResource(R.drawable.ic_sort_ascending);
        holder.buttonDescend.setBackgroundResource(R.drawable.ic_sort_descending);

    }

    @Override
    public int getItemCount() {
        return this.sortTitleList.size();
    }

    public static class SortViewHolder extends RecyclerView.ViewHolder{
        ImageButton buttonAscend, buttonDescend;
        TextView textViewSortTitle;

        ISortRecyclerView mlistener;

        public SortViewHolder(@NonNull View itemView, ISortRecyclerView mlistener) {
            super(itemView);

            this.mlistener = mlistener;
            textViewSortTitle = itemView.findViewById(R.id.textViewSortTitle);
            buttonAscend = itemView.findViewById(R.id.buttonAscending);
            buttonDescend = itemView.findViewById(R.id.buttonDescending);

            buttonAscend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int criteria = 0;
                    String attribute = textViewSortTitle.getText().toString();
                    mlistener.goToUserFragmentWithSort(attribute,criteria);
                }
            });

            buttonDescend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int criteria = 1;
                    String attribute = textViewSortTitle.getText().toString();
                    mlistener.goToUserFragmentWithSort(attribute,criteria);
                }
            });
        }
    }
    interface ISortRecyclerView{
        void goToUserFragmentWithSort(String attribute, int criteria);
    }
}
