package com.example.inclass10;

/*
Assignment #: InClass10
File Name: AddCourseActivity.java
Full Name of Student1: Sahana Srinivas
Full Name of Student2: Krithika Kasaragod
 */

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.inclass10.databinding.ActivityAddCourseBinding;

public class AddCourseActivity extends AppCompatActivity {

    ActivityAddCourseBinding binding;
    String courseName, courseNumber, grade, creditHours;
    Double gradePts = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getString(R.string.title_add_course));
        binding.radioButtonA.setChecked(true);

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCourseActivity.super.finish();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseName = binding.editTextCName.getText().toString();
                courseNumber = binding.editTextCNumber.getText().toString();
                creditHours = binding.editTextCreditHours.getText().toString();

                int checkID=binding.rgGrade.getCheckedRadioButtonId();


                switch (checkID){
                    case R.id.radioButtonA:
                        grade = getString(R.string.label_a);
                        break;
                    case R.id.radioButtonB:
                        grade = getString(R.string.label_b);
                        break;
                    case R.id.radioButtonC:
                        grade = getString(R.string.label_c);
                        break;
                    case R.id.radioButtonD:
                        grade = getString(R.string.label_d);
                        break;
                    case R.id.radioButtonF:
                        grade = getString(R.string.label_f);
                        break;
                }

                if(validation()){
                    switch(grade){
                        case "A":
                            gradePts = (Double.parseDouble(creditHours) * 4.0);
                            break;
                        case "B":
                            gradePts = (Double.parseDouble(creditHours) * 3.0);
                            break;
                        case "C":
                            gradePts = (Double.parseDouble(creditHours) * 2.0);
                            break;
                        case "D":
                            gradePts = (Double.parseDouble(creditHours) * 1.0);
                            break;
                        case "F":
                            gradePts = (Double.parseDouble(creditHours) * 0.0);
                            break;
                    }

                    insertData(courseNumber,courseName,creditHours,grade,gradePts);
                    Toast.makeText(AddCourseActivity.this,getString(R.string.label_successfull),Toast.LENGTH_SHORT).show();
                    AddCourseActivity.super.finish();
                }

            }
        });

    }

    private void insertData(String courseNumber, String courseName, String creditHours, String grade, Double gradePts) {

        GradeScreenActivity.db.CDao().insertAll(new Course(courseNumber,courseName,creditHours,grade,String.valueOf(gradePts)));
        //Log.d("TAG", "insertData: "+GradeScreenActivity.db.CDao().getAll());
    }

    public boolean validation(){
        if(courseName.isEmpty() && courseNumber.isEmpty() && creditHours.isEmpty()){
            displayAlert(getString(R.string.label_error_all_fields));
            return false;
        }else if(courseNumber.isEmpty()){
            displayAlert(getString(R.string.label_error_course_number_field));
            return false;
        }else if(creditHours.isEmpty()){
            displayAlert(getString(R.string.label_error_course_hour_field));
            return false;
        }else if(courseName.isEmpty()){
            displayAlert(getString(R.string.label_error_course_name_field));
            return false;
        }

        return true;
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.label_alert))
                .setMessage(message)
                .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

}