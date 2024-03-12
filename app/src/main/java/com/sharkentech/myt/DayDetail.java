package com.sharkentech.myt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharkentech.myt.Add.AddDayDetailActivity;
import com.sharkentech.myt.Add.DayDetailNote;
import com.sharkentech.myt.Add.DayDetailNoteAdapter;
import com.sharkentech.myt.Add.FoodTrackerNote;
import com.sharkentech.myt.Add.SQLiteManager;
import com.sharkentech.myt.Utils.LetterImageView;

public class DayDetail extends AppCompatActivity {

    //private Toolbar toolbar;
    private ListView listView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        DayDetailNote.dayDetailNoteArrayList.clear();
        setupUIViews();
        setupBottomAppBar();
        initToolbar();
        //setupListView();
        setDayDetailNoteAdapter();
        loadFromDBToMemory();
        setOnCLickListener();
    }
    private void loadFromDBToMemory(){
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateDayDetailNoteListArray();
        //System.out.println("loading database into memory");
    }
    private void setupUIViews(){
        //toolbar = (Toolbar)findViewById(R.id.ToolbarDayDetail);
        listView = (ListView)findViewById(R.id.lvDayDetail);
        bottomNavigationView= findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.timetable);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
    }
    private void setupBottomAppBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                DayDetailNote.dayDetailNoteArrayList.clear();
                switch (item.getItemId()) {
                    case R.id.timetable: {
                        finish();
                        return true;
                    }
                    case R.id.subjects: {
                        Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
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
    private void initToolbar(){
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY,null));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }
    private void setDayDetailNoteAdapter(){
        DayDetailNoteAdapter dayDetailNoteAdapter = new DayDetailNoteAdapter(getApplicationContext(),DayDetailNote.nonDeletedDetailNotes());
        listView.setAdapter(dayDetailNoteAdapter);
    }
    private void setOnCLickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DayDetailNote selectedDayDetailNote = (DayDetailNote) listView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(),AddDayDetailActivity.class);
                editNoteIntent.putExtra(DayDetailNote.NOTE_EDIT_EXTRA,selectedDayDetailNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }
    public void newDayDetailNote(View view){
        Intent newNodeIntent = new Intent(DayDetail.this, com.sharkentech.myt.Add.AddDayDetailActivity.class);
        startActivity(newNodeIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDayDetailNoteAdapter();
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