package com.example.dharam.police;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private final CarFragment carFragment =new CarFragment();
    final private FragmentManager fragmentManager=getSupportFragmentManager();
    Fragment currentFragment;
    NavigationView navigationView=null;
    Toolbar toolbar=null;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        navigationView.getMenu().getItem(0
        ).setChecked(true);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialization
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Toolbar or the action bar here
        toolbar.setTitle("Cars");
        setSupportActionBar(toolbar);

        fragmentManager.beginTransaction().add(R.id.mainLayout, carFragment,"carfragment").commit();


        //DrawerLayout and stuff here
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.cars)
        {
            //TODO: Handle cars.




            Fragment fragment=  fragmentManager.findFragmentByTag("carfragment");

            if(fragment!=null)
            {
                Log.d("msg","fragment was not null");
                fragmentManager.beginTransaction().replace(R.id.mainLayout, fragment).commit();
            }
            else
            {
                Log.d("msg","fragment was null");
                fragmentManager.beginTransaction().add(R.id.mainLayout, carFragment,"carfragment").commit();
                fragment= fragmentManager.findFragmentByTag("carfragment");
            }

        }
        else if (id == R.id.scan)
        {
            startActivity(new Intent(MainActivity.this,ScanCar.class));

        }
        else if (id == R.id.logout)
        {
            //TODO: Handle Logout button.
            int flag=0;
            try
            {
                FirebaseAuth.getInstance().signOut();
            }
            catch (Exception e)
            {
                Snackbar.make(null, "Something went wrong", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                flag=1;
            }

            if(flag==0)
            {
                finish();
                startActivity(new Intent(getApplicationContext(),Login_Activity.class));
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
