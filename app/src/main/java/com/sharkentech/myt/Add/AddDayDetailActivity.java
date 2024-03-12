package com.sharkentech.myt.Add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.sharkentech.myt.MainActivity;
import com.sharkentech.myt.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDayDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button deleteButton;
    private EditText subjectEditText,startTimeEditText,endTimeEditText,facultyEditText,locationEditText;
    int t1Hour,t1Minute,t2Hour,t2Minute;

    private DayDetailNote selectedDayDetailNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_day_detail);

        setupUIViews();
        initToolbar();
        checkForEditNote();

        setupTime();
        startTimeEditText.setFocusable(false);
        startTimeEditText.setKeyListener(null);
        endTimeEditText.setFocusable(false);
        endTimeEditText.setKeyListener(null);
    }
    private void setupTime(){
        startTimeEditText.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     TimePickerDialog timePickerDialog = new TimePickerDialog(AddDayDetailActivity.this,
                             android.R.style.Theme_Holo_Dialog_MinWidth,
                             new TimePickerDialog.OnTimeSetListener() {
                                 @Override
                                 public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                     t1Hour = hourOfDay;
                                     t1Minute = minute;
                                     String time = t1Hour+":"+t1Minute;
                                     SimpleDateFormat f24Hours = new SimpleDateFormat(
                                             "HH:mm"
                                     );
                                     try {
                                         Date date = f24Hours.parse(time);
                                         SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                         startTimeEditText.setText(f12Hours.format(date));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                 }
                             },12,0,false);
                     timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                     timePickerDialog.updateTime(t1Hour,t1Minute);
                     timePickerDialog.show();
                 }
             }
        );
        endTimeEditText.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     TimePickerDialog timePickerDialog = new TimePickerDialog(AddDayDetailActivity.this,
                             android.R.style.Theme_Holo_Dialog_MinWidth,
                             new TimePickerDialog.OnTimeSetListener() {
                                 @Override
                                 public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                     t2Hour = hourOfDay;
                                     t2Minute = minute;
                                     String time = t2Hour+":"+t2Minute;
                                     SimpleDateFormat f24Hours = new SimpleDateFormat(
                                             "HH:mm"
                                     );
                                     try {
                                         Date date = f24Hours.parse(time);
                                         SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                         endTimeEditText.setText(f12Hours.format(date));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                 }
                             },12,0,false);
                     timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                     timePickerDialog.updateTime(t2Hour,t2Minute);
                     timePickerDialog.show();
                 }
             }
        );
    }
    private void setupUIViews(){
        toolbar = (Toolbar)findViewById(R.id.ToolbarDayDetail);
        subjectEditText = findViewById(R.id.subjectEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        locationEditText = findViewById(R.id.locationEditText);
        facultyEditText = findViewById(R.id.facultyEditText);
        deleteButton = findViewById(R.id.deleteDetailButton);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add To TimeTable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void checkForEditNote(){
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(DayDetailNote.NOTE_EDIT_EXTRA,-1);
        selectedDayDetailNote = DayDetailNote.getNoteForID(passedNoteID);
        if(selectedDayDetailNote != null){
            subjectEditText.setText(selectedDayDetailNote.getSubject());
            startTimeEditText.setText(selectedDayDetailNote.getStartTime());
            endTimeEditText.setText(selectedDayDetailNote.getEndTime());
            locationEditText.setText(selectedDayDetailNote.getLocation());
            facultyEditText.setText(selectedDayDetailNote.getFaculty());
        }else{
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }
    public void saveDetail(View view){
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String subject = String.valueOf(subjectEditText.getText());
        String startTime = String.valueOf(startTimeEditText.getText());
        String endTime = String.valueOf(endTimeEditText.getText()) ;
        String location = String.valueOf(locationEditText.getText());
        String faculty = String.valueOf(facultyEditText.getText());

        if(selectedDayDetailNote == null){
            int id = DayDetailNote.dayDetailNoteArrayList.size();
            DayDetailNote newNote = new DayDetailNote(id,subject,location,faculty,startTime,endTime, MainActivity.sharedPreferences.getString(MainActivity.SEL_DAY,null));
            DayDetailNote.dayDetailNoteArrayList.add(newNote);
            sqLiteManager.addDayDetailNoteToDatabase(newNote);
        }else{
            selectedDayDetailNote.setSubject(subject);
            selectedDayDetailNote.setLocation(location);
            selectedDayDetailNote.setFaculty(faculty);
            selectedDayDetailNote.setStartTime(startTime);
            selectedDayDetailNote.setEndTime(endTime);
            selectedDayDetailNote.setDay(MainActivity.sharedPreferences.getString(MainActivity.SEL_DAY,null));
            sqLiteManager.updateDayDetailNoteListArray(selectedDayDetailNote);
        }
        finish();
    }
    public void deleteDetail(View view){
        selectedDayDetailNote.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateDayDetailNoteListArray(selectedDayDetailNote);
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
