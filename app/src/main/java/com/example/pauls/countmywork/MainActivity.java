package com.example.pauls.countmywork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public Button login;
    public EditText user_text;
    public EditText pass_text;
    public void init(){
        login=(Button)findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_text.getText().toString().equals("test")&&pass_text.getText().toString().equals("test")) {
                    Intent homeIntent = new Intent(MainActivity.this, Home.class);
                    startActivity(homeIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please try again!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        user_text = (EditText) findViewById(R.id.editText);
        pass_text = (EditText) findViewById(R.id.editText2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
}
