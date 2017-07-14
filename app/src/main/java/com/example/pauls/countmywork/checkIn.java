package com.example.pauls.countmywork;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.pauls.countmywork.MainActivity.user_text;

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
        new RetrieveFeedTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //Clock create
        TextClock clock=(TextClock)findViewById(R.id.clock1);

    }
    public void onClickButtonCheckIn(View v) {

        //Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
        //for connect laptop to android
        //static final String API_URL = "http://192.168.137.1:1234/AndroidServer/rest/api/tasks";
        //for connect android to laptop
        String API_URL = "http://192.168.43.172:1234/AndroidServer/rest/api/checkIn/";
        Spinner spinner=(Spinner)findViewById(R.id.spinner_task);
        String textSpinner=spinner.getSelectedItem().toString();
        TextClock clock=(TextClock)findViewById(R.id.clock1);
        try {
            final URL url = new URL(API_URL + user_text.getText().toString() + "/" + clock.getText().toString());
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    HttpURLConnection urlConnection=null;
                    try {
                        urlConnection = (HttpURLConnection)url.openConnection();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        if (stringBuilder.toString().equals("ok"))
                            System.out.println("okay");
                            //Toast.makeText(checkIn.this, "Done Check In!", Toast.LENGTH_LONG).show();
                        //else
                            //Toast.makeText(checkIn.this, "Error Check In!", Toast.LENGTH_LONG).show();
                        //System.out.println(stringBuilder.toString());
                        bufferedReader.close();
                        return stringBuilder.toString();
                    }
                    catch(Exception e){
                        Toast.makeText(checkIn.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } finally {
                        urlConnection.disconnect();
                    }
                    return null;
                }
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    if(response.equals("ok"))
                        Toast.makeText(checkIn.this, "Done Check In!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(checkIn.this, "Plese Check Out!", Toast.LENGTH_LONG).show();
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        catch (MalformedURLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

        private checkIn x;
        private Exception exception;
        public String user, password;

        RetrieveFeedTask(checkIn x) {
            this.x = x;
        }

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
            final Spinner sItems = (Spinner) findViewById(R.id.spinner_task);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    x, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sItems.setAdapter(adapter);
        }
    }
}