package id.akhir.proyek.rukuntetangga.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public EditText holder;
    public UniversalListener listener;
    public DatePickerDialog dpd;
    private final long maxDate;
    private final long minDate;

    public DatePickerFragment(long maxDate, long minDate) {
        this.maxDate = maxDate;
        this.minDate = minDate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dpd = new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
//        dpd.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorGray));
        dpd.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.label_cancel), (dialog, which) -> {
//                if(holder!=null)
//                    holder.setText(holder.getHint());
//                if(listener!=null)
//                    listener.onResetData();
            dialog.dismiss();
        });
//        dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if (minDate != 0) {
            dpd.getDatePicker().setMinDate(minDate);
        }

        if (maxDate != 0)
            dpd.getDatePicker().setMaxDate(maxDate);
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String setDate = getTwoDigitNumber(dayOfMonth)+"-"+getTwoDigitNumber(month+1)+"-"+year;
        if(holder!=null)
            holder.setText(setDate);
        if(listener!=null)
            listener.onSetData();
    }

    public static String getTwoDigitNumber(int number){
        if(number/10 > 0)return ""+number;
        return "0"+number;
    }
}
