package com.example.hw05;

/*
Assignment #: Homework 05
File Name: PostListFragment.java
Full Name of Student: Sahana Srinivas
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hw05.databinding.FragmentPostListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class PostListFragment extends Fragment {

    FragmentPostListBinding binding;
    public static final String FILE_NAME = "com.example.hw05_sharedPreferenceFile";
    OkHttpClient client = new OkHttpClient();
    IFragment iListener;
    String username, token, userid;
    int totalCount = 0;
    ArrayList<String> pageNumber = new ArrayList<>();
    LinearLayoutManager list_linearLayoutManager, page_linearLayoutManager;
    PostsListRecyclerViewAdapter listAdapter;
    PageListRecyclerViewAdapter pageAdapter;
    int page_param = 1;
    ArrayList<Posts> postsArrayList = new ArrayList<>();


    public PostListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentPostListBinding.inflate(inflater, container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.label_posts_fragment_title));

        binding.buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.logOutUser();
            }
        });

        binding.buttonCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.goToCreatePost();
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        username = sharedPreferences.getString(getString(R.string.username),"");
        userid = sharedPreferences.getString(getString(R.string.user_id),"");
        token = sharedPreferences.getString(getString(R.string.token),"");

        if(!username.equals("")) {
            binding.textViewWelcome.setText(getString(R.string.welcome)+" "+username);
        }

        binding.recyclerViewPostList.setHasFixedSize(true);
        binding.recyclerViewPage.setHasFixedSize(true);
        list_linearLayoutManager = new LinearLayoutManager(getContext());
        page_linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerViewPostList.setLayoutManager(list_linearLayoutManager);
        binding.recyclerViewPage.setLayoutManager(page_linearLayoutManager);

        getPostList();




    }

    public void getPostList(){

    HttpUrl url =HttpUrl.parse("https://www.theappsdr.com/posts?").newBuilder()
            .addQueryParameter("page", String.valueOf(page_param))
            .build();

    Request request = new Request.Builder()
            .url(url)
            .addHeader("Authorization", "BEARER " + token)
            .build();
    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("TAG", "onFailure: PostsList****");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            ResponseBody responseBody = response.body();
            String responseValue = responseBody.string();
            Log.d("TAG", "onResponse: PostsListSuccessful" + responseValue);

            try {
            if (response.isSuccessful()) {
                postsArrayList.clear();
                    JSONObject jsonObject = new JSONObject(responseValue);
                    JSONArray jsonArrayList = jsonObject.getJSONArray("posts");
                    for (int i = 0; i < jsonArrayList.length(); i++) {
                        JSONObject json = jsonArrayList.getJSONObject(i);
                        Posts posts = new Posts();
                        posts.setUserName(json.getString("created_by_name"));
                        posts.setpID(json.getString("post_id"));
                        posts.setuID(json.getString("created_by_uid"));
                        posts.setpText(json.getString("post_text"));
                        posts.setpDateTime(json.getString("created_at"));

                        postsArrayList.add(posts);

                    }
                totalCount = 0;
                totalCount = Integer.parseInt(jsonObject.getString("totalCount"));
                for (int i = 1; i <= totalCount; i++) {
                    pageNumber.add(String.valueOf(i));
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter = new PostsListRecyclerViewAdapter(postsArrayList);
                        binding.recyclerViewPostList.getRecycledViewPool().setMaxRecycledViews(0,0);
                        binding.recyclerViewPostList.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();

                        pageAdapter = new PageListRecyclerViewAdapter(pageNumber);
                        binding.recyclerViewPage.setAdapter(pageAdapter);
                        pageAdapter.notifyDataSetChanged();

                        binding.textViewPage.setText("Showing "+page_param+" out of "+totalCount);

                    }
                });


            } else {
                JSONObject jsonObject = new JSONObject(responseValue);
                String error = jsonObject.getString(getString(R.string.message));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setMessage(error);
                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.dismiss();
                                    }
                                });
                        alertDialogBuilder.show();
                    }
                });
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });
 }

    public void deletePost(String postID){
        FormBody formBody= new FormBody.Builder()
                .add("post_id",postID)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/posts/delete")
                .addHeader("Authorization", "BEARER " + token)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: delete");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseValue = responseBody.string();
                Log.d("TAG", "onResponse: delete"+ responseValue);

                if (response.isSuccessful()) {

                    getPostList();
                }
                else{
                    Log.d("TAG", "onResponse: delete Unsuccess");
                }
            }
        });
    }

    public class PostsListRecyclerViewAdapter extends RecyclerView.Adapter<PostsListRecyclerViewAdapter.PostsListViewHolder> {
        ArrayList<Posts> arrayList;

        public PostsListRecyclerViewAdapter(ArrayList<Posts> arrayList) {
            super();
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public PostsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_postlist, parent, false);
            PostsListViewHolder viewHolder = new PostsListViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PostsListRecyclerViewAdapter.PostsListViewHolder holder, int position) {
            holder.tvMessage.setText(this.arrayList.get(position).pText);
            holder.tvName.setText(this.arrayList.get(position).userName);
            String[] str = this.arrayList.get(position).pDateTime.split("\\s+");
            holder.tvDate.setText(str[0]);
            holder.tvTime.setText(str[1]);

            String post_id = this.arrayList.get(position).pID;

            if(this.arrayList.get(position).uID.equals(userid)) {
                holder.imageViewTrash.setImageResource(R.drawable.ic_trash);
            }
            else{
                holder.imageViewTrash.setVisibility(View.INVISIBLE);
            }

            holder.imageViewTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                            alertDialogBuilder.setMessage("Selected post will be deleted permanently");
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            deletePost(post_id);
                                            alertDialog.dismiss();
                                        }
                                    });
                            alertDialogBuilder.show();
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

            public class PostsListViewHolder extends RecyclerView.ViewHolder{
            TextView tvMessage, tvName, tvDate,tvTime;
            ImageView imageViewTrash;

            public PostsListViewHolder(@NonNull View itemView) {
                super(itemView);
                tvMessage = itemView.findViewById(R.id.textViewMsg);
                tvName = itemView.findViewById(R.id.textViewName);
                tvDate = itemView.findViewById(R.id.textViewDate);
                tvTime = itemView.findViewById(R.id.textViewTime);
                imageViewTrash = itemView.findViewById(R.id.imageViewTrash);
            }
        }
    }

    public class PageListRecyclerViewAdapter extends RecyclerView.Adapter<PageListRecyclerViewAdapter.PageListViewHolder>{
    ArrayList<String> pageNumberList;

        public PageListRecyclerViewAdapter(ArrayList<String> arrayList) {
            this.pageNumberList = arrayList;
        }

        @NonNull
        @Override
        public PageListRecyclerViewAdapter.PageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_pagenumber, parent, false);
            PageListRecyclerViewAdapter.PageListViewHolder viewHolder = new PageListViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PageListRecyclerViewAdapter.PageListViewHolder holder, int position) {
            holder.tvPageNumber.setText(this.pageNumberList.get(position));
            holder.tvPageNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    page_param = 0;
                    page_param = (holder.getAdapterPosition()+1);
                    getPostList();
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.pageNumberList.size();
        }

        public class PageListViewHolder extends RecyclerView.ViewHolder {
            TextView tvPageNumber;

            public PageListViewHolder(@NonNull View itemView) {
                super(itemView);
                tvPageNumber = itemView.findViewById(R.id.textViewPageNumber);
            }
        }

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IFragment) {
                iListener = (IFragment) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }

}