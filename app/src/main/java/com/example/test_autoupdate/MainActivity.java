package com.example.test_autoupdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

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
                        Toast.makeText(MainActivity.this, "open:"+urlApp,Toast.LENGTH_SHORT).show();
                        /*try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urlApp));
                        startActivity(intent);
                        System.exit(0);//關閉App
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