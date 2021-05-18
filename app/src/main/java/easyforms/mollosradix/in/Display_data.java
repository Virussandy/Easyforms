package easyforms.mollosradix.in;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Display_data extends AppCompatActivity {

    MaterialTextView folder_name;
    ImageView Id_card,Photo,Form;
    ImageView edit,delete;
    String id,serial_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        Intent intent = getIntent();
         id = intent.getStringExtra("id");
         serial_no = intent.getStringExtra("sl_no");

        Id_card = findViewById(R.id.id_card);
        Photo = findViewById(R.id.photo);
        Form = findViewById(R.id.form);


        edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Display_data.this,Edit_data.class);
                intent1.putExtra("id",id);
                intent1.putExtra("sl_no",serial_no);
                startActivity(intent1);
            }
        });

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Display_data.this);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String Url = "https://easyforms.in/api/data/delete/" + id;

                        ProgressDialog progressDialog = new ProgressDialog(Display_data.this);
                        progressDialog.setMessage("Loading data...");
                        progressDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    String message = obj.getString("message");
                                    Toast.makeText(Display_data.this, message, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }




                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Display_data.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<>();
                                String auth = "Bearer " + SharedPreference.getInstance(getApplicationContext()).getToken().getToken();
                                headers.put("Authorization", auth);
                                return headers;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(Display_data.this);
                        requestQueue.add(stringRequest);




                        finish();

//                        startActivity(new Intent(Display_data.this,Dashboard.class));

                    }
                });

                alert.show();
            }
        });

        folder_name = findViewById(R.id.folder_name);
        folder_name.setText(String.valueOf(serial_no));

        getJSON(id,serial_no);
    }

    private void getJSON(String id,String serial_no) {

        final String Url = "https://easyforms.in/api/data/show/" + id;

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(Display_data.this, response, Toast.LENGTH_SHORT).show();

                try {

                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        String id_card = jsonObject.getString("id_card");
                        String photo = jsonObject.getString("photo");
                        String form = jsonObject.getString("form");

                        String user_id = SharedPreference.getInstance(getApplicationContext()).getUser().getUser_id();
                        String url = "https://easyforms.in/storage/data/" + user_id + "/" + serial_no;

                        Picasso.get().load("" + url + "/" + id_card).into(Id_card);
                        Picasso.get().load("" + url + "/" + photo).into(Photo);
                        Picasso.get().load("" + url + "/" + form).into(Form);

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Display_data.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String auth = "Bearer " + SharedPreference.getInstance(getApplicationContext()).getToken().getToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}