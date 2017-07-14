package com.example.pauls.countmywork;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.pauls.countmywork.MainActivity.user_text;

public class checkOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        TextClock clock2 = (TextClock)findViewById(R.id.clock2);
    }
    //create button nav-bar
    public void onClickButtonCheckOut(View v) {
        //for connect laptop to android
        //static final String API_URL = "http://192.168.43.172:1234/AndroidServer/rest/api/checkOut/";
        //for connect android to laptop
        String API_URL = "http://192.168.43.172:1234/AndroidServer/rest/api/checkOut/";
        TextClock clock2 = (TextClock)findViewById(R.id.clock2);
        try {
            final URL url = new URL(API_URL + user_text.getText().toString() + "/" + clock2.getText().toString());
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
                        Toast.makeText(checkOut.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } finally {
                        urlConnection.disconnect();
                    }
                    return null;
                }
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    if(response.equals("ok"))
                        Toast.makeText(checkOut.this, "Done Check Out!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(checkOut.this, "Plese Check In!", Toast.LENGTH_LONG).show();
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        catch (MalformedURLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
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
        else {
            if (id == R.id.nav_myReports) {
                startActivity(new Intent(this, myReports.class));
            }
        }
        return  super.onOptionsItemSelected(menu);
    }
}
