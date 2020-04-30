package com.example.pressnewspaper.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.pressnewspaper.Fragments.FragmentSavedPost;
import com.example.pressnewspaper.Fragments.FragmentSetting;
import com.example.pressnewspaper.Fragments.FragmentMain;
import com.example.pressnewspaper.Fragments.FragmentMySub;
import com.example.pressnewspaper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton imageButton_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        init();
        switchToFragment(1);
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.btn_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        imageButton_search = findViewById(R.id.image_btn_search);

        toolbar = findViewById(R.id.public_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.n_drawer);
        navigationView = findViewById(R.id.n_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        imageButton_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });

    }



    private void SetNavigationItemSelected(int id){
        bottomNavigationView.getMenu().findItem(id).setChecked(true);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //nav_btn

            case R.id.btn_nav_main_ac: {
                switchToFragment(1);
                break;
            }
            case R.id.btn_nav_my_sub: {
                switchToFragment(2);
                break;
            }
            case R.id.btn_nav_notification: {
                switchToFragment(3);
                break;
            }
            case R.id.btn_nav_saved: {
                switchToFragment(4);
                break;
            }
            case R.id.btn_nav_setting: {
                switchToFragment(5);
                break;
            }


            //navigation menu
            case R.id.nav_menu_main: {
                switchToFragment(1);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_news_paper: {
                startActivity(new Intent(this,NewsPapers.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_my_sub: {
                switchToFragment(2);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_notification: {
                switchToFragment(3);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_saved: {
                switchToFragment(4);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_setting: {
                switchToFragment(5);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }

        }
        return true;
    }

    public void switchToFragment(int f_no) {
        FragmentManager manager = getSupportFragmentManager();
        switch (f_no) {
            case 1: {
                manager.beginTransaction().replace(R.id.fragment_container, new FragmentMain()).commit();
                SetNavigationItemSelected(R.id.btn_nav_main_ac);
                break;
            }
            case 2: {
                manager.beginTransaction().replace(R.id.fragment_container, new FragmentMySub()).commit();
                SetNavigationItemSelected(R.id.btn_nav_my_sub);
                break;
            }
            case 3: {
                SetNavigationItemSelected(R.id.btn_nav_notification);
                break;
            }
            case 4: {
                manager.beginTransaction().replace(R.id.fragment_container, new FragmentSavedPost()).commit();
                SetNavigationItemSelected(R.id.btn_nav_saved);
                break;
            }
            case 5: {
                manager.beginTransaction().replace(R.id.fragment_container, new FragmentSetting()).commit();
                SetNavigationItemSelected(R.id.btn_nav_setting);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (bottomNavigationView.getSelectedItemId()!=R.id.btn_nav_main_ac){
                switchToFragment(1);
            }else{
                super.onBackPressed();
            }
        }
    }




    //end of class
}
