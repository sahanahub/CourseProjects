package com.example.hw05;

/*
Assignment #: Homework 05
File Name: RegisterFragment.java
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
import com.example.hw05.databinding.FragmentRegisterBinding;

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


public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    OkHttpClient client = new OkHttpClient();
    String user_id, user_fullname, token;
    IFragment iListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.label_register_fragment_title));

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.goToLogin();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.editTextEmailAddress.getText().toString().equals("")) {
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
                } else if (binding.editTextName.getText().toString().equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Please enter Name");
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.dismiss();
                                }
                            });
                    alertDialogBuilder.show();
                } else if (binding.editTextPassword.getText().toString().equals("")) {
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
                } else {
                    createAccount(binding.editTextName.getText().toString(), binding.editTextEmailAddress.getText().toString(), binding.editTextPassword.getText().toString());
                }
            }
        });
    }

    public void createAccount(String name, String email, String pswd) {
        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", pswd)
                .add("name", name)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/posts/signup")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: Register");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseValue = responseBody.string();
                Log.d("TAG", "onResponse: Register" + responseValue);

                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(responseValue);
                        user_id =jsonObject.getString(getString(R.string.user_id));
                        user_fullname= jsonObject.getString(getString(R.string.username));
                        token = jsonObject.getString(getString(R.string.token));
                        iListener.goToPostsList(user_id, user_fullname, token);

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