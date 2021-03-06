package com.example.pressnewspaper.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.pressnewspaper.Fragments.FragmentDepts;
import com.example.pressnewspaper.Fragments.FragmentMain;
import com.example.pressnewspaper.Fragments.FragmentMySub;
import com.example.pressnewspaper.Fragments.FragmentNotification;
import com.example.pressnewspaper.Fragments.FragmentSavedPost;
import com.example.pressnewspaper.Fragments.FragmentSetting;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.CustomViewPager;
import com.example.pressnewspaper.Utils.SharedPrefManager;
import com.example.pressnewspaper.Utils.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton imageButton_search;
    private CustomViewPager viewPager;

    View nHeader;
    CircleImageView circleImageViewUserImg;
    TextView textViewPhone, textViewEmail, textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        init();
        initViewPager();
//        CheckLogin();

        viewPager.setOffscreenPageLimit(5);

        getFirebaseToken();

    }

    private void getFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Error", "Failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        Log.i("FCM Token", "Current token=" + token);
                    }
                });
    }

    MenuItem prevMenuItem;
    MenuItem navLogin;

    private void initNavHeader() {
        navLogin = navigationView.getMenu().findItem(R.id.login);
        nHeader = navigationView.getHeaderView(0);
        circleImageViewUserImg = nHeader.findViewById(R.id.profile_image);
        textViewName = nHeader.findViewById(R.id.name);
        textViewPhone = nHeader.findViewById(R.id.phone);
        textViewEmail = nHeader.findViewById(R.id.email);
        if (!SharedPrefManager.getInstance(this).GetToken().equals("")){
            String name = SharedPrefManager.getInstance(this).GetUserName();
            String phone = SharedPrefManager.getInstance(this).GetUserPhone();
            String email = SharedPrefManager.getInstance(this).GetUserEmail();
            textViewName.setText(name);

            if (phone.equals("")||phone.equals("null")){
                textViewPhone.setVisibility(View.GONE);
            }else{
                textViewPhone.setText(phone);
            }

            if (email.equals("")||email.equals("null")){
                textViewEmail.setVisibility(View.GONE);
            }else{
                textViewEmail.setText(email);
            }
            navLogin.setVisible(false);
        }else{
            nHeader.setVisibility(View.GONE);
            navLogin.setVisible(true);
            Toast.makeText(this, "انت تتصفح التطبيق كزائر", Toast.LENGTH_SHORT).show();
        }
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

        fragmentDepts = new FragmentDepts();

        adapter.addFragment(fragmentMain,"الرئيسية");
//        adapter.addFragment(fragmentMySub,"اشتراكاتي");
        adapter.addFragment(fragmentDepts,"اقسام");
        adapter.addFragment(fragmentNotification,"اشعارات");
        adapter.addFragment(fragmentSavedPost,"المفضلة");
        adapter.addFragment(fragmentSetting,"الضبط");
        viewPager.setAdapter(adapter);
    }

    FragmentMain fragmentMain;
    FragmentMySub fragmentMySub;
    FragmentNotification fragmentNotification;
    FragmentSavedPost fragmentSavedPost;
    FragmentSetting fragmentSetting;

    FragmentDepts fragmentDepts;



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
            case R.id.login: {
                startActivity(new Intent(getApplicationContext(),Login.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_news_paper: {
                startActivity(new Intent(this,NewsPapers.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_menu_mySub: {
                startActivity(new Intent(this,MySubActivity.class));
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
//                openUrl("https://shamilpress.com/");// TODO: 8/7/2020
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_twitter: {
//                openUrl("https://shamilpress.com/");// TODO: 8/7/2020
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_website: {
                openUrl("https://shamilpress.com/");
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
        switch (f_no) {
            case 1: {
                viewPager.setCurrentItem(0);
                toolbar.setTitle("");
                SetNavigationItemSelected(R.id.btn_nav_main_ac);
                break;
            }
            case 2: {
                viewPager.setCurrentItem(1);
                toolbar.setTitle("اقسام");
                SetNavigationItemSelected(R.id.btn_nav_my_sub);
                break;
            }
            case 3: {
                viewPager.setCurrentItem(2);
                toolbar.setTitle("عاجل");
                SetNavigationItemSelected(R.id.btn_nav_notification);
                break;
            }
            case 4: {
//                if (SharedPrefManager.getInstance(getApplicationContext()).receiveNotification()){
//                    viewPager.setCurrentItem(3);
//                }else{
//                    viewPager.setCurrentItem(2);
//                }
                viewPager.setCurrentItem(3);
                toolbar.setTitle("المفضلة");
                SetNavigationItemSelected(R.id.btn_nav_saved);
                break;
            }
            case 5: {
//                if (SharedPrefManager.getInstance(getApplicationContext()).receiveNotification()){
//                    viewPager.setCurrentItem(4);
//                }else{
//                    viewPager.setCurrentItem(3);
//                }

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
//        switchToFragment(current_fragment);
        initNavHeader();
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
                        .setSmallIcon(R.mipmap.ic_splash_logo)
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

    private void openUrl(String url){
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    //end of class
}
