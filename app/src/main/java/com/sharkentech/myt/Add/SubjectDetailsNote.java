package com.sharkentech.myt.Add;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;

public class SubjectDetailsNote {
    public static ArrayList<SubjectDetailsNote> subjectDetailsNoteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA = "subjectDetailsNoteEdit";

    private int id;
    private String topicName;
    private String topicDetails;
    private int realId;

    private Date deleted;

    public SubjectDetailsNote(int id, String topicName, String topicDetails, int realId, Date deleted) {
        this.id = id;
        this.topicName = topicName;
        this.topicDetails = topicDetails;
        this.realId = realId;
        this.deleted = deleted;
    }
    public SubjectDetailsNote(int id, String topicName, String topicDetails,int realId) {
        this.id = id;
        this.topicName = topicName;
        this.topicDetails = topicDetails;
        this.realId = realId;
        deleted = null;
    }

    public static SubjectDetailsNote getNoteForID(int passedNoteID) {
        for (SubjectDetailsNote subjectDetailsNote : subjectDetailsNoteArrayList) {
            if (subjectDetailsNote.getId() == passedNoteID) {
                return subjectDetailsNote;
            }
        }
        return null;
    }

    public static ArrayList<SubjectDetailsNote> nonDeletedSubjectDetailsNotes(Intent intent) {
        ArrayList<SubjectDetailsNote> nonDeleted = new ArrayList<>();
        for (SubjectDetailsNote subjectDetailsNote : subjectDetailsNoteArrayList) {
            if (subjectDetailsNote.getDeleted() == null && subjectDetailsNote.getRealId()==intent.getIntExtra("SubjectRealId",-1)) {
                nonDeleted.add(subjectDetailsNote);
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDetails() {
        return topicDetails;
    }

    public void setTopicDetails(String topicDetails) {
        this.topicDetails = topicDetails;
    }

    public int getRealId() {
        return realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

}