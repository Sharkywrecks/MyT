package com.sharkentech.myt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharkentech.myt.Utils.LetterImageView;

public class MainActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private ListView listView;
    //private BottomAppBar bottomAppBar;
    private BottomNavigationView bottomNavigationView;
    public static SharedPreferences sharedPreferences;
    public static String SEL_DAY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();
        setupListView();
        setupBottomAppBar();
        initToolbar();
        setupBottomAppBar();
    }

    private void setupUIViews(){
        //toolbar = (Toolbar)findViewById(R.id.ToolbarMain);
        listView = findViewById(R.id.lvMain);
        bottomNavigationView= findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.timetable);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        sharedPreferences = getSharedPreferences("MY_DAY",MODE_PRIVATE);
    }
    private void initToolbar(){
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");*/
    }
    private void setupBottomAppBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timetable: {
                        /*Intent intent = new Intent(MainActivity.this, WeekActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);*/
                        return true;
                    }
                    case R.id.subjects: {
                        Intent intent = new Intent(MainActivity.this, SubjectActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.faculty: {
                        Intent intent = new Intent(MainActivity.this, FacultyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    case R.id.foodTracker: {
                        Intent intent = new Intent(MainActivity.this, TrackFoodActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    }
                }
                return false;
            }
        }
        );
    }
    private void setupListView(){
        String[] week = getResources().getStringArray(R.array.Week);
        MainActivity.WeekAdapter adapter = new MainActivity.WeekAdapter(this,R.layout.activity_week_single_item,week);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: {
                        startActivity(new Intent(MainActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Monday").apply();
                        break;
                    }
                    case 1: {
                        startActivity(new Intent(MainActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Tuesday").apply();
                        break;
                    }
                    case 2: {
                        startActivity(new Intent(MainActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Wednesday").apply();
                        break;
                    }
                    case 3: {
                        startActivity(new Intent(MainActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Thursday").apply();
                        break;
                    }
                    case 4: {
                        startActivity(new Intent(MainActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Friday").apply();
                        break;
                    }
                    case 5: {
                        startActivity(new Intent(MainActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Saturday").apply();
                        break;
                    }
                    case 6: {
                        startActivity(new Intent(MainActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Sunday").apply();
                        break;
                    }
                    default:break;
                }
            }
        });
    }
    public class WeekAdapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater layoutInflater;//loads another xml inside current xml
        private String[] week = new String[]{};
        public WeekAdapter(Context context, int resource, String[] objects) {
            super(context, resource,objects);
            this.resource = resource;
            this.week = objects;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            MainActivity.WeekAdapter.ViewHolder holder;
            if(convertView == null){
                holder = new MainActivity.WeekAdapter.ViewHolder();
                convertView = layoutInflater.inflate(resource,null);
                holder.ivLogo = (LetterImageView)convertView.findViewById(R.id.ivLetter);
                holder.tvWeek = (TextView)convertView.findViewById(R.id.tvMain);
                convertView.setTag(holder);
            }else{
                holder = (MainActivity.WeekAdapter.ViewHolder)convertView.getTag();
            }
            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter(week[position].charAt(0));
            holder.tvWeek.setText(week[position]);
            return convertView;
        }
        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView tvWeek;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.timetable);
    }
}