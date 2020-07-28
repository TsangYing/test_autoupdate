package com.example.test_autoupdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements  UpdateHelper.OnUpdateCheckListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateHelper.with(this).onUpdateCheck(this).check();
    }

    @Override
    public void onUpdateCheckListener(final String urlApp) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("New Version Available")
                .setMessage("Please update to new version to continue use")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //begin download
                        DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                        String serviceString = Context.DOWNLOAD_SERVICE;
                        DownloadManager downloadManager;
                        downloadManager = (DownloadManager)getSystemService(serviceString);
                        Uri uri = Uri.parse(urlApp);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        final long reference = downloadManager.enqueue(request);
                        request.setAllowedNetworkTypes(request.NETWORK_WIFI|request.NETWORK_MOBILE);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "test.apk");
                        downloadManager.enqueue(request);
                        //
                        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        BroadcastReceiver receiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                                final long  myDownloadReference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                if (reference == myDownloadReference) {
                                    System.out.println("123123"+reference);
                                }
                            }
                        };
                        registerReceiver(receiver, filter);
                        //begin running apk


                    }

                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//Want to that user can select to update?
                        dialogInterface.dismiss();
                        System.exit(0);
                    }

                }).create();

        alertDialog.show();

    }

}