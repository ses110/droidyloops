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
import io.github.ses110.dloops.looper.RecordFragment;

/**
 * Created by sid9102 on 4/29/2014.
 */
public class NameDialog extends DialogFragment
{
    Context mContext;
    RecordFragment mRecordFragment;

    public NameDialog() {

    }
    public NameDialog(Context context, RecordFragment recordFragment) {
        mContext = context;
        mRecordFragment = recordFragment;

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Name your sample");
        builder.setMessage("Name: ");

        final EditText input = new EditText(mContext);
        input.setHint("name");
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(input);

        builder
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String name = input.getText().toString();
                                mRecordFragment.saveSample(name);
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
}
