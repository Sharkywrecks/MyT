package com.sharkentech.myt;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.sharkentech.myt.Add.DayDetailNote;
import com.sharkentech.myt.Add.FacultyNote;
import com.sharkentech.myt.Add.FacultySQLiteManager;
import com.sharkentech.myt.Add.SQLiteManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFacultyActivity extends AppCompatActivity {

    private CircleImageView facultyImage;
    ActivityResultLauncher<String> mGetContent;

    private Toolbar toolbar;
    private EditText facultyNameEditText, phoneNumberEditText, emailEditText,placeEditText;
    private Button deleteButton;
    private ImageButton btBrowse;

    private FacultyNote selectedFacultyNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        setupUIViews();
        initToolbar();
        checkForEditNote();

        btBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(AddFacultyActivity.this,CropperActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent,101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1 && requestCode == 101){
            String result = null;
            if (data != null) {
                result = data.getStringExtra("RESULT");
            }else{
                Toast.makeText(getApplicationContext(),"Image could not be added",Toast.LENGTH_SHORT).show();
            }
            Uri resultUri = null;
            try {
                //resultUri = data.getData();
                resultUri = Uri.parse(result);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),resultUri);
                /*ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);*/
                //byte[] img =FacultyNote.convertToBytes(bitmap);// byteArray.toByteArray();
                facultyImage.setImageBitmap(bitmap);
                facultyImage.setTag(bitmap);
            } catch (IOException e) {
                //e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Image could not be added",Toast.LENGTH_SHORT).show();
            }
            /*if(result!=null){
                resultUri = Uri.parse(result);
            }
            facultyImage.setImageURI(resultUri);
            facultyImage.setTag(getRealPathFromURI(resultUri));*/
        }
    }
    private String getRealPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA} ;
        Cursor cursor = managedQuery(contentUri,proj,null,null,null);
        if(cursor == null){
            return contentUri.getPath();
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void setupUIViews(){
        toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.ToolbarFacultyDetails);
        facultyImage = (CircleImageView)findViewById(R.id.ivFaculty);
        facultyNameEditText = (EditText) findViewById(R.id.facultyName);
        phoneNumberEditText = (EditText) findViewById(R.id.evPhoneNumber);
        emailEditText = (EditText) findViewById(R.id.evEmail);
        placeEditText = (EditText) findViewById(R.id.evPlace);
        deleteButton = findViewById(R.id.deleteFacultyButton);
        btBrowse = findViewById(R.id.bt_browse);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Faculty Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void checkForEditNote(){
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(FacultyNote.NOTE_EDIT_EXTRA,-1);
        selectedFacultyNote = FacultyNote.getNoteForID(passedNoteID);
        if(selectedFacultyNote != null){
            facultyNameEditText.setText(selectedFacultyNote.getFacultyName());
            phoneNumberEditText.setText(selectedFacultyNote.getFacultyPhoneNumber());
            emailEditText.setText(selectedFacultyNote.getFacultyEmail());
            placeEditText.setText(selectedFacultyNote.getFacultyLocation());

            /*facultyImage.setImageURI(Uri.parse(selectedFacultyNote.getFacultyImage()));
            facultyImage.setTag(selectedFacultyNote.getFacultyImage());*/
            facultyImage.setImageBitmap(FacultyNote.convertToBitmap(selectedFacultyNote.getFacultyImage()));
            facultyImage.setTag(FacultyNote.convertToBitmap(selectedFacultyNote.getFacultyImage()));
        }else{
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }
    public void saveFaculty(View view){
        FacultySQLiteManager sqLiteManager = FacultySQLiteManager.instanceOfDatabase(this);
        String facultyName = String.valueOf(facultyNameEditText.getText());
        String phoneNumber = String.valueOf(phoneNumberEditText.getText());
        String email = String.valueOf(emailEditText.getText()) ;
        String place = String.valueOf(placeEditText.getText());
        byte[] image = FacultyNote.convertToBytes((Bitmap) facultyImage.getTag());

        if(selectedFacultyNote == null){
            int id = FacultyNote.facultyNoteArrayList.size();
            FacultyNote newNote = new FacultyNote(id,facultyName,phoneNumber,email,place,image);
            FacultyNote.facultyNoteArrayList.add(newNote);
            sqLiteManager.addFacultyNoteToDatabase(newNote);
        }else{
            selectedFacultyNote.setFacultyName(facultyName);
            selectedFacultyNote.setFacultyPhoneNumber(phoneNumber);
            selectedFacultyNote.setFacultyEmail(email);
            selectedFacultyNote.setFacultyLocation(place);
            selectedFacultyNote.setFacultyImage(image);
            sqLiteManager.updateFacultyNoteListArray(selectedFacultyNote);
        }
        finish();
    }
    public void deleteFaculty(View view){
        selectedFacultyNote.setDeleted(new Date());
        FacultySQLiteManager sqLiteManager = FacultySQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateFacultyNoteListArray(selectedFacultyNote);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//for going back to home page from physical button
        switch (item.getItemId()){
            case android.R.id.home :{
                DayDetailNote.dayDetailNoteArrayList.clear();
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}