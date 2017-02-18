package com.cats.android.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cats.android.R;
import com.cats.android.repository.CatRepository;
import com.cats.android.model.Cat;
import com.cats.android.util.WebManager;

/**
 * Created by andrey on 14.02.17.
 */

public final class CatAlertDialog {

    private CatAlertDialog() {
    }

    public static void openCreateCatDialog(final Context context, final ResultReceiver receiver) {
        openCatDialog(context, receiver, null);
    }

    public static void openUpdateCatDialog(final Context context, final ResultReceiver receiver, Integer id) {
        final Cat cat = CatRepository.getItemMap().get(id);
        if (cat == null) {
            return;
        }
        openCatDialog(context, receiver, cat);
    }

    private static void openCatDialog(final Context context, final ResultReceiver receiver, final Cat cat) {
        final String action;
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final TextView titleTextView = (TextView) mView.findViewById(R.id.dialogTitle);
        final EditText nameEditText = (EditText) mView.findViewById(R.id.nameEditText);
        final EditText ageEditText = (EditText) mView.findViewById(R.id.ageEditText);
        final EditText colorEditText = (EditText) mView.findViewById(R.id.colorEditText);
        final EditText breedEditText = (EditText) mView.findViewById(R.id.breedEditText);
        final EditText weightEditText = (EditText) mView.findViewById(R.id.weightEditText);

        if (cat == null) {
            action = "Create";
        } else {
            action = "Update";
            nameEditText.setText(cat.getName());
            ageEditText.setText("" + cat.getAge());
            colorEditText.setText(cat.getColor());
            breedEditText.setText(cat.getBreed());
            weightEditText.setText("" + cat.getWeight());
        }
        titleTextView.setText(action + " Cat");

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(action, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Cat editCat;
                        if (cat != null) {
                            editCat = new Cat(cat.getId(), nameEditText.getText().toString(),
                                    Integer.parseInt(ageEditText.getText().toString()),
                                    colorEditText.getText().toString(),
                                    breedEditText.getText().toString(),
                                    Integer.parseInt(weightEditText.getText().toString()));
                            WebManager.update(context, receiver, editCat);
                        } else {
                            editCat = new Cat(nameEditText.getText().toString(),
                                    Integer.parseInt(ageEditText.getText().toString()),
                                    colorEditText.getText().toString(),
                                    breedEditText.getText().toString(),
                                    Integer.parseInt(weightEditText.getText().toString()));
                            WebManager.create(context, receiver, editCat);
                        }
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
