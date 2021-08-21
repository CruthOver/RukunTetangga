package id.akhir.proyek.rukuntetangga.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public EditText holder;
    public UniversalListener listener;
    public TimePickerDialog timePickerDialog;
    private int hour, minutes;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        timePickerDialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, this, hour, minutes, true);
        timePickerDialog.getWindow().setTitleColor(getResources().getColor(R.color.colorSecondary));
        timePickerDialog.setTitle("Pilih Jam");
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        hour = hourOfDay;
        minutes = minute;
        holder.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minutes));
    }
}
