package com.example.pauls.countmywork;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class checkIn extends AppCompatActivity {

    public String [] words;
    public List<String> spinnerArray =  new ArrayList<String>();
    //for connect laptop to android
    //static final String API_URL = "http://192.168.137.1:1234/AndroidServer/rest/api/tasks";
    //for connect android to laptop
    static final String API_URL = "http://192.168.43.172:1234/AndroidServer/rest/api/tasks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        final Context context=this;

        //Clock create
        TextClock clock=(TextClock)findViewById(R.id.clock1);

        new RetrieveFeedTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //Spinner create
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner_task);
        sItems.setAdapter(adapter);

    }

    //create button nav-bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;
    }
    //change activity page from nav-bar
    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        int id=menu.getItemId();
        if(id==R.id.nav_myReports){
            startActivity(new Intent(this,myReports.class));
        }
        else{
            if(id==R.id.nav_checkOut){
                startActivity(new Intent(this,checkOut.class));
            }
        }
        return  super.onOptionsItemSelected(menu);
    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        public String user, password;

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                final URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    System.out.println(stringBuilder.toString());
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
            String text1=response,text2;
            StringBuilder sb = new StringBuilder();
            for(int i=1;i<response.length()-1;i++){
                sb.append(text1.charAt(i));
            }
            text2=sb.toString();
            words = text2.split(",");
            for(int i=0;i<words.length;i++){
                spinnerArray.add(words[i]);
            }
        }
    }
}