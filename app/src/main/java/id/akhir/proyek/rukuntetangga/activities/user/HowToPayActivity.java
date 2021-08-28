package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;

public class HowToPayActivity extends BaseActivity {
    Toolbar toolbar;
    TextView noRek, bankName;
    Button btnCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_pay);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.add_job));

        noRek = findViewById(R.id.tv_no_rek);
        bankName = findViewById(R.id.label_no_rek);
        btnCopy = findViewById(R.id.btn_copy);

        if (getStringExtraData("nomor_rekening") != null) {
            noRek.setText(getStringExtraData("nomor_rekening"));
        }

        if (getStringExtraData("bank_name") != null) {
            bankName.setText("Bank " +getStringExtraData("bank_name"));
        }

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("nomor_rekening", noRek.getText());
                clipboard.setPrimaryClip(clip);
            }
        });
    }
}