package com.sharkentech.myt.Add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sharkentech.myt.R;
import com.sharkentech.myt.Utils.LetterImageView;

import java.util.List;

public class DayDetailNoteAdapter extends ArrayAdapter<DayDetailNote> {

    public DayDetailNoteAdapter(Context context, List<DayDetailNote> dayDetailNotes){
        super(context,0,dayDetailNotes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DayDetailNote dayDetailNote = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.day_detail_single_item,parent,false);
        }
        TextView subject = convertView.findViewById(R.id.tvSubjectDayDetails);
        TextView timeDuration = convertView.findViewById(R.id.tvSubjectDayDetailsTime);
        TextView location = convertView.findViewById(R.id.tvSubjectDayDetailsLocation);
        TextView faculty = convertView.findViewById(R.id.tvSubjectDayDetailsFaculty);

        LetterImageView letterImageView = (LetterImageView) convertView.findViewById(R.id.ivDayDetails);;

        subject.setText(dayDetailNote.getSubject());
        timeDuration.setText(dayDetailNote.getStartTime()+" - "+dayDetailNote.getEndTime());
        location.setText(dayDetailNote.getLocation());
        faculty.setText(dayDetailNote.getFaculty());
        letterImageView.setOval(true);
        if(dayDetailNote.getSubject().length()!=0){
            letterImageView.setLetter(dayDetailNote.getSubject().charAt(0));
        }

        return convertView;
    }
}
