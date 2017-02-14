package com.cats.android.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cats.android.R;
import com.cats.android.data.CatContent;
import com.cats.android.model.Cat;
import com.cats.android.service.MyIntentService;

/**
 * Created by andrey on 14.02.17.
 */

public final class CatAlertDialog {

    private CatAlertDialog() {
    }

    public static void openCreateCatDialog(final Context context, final ResultReceiver receiver) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final TextView titleTextView = (TextView) mView.findViewById(R.id.dialogTitle);
        titleTextView.setText("Create Cat");

        final EditText nameEditText = (EditText) mView.findViewById(R.id.nameEditText);
        final EditText ageEditText = (EditText) mView.findViewById(R.id.ageEditText);
        final EditText colorEditText = (EditText) mView.findViewById(R.id.colorEditText);
        final EditText breedEditText = (EditText) mView.findViewById(R.id.breedEditText);
        final EditText weightEditText = (EditText) mView.findViewById(R.id.weightEditText);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        MyIntentService.create(context, receiver,
                                new Cat(nameEditText.getText().toString(),
                                        Integer.parseInt(ageEditText.getText().toString()),
                                        colorEditText.getText().toString(),
                                        breedEditText.getText().toString(),
                                        Integer.parseInt(weightEditText.getText().toString())));
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    public static void openUpdateCatDialog(final Context context, final ResultReceiver receiver, Integer id) {
        final Cat cat = CatContent.getItemMap().get(id);
        if (cat == null) {
            return;
        }
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final TextView titleTextView = (TextView) mView.findViewById(R.id.dialogTitle);
        titleTextView.setText("Update Cat");

        final EditText nameEditText = (EditText) mView.findViewById(R.id.nameEditText);
        nameEditText.setText(cat.getName());

        final EditText ageEditText = (EditText) mView.findViewById(R.id.ageEditText);
        ageEditText.setText(""+cat.getAge());

        final EditText colorEditText = (EditText) mView.findViewById(R.id.colorEditText);
        colorEditText.setText(cat.getColor());

        final EditText breedEditText = (EditText) mView.findViewById(R.id.breedEditText);
        breedEditText.setText(cat.getBreed());

        final EditText weightEditText = (EditText) mView.findViewById(R.id.weightEditText);
        weightEditText.setText(""+cat.getWeight());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        MyIntentService.update(context, receiver,
                                new Cat(cat.getId(), nameEditText.getText().toString(),
                                        Integer.parseInt(ageEditText.getText().toString()),
                                        colorEditText.getText().toString(),
                                        breedEditText.getText().toString(),
                                        Integer.parseInt(weightEditText.getText().toString())));
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
}
