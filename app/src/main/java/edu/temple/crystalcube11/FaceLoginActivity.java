package edu.temple.crystalcube11;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongear on 4/24/17.
 */

public class FaceLoginActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button faceCapture;
    private String TAG2 = "JSONpost";
    private EditText firstName;
    private EditText lastName;

    private String firstname;
    private String lastname;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_login);

        Bundle extra = getIntent().getExtras();
        firstname = extra.getString("firstname");
        lastname = extra.getString("lastname");
        uid = extra.getString("uid");

        // front camera face capture ==============================================================
        faceCapture = (Button) findViewById(R.id.face_camera_btn);

        firstName = (EditText) findViewById(R.id.firstname_edit_text);
        lastName = (EditText) findViewById(R.id.lastname_edit_text);
        firstName.setText(firstname);
        lastName.setText(lastname);

        faceCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check camera permission at run-time
                if(ContextCompat.checkSelfPermission(FaceLoginActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    // request permission
                    ActivityCompat.requestPermissions(FaceLoginActivity.this,
                            new String[] {android.Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    // check first and last name edit text is fill
                    if (!firstName.toString().isEmpty() && !lastName.toString().isEmpty()) {
                        dispatchTakePictureIntent();
                    } else {
                        Toast.makeText(FaceLoginActivity.this, "Error: first and last required for face recognition login.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    private void dispatchTakePictureIntent() {
        // method to create intent for image capture
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // if permission is granted
            if (!firstName.toString().isEmpty() && !lastName.toString().isEmpty()) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(FaceLoginActivity.this, "Error: first and last required for face recognition login.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // face capture ==========================
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo_bitmap = (Bitmap) extras.get("data");

            String photoString = getStringFromBitmap(photo_bitmap);

            // send photo string to method to be post to server
            sendJson(photoString);
            finish();

        }
    }


    protected void sendJson(final String photo) {
        // Instantiate the RequestQueue.
        final Context context = getApplicationContext();
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://34.206.165.219/createUser";

        HashMap<String, String> postParam = new HashMap<String, String>();
        postParam.put("file", photo);
        postParam.put("fileName", "face.png");
        postParam.put("firstName", firstName.getText().toString());
        postParam.put("lastName", lastName.getText().toString());
        // todo change hardcoded crystal cube id
        postParam.put("machineId", "crystal_chan_6");
        postParam.put("gmail", uid);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // your response
                        Log.d(TAG2, response.toString());
                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d(TAG2, error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("file", photo);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
    /*
    * This functions converts Bitmap picture to a string which can be
    * JSONified.
    * */
        final int COMPRESSION_QUALITY = 0;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);

        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
}
