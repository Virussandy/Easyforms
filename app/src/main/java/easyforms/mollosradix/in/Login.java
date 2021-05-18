package easyforms.mollosradix.in;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    private Button login;
    private final String Url = "https://easyforms.in/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);

        if (SharedPreference.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Dashboard.class));
            return;
        }
        editTextUsername = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }
    private void userLogin() {

        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        else {

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please Wait...");
            dialog.show();

            StringRequest request = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();

                    try {
                        JSONObject obj = new JSONObject(response);
                        String message = obj.getString("message");
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        Boolean success = obj.getBoolean("success");

                        JSONObject UserJson = obj.getJSONObject("0");
                        String token = UserJson.getString("token");
                        Token token1 = new Token(token);
                        JSONObject userJson = UserJson.getJSONObject("user");

                        if(success){
                            User users = new User(
                                    userJson.getString("id"),
                                    userJson.getString("name"),
                                    userJson.getString("email"),
                                    userJson.getString("profile_photo_url")
                            );
                            SharedPreference.getInstance(getApplicationContext()).userLogin(users,token1);
                        }
                            dialog.dismiss();
                            finish();
                            Intent i = new Intent(Login.this, Dashboard.class);
                            startActivity(i);
                    } catch (JSONException e) {
                        Log.i("tagconvertstr", "[" + e + "]");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("email",username);
                    params.put("password",password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        }

    }
}