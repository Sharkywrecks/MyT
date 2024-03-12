package com.sharkentech.myt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharkentech.myt.Add.FoodTrackSQLiteManager;
import com.sharkentech.myt.Add.FoodTrackerNote;
import com.sharkentech.myt.Add.FoodTrackerNotificationService;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackFoodActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private ListView listView;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_food);

        FoodTrackerNote.foodTrackerNoteArrayList.clear();
        setupUIViews();
        initToolbar();
        setupBottomAppBar();
        setFoodTrackerNoteAdapter();
        loadFromDBToMemory();
        setOnCLickListener();
    }
    private void loadFromDBToMemory(){
        FoodTrackSQLiteManager foodTrackSQLiteManager = FoodTrackSQLiteManager.instanceOfDatabase(this);
        foodTrackSQLiteManager.populateFoodTrackerNoteListArray();
        //System.out.println("loading database into memory");
    }
    private void setupUIViews(){
        //toolbar = (Toolbar)findViewById(R.id.ToolbarDayDetail);
        listView = (ListView)findViewById(R.id.lvDayDetail);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.foodTracker);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
    }
    private void initToolbar(){
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Food tracking");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setFoodTrackerNoteAdapter(){
        FoodTrackerNoteAdapter foodTrackerNoteAdapter = new FoodTrackerNoteAdapter(getApplicationContext(), FoodTrackerNote.nonDeletedDetailNotes());
        listView.setAdapter(foodTrackerNoteAdapter);
    }
    public void newFoodTrackerNote(View view){
        Intent newNodeIntent = new Intent(TrackFoodActivity.this, CodeScannerActivity.class);
        startActivity(newNodeIntent);
    }
    private void setupBottomAppBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timetable: {
                        FoodTrackerNote.foodTrackerNoteArrayList.clear();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.subjects: {
                        FoodTrackerNote.foodTrackerNoteArrayList.clear();
                        Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.faculty: {
                        FoodTrackerNote.foodTrackerNoteArrayList.clear();
                        Intent intent = new Intent(getApplicationContext(), FacultyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.foodTracker: {
                        /*Intent intent = new Intent(getApplicationContext(), TrackFoodActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);*/
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
                FoodTrackerNote selectedFoodTrackerNote = (FoodTrackerNote) listView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), CodeScannerActivity.class);
                editNoteIntent.putExtra(FoodTrackerNote.NOTE_EDIT_EXTRA,selectedFoodTrackerNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFoodTrackerNoteAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//for going back to home page from physical button
        switch (item.getItemId()){
            case android.R.id.home :{
                FoodTrackerNote.foodTrackerNoteArrayList.clear();
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private class FoodTrackerNoteAdapter extends ArrayAdapter<FoodTrackerNote> {
        Context context;
        private FoodTrackerNoteAdapter(Context context, List<FoodTrackerNote> foodTrackerNote){
            super(context,0,foodTrackerNote);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            FoodTrackerNote foodTrackerNote = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_tracker_single_item,parent,false);
            }
            TextView foodName = convertView.findViewById(R.id.tvFood);
            TextView expiryDate = convertView.findViewById(R.id.tv_expiryDate);
            LinearLayout foodTrackerLayout = convertView.findViewById(R.id.foodTrackerSingle);

            if(foodTrackerNote.getExpiryDate()!=null && !foodTrackerNote.getExpiryDate().equals("")){
                Calendar calendar = Calendar.getInstance();
                DateFormat dateFormat = DateFormat.getDateInstance();
                Date startDateValue = calendar.getTime();
                int day = Integer.parseInt(foodTrackerNote.getExpiryDate().substring(0,2));
                int month = Integer.parseInt(foodTrackerNote.getExpiryDate().substring(3,5));
                int year = Integer.parseInt(foodTrackerNote.getExpiryDate().substring(6,10));
                Calendar calendar2 = Calendar.getInstance();
                System.out.println(year+" "+month+" "+day);
                calendar2.set(Calendar.YEAR,year);
                calendar2.set(Calendar.MONTH,month-1);
                calendar2.set(Calendar.DAY_OF_MONTH,day);
                Date endDateValue = calendar2.getTime();
                long diff = endDateValue.getTime() - startDateValue.getTime();
                int days = Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                System.out.println("Difference is "+days);
                if(days<=14 && days>0){
                    foodTrackerLayout.setBackgroundColor( getResources().getColor(R.color.colorFoodAlmostExpired));
                }
                if(days<=0){
                    foodTrackerLayout.setBackgroundColor( getResources().getColor(R.color.colorFoodExpired));
                }
                FoodTrackerNotificationService service = new FoodTrackerNotificationService(context);
                service.startAlarmBroadcastReceiver(foodTrackerNote,day, month, year);
            }

            foodName.setText(foodTrackerNote.getFoodName());
            expiryDate.setText(foodTrackerNote.getExpiryDate());

            return convertView;
        }
    }
}