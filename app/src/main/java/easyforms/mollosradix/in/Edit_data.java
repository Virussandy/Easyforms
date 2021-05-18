package easyforms.mollosradix.in;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Edit_data extends AppCompatActivity {

    private ImageView Id_card, Photo, Form;
    private Button Submit;

    static int ACTION_IMAGE_CAPTURE1 = 1;
    private Bitmap bitmap1 = null;

    static int ACTION_IMAGE_CAPTURE2 = 2;
    private Bitmap bitmap2 = null;

    static int ACTION_IMAGE_CAPTURE3 = 3;
    private Bitmap bitmap3 = null;

    String uploadImage1 = "";
    String uploadImage2 = "";
    String uploadImage3 = "";

    private Uri fileUri;
    String picturePath;
    Uri selectedImage;

    String id = null, serial_no = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        Id_card = findViewById(R.id.id_card);
        Photo = findViewById(R.id.photo);
        Form = findViewById(R.id.form);
        Submit = findViewById(R.id.Upload);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        serial_no = intent.getStringExtra("sl_no");

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    editImage();
            }
        });


        Id_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext()).withPermission(
                        Manifest.permission.CAMERA
                ).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                        File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                        output1 = new File (dir,(System.currentTimeMillis()) + ".jpeg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output1));
                        startActivityForResult(intent, ACTION_IMAGE_CAPTURE1);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }

        });

        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withContext(getApplicationContext()).withPermission(
                        Manifest.permission.CAMERA
                ).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                        File file2 = new File(Environment.getExternalStorageDirectory(),
//                                "photo.jpeg");
//                        outPutfileUri2 = Uri.fromFile(file2);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri2);
                        startActivityForResult(intent, ACTION_IMAGE_CAPTURE2);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

            }
        });

        Form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext()).withPermission(
                        Manifest.permission.CAMERA
                ).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                        File file3 = new File(Environment.getExternalStorageDirectory(),
//                                "form_photo.jpg");
//                        outPutfileUri3 = Uri.fromFile(file3);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri3);
                        startActivityForResult(intent, ACTION_IMAGE_CAPTURE3);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                    bitmap1 = (Bitmap) data.getExtras().get("data");
                    Id_card.setImageBitmap(bitmap1);
                    break;
                }
            case 2:
                if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
                    bitmap2 = (Bitmap) data.getExtras().get("data");
                    Photo.setImageBitmap(bitmap2);
                }
                break;

            case 3:
              if  (requestCode == 3 && resultCode== RESULT_OK && data != null) {
                bitmap3 = (Bitmap)data.getExtras().get("data");
                Form.setImageBitmap(bitmap3);
            }
        }








    }

//        public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
//            int width = image.getWidth();
//            int height = image.getHeight();
//
//            float bitmapRatio = (float) width / (float) height;
//            if (bitmapRatio > 1) {
//                width = maxSize;
//                height = (int) (width / bitmapRatio);
//            } else {
//                height = maxSize;
//                width = (int) (height * bitmapRatio);
//            }
//            return Bitmap.createScaledBitmap(image, width, height, true);
//        }


    public String getStringImage1(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public String getStringImage2(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public String getStringImage3(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void editImage(){

        final String Url = "https://easyforms.in/api/data/update/"+id;

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    String message = obj.getString("message");
                    Toast.makeText(Edit_data.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(Edit_data.this, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Edit_data.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                if (bitmap1 == null) {
                    uploadImage1 = "";
                } else {
                    uploadImage1 = getStringImage1(bitmap1);
                }

                if (bitmap2 == null) {
                    uploadImage2 = "";
                } else {
                    uploadImage2 = getStringImage2(bitmap2);
                }

                if (bitmap3== null) {
                    uploadImage3 = "";
                } else {
                    uploadImage3 = getStringImage3(bitmap3);
                }

                Map<String,String> params = new HashMap<String, String>();
//                params.put("sl_no",serial_no);
                params.put("id_card_photo", uploadImage1);
                params.put("image", uploadImage2);
                params.put("form_photo", uploadImage3);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String auth = "Bearer "+ SharedPreference.getInstance(getApplicationContext()).getToken().getToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}