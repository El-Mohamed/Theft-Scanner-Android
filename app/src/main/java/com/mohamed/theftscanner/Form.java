package com.mohamed.theftscanner;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;


public class Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    DatabaseReference mDatabaseReference;
    StorageReference mStorageReference;
    Spinner mSpinner;
    ImageView mPreviewImage;
    Button mSendButton;
    TextView mChooseImage;
    EditText mOwner, mBrand, mModel, mStreet, mCity;
    Uri mImageUri;
    String owner, type, brand, model, street, city, imageURL;
    double latitude = 0;
    double longitude = 0;
    Theft tempTheft;
    Boolean inputValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Thefts");
        mStorageReference = FirebaseStorage.getInstance().getReference("Images");

        mSpinner = findViewById(R.id.spinner_types);
        mSendButton = findViewById(R.id.send_button);
        mChooseImage = findViewById(R.id.choose_image);
        mPreviewImage = findViewById(R.id.image_preview);
        mOwner = findViewById(R.id.owner);
        mBrand = findViewById(R.id.brand);
        mModel = findViewById(R.id.model);
        mStreet = findViewById(R.id.street);
        mCity = findViewById(R.id.city);

        setSpinner();

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInput();
                if (inputValid) {
                    validateLocation();
                }
            }
        });

    }

    //Spinner

    private void setSpinner() {
        if (mSpinner != null) {
            mSpinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this, R.array.vehicle_array, R.layout.spinner_layout);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        if (mSpinner != null) {
            mSpinner.setAdapter(myAdapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Image

    private void chooseImage() {
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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    // Form

    private void checkUserInput() {
        mCity.onEditorAction(EditorInfo.IME_ACTION_DONE);
        inputValid = false;

        owner = mOwner.getText().toString();
        brand = mBrand.getText().toString();
        model = mModel.getText().toString();
        street = mStreet.getText().toString();
        city = mCity.getText().toString();

        if (owner.isEmpty() || type.isEmpty() || brand.isEmpty() || model.isEmpty() || street.isEmpty() || city.isEmpty()) {
            Toast.makeText(Form.this, R.string.message_empty_fields, Toast.LENGTH_LONG).show();
        } else if (mImageUri == null) {
            Toast.makeText(Form.this, R.string.message_no_image, Toast.LENGTH_LONG).show();
        } else {
            inputValid = true;
        }

    }

    private void validateLocation() {

        String location = street + city;
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this);

        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList.size() != 0) {
            Address address = addressList.get(0);
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            showAlert();
        } else {
            Toast.makeText(Form.this, R.string.message_invalid_street, Toast.LENGTH_LONG).show();
        }

    }

    private void showAlert() {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(Form.this);
        myAlertBuilder.setTitle("Confirm");
        myAlertBuilder.setMessage("Are u sure you want to send?");

        myAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                uploadImage();

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

    // Database


    private void uploadEntry() {

        tempTheft = new Theft(owner, type, brand, model, street, city, imageURL, latitude, longitude);
        mDatabaseReference.push().setValue(tempTheft);
    }

    private void uploadImage() {
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
                        imageURL = download.toString();
                    }
                    uploadEntry();
                }
            });
        }

    }

}
