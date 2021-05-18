package easyforms.mollosradix.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class Add_data extends AppCompatActivity {

    private ImageView Id_card = null, Photo = null, Form = null;
    private Button Submit;
    private EditText sl_no;

    static int ACTION_IMAGE_CAPTURE1 = 1;
    private Bitmap bitmap1 = null;
    private final Bitmap id_card_photo = null;

    static int ACTION_IMAGE_CAPTURE2 = 2;
    private Bitmap bitmap2 = null;
    private final Bitmap photo = null;

    static int ACTION_IMAGE_CAPTURE3 = 3;
    private Bitmap bitmap3 = null;
    private final Bitmap form_photo = null;

    String uploadImage1 = "";
    String uploadImage2 = "";
    String uploadImage3 = "";

    public final String APP_TAG = "MyCustomApp";
    public String id_cardFileName = "id_card.jpg";
    public String photoFileName = "photo.jpg";
    public String formFileName = "form.jpg";

    File id_card_image,photo_image,form_image;

     final String Url = "https://easyforms.in/api/data/store";

    String serial_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);



        Id_card = findViewById(R.id.id_card);
        Photo = findViewById(R.id.photo);
        Form = findViewById(R.id.form);

        Submit = findViewById(R.id.Upload);
        sl_no = findViewById(R.id.sl_no);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serial_no = sl_no.getText().toString();

                if (TextUtils.isEmpty(serial_no)) {
                    sl_no.setError("Please enter sl_no");
                    sl_no.requestFocus();
                }else{
                    uploadImage();
                }

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
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                        file1 = new File (dir,(System.currentTimeMillis()) + ".jpeg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file1));
//                        mCurrentPhotoPath = file1.getAbsolutePath();
//
//                        if (intent.resolveActivity(getPackageManager()) != null) {
//                            // Start the image capture intent to take photo
//                            startActivityForResult(intent, ACTION_IMAGE_CAPTURE1);
//                        }

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        id_card_image = getPhotoFileUri(id_cardFileName);
                        Uri fileProvider = FileProvider.getUriForFile(Add_data.this, BuildConfig.APPLICATION_ID + ".provider", id_card_image);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, ACTION_IMAGE_CAPTURE1);
                        }


                    }

                    private File getPhotoFileUri(String id_cardFileName) {

                        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
                        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                            Log.d(APP_TAG, "failed to create directory");
                        }
                        return new File(mediaStorageDir.getPath() + File.separator + id_cardFileName);
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
                        photo_image = getPhotoFileUri(photoFileName);
                        Uri fileProvider = FileProvider.getUriForFile(Add_data.this, BuildConfig.APPLICATION_ID + ".provider", photo_image);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, ACTION_IMAGE_CAPTURE2);
                        }

                    }

                    private File getPhotoFileUri(String photoFileName) {

                        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
                        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                            Log.d(APP_TAG, "failed to create directory");
                        }
                        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
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
                        form_image = getPhotoFileUri(formFileName);
                        Uri fileProvider = FileProvider.getUriForFile(Add_data.this, BuildConfig.APPLICATION_ID + ".provider", form_image);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, ACTION_IMAGE_CAPTURE3);
                        }

                    }

                    private File getPhotoFileUri(String formFileName) {

                        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
                        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                            Log.d(APP_TAG, "failed to create directory");
                        }
                        return new File(mediaStorageDir.getPath() + File.separator + formFileName);
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

                if (requestCode == ACTION_IMAGE_CAPTURE1 && resultCode== RESULT_OK /*&& data != null*/) {
                    bitmap1 = BitmapFactory.decodeFile(id_card_image.getAbsolutePath());
                    Id_card.setImageBitmap(bitmap1);
                }


            else if (requestCode == ACTION_IMAGE_CAPTURE2 && resultCode== RESULT_OK ) {

                bitmap2 = BitmapFactory.decodeFile(photo_image.getAbsolutePath());
                Photo.setImageBitmap(bitmap2);
            }

            else if (requestCode == ACTION_IMAGE_CAPTURE3 && resultCode== RESULT_OK) {

                bitmap3 = BitmapFactory.decodeFile(form_image.getAbsolutePath());
                Form.setImageBitmap(bitmap3);
            }



        }

//    private void setPic() {
//
//        // Get the dimensions of the View
//        int targetW = mImageView.getWidth();
//        int targetH = mImageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        bitmap1 = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        mImageView.setImageBitmap(bitmap1);
//    }


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

    private void uploadImage(){


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
                        Toast.makeText(Add_data.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject errors = obj.getJSONObject("errors");
                        String error = errors.getString("sl_no");
                        Toast.makeText(Add_data.this, error, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Add_data.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

                    if (bitmap3 == null) {
                        uploadImage3 = "";
                    } else {
                        uploadImage3 = getStringImage3(bitmap3);
                    }

                    Map<String,String> params = new HashMap<String, String>();
                    params.put("sl_no",serial_no);
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