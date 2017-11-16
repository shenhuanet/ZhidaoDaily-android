package com.shenhua.zhidaodaily.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by shenhua on 2017-11-02-0002.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class PermissionDeclarationDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Fragment parent = getParentFragment();
        return new AlertDialog.Builder(getActivity())
                .setMessage("需要赋予读写SD卡权限")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parent.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MainActivity.REQUEST_STORAGE_PERMISSION);
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "权限不足", Toast.LENGTH_SHORT).show();
                            }
                        })
                .create();
    }
}