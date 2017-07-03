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
    static final String API_URL = "http://192.168.137.1:1234/AndroidServer/rest/api/checkUser/";
    //for connect android to laptop
    //static final String API_URL = "http://192.168.43.172:1234/AndroidServer/rest/api/checkUser/";

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
        String user, password;

        public void setUser(){
            runOnUiThread(new Runnable() {
                public void run() {
                    user = MainActivity.user_text.getText().toString();
                }
            });
        }

        public void setPass(){
            runOnUiThread(new Runnable() {
                public void run() {
                    password = MainActivity.pass_text.getText().toString();
                }
            });
        }

        public void tryAgain(){
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.pass_text.getText().toString();
                }
            });
        }


        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                this.setUser();
                this.setPass();
                URL url = new URL(API_URL + user + "/" + password);
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
                MainActivity mActivity= new MainActivity();
                mActivity.init();
            }
            //"0\n"
            else {
                Intent homeIntent = new Intent(MainActivity.this, Home.class);
                startActivity(homeIntent);
            }
        }
    }
}
        /*protected void onPreExecute() {

        }*/
        /*private static String getUrlContents(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
            return content.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }*/

