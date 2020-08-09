package com.example.pressnewspaper.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pressnewspaper.BuildConfig;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentSetting extends Fragment {
    View view;
    CircleImageView circleImageViewUserImg;
    TextView textViewPhone, textViewEmail, textViewName;
    RelativeLayout layUpdate, layShare, layAbout;
    SwitchCompat switchCompat;

    public FragmentSetting() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        initNavHeader();
        return view;
    }

    private void init() {
        layUpdate = view.findViewById(R.id.layUpdate);
        layShare = view.findViewById(R.id.layShare);
        layAbout = view.findViewById(R.id.layAbout);
        switchCompat = view.findViewById(R.id.switchC);

        if (SharedPrefManager.getInstance(getContext()).receiveNotification()){
            switchCompat.setChecked(true);
        }else{
            switchCompat.setChecked(false);
        }

        layUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStoreForUpdate();
            }
        });
        layShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareAppLink();
            }
        });
        layAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openUrl("http://onlinefit.com.sd/papers/public/");
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    SharedPrefManager.getInstance(getContext()).putReceiveNotification(true);
                }else {
                    SharedPrefManager.getInstance(getContext()).putReceiveNotification(false);
                }
            }
        });

    }

    private void initNavHeader() {
        circleImageViewUserImg = view.findViewById(R.id.profile_image);
        textViewName = view.findViewById(R.id.name);
        textViewPhone = view.findViewById(R.id.phone);
        textViewEmail = view.findViewById(R.id.email);
        if (!SharedPrefManager.getInstance(getActivity()).GetToken().equals("")){
            String name = SharedPrefManager.getInstance(getActivity()).GetUserName();
            String phone = SharedPrefManager.getInstance(getActivity()).GetUserPhone();
            String email = SharedPrefManager.getInstance(getActivity()).GetUserEmail();
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

        }else{
            textViewName.setText("لم تقم بتسجيل الدخول");
            textViewPhone.setVisibility(View.GONE);
            textViewEmail.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "انت تتصفح التطبيق كزائر", Toast.LENGTH_SHORT).show();
        }
    }

    private void openUrl(String url){
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void openStoreForUpdate(){
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void shareAppLink(){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            Toast.makeText(getContext(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
            //e.toString();
        }
    }




}
