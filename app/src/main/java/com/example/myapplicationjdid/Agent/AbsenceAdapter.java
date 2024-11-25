package com.example.myapplicationjdid.Agent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationjdid.R;
import com.example.myapplicationjdid.models.Absence;

import java.util.List;

public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {

    private final List<Absence> absenceList;

    public AbsenceAdapter(List<Absence> absenceList) {
        this.absenceList = absenceList;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_absence, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        Absence absence = absenceList.get(position);
        Log Log = null;
        Log.d("AbsenceAdapter", "Absence bind : " + absence.getTeacherAddress());
        holder.textTeacherAddress.setText("Nom: " + absence.getTeacherAddress());
        holder.textSalleNumber.setText("Salle: " + absence.getSalleNumber());
        holder.textDate.setText("Date: " + absence.getDate());
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView textTeacherAddress, textSalleNumber, textDate;
        ImageView scaredImage;

        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);
            textTeacherAddress = itemView.findViewById(R.id.textTeacherAddress);
            textSalleNumber = itemView.findViewById(R.id.textSalleNumber);
            textDate = itemView.findViewById(R.id.textDate);
            scaredImage = itemView.findViewById(R.id.scaredImage);
        }
    }
}
