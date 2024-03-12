package com.sharkentech.myt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharkentech.myt.Add.AddSubjectDetails;
import com.sharkentech.myt.Add.SubjectDetailsNote;
import com.sharkentech.myt.Add.SubjectDetailsSQLiteManager;
import com.sharkentech.myt.Add.SubjectNote;
import com.sharkentech.myt.Add.SubjectSQLiteManager;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SubjectDetails extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ListView listView;
    private EditText subjectNameEditText;
    //private SharedPreferences subjectNamePreferences;
    private SubjectNote selectedSubjectNote;
    SharedPreferences subjectPreferences;
    public static final String SHARED_PREF_NAME = "MY_SUBJECT_ID";
    public static final String SUB_ID = "realId";
    public static final String SUB_NAME = "subjectName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        SubjectDetailsNote.subjectDetailsNoteArrayList.clear();
        setupUIViews();
        setupBottomAppBar();
        initToolbar();
        checkForEditNote();
        setSubjectDetailsNoteAdapter();
        loadFromDBToMemory();
        setOnCLickListener();
    }
    private void loadFromDBToMemory(){
        SubjectDetailsSQLiteManager subjectDetailsSQLiteManager = SubjectDetailsSQLiteManager.instanceOfDatabase(this);
        subjectDetailsSQLiteManager.populateSubjectDetailsNoteListArray(selectedSubjectNote);
    }
    private void setupUIViews(){
        toolbar = (Toolbar)findViewById(R.id.ToolbarSubjectDetails);
        listView = (ListView)findViewById(R.id.lvSubjectDetails);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.subjects);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        subjectPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        subjectNameEditText = findViewById(R.id.subjectNameEditText);
        subjectNameEditText.setText(getIntent().getExtras().getString("SubjectName"));
    }
    private void setSubjectDetailsNoteAdapter(){
        Intent intent = getIntent();
        SubjectDetailsNoteAdapter subjectDetailsNoteAdapter = new SubjectDetailsNoteAdapter(getApplicationContext(),SubjectDetailsNote.nonDeletedSubjectDetailsNotes(intent));
        listView.setAdapter(subjectDetailsNoteAdapter);
    }
    public void newSubjectDetailsNote(View view){
        Intent newNodeIntent = new Intent(SubjectDetails.this, AddSubjectDetails.class);
        newNodeIntent.putExtra(SubjectDetailsNote.NOTE_EDIT_EXTRA,selectedSubjectNote.getId());
        newNodeIntent.putExtra("SubjectRealId",selectedSubjectNote.getRealId());
        startActivity(newNodeIntent);
    }
    private void setOnCLickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SubjectDetailsNote selectedSubjectDetailsNote = (SubjectDetailsNote) listView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), AddSubjectDetails.class);
                editNoteIntent.putExtra(SubjectDetailsNote.NOTE_EDIT_EXTRA,selectedSubjectDetailsNote.getId());
                editNoteIntent.putExtra("SubjectRealId",selectedSubjectDetailsNote.getRealId());
                startActivity(editNoteIntent);
            }
        });
    }

    private class SubjectDetailsNoteAdapter extends ArrayAdapter<SubjectDetailsNote> {

        private SubjectDetailsNoteAdapter(Context context, List<SubjectDetailsNote> subjectDetailsNotes){
            super(context,0,subjectDetailsNotes);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            SubjectDetailsNote subjectDetailsNote = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_details_single_item,parent,false);
            }
            TextView topicName = convertView.findViewById(R.id.tvSubjectTitle);
            TextView topicDetails = convertView.findViewById(R.id.tvSyllabus);

            topicName.setText(subjectDetailsNote.getTopicName());
            topicDetails.setText(subjectDetailsNote.getTopicDetails());

            return convertView;
        }
    }
    private void checkForEditNote(){
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra("SubjectRealId",-1);
        selectedSubjectNote = SubjectNote.getNoteForRealID(passedNoteID);
        if(selectedSubjectNote != null){
            System.out.println(selectedSubjectNote.getRealId()+ " is passed note id");
            if(selectedSubjectNote.getRealId() == -1) {
                selectedSubjectNote.setRealId(getIntent().getIntExtra("SubjectRealId", -1));
            }
        }

        if(selectedSubjectNote == null){
            Button deleteButton = findViewById(R.id.deleteSubjectButton);
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }
    public void saveSubject(View view){
        SubjectSQLiteManager sqLiteManager = SubjectSQLiteManager.instanceOfDatabase(this);
        int id = SubjectNote.subjectNoteArrayList.size();
        int realId = getIntent().getIntExtra("SubjectRealId",-1);

        if(selectedSubjectNote == null){
            String temp = "Add Subject Name";
            if(subjectNameEditText.getText().toString().equals("") ||subjectNameEditText.getText() == null){
                subjectNameEditText.setText(temp);
            }
            SubjectNote newNote = new SubjectNote(id,subjectNameEditText.getText().toString(),getIntent().getIntExtra("SubjectRealId",-1));
            SubjectNote.subjectNoteArrayList.add(newNote);
            sqLiteManager.addSubjectNoteToDatabase(newNote);
        }else{
            selectedSubjectNote.setSubjectName(subjectNameEditText.getText().toString());
            selectedSubjectNote.setRealId(realId);
            sqLiteManager.updateSubjectNoteListArray(selectedSubjectNote);
        }
        subjectPreferences.edit().putString(SUB_NAME,subjectNameEditText.getText().toString()).apply();
        subjectPreferences.edit().putInt(SUB_ID,getIntent().getIntExtra("SubjectRealId",-1)).apply();
        finish();
    }
    public void deleteSubject(View view){
        SubjectSQLiteManager sqLiteManager = SubjectSQLiteManager.instanceOfDatabase(this);
        if(selectedSubjectNote == null){
            int id = SubjectNote.subjectNoteArrayList.size();
            String temp = "Add Subject Name";
            if(subjectNameEditText.getText().toString().equals("") ||subjectNameEditText.getText() == null){
                subjectNameEditText.setText(temp);
            }
            SubjectNote newNote = new SubjectNote(id,subjectNameEditText.getText().toString(),selectedSubjectNote.getRealId(),new Date());
            SubjectNote.subjectNoteArrayList.add(newNote);
            sqLiteManager.addSubjectNoteToDatabase(newNote);
        }
        selectedSubjectNote.setDeleted(new Date());
        //SubjectNote.subjectNoteArrayList.clear();
        sqLiteManager.updateSubjectNoteListArray(selectedSubjectNote);

        finish();
    }
    private void setupBottomAppBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                subjectPreferences.edit().putString(SUB_NAME,subjectNameEditText.getText().toString()).apply();
                subjectPreferences.edit().putInt(SUB_ID,getIntent().getIntExtra("SubjectRealId",-1)).apply();
                SubjectNote.subjectNoteArrayList.clear();
                switch (item.getItemId()) {
                    case R.id.timetable: {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.subjects: {
                        finish();
                        return true;
                    }
                    case R.id.faculty: {
                        Intent intent = new Intent(getApplicationContext(), FacultyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.foodTracker: {
                        Intent intent = new Intent(getApplicationContext(), TrackFoodActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        //bottomNavigationView.setSelectedItemId(R.id.subjects);
        setSubjectDetailsNoteAdapter();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//for going back to home page from physical button
        switch (item.getItemId()){
            case android.R.id.home :{
                subjectPreferences.edit().putString(SUB_NAME,subjectNameEditText.getText().toString()).apply();
                subjectPreferences.edit().putInt(SUB_ID,getIntent().getIntExtra("SubjectRealId",-1)).apply();
                SubjectNote.subjectNoteArrayList.clear();
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}