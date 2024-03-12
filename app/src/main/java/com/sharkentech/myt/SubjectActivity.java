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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharkentech.myt.Add.SubjectNote;
import com.sharkentech.myt.Add.SubjectSQLiteManager;
import com.sharkentech.myt.Utils.LetterImageView;

import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private static int realId = 100;
    private Toolbar toolbar;
    private ListView listView;
    private int id = 0;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        SubjectNote.subjectNoteArrayList.clear();
        setupUIViews();
        initToolbar();
        setupBottomAppBar();
        setSubjectNoteAdapter();
        loadFromDBToMemory();
        setOnCLickListener();
    }
    private void setupUIViews(){
        //toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.ToolbarSubject);
        listView = (ListView)findViewById(R.id.lvSubject);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.subjects);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
    }
    private void initToolbar(){
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }
    private void setSubjectNoteAdapter(){
        SubjectActivity.SubjectNoteAdapter subjectNoteAdapter = new SubjectActivity.SubjectNoteAdapter(getApplicationContext(),SubjectNote.nonDeletedSubjectNotes());
        //SubjectNote.subjectNoteArrayList = SubjectNote.nonDeletedSubjectNotes();
        listView.setAdapter(subjectNoteAdapter);
    }
    private class SubjectNoteAdapter extends ArrayAdapter<SubjectNote> {
        private SubjectNoteAdapter(Context context, List<SubjectNote> subjectNotes){
            super(context,0,subjectNotes);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            SubjectNote subjectNote = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_single_item,parent,false);
            }
            TextView tvSubject = convertView.findViewById(R.id.tvSubject);
            LetterImageView ivLogo = (LetterImageView) convertView.findViewById(R.id.ivLetterSubject);

            tvSubject.setText(subjectNote.getSubjectName());

            SharedPreferences sharedPreferences = getSharedPreferences(SubjectDetails.SHARED_PREF_NAME,MODE_PRIVATE);
            String subjectName = sharedPreferences.getString(SubjectDetails.SUB_NAME,null);
            int backRealId = sharedPreferences.getInt(SubjectDetails.SUB_ID,-1);
            if(backRealId == subjectNote.getRealId() && !subjectName.equals("") &&backRealId!=-1) {
                subjectNote.setSubjectName(subjectName);
                tvSubject.setText(subjectNote.getSubjectName());
            }
            if(realId<=subjectNote.getRealId()){
                realId = subjectNote.getRealId() + 1;
            }
            if(id<=subjectNote.getId()){
                id+=1;
            }
            ivLogo.setOval(true);
            if(!tvSubject.getText().toString().equals("")){
                ivLogo.setLetter(tvSubject.getText().toString().charAt(0));
            }

            return convertView;
        }
    }
   private void loadFromDBToMemory(){
        SubjectSQLiteManager subjectSQLiteManager = SubjectSQLiteManager.instanceOfDatabase(this);
        subjectSQLiteManager.populateSubjectNoteListArray();
    }
    private void setupBottomAppBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timetable: {
                        SubjectNote.subjectNoteArrayList.clear();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.subjects: {
                        /*Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);*/
                        return true;
                    }
                    case R.id.faculty: {
                        SubjectNote.subjectNoteArrayList.clear();
                        Intent intent = new Intent(getApplicationContext(), FacultyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.foodTracker: {
                        SubjectNote.subjectNoteArrayList.clear();
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
    private void setOnCLickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SubjectNote selectedSubjectNote = (SubjectNote) listView.getItemAtPosition(position);

                Intent editNoteIntent = new Intent(getApplicationContext(), SubjectDetails.class);
                editNoteIntent.putExtra("SubjectRealId",selectedSubjectNote.getRealId());
                editNoteIntent.putExtra("SubjectName",selectedSubjectNote.getSubjectName());
                startActivity(editNoteIntent);
            }
        });
    }
    public void createSubject(View view) {
        SubjectSQLiteManager sqLiteManager = SubjectSQLiteManager.instanceOfDatabase(this);
        //int id = SubjectNote.subjectNoteArrayList.size();
        SubjectNote newNote = new SubjectNote(id, null,SubjectActivity.realId);
        SubjectNote.subjectNoteArrayList.add(newNote);
        sqLiteManager.addSubjectNoteToDatabase(newNote);
        Intent newNodeIntent = new Intent(SubjectActivity.this, SubjectDetails.class);
        newNodeIntent.putExtra("SubjectRealId",newNote.getRealId());
        SubjectActivity.realId = SubjectActivity.realId + 1;
        startActivity(newNodeIntent);
    }
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.subjects);
        setSubjectNoteAdapter();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//for going back to home page from physical button
        switch (item.getItemId()){
            case android.R.id.home :{
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