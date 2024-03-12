package com.sharkentech.myt.Add;

import com.sharkentech.myt.MainActivity;

import java.util.ArrayList;
import java.util.Date;

public class DayDetailNote {

    public static ArrayList<DayDetailNote> dayDetailNoteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA = "dayDetailNoteEdit";

    private int id;
    private String subject;
    private String location;
    private String faculty;
    private String startTime;
    private String endTime;
    private String day;
    private Date deleted;

    public DayDetailNote(int id, String subject,String location, String faculty, String startTime,String endTime,String day, Date deleted) {
        this.id = id;
        this.subject = subject;
        this.location = location;
        this.faculty = faculty;
        this.startTime = startTime;
        this.endTime =  endTime;
        this.day = day;
        this.deleted = deleted;
    }

    public DayDetailNote(int id, String subject, String location, String faculty, String startTime,String endTime,String day) {
        this.id = id;
        this.subject = subject;
        this.location = location;
        this.faculty = faculty;
        this.startTime = startTime;
        this.endTime =  endTime;
        this.day =day;
        deleted = null;
    }

    public static DayDetailNote getNoteForID(int passedNoteID) {
        for(DayDetailNote dayDetailNote: dayDetailNoteArrayList){
            if(dayDetailNote.getId() == passedNoteID){
                return dayDetailNote;
            }
        }
        return null;
    }
    public static ArrayList<DayDetailNote> nonDeletedDetailNotes(){
        ArrayList<DayDetailNote> nonDeleted = new ArrayList<>();
        for(DayDetailNote dayDetailNote:dayDetailNoteArrayList){
            if(dayDetailNote.getDeleted() == null && dayDetailNote.getDay().equalsIgnoreCase(MainActivity.sharedPreferences.getString(MainActivity.SEL_DAY,null))){
                nonDeleted.add(dayDetailNote);
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDay(String startTime) {
        this.day = day;
    }
    public String getDay() {
        return day;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
