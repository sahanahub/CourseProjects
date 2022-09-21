package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Department extends AppCompatActivity {
    RadioGroup rgDept;
    String dept;
    Button btnDeptSelect, btnDeptCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        rgDept = findViewById(R.id.rgDept);
        btnDeptSelect = findViewById(R.id.btnDeptSelect);
        btnDeptCancel = findViewById(R.id.btnDeptCancel);

        btnDeptSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    int checkId = rgDept.getCheckedRadioButtonId();
                    if(checkId < 0)
                        throw new Exception();
                    else{

                        if(checkId == R.id.rbCS)
                            dept = getString(R.string.rbCS);
                        else if(checkId == R.id.rgBio)
                            dept = getString(R.string.rbBio);
                        else if(checkId == R.id.rgDS)
                            dept = getString(R.string.rbDS);
                        else if(checkId == R.id.rgSis)
                            dept = getString(R.string.rbSis);
                    }

                }catch(Exception e){
                    Toast.makeText(Department.this, getString(R.string.regErrorDept), Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.KEY_DEPT),dept);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        btnDeptCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.KEY_DEPT),dept);
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

    }
}