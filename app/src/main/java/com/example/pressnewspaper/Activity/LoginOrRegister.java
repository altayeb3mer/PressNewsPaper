package com.example.pressnewspaper.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.SharedPrefManager;
import com.example.pressnewspaper.Utils.ToolbarClass;

public class LoginOrRegister extends ToolbarClass implements View.OnClickListener {


    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_login_or_register, "تسجيل الدخول");


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login: {
                startActivity(new Intent(LoginOrRegister.this,Login.class));
                finish();
                break;
            }
            case R.id.register: {
                startActivity(new Intent(LoginOrRegister.this,RegistrationActivity.class));
                finish();
                break;
            }
        }
    }
}
