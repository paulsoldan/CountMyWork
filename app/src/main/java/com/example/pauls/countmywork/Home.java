package com.example.pauls.countmywork;

import android.content.ClipData;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Home extends AppCompatActivity {

    private DrawerLayout homeDrawLayout;
    private ActionBarDrawerToggle hToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeDrawLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        hToggle = new ActionBarDrawerToggle(this,homeDrawLayout,R.string.open,R.string.close);

        homeDrawLayout.addDrawerListener(hToggle);
        hToggle.syncState();

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.navigation_menu,menu);
        return  super.onCreateOptionsMenu(menu);
    }
    /*public boolean onOptionsItemSelection(MenuItem item){
        if(hToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
