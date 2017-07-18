package com.example.pauls.countmywork;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.app.Activity;

import java.net.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    public Button login;
    static public EditText user_text;
    static public EditText pass_text;
    //for connect laptop to android
    //static final String API_URL = "http://192.168.137.1:1234/AndroidServer/rest/api/checkUser/";
    //for connect android to laptop
    static final String API_URL = "http://192.168.43.172:1234/AndroidServer/rest/api/checkUser/";

    public void init() {
        login = (Button) findViewById(R.id.button);
        user_text = (EditText) findViewById(R.id.editText);
        pass_text = (EditText) findViewById(R.id.editText2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String output = null;
                //new RetrieveFeedTask().execute();
                new RetrieveFeedTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }



    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        public String user, password;

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                Runnable r1 = new Runnable() {
                    public void run() {
                        user = MainActivity.user_text.getText().toString();
                        synchronized(this)
                        {
                            this.notify();
                        }
                    }
                };
                synchronized(r1) {
                    runOnUiThread(r1);
                    r1.wait() ; // unlocks r1 while waiting
                }

                Runnable r2 = new Runnable() {
                    public void run() {
                        password = MainActivity.pass_text.getText().toString();
                        synchronized(this)
                        {
                            this.notify();
                        }
                    }
                };
                synchronized(r2) {
                    runOnUiThread(r2);
                    r2.wait() ; // unlocks r2 while waiting
                }
                final URL url = new URL(API_URL + user + "/" + password);
                /*runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(),""+url.toString(),Toast.LENGTH_SHORT).show();
                    }
                });*/
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response.equals("0\n")) {
                Toast.makeText(getApplicationContext(),"Please try again!",Toast.LENGTH_SHORT).show();
            //    MainActivity mActivity= new MainActivity();
             //   mActivity.init();
            }
            //"0\n"
            else {
                //Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(MainActivity.this, Home.class);
                startActivity(homeIntent);
            }
        }
    }
}