package com.example.hw05;

/*
Assignment #: Homework 05
File Name: CreatePostFragment.java
Full Name of Student: Sahana Srinivas
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hw05.databinding.FragmentCreatePostBinding;
import com.example.hw05.databinding.FragmentLoginBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CreatePostFragment extends Fragment {

    FragmentCreatePostBinding binding;
    OkHttpClient client = new OkHttpClient();
    IFragment iListener;
    public static final String FILE_NAME = "com.example.hw05_sharedPreferenceFile";
    String token, post_text;


    public CreatePostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        token = sharedPreferences.getString(getString(R.string.token),"");


        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.goToLogin();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_text = binding.editTextPost.getText().toString();
                if(post_text.equals("")){
                    Toast.makeText(getContext(),"Please enter a message to post!", Toast.LENGTH_SHORT).show();
                }
                else if(post_text.length() > 1){
                    createPost();
                }
            }
        });
    }

    public void createPost(){
        FormBody formBody= new FormBody.Builder()
                .add("post_text",post_text)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/posts/create")
                .addHeader("Authorization", "BEARER " + token)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: create");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseValue = responseBody.string();
                Log.d("TAG", "onResponse: create"+ responseValue);

                if (response.isSuccessful()) {
                   iListener.goToPostListfromCreate();
                }
                else{
                    Log.d("TAG", "onResponse: create Unsuccess");
                }
            }
        });
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