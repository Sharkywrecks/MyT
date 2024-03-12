package com.sharkentech.myt.Add;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class SubjectSQLiteManager extends SQLiteOpenHelper {

    private static SubjectSQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "SubjectNoteDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "SubjectNoteDB";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "ID";
    private static final String SUBJECTNAME_FIELD = "SubjectName";
    private static final String REALID_FIELD = "RealId";
    private static final String DELETED_FIELD = "Deleted";

    //Didnt do like video with bottom one
    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    public SubjectSQLiteManager( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static SubjectSQLiteManager instanceOfDatabase(Context context){
        if(sqLiteManager == null){
            sqLiteManager = new SubjectSQLiteManager(context);
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
                .append(SUBJECTNAME_FIELD)
                .append(" TEXT, ")
                .append(REALID_FIELD)
                .append(" INT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void addSubjectNoteToDatabase(SubjectNote subjectNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,subjectNote.getId());
        contentValues.put(SUBJECTNAME_FIELD,subjectNote.getSubjectName());
        contentValues.put(REALID_FIELD,subjectNote.getRealId());
        contentValues.put(DELETED_FIELD,getStringFromDate(subjectNote.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }
    public void populateSubjectNoteListArray(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount()!=0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String subjectName = result.getString(2);
                    int realId = result.getInt(3);

                    System.out.println("Real ID is "+realId);

                    String stringDeleted = result.getString(4);
                    Date deleted = getDateFromString(stringDeleted);

                    if(deleted == null&&id!=-1){
                        SubjectNote subjectNote = new SubjectNote(id,subjectName,realId,deleted);
                        SubjectNote.subjectNoteArrayList.add(subjectNote);
                    }
                }
            }
        }
    }
    public void updateSubjectNoteListArray(SubjectNote subjectNote){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,subjectNote.getId());
        contentValues.put(SUBJECTNAME_FIELD,subjectNote.getSubjectName());
        contentValues.put(REALID_FIELD,subjectNote.getRealId());
        contentValues.put(DELETED_FIELD,getStringFromDate(subjectNote.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME,contentValues,ID_FIELD + " =? ", new String[]{String.valueOf(subjectNote.getId())});
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
