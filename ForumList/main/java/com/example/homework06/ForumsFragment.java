package com.example.homework06;

/*
Assignment #: Homework06
File Name: ForumsFragment.java
Full Name of Student1: Sahana Srinivas
*/

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.homework06.databinding.FragmentForumsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;


public class ForumsFragment extends Fragment {


    FragmentForumsBinding forumsBinding;
    IService mListener;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Forum> forumArrayList = new ArrayList<>();
    ForumListRecyclerViewAdapter listAdapter;
    String currentUserId;
    private FirebaseAuth mAuth;


    public ForumsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        forumsBinding = FragmentForumsBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_forum));
        return forumsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        forumsBinding.recyclerViewForumList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        forumsBinding.recyclerViewForumList.setLayoutManager(linearLayoutManager);
        listAdapter = new ForumListRecyclerViewAdapter(forumArrayList);
        forumsBinding.recyclerViewForumList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        forumsBinding.recyclerViewForumList.setAdapter(listAdapter);


        getForumListData();

        forumsBinding.buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                FirebaseAuth.getInstance().signOut();

                mListener.gotoLoginFromForumsFragment();
            }
        });

        forumsBinding.buttonNewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoNewForumFragment();
            }
        });

    }


    public void getForumListData() {
        db.collection("Forum")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        forumArrayList.clear();

                        for (QueryDocumentSnapshot document : value) {
                            Forum forum = document.toObject(Forum.class);
                            forumArrayList.add(forum);
                            forumArrayList.sort(new Comparator<Forum>() {
                                @Override
                                public int compare(Forum t1, Forum t2) {
                                    if(!forumArrayList.isEmpty()){
                                        return (t2.forumDateTime.compareTo(t1.forumDateTime));
                                    }
                                    return 0;
                                }
                            });
                        }

                        listAdapter.notifyDataSetChanged();

                    }
                });

    }

    public void updateForumWithLike(String docId, ArrayList<String> likeList) {
        if (likeList.contains(currentUserId)) {
            likeList.remove(currentUserId);
        } else if (!likeList.contains(currentUserId)) {
            likeList.add(currentUserId);
        }
        DocumentReference docRef = db.collection("Forum").document(docId);
        docRef.update("likedUsers", likeList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: likelist");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: likelist");
            }
        });
        listAdapter.notifyDataSetChanged();
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

    public void deleteForum(String document_id) {

       db.collection("Forum").document(document_id).collection("Comments")
               .addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            for (QueryDocumentSnapshot doc:value){
                                db.collection("Forum").document(document_id).collection("Comments").document(doc.getId())
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Log.d("TAG", "onSuccess: delete subcollection"+doc.getId());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.d("TAG", "onFailure: delete subcollection");
                                    }
                                });
                            }
                        }
                   }
               });

        db.collection("Forum").document(document_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getForumListData();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "The selected document does not exist", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class ForumListRecyclerViewAdapter extends RecyclerView.Adapter<ForumListRecyclerViewAdapter.ForumListViewHolder> {
        ArrayList<Forum> arrayList;

        public ForumListRecyclerViewAdapter(ArrayList<Forum> arrayList) {
            super();
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ForumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_forumslist, parent, false);
            ForumListViewHolder viewHolder = new ForumListViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ForumListViewHolder holder, int position) {
            Forum forum = this.arrayList.get(position);
            holder.tvTitle.setText(this.arrayList.get(position).forumTitle);
            holder.tvName.setText(this.arrayList.get(position).forumCreator);
            holder.tvDetail.setText(this.arrayList.get(position).forumDescription);
            holder.tvDateTime.setText(this.arrayList.get(position).forumDateTime);
            holder.tvlikes.setText(this.arrayList.get(position).getLikedUsers().size() + " likes|");

            currentUserId = mAuth.getCurrentUser().getUid();
            String user_id = this.arrayList.get(position).UID;
            String document_id = this.arrayList.get(position).DID;
            if (user_id.equals(currentUserId)) {
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
                            alertDialogBuilder.setMessage("Selected Forum will be deleted permenantly");
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            deleteForum(document_id);
                                            alertDialog.dismiss();
                                        }
                                    });
                            alertDialogBuilder.show();
                        }
                    });
                }
            });

            ArrayList<String> likedUsers = forum.getLikedUsers();

            if (likedUsers.contains(currentUserId)) {
                holder.imageViewLike.setImageResource(R.drawable.like_favorite);
            } else if (!likedUsers.contains(currentUserId)) {
                holder.imageViewLike.setImageResource(R.drawable.like_not_favorite);
            }

            holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateForumWithLike(document_id, likedUsers);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.goToForumDetailFragment(document_id);
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

        public class ForumListViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvName, tvDetail, tvDateTime, tvlikes;
            ImageView imageViewTrash, imageViewLike;

            public ForumListViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.textViewC_ForumName);
                tvName = itemView.findViewById(R.id.textViewC_ForumUserName);
                tvDetail = itemView.findViewById(R.id.textViewC_ForumDetail);
                tvDateTime = itemView.findViewById(R.id.textViewC_ForumDateTime);
                imageViewTrash = itemView.findViewById(R.id.imageViewCmntTrash);
                imageViewLike = itemView.findViewById(R.id.imageViewLike);
                tvlikes = itemView.findViewById(R.id.textViewLikes);
            }
        }
    }

}