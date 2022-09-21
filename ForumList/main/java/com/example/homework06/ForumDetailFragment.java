package com.example.homework06;

/*
Assignment #: Homework06
File Name: ForumDetailFragment.java
Full Name of Student1: Sahana Srinivas
 */

import static com.example.homework06.MainActivity.creatorName;
import static com.example.homework06.MainActivity.myPreference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homework06.databinding.FragmentForumDetailBinding;
import com.example.homework06.databinding.FragmentForumsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ForumDetailFragment extends Fragment {


    private static final String ARG_FORUM_DOC_ID = "ARG_FORUM_DOC_ID";
    FragmentForumDetailBinding binding;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedpreferences;
    CommentsRecyclerViewAdapter listAdapter;
    ArrayList<Comments> commentList = new ArrayList<>();
    int totalComments = 0;
    private String fDocumentId;

    public ForumDetailFragment() {
        // Required empty public constructor
    }

    public static ForumDetailFragment newInstance(String docId) {
        ForumDetailFragment fragment = new ForumDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FORUM_DOC_ID, docId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fDocumentId = getArguments().getString(ARG_FORUM_DOC_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForumDetailBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_forum_detail));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        binding.recyclerViewComment.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewComment.setLayoutManager(linearLayoutManager);
        listAdapter = new CommentsRecyclerViewAdapter(commentList);
        binding.recyclerViewComment.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.recyclerViewComment.setAdapter(listAdapter);
        getForumDetail(fDocumentId);
        getCommentList();

        binding.textViewTotalCmnt.setText(totalComments + " Comments");

        binding.buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
    }

    public void getCommentList() {
        db.collection("Forum").document(fDocumentId).collection("Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        commentList.clear();

                        for (QueryDocumentSnapshot document : value) {
                            //Log.d("TAG", "onEvent: getComment-" + document.getData().toString());
                            Comments comment = document.toObject(Comments.class);
                            if (comment.forumDocId.equals(fDocumentId)) {
                                commentList.add(comment);
                            }
                        }

                        listAdapter.notifyDataSetChanged();

                    }
                });
    }

    public void postComment() {
        String commentText = binding.editTextComment.getText().toString();

        if (commentText.isEmpty()) {
            displayAlert(getString(R.string.label_error_comment_post));
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

            DocumentReference newDoc = db.collection("Forum").document(fDocumentId).collection("Comments").document();
            String DID = newDoc.getId();
            newDoc.set(new Comments(fDocumentId, DID, creator, UID, commentText, createdDateTime))
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.editTextComment.setText("");
                            listAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    public void getForumDetail(String fDocumentId) {

        DocumentReference docRef = db.collection("Forum").document(fDocumentId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Forum forum = documentSnapshot.toObject(Forum.class);
                binding.textViewCForumName.setText(forum.getForumTitle());
                binding.textViewCForumUserName.setText(forum.getForumCreator());
                binding.textViewCForumDetail.setText(forum.getForumDescription());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: ForumDetail");
            }
        });
    }

    public void deleteComment(String document_id) {
        //db.collection("Comment").document(document_id)
        db.collection("Forum").document(fDocumentId).collection("Comments").document(document_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getCommentList();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "The selected document does not exist", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!!")
                .setMessage(message)
                .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.CommentsViewHolder> {
        ArrayList<Comments> arrayList;

        public CommentsRecyclerViewAdapter(ArrayList<Comments> arrayList) {
            super();
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comments, parent, false);
            CommentsViewHolder viewHolder = new CommentsViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
            holder.tvName.setText(this.arrayList.get(position).userName);
            holder.tvDetail.setText(this.arrayList.get(position).commentText);
            holder.tvDateTime.setText(this.arrayList.get(position).dateTime);
            String U_ID = "";
            sharedpreferences = getActivity().getSharedPreferences(myPreference,
                    Context.MODE_PRIVATE);
            if (sharedpreferences.contains(MainActivity.UID)) {
                U_ID = sharedpreferences.getString(MainActivity.UID, null);
            }
            String user_id = this.arrayList.get(position).userId;
            String document_id = this.arrayList.get(position).docId;
            if (user_id.equals(U_ID)) {
                holder.imageViewTrash.setImageResource(R.drawable.ic_trash);
            } else {
                holder.imageViewTrash.setVisibility(View.INVISIBLE);
            }

            holder.imageViewTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                            alertDialogBuilder.setMessage("Selected Comment will be deleted permenantly");
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            deleteComment(document_id);
                                            alertDialog.dismiss();
                                        }
                                    });
                            alertDialogBuilder.show();
                        }
                    });
                }
            });
            binding.textViewTotalCmnt.setText(totalComments + " Comments");
        }

        @Override
        public int getItemCount() {
            totalComments = this.arrayList.size();
            return totalComments;
        }

        public class CommentsViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvDetail, tvDateTime;
            ImageView imageViewTrash;

            public CommentsViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.textViewCmntCreatorName);
                tvDetail = itemView.findViewById(R.id.textViewCmntDetail);
                tvDateTime = itemView.findViewById(R.id.textViewCmntDateTime);
                imageViewTrash = itemView.findViewById(R.id.imageViewCmntTrash);
            }
        }
    }
}