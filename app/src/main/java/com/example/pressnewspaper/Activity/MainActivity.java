package com.example.pressnewspaper.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.pressnewspaper.Fragments.FragmentMain;
import com.example.pressnewspaper.Fragments.FragmentMySub;
import com.example.pressnewspaper.Fragments.FragmentNotification;
import com.example.pressnewspaper.Fragments.FragmentSavedPost;
import com.example.pressnewspaper.Fragments.FragmentSetting;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.CustomViewPager;
import com.example.pressnewspaper.Utils.SharedPrefManager;
import com.example.pressnewspaper.Utils.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton imageButton_search;
    private CustomViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        init();
        initViewPager();
        CheckLogin();
    }

    private void init() {
        viewPager =  findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.btn_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        imageButton_search = findViewById(R.id.image_btn_search);

        toolbar = findViewById(R.id.public_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.n_drawer);
        navigationView = findViewById(R.id.n_view);
        navigationView.setItemIconTintList(null);

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


    MenuItem prevMenuItem;
    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page",""+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }

    private void CheckLogin() {
        String token = SharedPrefManager.getInstance(this).GetToken();
        if (token.equals("")) {
            startActivity(new Intent(this, LoginOrRegister.class));
            finish();
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentMain = new FragmentMain();
        fragmentMySub = new FragmentMySub();
        fragmentNotification = new FragmentNotification();
        fragmentSavedPost = new FragmentSavedPost();
        fragmentSetting = new FragmentSetting();

        adapter.addFragment(fragmentMain,"الرئيسية");
        adapter.addFragment(fragmentMySub,"اشتراكاتي");
        adapter.addFragment(fragmentNotification,"اشعارات");
        adapter.addFragment(fragmentSavedPost,"المحفوظات");
        adapter.addFragment(fragmentSetting,"الضبط");
        viewPager.setAdapter(adapter);
    }

    FragmentMain fragmentMain;
    FragmentMySub fragmentMySub;
    FragmentNotification fragmentNotification;
    FragmentSavedPost fragmentSavedPost;
    FragmentSetting fragmentSetting;



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
            case R.id.nav_facebook: {
                startActivity(new Intent(MainActivity.this,Login.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_twitter: {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_website: {
                startActivity(new Intent(MainActivity.this,ConfirmActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_notification: {
                PushNotification("اختبار","يعمل الاختبار بنجاح");
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
                viewPager.setCurrentItem(0);
                toolbar.setTitle("الرئيسية");
                SetNavigationItemSelected(R.id.btn_nav_main_ac);
                break;
            }
            case 2: {
                viewPager.setCurrentItem(1);
                toolbar.setTitle("اشتراكاتي");
                SetNavigationItemSelected(R.id.btn_nav_my_sub);
                break;
            }
            case 3: {
                viewPager.setCurrentItem(2);
                toolbar.setTitle("الاشعارات");
                SetNavigationItemSelected(R.id.btn_nav_notification);
                break;
            }
            case 4: {
                viewPager.setCurrentItem(3);
                toolbar.setTitle("المحفوظات");
                SetNavigationItemSelected(R.id.btn_nav_saved);
                break;
            }
            case 5: {
                viewPager.setCurrentItem(4);
                toolbar.setTitle("الضبط");
                SetNavigationItemSelected(R.id.btn_nav_setting);
                break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        switchToFragment(current_fragment);
    }

    int current_fragment=1;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (viewPager.getCurrentItem()==0){
                super.onBackPressed();
            }else{
                viewPager.setCurrentItem(0);
            }
        }
    }


    private void PushNotification(String title, String content){

        final Intent notify_intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notify_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true);



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (!isNotificationVisible(1)) {
            notificationManager.notify(1, mBuilder.build());

        }
    }

    private boolean isNotificationVisible(int not_id) {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent test = PendingIntent.getActivity(getApplicationContext(), not_id, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        return test != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PushNotification("اختبار","يعمل الاختبار بنجاح");
    }
    //end of class
}
