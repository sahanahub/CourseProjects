package com.example.inclass_07;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inclass_07.databinding.FragmentContactListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ContactListFragment extends Fragment {


    FragmentContactListBinding fragmentContactListBinding;
    private final OkHttpClient client = new OkHttpClient();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ContactListFragment() {
        // Required empty public constructor
    }

    public static ContactListFragment newInstance(String param1, String param2) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentContactListBinding = FragmentContactListBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_contact_list));
        return fragmentContactListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getContactRequest();
    }

    private void getContactRequest() {
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contacts/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ArrayList<Contacts> listContacts= new ArrayList<>();
                    try {
                        ResponseBody responseBody = response.body();
                        String responseValue= responseBody.string();
                        JSONObject jsonObject= new JSONObject(responseValue);
                        JSONArray jsonArray = jsonObject.getJSONArray("contacts");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject json= jsonArray.getJSONObject(i);

                            Contacts contact= new Contacts();
                            contact.setId(json.getString("Cid"));
                            contact.setName(json.getString("Name"));
                            contact.setEmail(json.getString("Email"));
                            contact.setPhone(json.getString("Phone"));
                            contact.setType(json.getString("PhoneType"));
                            listContacts.add(contact);

                        }
                        Log.d("TAG", "onResponse: list"+listContacts);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}