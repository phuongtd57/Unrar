package com.example.phuongtd.moolamoola.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.phuongtd.moolamoola.R;

/**
 * Created by qs109 on 6/4/2016.
 */
public class PasswordDialog extends Dialog implements View.OnClickListener {
    ActionImpl action;
    Context context;
    EditText etPass;
    Button btCancel;
    Button btDone;

    public PasswordDialog(Context context, ActionImpl action) {
        super(context);
        this.context = context;
        this.action = action;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.pass_layout);
        etPass = (EditText) findViewById(R.id.etPass);
        btDone = (Button) findViewById(R.id.btDone);
        btCancel = (Button) findViewById(R.id.btCancel);

        btCancel.setOnClickListener(this);
        btDone.setOnClickListener(this);
    }

    public  interface ActionImpl {
         void execute(String pass);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancel:
                dismiss();
                break;
            case R.id.btDone:
                dismiss();
                action.execute(etPass.getText().toString().trim());
                break;
        }
    }
}
