package com.sharkentech.myt.Add;

import java.util.ArrayList;
import java.util.Date;

public class FoodTrackerNote {
    public static ArrayList<FoodTrackerNote> foodTrackerNoteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA = "foodTrackerNoteEdit";

    private int id;
    private String foodName;
    private String expiryDate;
    private Date deleted;

    public FoodTrackerNote(int id, String foodName,String expiryDate, Date deleted) {
        this.id = id;
        this.foodName = foodName;
        this.expiryDate = expiryDate;
        this.deleted = deleted;
    }

    public FoodTrackerNote(int id, String foodName,String expiryDate) {
        this.id = id;
        this.foodName = foodName;
        this.expiryDate = expiryDate;
        deleted = null;
    }

    public static FoodTrackerNote getNoteForID(int passedNoteID) {
        for(FoodTrackerNote foodTrackerNote: foodTrackerNoteArrayList){
            if(foodTrackerNote.getId() == passedNoteID){
                return foodTrackerNote;
            }
        }
        return null;
    }
    public static ArrayList<FoodTrackerNote> nonDeletedDetailNotes(){
        ArrayList<FoodTrackerNote> nonDeleted = new ArrayList<>();
        for(FoodTrackerNote foodTrackerNote:foodTrackerNoteArrayList){
            if(foodTrackerNote.getDeleted() == null ){
                nonDeleted.add(foodTrackerNote);
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

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
