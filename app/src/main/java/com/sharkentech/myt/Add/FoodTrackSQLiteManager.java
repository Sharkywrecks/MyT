package com.sharkentech.myt.Add;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class FoodTrackSQLiteManager extends SQLiteOpenHelper {
    private static FoodTrackSQLiteManager foodTrackSQLiteManager;
    private static final String DATABASE_NAME = "FoodTrackerNoteDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "FoodTrackerNoteDB";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "ID";
        private static final String FOODNAME_FIELD = "FoodName";
    private static final String EXPIRYDATE_FIELD = "ExpiryDate";
    private static final String DELETED_FIELD = "Deleted";

    //Didnt do like video with bottom one
    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    public FoodTrackSQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static FoodTrackSQLiteManager instanceOfDatabase(Context context){
        if(foodTrackSQLiteManager == null){
            foodTrackSQLiteManager = new FoodTrackSQLiteManager(context);
        }
        return foodTrackSQLiteManager;
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
                .append(FOODNAME_FIELD)
                .append(" TEXT, ")
                .append(EXPIRYDATE_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());
    }

    //on addition of new field into database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
    public void addFoodTrackerNoteToDatabase(FoodTrackerNote foodTrackerNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,foodTrackerNote.getId());
        contentValues.put(FOODNAME_FIELD,foodTrackerNote.getFoodName());
        contentValues.put(EXPIRYDATE_FIELD,foodTrackerNote.getExpiryDate());
        contentValues.put(DELETED_FIELD,getStringFromDate(foodTrackerNote.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }
    public void populateFoodTrackerNoteListArray(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount()!=0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String foodName = result.getString(2);
                    String expiryDate = result.getString(3);
                    String stringDeleted = result.getString(4);
                    Date deleted = getDateFromString(stringDeleted);
                    //if(day.equalsIgnoreCase(WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY,null))){
                        FoodTrackerNote foodTrackerNote = new FoodTrackerNote(id,foodName,expiryDate,deleted);
                        FoodTrackerNote.foodTrackerNoteArrayList.add(foodTrackerNote);
                    //}
                }
            }
        }
    }
    public void updateFoodTrackerNoteListArray(FoodTrackerNote foodTrackerNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,foodTrackerNote.getId());
        contentValues.put(FOODNAME_FIELD,foodTrackerNote.getFoodName());
        contentValues.put(EXPIRYDATE_FIELD,foodTrackerNote.getExpiryDate());
        contentValues.put(DELETED_FIELD,getStringFromDate(foodTrackerNote.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME,contentValues,ID_FIELD + " =? ", new String[]{String.valueOf(foodTrackerNote.getId())});
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
