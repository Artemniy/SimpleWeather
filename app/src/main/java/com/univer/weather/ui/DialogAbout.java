package com.univer.weather.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.univer.weather.R;

import org.jetbrains.annotations.NotNull;

public class DialogAbout extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String MAILTO_SCHEME = "mailto";

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_about, container, false);
        view.findViewById(R.id.close).setOnClickListener(this);
        view.findViewById(R.id.developer_mail).setOnClickListener(this);
        TextView version = view.findViewById(R.id.version);
        version.setText(String.format("v%s", getAppVersion()));
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.developer_mail) {
            if (getContext() != null)
                sendEmail(getContext());
        } else if (view.getId() == R.id.close) {
            dismiss();
        }
    }

    private String getAppVersion() {
        try {
            if (getContext() != null) {
                PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                return pInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    @SuppressLint("QueryPermissionsNeeded")
    public static void sendEmail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(MAILTO_SCHEME, context.getString(R.string.developer_mail), null));
        if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.send_email)));
        } else {
            Toast.makeText(context, R.string.no_apps_can_do_action, Toast.LENGTH_SHORT).show();
        }
    }

}
