package com.sharkentech.myt.Add;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharkentech.myt.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "DayDetailNoteDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "DayDetailNoteDB";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "ID";
    private static final String SUBJECT_FIELD = "Subject";
    private static final String LOCATION_FIELD = "Location";
    private static final String FACULTY_FIELD = "Faculty";
    private static final String START_TIME_FIELD = "StartTime";
    private static final String END_TIME_FIELD = "EndTime";
    private static final String DELETED_FIELD = "Deleted";
    private static final String DAY_FIELD = "DayOfWeek";

    //Didnt do like video with bottom one
    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    public SQLiteManager( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static SQLiteManager instanceOfDatabase(Context context){
            if(sqLiteManager == null){
                sqLiteManager = new SQLiteManager(context);
            }
            return sqLiteManager;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(SUBJECT_FIELD)
                .append(" TEXT, ")
                .append(LOCATION_FIELD)
                .append(" TEXT, ")
                .append(FACULTY_FIELD)
                .append(" TEXT, ")
                .append(START_TIME_FIELD)
                .append(" TEXT, ")
                .append(END_TIME_FIELD)
                .append(" TEXT, ")
                .append(DAY_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
    public void addDayDetailNoteToDatabase(DayDetailNote dayDetailNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,dayDetailNote.getId());
        contentValues.put(SUBJECT_FIELD,dayDetailNote.getSubject());
        contentValues.put(LOCATION_FIELD,dayDetailNote.getLocation());
        contentValues.put(FACULTY_FIELD,dayDetailNote.getFaculty());
        contentValues.put(START_TIME_FIELD,dayDetailNote.getStartTime());
        contentValues.put(END_TIME_FIELD,dayDetailNote.getEndTime());
        contentValues.put(DAY_FIELD, dayDetailNote.getDay());
        contentValues.put(DELETED_FIELD,getStringFromDate(dayDetailNote.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }
    public void populateDayDetailNoteListArray(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount()!=0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String subject = result.getString(2);
                    String location = result.getString(3);
                    String faculty = result.getString(4);
                    String startTime = result.getString(5);
                    String endTime = result.getString(6);
                    String day = result.getString(7);
                    String stringDeleted = result.getString(8);
                    Date deleted = getDateFromString(stringDeleted);
                    if(day.equalsIgnoreCase(MainActivity.sharedPreferences.getString(MainActivity.SEL_DAY,null))){
                        DayDetailNote dayDetailNote = new DayDetailNote(id,subject,location,faculty,startTime,endTime,day,deleted);
                        DayDetailNote.dayDetailNoteArrayList.add(dayDetailNote);
                    }
                }
            }
        }
    }
    public void updateDayDetailNoteListArray(DayDetailNote dayDetailNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,dayDetailNote.getId());
        contentValues.put(SUBJECT_FIELD,dayDetailNote.getSubject());
        contentValues.put(LOCATION_FIELD,dayDetailNote.getLocation());
        contentValues.put(FACULTY_FIELD,dayDetailNote.getFaculty());
        contentValues.put(START_TIME_FIELD,dayDetailNote.getStartTime());
        contentValues.put(END_TIME_FIELD,dayDetailNote.getEndTime());
        contentValues.put(DAY_FIELD,dayDetailNote.getDay());
        contentValues.put(DELETED_FIELD,getStringFromDate(dayDetailNote.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME,contentValues,ID_FIELD + " =? ", new String[]{String.valueOf(dayDetailNote.getId())});
    }
    private String getStringFromDate(Date date) {
        if(date == null){
            return null;
        }
        return dateFormat.format(date);
    }
    private Date getDateFromString(String string){

        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }
}
