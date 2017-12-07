package com.teentitans.cakeproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.utils.ConnectionUtil;
import com.teentitans.cakeproject.utils.UserVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private static String URL_LOGIN = "http://cakeproject.whostf.com/php/get_user.php";
    UserVO userVO;
    private Button btnLogin;
    private TextView btnRegister;
    private TextView btnGuestLogin;
    private View.OnClickListener onClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.equals(btnLogin)) {
                new LoginTask().execute();
            } else if (v.equals(btnRegister)) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            } else if (v.equals(btnGuestLogin)) {
                userVO = new UserVO("id", "username", null, "date", 0, 0, true);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("user", userVO);
                intent.putExtra("bundle", b);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGuestLogin = findViewById(R.id.btnGuestLogin);

        btnLogin.setOnClickListener(onClick);
        btnRegister.setOnClickListener(onClick);
        btnGuestLogin.setOnClickListener(onClick);

    }

    protected String tryLogin(String mUsername, String mPassword) {

        String parameters = "username=" + mUsername + "&password=" + mPassword;
        try {
            return ConnectionUtil.getResponseFromURL(URL_LOGIN, parameters);
        } catch (IOException e) {
            return null;
        }
    }

    private class LoginTask extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Login
         */
        protected String doInBackground(String... args) {

            EditText etUsername = (EditText) findViewById(R.id.etUsername);
            EditText etPassword = (EditText) findViewById(R.id.etPassword);

            JSONObject userJson;
            String response = tryLogin(etUsername.getText().toString(), etPassword.getText().toString());
            if (response == null)
                return "Connection failed";

            try {
                userJson = new JSONObject(response).getJSONArray("user").getJSONObject(0);

            } catch (JSONException e) {
                return null;
            }

            try {
                userVO = new UserVO(userJson.getString("id"), userJson.getString("username"), null, userJson.getString("date"), Integer.valueOf(userJson.getString("gender")), Integer.valueOf(userJson.getString("experience")), false);
                JSONArray array = userJson.getJSONArray("tags");

                for (int j = 0; j < array.length(); j++)
                    userVO.addTag(array.getString(j));

            } catch (JSONException e) {
                return null;
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if (result != null) {
                if (result.equals("Connection failed"))
                    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("user", userVO);
                    intent.putExtra("bundle", b);
                    startActivity(intent);
                }
            } else
                Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();
        }
    }
}
