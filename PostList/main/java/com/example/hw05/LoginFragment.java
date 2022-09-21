package com.example.hw05;


/*
Assignment #: Homework 05
File Name: LoginFragment.java
Full Name of Student: Sahana Srinivas
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw05.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    OkHttpClient client = new OkHttpClient();
    IFragment iListener;
    String user_id,user_fullname, token;



    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.label_login_fragment_title));

        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.goToCreateAccount();
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.editTextEmail.getText().toString().equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Please enter Email");
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
                else if(binding.editTextPswd.getText().toString().equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Please enter Password");
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
                else{
                    userLogin(binding.editTextEmail.getText().toString(), binding.editTextPswd.getText().toString());
                }
            }
        });

    }

    public void userLogin(String email, String pswd){
        FormBody formBody= new FormBody.Builder()
                .add("email",email)
                .add("password", pswd)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/posts/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: Login");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseValue = responseBody.string();
                Log.d("TAG", "onResponse: Login"+ responseValue);

                try{
                    if(response.isSuccessful()){
                        JSONObject jsonObject = new JSONObject(responseValue);
                        user_id =jsonObject.getString(getString(R.string.user_id));
                        user_fullname= jsonObject.getString(getString(R.string.username));
                        token = jsonObject.getString(getString(R.string.token));
                        iListener.goToPostsList(user_id,user_fullname,token);

                    }else{
                        JSONObject jsonObject = new JSONObject(responseValue);
                        String error = jsonObject.getString("message");
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
                }catch (JSONException e){
                    e.printStackTrace();
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