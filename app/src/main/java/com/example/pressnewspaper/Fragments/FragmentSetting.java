package com.example.pressnewspaper.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentSetting extends Fragment {
    View view;
    CircleImageView circleImageViewUserImg;
    TextView textViewPhone, textViewEmail, textViewName;

    public FragmentSetting() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initNavHeader();
        return view;
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

}
