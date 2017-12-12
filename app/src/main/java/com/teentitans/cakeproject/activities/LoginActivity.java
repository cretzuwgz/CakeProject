package com.teentitans.cakeproject.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends Activity {

    private final static String URL_LOGIN = ConnectionUtil.URL_BASE + "get_user.php";
    private static LoginActivity ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ACTIVITY = this;
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        TextView btnRegister = findViewById(R.id.btnRegister);
        TextView btnGuestLogin = findViewById(R.id.btnGuestLogin);

        btnLogin.setOnClickListener(v -> new LoginTask().execute());
        btnRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        btnGuestLogin.setOnClickListener(v -> {
            UserVO userVO = new UserVO("id", "username", null, "date", 0, 0, true);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("user", userVO);
            intent.putExtra("bundle", b);
            startActivity(intent);
        });

    }

    private static class LoginTask extends AsyncTask<String, String, UserVO> {

        private ProgressDialog _progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _progressDialog = new ProgressDialog(ACTIVITY);
            _progressDialog.setMessage("Logging in...");
            _progressDialog.setIndeterminate(true);
            _progressDialog.setCancelable(false);
            _progressDialog.show();
        }

        private String tryLogin(String mUsername, String mPassword) {

            String parameters = "username=" + mUsername + "&password=" + mPassword;
            try {
                return ConnectionUtil.getResponseFromURL(URL_LOGIN, parameters);
            } catch (IOException e) {
                Log.e(e.getMessage(), e.toString());
                return null;
            }
        }

        /**
         * Login
         */
        protected UserVO doInBackground(String... args) {

            EditText etUsername = ACTIVITY.findViewById(R.id.etUsername);
            EditText etPassword = ACTIVITY.findViewById(R.id.etPassword);

            JSONObject userJson;
            String response = tryLogin(etUsername.getText().toString(), etPassword.getText().toString());
            if (response == null)
                return null;

            try {
                userJson = new JSONObject(response).getJSONArray("user").getJSONObject(0);
            } catch (JSONException e) {
                return null;
            }

            UserVO userVO;
            try {
                userVO = new UserVO(userJson.getString("id"), userJson.getString("username"), null, userJson.getString("date"), Integer.valueOf(userJson.getString("gender")), Integer.valueOf(userJson.getString("experience")), false);
                if (userJson.has("tags")) {
                    JSONArray array = userJson.getJSONArray("tags");
                    for (int j = 0; j < array.length(); j++)
                        userVO.addTag(array.getString(j));
                }
            } catch (JSONException e) {
                return null;
            }

            return userVO;
        }

        @Override
        protected void onPostExecute(UserVO result) {
            super.onPostExecute(result);
            _progressDialog.dismiss();

            if (result != null) {
                Intent intent = new Intent(ACTIVITY, MainActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("user", result);
                intent.putExtra("bundle", b);
                ACTIVITY.startActivity(intent);
            } else
                Toast.makeText(ACTIVITY, R.string.error_login, Toast.LENGTH_SHORT).show();
        }
    }
}
