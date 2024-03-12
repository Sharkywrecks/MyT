package com.sharkentech.myt.Add;

import java.util.ArrayList;
import java.util.Date;

public class SubjectNote {

        public static ArrayList<SubjectNote> subjectNoteArrayList = new ArrayList<>();
        public static String NOTE_EDIT_EXTRA = "subjectNoteEdit";

        private int id;
        private String subjectName;
        private int realId;

        private Date deleted;

        public SubjectNote(int id, String subjectName,int realId, Date deleted) {
            this.id = id;
            this.subjectName = subjectName;
            this.realId = realId;
            this.deleted = deleted;
        }

        public SubjectNote(int id, String subjectName,int realId) {
            this.id = id;
            this.subjectName = subjectName;
            this.realId = realId;
            deleted = null;
        }

        public static SubjectNote getNoteForRealID(int passedNoteID) {
            for(SubjectNote subjectNote: subjectNoteArrayList){
                if(subjectNote.getRealId() == passedNoteID){
                    return subjectNote;
                }
            }
            return null;
        }
    public static ArrayList<SubjectNote> nonDeletedSubjectNotes(){
        ArrayList<SubjectNote> nonDeleted = new ArrayList<>();
        int prevId = -1;
        for(SubjectNote subjectNote:subjectNoteArrayList){
            if(subjectNote.getDeleted() == null && prevId !=subjectNote.getId()){
                nonDeleted.add(subjectNote);
            }
            prevId =subjectNote.getId();
        }

        return nonDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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
