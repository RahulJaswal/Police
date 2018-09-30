package com.example.dharam.police;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class ScanCar extends AppCompatActivity
{
    CameraSource cameraSource=null;
    ImageView cameraView;
    EditText textView;
    ImageButton capture;
    TextRecognizer textRecognizer;
    TextView locationView;
    Location location=null;
    LocationManager locationManager=null;
    Button submit;
    Intent intent;
    double latitude=0.0d;
    double longitude=0.0d;
    String Car_Number;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_car);

        cameraView=findViewById(R.id.imageView);
        textView=findViewById(R.id.text_view);
        capture = findViewById(R.id.capture);
        locationView=findViewById(R.id.location);

        textView.setFilters(new InputFilter[] {new InputFilter.AllCaps()});     //capatilize all letters


        textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();

        //click location view to load location
             getLocation(getApplicationContext());

        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation(getApplicationContext());
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textRecognizer.isOperational())
                {
                    Toast.makeText(getApplicationContext(),"Depndencies not loaded yet !",Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                            .setAutoFocusEnabled(true)
                            .setFacing(CameraSource.CAMERA_FACING_BACK)
                            .setRequestedPreviewSize(1280, 1024)
                            .setRequestedFps(2.0f)
                            .build();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) !=
                            PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(ScanCar.this,
                                new String[]{android.Manifest.permission.CAMERA}, 101);
                        return;
                    }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
            }

        });

        //Submit Listener.
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                intent=new Intent(ScanCar.this,GetCarDetails.class);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                Car_Number=textView.getText().toString().trim();
                intent.putExtra("Car_Number",Car_Number);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode,int resutCode,Intent data)
    {
        if(resutCode==RESULT_OK)
        {
            Bundle extra=data.getExtras();
            Bitmap imap= (Bitmap) extra.get("data");
            cameraView.setImageBitmap(imap);

            Frame frame=new Frame.Builder().setBitmap(imap).build();
            final SparseArray<TextBlock> items =textRecognizer.detect(frame);
            if (items.size() != 0 ){

                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i=0;i<items.size();i++){
                            TextBlock item = items.valueAt(i);
                            stringBuilder.append(item.getValue());
                            stringBuilder.append("\n");
                        }
                        textView.setText(stringBuilder.toString());
                    }
                });
            }
        }
    }

    void getLocation(Context context)
    {
        locationManager=(LocationManager) getSystemService(context.LOCATION_SERVICE);


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            ActivityCompat.requestPermissions(ScanCar.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }



        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null)
        {
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            locationView.setText("Latitude:" + latitude + ", Longitude:" + longitude);
            Log.d("Location","Set Text initialized");
        }
        else
        {
            Toast.makeText(context, "Unable to track location", Toast.LENGTH_SHORT).show();
        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("please turn of your gps connection").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                    ;
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }


}
