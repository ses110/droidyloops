package io.github.ses110.dloops.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;

import io.github.ses110.dloops.MainActivity;

/**
 * Created by sergioescoto on 4/27/14.
 */
public class BpmDialog extends DialogFragment {
    Context mContext;

    int mDialogBPM;

    public BpmDialog() {

    }
    public BpmDialog(Context context, int bpm) {
        mContext = context;
        mDialogBPM = bpm;

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Set BPM");
        builder.setMessage("Input your BPM: ");

        final EditText input = new EditText(mContext);
        input.setText(Integer.toString(mDialogBPM));
        input.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        builder.setView(input);

        builder
        .setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int bpm = Integer.parseInt((input.getText().toString()));
                        if (bpm < 30)
                            bpm = 30;
                        if (bpm > 300)
                            bpm = 300;
                        mDialogBPM = bpm;
                        ((MainActivity) mContext).setBPM(mDialogBPM);
                    }
                }
        )
        .setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().dismiss();
                    }
                }
        );

        return builder.create();
    }

    public int newBPM() {
        return mDialogBPM;
    }
}
