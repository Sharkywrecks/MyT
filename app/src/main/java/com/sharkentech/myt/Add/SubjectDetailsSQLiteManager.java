package com.sharkentech.myt.Add;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharkentech.myt.SubjectActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class SubjectDetailsSQLiteManager extends SQLiteOpenHelper {
    private static SubjectDetailsSQLiteManager subjectDetailsSQLiteManager;
    private static final String DATABASE_NAME = "SubjectDetailsNoteDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SubjectDetailsNoteDB";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "ID";
    private static final String TOPICNAME_FIELD = "TopicName";
    private static final String TOPICDETAIL_FIELD = "TopicDetail";
    private static final String REALID_FIELD = "RealId";
    private static final String DELETED_FIELD = "Deleted";

    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    public SubjectDetailsSQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SubjectDetailsSQLiteManager instanceOfDatabase(Context context){
        if(subjectDetailsSQLiteManager == null){
            subjectDetailsSQLiteManager = new SubjectDetailsSQLiteManager(context);
        }
        return subjectDetailsSQLiteManager;
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
                .append(TOPICNAME_FIELD)
                .append(" TEXT, ")
                .append(TOPICDETAIL_FIELD)
                .append(" TEXT, ")
                .append(REALID_FIELD)
                .append(" INT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());
    }

    //on addition of new field into database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
    public void addSubjectDetailsNoteToDatabase(SubjectDetailsNote subjectDetailsNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,subjectDetailsNote.getId());
        contentValues.put(TOPICNAME_FIELD,subjectDetailsNote.getTopicName());
        contentValues.put(TOPICDETAIL_FIELD,subjectDetailsNote.getTopicDetails());
        contentValues.put(REALID_FIELD,subjectDetailsNote.getRealId());
        contentValues.put(DELETED_FIELD,getStringFromDate(subjectDetailsNote.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }
    public void populateSubjectDetailsNoteListArray(SubjectNote subjectNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount()!=0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String topicName = result.getString(2);
                    String topicDetails = result.getString(3);
                    int realId = result.getInt(4);
                    String stringDeleted = result.getString(5);
                    Date deleted = getDateFromString(stringDeleted);
                    System.out.println(realId+" is meant to equal "+subjectNote.getRealId());
                    if(realId == subjectNote.getRealId() &&deleted == null &&id!=-1){
                        SubjectDetailsNote subjectDetailsNote = new SubjectDetailsNote(id,topicName,topicDetails,realId,deleted);
                        SubjectDetailsNote.subjectDetailsNoteArrayList.add(subjectDetailsNote);
                        System.out.println("Populated list");
                    }
                }
            }
        }
    }
    public void updateSubjectDetailsNoteListArray(SubjectDetailsNote subjectDetailsNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,subjectDetailsNote.getId());
        contentValues.put(TOPICNAME_FIELD,subjectDetailsNote.getTopicName());
        contentValues.put(TOPICDETAIL_FIELD,subjectDetailsNote.getTopicDetails());
        contentValues.put(REALID_FIELD,subjectDetailsNote.getRealId());
        contentValues.put(DELETED_FIELD,getStringFromDate(subjectDetailsNote.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME,contentValues,ID_FIELD + " =? ", new String[]{String.valueOf(subjectDetailsNote.getId())});
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
