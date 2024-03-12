package com.sharkentech.myt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.sharkentech.myt.Add.FacultyNote;
import com.sharkentech.myt.Add.FacultySQLiteManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        FacultyNote.facultyNoteArrayList.clear();
        setupUIViews();
        initToolbar();
        setupBottomAppBar();
        setFacultyNoteAdapter();
        loadFromDBToMemory();
        setOnCLickListener();
    }
    private void loadFromDBToMemory(){
        FacultySQLiteManager facultySQLiteManager = FacultySQLiteManager.instanceOfDatabase(this);
        facultySQLiteManager.populateFacultyNoteListArray();
        //System.out.println("loading database into memory");
    }
    private void setupBottomAppBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timetable: {
                        FacultyNote.facultyNoteArrayList.clear();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.subjects: {
                        FacultyNote.facultyNoteArrayList.clear();
                        Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.faculty: {
                        /*Intent intent = new Intent(getApplicationContext(), FacultyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);*/
                        return true;
                    }
                    case R.id.foodTracker: {
                        FacultyNote.facultyNoteArrayList.clear();
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
    private void setupUIViews(){
        //toolbar = (Toolbar)findViewById(R.id.ToolbarFaculty);
        listView = (ListView)findViewById(R.id.lvFaculty);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.faculty);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
    }
    private void initToolbar(){
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Faculty");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }
    private void setFacultyNoteAdapter(){
        FacultyActivity.FacultyNoteAdapter facultyNoteAdapter = new FacultyActivity.FacultyNoteAdapter(getApplicationContext(), FacultyNote.nonDeletedFacultyNotes());
        listView.setAdapter(facultyNoteAdapter);
    }
    public void newFacultyNote(View view){
        Intent newNodeIntent = new Intent(FacultyActivity.this, AddFacultyActivity.class);
        startActivity(newNodeIntent);
    }
    private void setOnCLickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FacultyNote selectedFacultyNote = (FacultyNote) listView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), AddFacultyActivity.class);
                editNoteIntent.putExtra(FacultyNote.NOTE_EDIT_EXTRA,selectedFacultyNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.faculty);
        setFacultyNoteAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//for going back to home page from physical button
        switch (item.getItemId()){
            case android.R.id.home :{
                FacultyNote.facultyNoteArrayList.clear();
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private class FacultyNoteAdapter extends ArrayAdapter<FacultyNote> {

        private FacultyNoteAdapter(Context context, List<FacultyNote> facultyNote){
            super(context,0,facultyNote);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            FacultyNote facultyNote = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.faculty_single_item,parent,false);
            }
            TextView facultyName = convertView.findViewById(R.id.tvFacultyName);
            //TextView expiryDate = convertView.findViewById(R.id.tv_expiryDate);

            CircleImageView imageFaculty = (CircleImageView) convertView.findViewById(R.id.ivImageFaculty);
            //imageFaculty.setImageURI(Uri.parse(facultyNote.getFacultyImage()));
            imageFaculty.setImageBitmap(FacultyNote.convertToBitmap(facultyNote.getFacultyImage()));

            facultyName.setText(facultyNote.getFacultyName());
            //expiryDate.setText(facultyNote.getExpiryDate());

            return convertView;
        }
    }
}