package com.teentitans.cakeproject.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.teentitans.cakeproject.R;

public class LoginActivity extends ActionBarActivity {

    private Button _btnLogin;
    private TextView _btnRegister;
    private TextView _btnForgotPassword;
    private EditText _etUsername;
    private EditText _etPassword;

    private View.OnClickListener onClick = new View.OnClickListener(){
        public void onClick(View v) {
            if(v.equals(_btnLogin)) {
                if(_etUsername.getText().toString().equals("admin") && _etPassword.getText().toString().equals("admin"))
                    Log.e("OnClick", "Login Success");
                else
                    Log.e("OnClick", "Login Failed");
            }
            else if(v.equals(_btnRegister))
                Log.e("OnClick", "Register Clicked");
            else if(v.equals(_btnForgotPassword))
                Log.e("OnClick", "ForgotPassword Clicked");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _btnLogin = (Button) findViewById(R.id.btnLogin);
        _btnRegister = (TextView) findViewById(R.id.btnRegister);
        _btnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);
        _etUsername = (EditText) findViewById(R.id.etUsername);
        _etPassword = (EditText) findViewById(R.id.etPassword);

        _btnLogin.setOnClickListener(onClick);
        _btnRegister.setOnClickListener(onClick);
        _btnForgotPassword.setOnClickListener(onClick);

    }

}
