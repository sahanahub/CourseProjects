package com.example.inclass10;

/*
Assignment #: InClass10
File Name: GradeScreenActivity.java
Full Name of Student1: Sahana Srinivas
Full Name of Student2: Krithika Kasaragod
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inclass10.databinding.ActivityAddCourseBinding;
import com.example.inclass10.databinding.ActivityGradeScreenBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;

public class GradeScreenActivity extends AppCompatActivity {

   public static InClassDatabase db;
   ActivityGradeScreenBinding binding;
   LinearLayoutManager linearLayoutManager;
   CourseListAdapter listAdapter;
   ArrayList<Course> courseArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityGradeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getString(R.string.title_grade_screen));

        //database setUp
         db = Room.databaseBuilder(this,InClassDatabase.class,"course.db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        binding.recyclerViewCourse.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewCourse.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("TAG", "onResume: checking");
        getCourseList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent=new Intent(GradeScreenActivity.this,AddCourseActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

        ArrayList<Course> arrayList;

        public CourseListAdapter(ArrayList<Course> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(GradeScreenActivity.this).inflate(R.layout.recyclerview_courselist, parent, false);
            CourseListViewHolder viewHolder = new CourseListViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
            Course course = this.arrayList.get(position);

            holder.tvCname.setText(this.arrayList.get(position).courseName);
            holder.tvCnumber.setText(this.arrayList.get(position).courseNumber);
            holder.tvCreditHour.setText(this.arrayList.get(position).creditHour+" "+getString(R.string.label_credit_hours));
            holder.tvGrade.setText(this.arrayList.get(position).grade);

            holder.imageViewTrash.setImageResource(R.drawable.ic_trash);

            holder.imageViewTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCourseItem(course);
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

        public class CourseListViewHolder extends RecyclerView.ViewHolder {
            TextView tvCname,tvCnumber, tvCreditHour, tvGrade;
            ImageView imageViewTrash;

            public CourseListViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCname = itemView.findViewById(R.id.textViewCourseName);
                tvCnumber = itemView.findViewById(R.id.textViewCourseNum);
                tvCreditHour = itemView.findViewById(R.id.textViewCredits);
                tvGrade = itemView.findViewById(R.id.textViewGrade);
                imageViewTrash = itemView.findViewById(R.id.imageViewTrash);
            }
        }
    }

    private void getCourseList() {

        Double totalHours = 0.0, totalGradePts = 0.0, gpa = 0.0;

        courseArrayList.clear();
        courseArrayList.addAll(db.CDao().getAll());

        for (int i = 0; i < courseArrayList.size(); i++) {
            totalHours = totalHours + Double.parseDouble(courseArrayList.get(i).creditHour);
            totalGradePts = totalGradePts + Double.parseDouble(courseArrayList.get(i).gradePts);
        }

        if(totalGradePts == 0.0){
            binding.textViewGpa.setText(getString(R.string.label_gpa_0));
            binding.textViewHours.setText(getString(R.string.label_hours)+" "+String.valueOf(totalHours));
        }else if(totalHours == 0.0){
            binding.textViewGpa.setText(getString(R.string.label_gpa_4));
            binding.textViewHours.setText(getString(R.string.label_hours_0));
        }else{
            gpa = totalGradePts/totalHours;

            Formatter formatter = new Formatter();
            formatter.format("%.2f", gpa);

            binding.textViewGpa.setText(getString(R.string.label_gpa)+" "+formatter.toString());
            binding.textViewHours.setText(getString(R.string.label_hours)+" "+String.valueOf(totalHours));
        }


        //Log.d("TAG", "onCreate: courselist:"+courseArrayList);
            listAdapter = new CourseListAdapter(courseArrayList);
            binding.recyclerViewCourse.getRecycledViewPool().setMaxRecycledViews(0, 0);
            binding.recyclerViewCourse.setAdapter(listAdapter);

    }

    private void deleteCourseItem(Course course) {

        db.CDao().delete(course);
        getCourseList();

    }
}