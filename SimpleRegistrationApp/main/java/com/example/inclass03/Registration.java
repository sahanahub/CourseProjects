package com.example.inclass03;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    EditText etName;
    EditText etEmail;
    EditText etID;
    TextView tvDeptResult;
    Button btnRegSubmit, btnDeptSelect;
    final static public int REQ_CODE = 100;
    String name, email, dept;
    String emailvalid;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etID = findViewById(R.id.etID);
        tvDeptResult = findViewById(R.id.tvDeptResult);
        btnRegSubmit = findViewById(R.id.btnRegSubmit);
        btnDeptSelect = findViewById(R.id.btnSelectDept);

        btnDeptSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Department.class);
                startActivityForResult(intent,REQ_CODE);
            }
        });
        btnRegSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    name = etName.getText().toString();
                    email = etEmail.getText().toString();
                    id = Integer.parseInt(etID.getText().toString());
                    dept = tvDeptResult.getText().toString();
                    emailvalid = email.trim();

                    if ((name.equals("")) && (email.equals("")) && (id == 0) && (dept.equals(""))) {
                        throw new Exception();
                    } else if (name.equals("")) {
                        Toast.makeText(Registration.this, getString(R.string.regErrorName), Toast.LENGTH_SHORT).show();
                    } else if (email.equals("")){
                    Toast.makeText(Registration.this, getString(R.string.regErrorEmail), Toast.LENGTH_SHORT).show();
                    }else if(id == 0) {
                        Toast.makeText(Registration.this, getString(R.string.regErrorId), Toast.LENGTH_SHORT).show();
                    }else if(dept.equals("")) {
                        Toast.makeText(Registration.this, getString(R.string.regErrorDept), Toast.LENGTH_SHORT).show();
                    } else if (!emailvalid.matches(emailPattern)) {
                        Toast.makeText(Registration.this, getString(R.string.regErrorEmailValid), Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intentSubmit = new Intent(Registration.this, Profile.class);
                        ProfileClass users = new ProfileClass(name, email, id, dept);
                        intentSubmit.putExtra(getString(R.string.KEY_USER), users);
                        startActivity(intentSubmit);
                    }

                }catch(Exception e){
                    Toast.makeText(Registration.this,getString(R.string.regErrorMain), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE){
            if(resultCode == RESULT_OK && data != null){
                tvDeptResult.setText(data.getStringExtra(getString(R.string.KEY_DEPT)));
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(Registration.this,getString(R.string.regErrorCancel), Toast.LENGTH_SHORT).show();
            }
        }
    }
}