package com.example.pauls.countmywork;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;

public class checkIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        final Context context=this;

        //Clock create
        TextClock clock=(TextClock)findViewById(R.id.clock1);

        //Spinner create
        Spinner spinner=(Spinner)findViewById(R.id.spinner_task);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.task_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;
    }
}