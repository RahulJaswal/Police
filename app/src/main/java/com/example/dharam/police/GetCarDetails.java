package com.example.dharam.police;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.dharam.police.MyArsenal;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GetCarDetails extends AppCompatActivity
{
    TextView carText;
    TextView carStatus;
    String Car_number;
    double longitude=0.0d;
    double latitude=0.0d;
    private DatabaseReference mReference;
    LinearLayout myLayout;

    MyArsenal obj=new MyArsenal();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_car_details);

        carText = findViewById(R.id.carText);
        carStatus = findViewById(R.id.carStatus);
        myLayout = findViewById(R.id.myLayout);
        Car_number = getIntent().getExtras().getString("Car_Number");
        longitude = getIntent().getExtras().getDouble("longitude");
        latitude = getIntent().getExtras().getDouble("latitude");

        mReference = FirebaseDatabase.getInstance().getReference("Cars");
        Query query = mReference.orderByChild("registration_no").equalTo(Car_number);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                DataSnapshot snapshot = null;
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    count++;
                    snapshot = shot;
                }


                if (count == 0) {
                    carText.setVisibility(View.VISIBLE);
                } else {
                    carStatus.setVisibility(View.VISIBLE);
                    if (snapshot.getValue(Car.class).getStatus().equals("Black_List")) {
                        carStatus.setBackgroundColor((int) Color.RED);
                        carStatus.setText("Black Listed");

                    } else {
                        carStatus.setBackgroundColor((int) Color.parseColor("#00FF00"));
                        carStatus.setText("White Listed");

                    }

                    //inflate carview here
                    inflateCar(snapshot);

                    //TODO: handle location here
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void inflateCar(DataSnapshot snapshot) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View carView = inflater.inflate(R.layout.carview2, null);

        final TextView regNo = carView.findViewById(R.id.regnoTextView);
        final TextView brand = carView.findViewById(R.id.brandTextView);
        final TextView model = carView.findViewById(R.id.modelTextView);
        final TextView color = carView.findViewById(R.id.colorTextView);

        regNo.setText(snapshot.getValue(Car.class).getRegistration_no());
        brand.setText(snapshot.getValue(Car.class).getBrand());
        model.setText(snapshot.getValue(Car.class).getModel());
        color.setText(snapshot.getValue(Car.class).getColor());

        //view Last Location here
        carView.findViewById(R.id.viewLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(obj.ifLocationExists((String) regNo.getText()))
                {
                    //get longitude and latitude here
                    latitude=obj.getLatitude();
                    longitude=obj.getLongitude();

                    //Todo: google map activity from here
                    Toast.makeText(GetCarDetails.this, "Map activity will open.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(GetCarDetails.this, "No Location is available for this car.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //save location
        carView.findViewById(R.id.saveLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (longitude == 0.0d && latitude == 0.0d) {
                    Toast.makeText(GetCarDetails.this, "Location Parameters are not initialized !", Toast.LENGTH_LONG).show();
                } else {
                    obj.saveLocation(longitude, latitude, (String) regNo.getText());
                    Toast.makeText(GetCarDetails.this, "Location added successfully.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        //add this view now
        myLayout.addView(carView);


    }
}
