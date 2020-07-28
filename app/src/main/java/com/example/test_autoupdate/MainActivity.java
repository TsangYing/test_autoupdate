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
import java.io.FileNotFoundException;

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
                        final DownloadManager downloadManager;
                        downloadManager = (DownloadManager)getSystemService(serviceString);
                        Uri uri = Uri.parse(urlApp);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        final long reference = downloadManager.enqueue(request);
                        request.setAllowedNetworkTypes(request.NETWORK_WIFI|request.NETWORK_MOBILE);
                       //request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "Bugdroid.png"); //下載檔案加入到APP私有目錄下()
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "test.apk");
                        downloadManager.enqueue(request);
//                        DownloadManager.Query  query = new DownloadManager.Query();
//                        query.setFilterById(reference);
//                        Cursor myDownload = downloadManager.query(query);
//                        int fileNameIdx=0;
//                        int fileUriIdx=0;
//                        int fileSizeIdx=0;
//                        int bytesDLIdx = 0;
//                        if (myDownload.moveToFirst()) {
//                            fileNameIdx =
//                                    myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME); //文件名
//                            fileUriIdx =
//                                    myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI); //文件uri
//                            fileSizeIdx =
//                                    myDownload.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);//文件大小
//                            bytesDLIdx =
//                                    myDownload.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);//文件下載大小
//                        }
//                        System.out.println("123456"+fileNameIdx);
//                        System.out.println("123456"+fileSizeIdx);
//                        System.out.println("123456"+fileUriIdx);
//                        System.out.println("123456"+bytesDLIdx);
//                        myDownload.close();
                        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        BroadcastReceiver receiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                long myDownloadReference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                if (myDownloadReference == reference) {
                                    Intent install = new Intent(Intent.ACTION_VIEW);
                                    Uri downloadFileUri = downloadManager.getUriForDownloadedFile(reference);
                                    if (downloadFileUri != null) {
//                                        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
//                                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        getApplication().startActivity(install);
                                        File apkFile = new File(Environment.getExternalStorageDirectory() + "/download/" + "test.apk");
                                        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                                        startActivity(install);
                                    }
                                    else {
                                        Log.e("DownloadManager", "download error");
                                    }
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