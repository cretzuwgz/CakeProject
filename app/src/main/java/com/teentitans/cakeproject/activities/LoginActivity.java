package com.teentitans.cakeproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teentitans.cakeproject.R;
import com.teentitans.cakeproject.utils.UserVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends ActionBarActivity {

    private static String URL_LOGIN = "http://cakeproject.whostf.com/php/get_user.php";
    UserVO userVO;
    //test commit chituc
    private Button btnLogin;
    private TextView btnRegister;
    private TextView btnForgotPassword;
    private View.OnClickListener onClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (v.equals(btnLogin)) {
                new LoginTask().execute();
            } else if (v.equals(btnRegister)) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            } else if (v.equals(btnForgotPassword))
                Log.e("OnClick", "ForgotPassword Clicked");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);


        btnLogin.setOnClickListener(onClick);
        btnRegister.setOnClickListener(onClick);
        btnForgotPassword.setOnClickListener(onClick);

    }

    protected String tryLogin(String mUsername, String mPassword) {
        HttpURLConnection connection = null;
        OutputStreamWriter request;

        URL url;
        String response;
        String parameters = "username=" + mUsername + "&password=" + mPassword;

        try {
            url = new URL(URL_LOGIN);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line;
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            // You can perform UI operations here
            Log.e("JSON", response);
            isr.close();
            reader.close();
            connection.disconnect();
            return response;

        } catch (IOException e) {
            if (connection != null)
                connection.disconnect();
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
            pDialog.setIndeterminate(false);
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
                userVO = new UserVO(userJson.getString("id"), userJson.getString("username"), null, userJson.getString("date"), userJson.getString("gender"), userJson.getString("experience"));
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
