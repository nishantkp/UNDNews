package com.example.android.undnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.undnews.Fragment.CultureFragment;
import com.example.android.undnews.Fragment.EducationFragment;
import com.example.android.undnews.Fragment.FashionFragment;
import com.example.android.undnews.Fragment.HomeFragment;
import com.example.android.undnews.Fragment.LifeStyleFragment;
import com.example.android.undnews.Fragment.PoliticsFragment;
import com.example.android.undnews.Fragment.SportsFragment;
import com.example.android.undnews.Fragment.TechnologyFragment;

public class NewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /* Tag for log messages */
    private static String LOG_TAG = NewsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        // Setup the navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Start the first Menu item in Navigation Drawer and highlight it
        onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //TODO define behaviour of items in navigation view
        switch (id) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_body, new HomeFragment())
                        .commit();
                getSupportActionBar().setTitle(getString(R.string.app_name));
                break;
            case R.id.nav_culture:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_body, new CultureFragment())
                        .commit();
                getSupportActionBar().setTitle(getString(R.string.nav_culture_title));
                break;
            case R.id.nav_education:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_body, new EducationFragment())
                        .commit();
                getSupportActionBar().setTitle(getString(R.string.nav_education_title));
                break;
            case R.id.nav_fashion:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_body, new FashionFragment())
                        .commit();
                getSupportActionBar().setTitle(getString(R.string.nav_fashion_title));
                break;
            case R.id.nav_life_style:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_body, new LifeStyleFragment())
                        .commit();
                getSupportActionBar().setTitle(getString(R.string.nav_life_style_title));
                break;
            case R.id.nav_politics:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_body, new PoliticsFragment())
                        .commit();
                getSupportActionBar().setTitle(getString(R.string.nav_politics_title));
                break;
            case R.id.nav_sports:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_body, new SportsFragment())
                        .commit();
                getSupportActionBar().setTitle(getString(R.string.nav_sports_title));
                break;
            case R.id.nav_technology:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_body, new TechnologyFragment())
                        .commit();
                getSupportActionBar().setTitle(getString(R.string.nav_technology_title));
                break;
            case R.id.nav_settings:
                // When user selects the settings from navigation drawer start the SettingsActivity
                Intent settingActivityIntent = new Intent(NewsActivity.this, SettingsActivity.class);
                startActivity(settingActivityIntent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}