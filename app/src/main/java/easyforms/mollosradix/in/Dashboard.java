package easyforms.mollosradix.in;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.security.identity.InvalidRequestMessageException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private String User_name,User_email;
    private TextView username,useremail;
    private ImageView profile_image;
    private RecyclerView recyclerView;
    private customadapter adapter;
    private TextView total_data, new_data_entry;
    private List<ListItem> listItems ;
    private final String Url = "https://easyforms.in/api/data/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        total_data = findViewById(R.id.totaldata);
        new_data_entry = findViewById(R.id.new_data_entry);

        new_data_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Add_data.class));
            }
        });
        listItems = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        User_name = SharedPreference.getInstance(getApplicationContext()).getUser().getName();
        User_email = SharedPreference.getInstance(getApplicationContext()).getUser().getEmail();
        drawer = findViewById(R.id.drawer);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar , R.string.open, R.string.close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();

        NavigationView navigationView = findViewById(R.id.navmenu);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        username = headerview.findViewById(R.id.username);
        useremail = headerview.findViewById(R.id.useremail);
        profile_image = headerview.findViewById(R.id.profile_image);
        Picasso.get().load(SharedPreference.getInstance(getApplicationContext()).getUser().getProfile_photo_url()).placeholder(R.mipmap.ic_launcher).fit().into(profile_image);
        username.setText(User_name);
        useremail.setText(User_email);
        getJSON();

    }

    private void getJSON() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                listItems.clear();
//                Toast.makeText(Dashboard.this, response, Toast.LENGTH_SHORT).show();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    int count = jsonArray.length();
                    if(count != 0){
                        total_data.setText(Integer.toString(count));
                    }
                    else {
                        total_data.setText("0");
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        ListItem item = new ListItem(
                                obj.getString("id"),
                                obj.getString("sl_no")
                        );
                        listItems.add(item);
                    }
                    adapter = new customadapter(listItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<>();
                String auth = "Bearer "+ SharedPreference.getInstance(getApplicationContext()).getToken().getToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.app_bar_search);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_dashboard :
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_add_data:
                Intent i = new Intent(Dashboard.this,Add_data.class);
                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.logout:
                finish();
                logout();
                SharedPreference.getInstance(getApplicationContext()).logout();
                drawer.closeDrawer(GravityCompat.START);
                break;
        }

        return true;
    }

    private void logout() {


        final String Url = "https://easyforms.in/api/logout";

        ProgressDialog progressDialog = new ProgressDialog(Dashboard.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String auth = "Bearer " + SharedPreference.getInstance(getApplicationContext()).getToken().getToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        requestQueue.add(stringRequest);

    }

}