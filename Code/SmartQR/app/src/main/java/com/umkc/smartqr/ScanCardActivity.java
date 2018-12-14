package com.umkc.smartqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.ArrayList;
import java.util.List;


public class ScanCardActivity extends AppCompatActivity {
    private Button btnOpenCam;
    private Button btnAnalyze;
    private ImageView imageView;
    private TextView txtView;
    private Bitmap imageBitmap;
    String email="";
    String id="";
    public DatabaseReference userd;
    List<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card);
        btnOpenCam = findViewById(R.id.btnOpenCam);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        imageView = findViewById(R.id.imgView);
        txtView = findViewById(R.id.txtView);
        email=getIntent().getStringExtra("email");
        id=getIntent().getStringExtra("id");
        Query query = FirebaseDatabase.getInstance().getReference(id);
        query.addListenerForSingleValueEvent(valueEventListener);
        userd = FirebaseDatabase.getInstance().getReference(id);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                contacts = (ArrayList<String>) dataSnapshot.child("contacts").getValue();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    static final int CAMERA_REQ = 1;

    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQ && resultCode == RESULT_OK) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void analyzePic(View view) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void processTxt(FirebaseVisionText text) {
        List<FirebaseVisionText.Block> blocks = text.getBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(ScanCardActivity.this, "Unable to detect the image", Toast.LENGTH_LONG).show();
            return;
        }
        for (FirebaseVisionText.Block block : text.getBlocks()) {
            String txt = block.getText();
            txtView.setTextSize(24);
            txtView.setText(txt);
            save(txt);
        }
    }

    public void save(String result) {
        if(contacts == null){
            contacts = new ArrayList<>();
        }
        contacts.add(result);
        userd.child("contacts").setValue(contacts);
    }

    public void home(View view) {
        Intent redirect = new Intent(ScanCardActivity.this,HomeActivity.class).putExtra("email",email).putExtra("id",id);
        startActivity(redirect);
    }
}
