package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    TextView tvName;
    TextView tvEmail;
    TextView tvID;
    TextView tvDeptResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvName = findViewById(R.id.tvNameResult);
        tvEmail = findViewById(R.id.tvEmailResult);
        tvID = findViewById(R.id.tvIDResult);
        tvDeptResult = findViewById(R.id.tvDeptResult2);
        ProfileClass users = (ProfileClass)getIntent().getSerializableExtra(getString(R.string.KEY_USER));
        tvName.setText(users.name);
        tvEmail.setText(users.email);
        tvID.setText(String.valueOf(users.id));
        tvDeptResult.setText(users.Dept);

    }

}