package com.example.theftscanner;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;


public class Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference MyReference;
    DatabaseReference mDatabaseRef;
    private StorageReference mStorageReference;

    Spinner mSpinner;
    ImageView mPreviewImage;
    Button mSendButton;
    TextView mChooseImage;
    EditText mOwner, mBrand, mModel, mStreet, mCity;
    String Owner, Type, Brand, Model, Street, City, ImageURL, ID;
    double Latitude, Longitude;
    Theft theft;
    long NumberOfChilds = 0;



    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        MyReference = FirebaseDatabase.getInstance().getReference().child("Thefts");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("uploads");
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");

        mSpinner = findViewById(R.id.spinner_types);
        mSendButton = findViewById(R.id.send_button);
        mChooseImage = findViewById(R.id.choose_image);
        mPreviewImage = findViewById(R.id.image_preview);
        mOwner = findViewById(R.id.owner);
        mBrand = findViewById(R.id.brand);
        mModel = findViewById(R.id.model);
        mStreet = findViewById(R.id.street);
        mCity = findViewById(R.id.city);

        if (mSpinner != null) {
            mSpinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this, R.array.vehicle_array, R.layout.spinner_layout);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        if (mSpinner != null) {
            mSpinner.setAdapter(myAdapter);
        }


        MyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    NumberOfChilds = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void ConvertToCoordinates(String InputStreet) {

        String location = InputStreet;
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            Latitude = address.getLatitude();
            Longitude = address.getLongitude();
        }
    }

    public void OpenFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            mChooseImage.setText("IMAGE PICKED");
            mPreviewImage.setImageURI(mImageUri);
        }
    }


    public void SendToDatabase(View view) {

        uploadFile();

    }



    public void MYFUCNTION() {

        mCity.onEditorAction(EditorInfo.IME_ACTION_DONE);
        Owner = mOwner.getText().toString();
        Brand = mBrand.getText().toString();
        Model = mModel.getText().toString();
        Street = mStreet.getText().toString();
        City = mCity.getText().toString();



        if (Owner.isEmpty() || Type.isEmpty() || Brand.isEmpty() || Model.isEmpty() || Street.isEmpty() || City.isEmpty()) {
            Toast.makeText(Form.this, R.string.message_empty_fields, Toast.LENGTH_LONG).show();
        } else {

            AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(Form.this);
            myAlertBuilder.setTitle("Confirm");
            myAlertBuilder.setMessage("Are u sure you want to send?");

            myAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    ConvertToCoordinates(Street + City);
                    ID = String.valueOf(NumberOfChilds + 1);
                    theft = new Theft(Owner, Type, Brand, Model, Street, City,ImageURL, ID, Latitude, Longitude);
                    MyReference.child(ID).setValue(theft);
                    Intent intent = new Intent(Form.this, Dashboard.class);
                    startActivity(intent);
                    Toast.makeText(Form.this, R.string.message_successfully, Toast.LENGTH_LONG).show();
                }
            });

            myAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            myAlertBuilder.show();

        }


    }









    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            UploadTask uploadTask = fileReference.putFile(mImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri download = task.getResult();
                        ImageURL = download.toString();
                    } else {
                        ImageURL = "noImage";
                    }
                    MYFUCNTION();
                }
            });
        }

    }

}
