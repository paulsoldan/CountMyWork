package com.example.pauls.countmywork;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.pauls.countmywork.MainActivity.user_text;

public class myReports extends AppCompatActivity {

    //for connect laptop to android
    //static final String API_URL = "http://192.168.137.1:1234/AndroidServer/rest/api/myReports";
    //for connect android to laptop
    static final String API_URL = "http://192.168.43.172:1234/AndroidServer/rest/api/myReports/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        new RetrieveFeedTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        if(id==R.id.nav_checkIn){
            startActivity(new Intent(this,checkIn.class));
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

        /*RetrieveFeedTask(checkIn x) {
            this.x = x;
        }*/

        protected String doInBackground(Void... urls) {
            // Do some validation here
            try {
                final URL url = new URL(API_URL + user_text.getText().toString());
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
            System.out.println(response);
            String [] words;
            List<String> in_response=new ArrayList<String>();
            String text1=response,text2;
            StringBuilder sb = new StringBuilder();
            for(int i=1;i<response.length()-1;i++){
                sb.append(text1.charAt(i));
            }
            text2=sb.toString();
            String my_new_str = text2.replaceAll(" ", "");
            words = my_new_str.split(",");
            for(int i=0;i<words.length;i++){
                in_response.add(words[i]);
            }

            TextView hoursWorked=(TextView)findViewById(R.id.textViewHoursWorked);
            String hour_final="00:00";
            String[] in_list = in_response.get(0).split(":");
            int h_in=Integer.parseInt(in_list[0]);
            int m_in=Integer.parseInt(in_list[1]);
            if(h_in<10){
                hour_final = "0" + h_in + ":" + m_in;
            }
            else {
                if (m_in < 10) {
                    hour_final = "" + h_in + ":0" + m_in;
                } else {
                    if (h_in < 10 && m_in < 10) {
                        hour_final = "0" + h_in + ":0" + m_in;
                    } else {
                        hour_final = "" + h_in + ":" + m_in;
                    }
                }
            }
            hoursWorked.setText(hour_final);
            TextView daysWorked=(TextView)findViewById(R.id.textViewWDTMonth);
            daysWorked.setText(in_response.get(1));
        }
    }
}