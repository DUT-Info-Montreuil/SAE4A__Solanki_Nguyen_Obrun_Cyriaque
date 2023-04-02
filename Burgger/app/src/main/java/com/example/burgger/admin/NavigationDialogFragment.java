package com.example.burgger.admin;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.burgger.home.HomeActivity;

public class NavigationDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Choose a page")
                .setItems(new CharSequence[]{"Home", "Admin"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        if (which == 0) {
                            intent = new Intent(requireActivity(), HomeActivity.class);
                        } else {
                            intent = new Intent(requireActivity(), AdminActivity.class);
                        }
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}