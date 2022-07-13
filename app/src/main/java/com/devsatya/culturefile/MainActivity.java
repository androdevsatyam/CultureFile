package com.devsatya.culturefile;

import static com.devsatya.culturefile.AccessHelper.ASKPERMISSION;
import static com.devsatya.culturefile.AccessHelper.WORKINGPKG;
import static com.devsatya.culturefile.AccessHelper.dataModels;
import static com.devsatya.culturefile.AccessHelper.makeToast;
import static com.devsatya.culturefile.AccessHelper.permission;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingComponent;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebHistoryItem;
import android.widget.Toast;

import com.devsatya.culturefile.databinding.ActivityMainBinding;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    final String TAG = "MAINACTIVITY";
    String dest = "in.culturemap";

    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            readFile(data.getData());
                        }
                    }
                });

    }

//    private void readFile(Uri data) {
//        try {
//            File csvfile = new File(data.getPath());
//            CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));
//            String[] nextLine;
//            while ((nextLine = reader.readNext()) != null) {
//                // nextLine[] is an array of values from the line
//                System.out.println(nextLine[0] + nextLine[1] + "etc...");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
//        }
//    }

    String[] row;

    private void readFile(Uri data) {
        InputStream is;
        BufferedReader reader;
        String lineread;
        dataModels.clear();
        try {
            is=getContentResolver().openInputStream(data);
            reader=new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((lineread = reader.readLine()) != null) {
                row=lineread.split(",");
                DataModel dataModel=new DataModel(row[0],row[1]);
                dataModels.add(dataModel);

                Log.d(TAG, "readFile: "+lineread);
            }
            startAutomate();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void Perform(View view) {

        try {
            if (AccessHelper.checkpermission(this)) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("text/*");
                launcher.launch(i);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permission, ASKPERMISSION);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Perform: " + e.getMessage());
        }


    }

    public void startAutomate() {
        if (AccessHelper.isAccessibilityOn(this, FilingService.class)) {
            FilingService.start = true;
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(WORKINGPKG);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            makeToast(this, "Turn On CultureFile Accessibility", Toast.LENGTH_LONG);
            startActivity(intent);
        }
    }
}