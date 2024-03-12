package com.sharkentech.myt.Add;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class FacultySQLiteManager  extends SQLiteOpenHelper {
    private static FacultySQLiteManager facultySQLiteManager;
    private static final String DATABASE_NAME = "FacultyNoteDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "FacultyNoteDB";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "ID";
    private static final String FACULTYNAME_FIELD = "FacultyName";
    private static final String PHONENUMBER_FIELD = "PhoneNumber";
    private static final String EMAIL_FIELD = "Email";
    private static final String LOCATION_FIELD = "Location";
    private static final String FACULTYIMAGE_FIELD = "Image";
    private static final String DELETED_FIELD = "Deleted";

    //Didnt do like video with bottom one
    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    public FacultySQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static FacultySQLiteManager instanceOfDatabase(Context context){
        if(facultySQLiteManager == null){
            facultySQLiteManager = new FacultySQLiteManager(context);
        }
        return facultySQLiteManager;
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
                .append(FACULTYNAME_FIELD)
                .append(" TEXT, ")
                .append(PHONENUMBER_FIELD)
                .append(" TEXT, ")
                .append(EMAIL_FIELD)
                .append(" TEXT, ")
                .append(LOCATION_FIELD)
                .append(" TEXT, ")
                /*.append(FACULTYIMAGE_FIELD)
                .append(" TEXT, ")*/
                .append(FACULTYIMAGE_FIELD)
                .append(" BLOB, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());
    }

    //on addition of new field into database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
    public void addFacultyNoteToDatabase(FacultyNote facultyNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,facultyNote.getId());
        contentValues.put(FACULTYNAME_FIELD,facultyNote.getFacultyName());
        contentValues.put(PHONENUMBER_FIELD,facultyNote.getFacultyPhoneNumber());
        contentValues.put(EMAIL_FIELD,facultyNote.getFacultyEmail());
        contentValues.put(LOCATION_FIELD,facultyNote.getFacultyLocation());
        contentValues.put(FACULTYIMAGE_FIELD,facultyNote.getFacultyImage());
        contentValues.put(DELETED_FIELD,getStringFromDate(facultyNote.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }
    public void populateFacultyNoteListArray(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount()!=0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String facultyName = result.getString(2);
                    String phoneNumber = result.getString(3);
                    String email = result.getString(4);
                    String location = result.getString(5);
                    byte[] image = result.getBlob(6);
                    String stringDeleted = result.getString(7);
                    Date deleted = getDateFromString(stringDeleted);
                    //if(day.equalsIgnoreCase(WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY,null))){
                    FacultyNote facultyNote = new FacultyNote(id,facultyName,phoneNumber,email,location,image,deleted);
                    FacultyNote.facultyNoteArrayList.add(facultyNote);
                    //}
                }
            }
        }
    }
    public void updateFacultyNoteListArray(FacultyNote facultyNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,facultyNote.getId());
        contentValues.put(FACULTYNAME_FIELD,facultyNote.getFacultyName());
        contentValues.put(PHONENUMBER_FIELD,facultyNote.getFacultyPhoneNumber());
        contentValues.put(EMAIL_FIELD,facultyNote.getFacultyEmail());
        contentValues.put(LOCATION_FIELD,facultyNote.getFacultyLocation());
        contentValues.put(FACULTYIMAGE_FIELD,facultyNote.getFacultyImage());
        contentValues.put(DELETED_FIELD,getStringFromDate(facultyNote.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME,contentValues,ID_FIELD + " =? ", new String[]{String.valueOf(facultyNote.getId())});
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
