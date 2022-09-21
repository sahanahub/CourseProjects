package com.example.class09;

/*
Assignment #: InClass09
File Name: NewForumFragment.java
Full Name of Student1: Sahana Srinivas
Full Name of Student2: Krithika Kasaragod
 */

import static com.example.class09.MainActivity.creatorName;
import static com.example.class09.MainActivity.myPreference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.example.class09.databinding.FragmentNewForumBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewForumFragment extends Fragment {

    FragmentNewForumBinding newForumBinding;
    IService mListener;
    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;

    public NewForumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newForumBinding = FragmentNewForumBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_new_forum));
        return newForumBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newForumBinding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();

                setForumData();
            }
        });

        newForumBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoPreviousFragment();
            }
        });
    }

    private void setForumData() {
        String forumTitle = newForumBinding.editTextTitle.getText().toString();
        String forumDescription = newForumBinding.editTextDetail.getText().toString();

        if (forumTitle.isEmpty()) {
            displayAlert(getString(R.string.label_error_forum_title));
        } else if (forumDescription.isEmpty()) {
            displayAlert(getString(R.string.label_error_forum_description));
        } else {
            String creator = "";
            String UID = "";

            sharedpreferences = getActivity().getSharedPreferences(myPreference,
                    Context.MODE_PRIVATE);
            if (sharedpreferences.contains(MainActivity.UID)) {
                creator = sharedpreferences.getString(creatorName, null);
                UID = sharedpreferences.getString(MainActivity.UID, null);
            }

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy h:mm a");
            String createdDateTime = dateFormatter.format(Calendar.getInstance().getTime());

            FirebaseFirestore db = FirebaseFirestore.getInstance();


            DocumentReference newDoc = db.collection("Forum").document();

            String DID = newDoc.getId();

            newDoc.set(new Forum(DID, UID, forumTitle, forumDescription, creator, createdDateTime))
                  .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void unused) {
                                 mListener.refreshForumsFragment();
                          }
                  });
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IService) {
                mListener = (IService) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {

        }
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!!")
                .setMessage(message)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }
}