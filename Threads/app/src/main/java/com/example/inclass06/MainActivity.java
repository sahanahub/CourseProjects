package com.example.inclass06;

/*
a. Assignment #:InClass06
b. File Name:MainActivity.java
c. Full name of the Student 1: Krithika Kasaragod
c. Full name of the Student 2: Sahana Srinivas
*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBarComplexity;
    ProgressBar progressBar;
    TextView textViewComplexityTimes,textViewProgress, textViewAvgNum;
    Button buttonGenerate;
    TextView textViewAvgTitle;
    int complexity = -1;
    ArrayList<Double> generatedList;
    ListView listView;
    ArrayAdapter<Double> adapter;
    Handler handler;
    Double avg = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        seekBarComplexity = findViewById(R.id.seekBarComplexity);
        textViewComplexityTimes = findViewById(R.id.textViewComplexityTimes);
        buttonGenerate = findViewById(R.id.buttonGenerate);
        progressBar = findViewById(R.id.progressBar);
        textViewProgress = findViewById(R.id.textViewProgress);
        textViewAvgNum = findViewById(R.id.textViewAverage);
        textViewAvgTitle = findViewById(R.id.textViewAverageTitle);
        listView = findViewById(R.id.listViewNumberList);

        seekBarComplexity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewComplexityTimes.setText(i+ getString(R.string.label_times));
                complexity = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        ExecutorService taskPool = Executors.newFixedThreadPool(2);
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(complexity==0){
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setMax(complexity);
                    textViewProgress.setVisibility(View.VISIBLE);
                    textViewAvgTitle.setVisibility(View.VISIBLE);
                    textViewAvgNum.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    progressBar.setProgress(0);
                    textViewProgress.setText(getString(R.string.label_zero)+getString(R.string.label_slash)+getString(R.string.label_zero));
                    textViewAvgNum.setText(String.valueOf(avg));
                }
                listView.setVisibility(View.INVISIBLE);
                taskPool.execute(new ThreadClass(complexity));
            }
        });


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                avg = 0.00;
                switch (message.what){
                    case ThreadClass.GENERATE_START:
                        buttonGenerate.setClickable(false);
                        seekBarComplexity.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setMax(complexity);
                        textViewProgress.setVisibility(View.VISIBLE);
                        textViewAvgTitle.setVisibility(View.VISIBLE);
                        textViewAvgNum.setVisibility(View.VISIBLE);
                        textViewAvgNum.setText(String.valueOf(avg));
                        textViewProgress.setText(getString(R.string.label_zero)+getString(R.string.label_slash)+getString(R.string.label_zero));
                        progressBar.setProgress(0);
                        break;

                    case ThreadClass.GENERATE_PROGRESS:
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setMax(complexity);
                        textViewProgress.setVisibility(View.VISIBLE);
                        textViewAvgTitle.setVisibility(View.VISIBLE);
                        textViewAvgNum.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        generatedList = (ArrayList<Double>) message.getData().getSerializable(ThreadClass.ARG_GENERATED_LIST);
                        int i = message.getData().getInt(ThreadClass.ARG_TASK_PARAM);
                        progressBar.setProgress(i);
                        Double num = message.getData().getDouble(ThreadClass.ARG_GENERATED_NUM);
                        textViewProgress.setText(String.valueOf(i)+getString(R.string.label_slash)+String.valueOf(complexity));
                        avg = (avg+num)/i;
                        textViewAvgNum.setText(String.valueOf(avg));
                        adapter = new ArrayAdapter<Double>(MainActivity.this, android.R.layout.simple_list_item_1,android.R.id.text1,generatedList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;

                    case ThreadClass.GENERATE_STOP:
                        buttonGenerate.setClickable(true);
                        seekBarComplexity.setEnabled(true);
                        break;
                }
                return false;
            }
        });


    }

    //INNER THREAD CLASS
    class ThreadClass implements Runnable{

        static final int GENERATE_START = 0;
        static final int GENERATE_PROGRESS = 1;
        static final int GENERATE_STOP = -1;
        static final String ARG_TASK_PARAM = "ARG_TASK_PARAM";
        static final String ARG_GENERATED_NUM = "ARG_GENERATED_NUM";
        static final String ARG_GENERATED_LIST = "ARG_GENERATED_LIST";
        ArrayList<Double> generatedList = new ArrayList<Double>();
        int complexity;
        double generatedNumber;

        public ThreadClass(int complexity){
            this.complexity = complexity;
        }

        @Override
        public void run() {

            Message startMessage = new Message();
            startMessage.what = GENERATE_START;
            handler.sendMessage(startMessage);

            for (int i = 1; i <= complexity ; i++) {
                generatedNumber = HeavyWork.getNumber();
                generatedList.add(generatedNumber);
                Message progressMessage = new Message();
                progressMessage.what = GENERATE_PROGRESS;
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_TASK_PARAM,i);
                bundle.putDouble(ARG_GENERATED_NUM,generatedNumber);
                bundle.putSerializable(ARG_GENERATED_LIST,generatedList);
                progressMessage.setData(bundle);
                handler.sendMessage(progressMessage);
            }
            Message stopMessage = new Message();
            stopMessage.what = GENERATE_STOP;
            handler.sendMessage(stopMessage);
        }
    }
}