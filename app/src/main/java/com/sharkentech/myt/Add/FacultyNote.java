package com.sharkentech.myt.Add;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class FacultyNote {
    public static ArrayList<FacultyNote> facultyNoteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA = "facultyNoteEdit";

    private int id;
    private String facultyName;
    private String facultyPhoneNumber;
    private String facultyEmail;
    private String facultyLocation;
    private byte[] facultyImage;


    private Date deleted;

    public FacultyNote(int id, String facultyName, String facultyPhoneNumber, String facultyEmail,String facultyLocation,byte[] facultyImage, Date deleted) {
        this.id = id;
        this.facultyName = facultyName;
        this.facultyPhoneNumber = facultyPhoneNumber;
        this.facultyEmail = facultyEmail;
        this.facultyLocation = facultyLocation;
        this.facultyImage = facultyImage;
        this.deleted = deleted;
    }

    public FacultyNote(int id, String facultyName, String facultyPhoneNumber, String facultyEmail,String facultyLocation,byte[] facultyImage) {
        this.id = id;
        this.facultyName = facultyName;
        this.facultyPhoneNumber = facultyPhoneNumber;
        this.facultyEmail = facultyEmail;
        this.facultyLocation = facultyLocation;
        this.facultyImage = facultyImage;
        deleted = null;
    }

    public static FacultyNote getNoteForID(int passedNoteID) {
        for(FacultyNote facultyNote: facultyNoteArrayList){
            if(facultyNote.getId() == passedNoteID){
                return facultyNote;
            }
        }
        return null;
    }
    public static ArrayList<FacultyNote> nonDeletedFacultyNotes(){
        ArrayList<FacultyNote> nonDeleted = new ArrayList<>();
        for(FacultyNote facultyNote:facultyNoteArrayList){
            if(facultyNote.getDeleted() == null ){
                nonDeleted.add(facultyNote);
            }
        }

        return nonDeleted;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getFacultyPhoneNumber() {
        return facultyPhoneNumber;
    }

    public void setFacultyPhoneNumber(String facultyPhoneNumber) {
        this.facultyPhoneNumber = facultyPhoneNumber;
    }

    public String getFacultyEmail() {
        return facultyEmail;
    }

    public void setFacultyEmail(String facultyEmail) {
        this.facultyEmail = facultyEmail;
    }

    public String getFacultyLocation() {
        return facultyLocation;
    }

    public void setFacultyLocation(String facultyLocation) {
        this.facultyLocation = facultyLocation;
    }
    public byte[] getFacultyImage() {
        return facultyImage;
    }

    public void setFacultyImage(byte[] facultyImage) {
        this.facultyImage = facultyImage;
    }
    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public static Bitmap convertToBitmap(byte[] bytes){
        if(bytes == null){
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
    public static byte[] convertToBytes(Bitmap bitmap){
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        if(bitmap == null){
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArray);
        return byteArray.toByteArray();
    }
}
