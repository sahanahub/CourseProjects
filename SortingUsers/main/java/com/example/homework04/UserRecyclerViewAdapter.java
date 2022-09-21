package com.example.homework04;
/*
a. Assignment #:Homework04
b. File Name:UserRecyclerViewAdapter.java
c. Full name of the Student :Sahana Srinivas
*/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder> {

    ArrayList<DataServices.User> usersList;
    public static final String female = "Female";
    public static final String male = "Male";

    public UserRecyclerViewAdapter(ArrayList<DataServices.User> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_recycler_view_layout,parent,false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        DataServices.User users = usersList.get(position);
        if(users.gender == female){
            holder.imageViewUserIcon.setImageResource(R.drawable.avatar_female);
        }else if(users.gender == male){
            holder.imageViewUserIcon.setImageResource(R.drawable.avatar_male);
        }
        holder.textViewUserName.setText(users.name);
        holder.textViewUserState.setText(users.state);
        holder.textViewUserAge.setText((String.valueOf(users.age).toString()));
        holder.textViewUserGroup.setText(users.group);

    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewUserIcon;
        TextView textViewUserName,textViewUserState, textViewUserAge, textViewUserGroup;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUserIcon = (ImageView)itemView.findViewById(R.id.imageViewUserIcon);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserState = itemView.findViewById(R.id.textViewUserState);
            textViewUserAge = itemView.findViewById(R.id.textViewUserAge);
            textViewUserGroup = itemView.findViewById(R.id.textViewUserGroup);

        }
    }
}
