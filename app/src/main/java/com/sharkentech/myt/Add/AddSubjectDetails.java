package com.sharkentech.myt.Add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sharkentech.myt.R;

import java.util.Date;

public class AddSubjectDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private Button deleteButton;
    private EditText topicNameEditText,topicDetailsEditText;

    private SubjectDetailsNote selectedSubjectDetailNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject_details);

        setupUIViews();
        initToolbar();
        checkForEditNote();
    }

    private void setupUIViews(){
        toolbar = (Toolbar)findViewById(R.id.AddSubjectDetails);
        topicNameEditText = findViewById(R.id.topicNameEditText);
        topicDetailsEditText = findViewById(R.id.topicDetailsEditText);
        deleteButton = findViewById(R.id.deleteSubjectDetailsButton);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add To Topics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void checkForEditNote(){
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(SubjectDetailsNote.NOTE_EDIT_EXTRA,-1);
        selectedSubjectDetailNote = SubjectDetailsNote.getNoteForID(passedNoteID);
        if(selectedSubjectDetailNote != null){
            topicNameEditText.setText(selectedSubjectDetailNote.getTopicName());
            topicDetailsEditText.setText(selectedSubjectDetailNote.getTopicDetails());
        }else{
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }
    public void saveTopic(View view){
        SubjectDetailsSQLiteManager sqLiteManager = SubjectDetailsSQLiteManager.instanceOfDatabase(this);
        String topicName = String.valueOf(topicNameEditText.getText());
        String topicDetails = String.valueOf(topicDetailsEditText.getText());
        int realId;
        //int realId = getIntent().getIntExtra("SubjectRealId",-1);
        if(selectedSubjectDetailNote == null){
            realId = getIntent().getIntExtra("SubjectRealId",-1) ;
        }else{
            realId = selectedSubjectDetailNote.getRealId();
        }

        if(selectedSubjectDetailNote == null){
            int id = SubjectDetailsNote.subjectDetailsNoteArrayList.size();
            SubjectDetailsNote newNote = new SubjectDetailsNote(id,topicName,topicDetails,realId);
            SubjectDetailsNote.subjectDetailsNoteArrayList.add(newNote);
            sqLiteManager.addSubjectDetailsNoteToDatabase(newNote);
        }else{
            selectedSubjectDetailNote.setTopicName(topicName);
            selectedSubjectDetailNote.setTopicDetails(topicDetails);
            selectedSubjectDetailNote.setRealId(realId);
            sqLiteManager.updateSubjectDetailsNoteListArray(selectedSubjectDetailNote);
        }

        finish();
    }
    public void deleteTopic(View view){
        selectedSubjectDetailNote.setDeleted(new Date());
        SubjectDetailsSQLiteManager sqLiteManager = SubjectDetailsSQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateSubjectDetailsNoteListArray(selectedSubjectDetailNote);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//for going back to home page from physical button
        switch (item.getItemId()){
            case android.R.id.home :{
                SubjectDetailsNote.subjectDetailsNoteArrayList.clear();
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}