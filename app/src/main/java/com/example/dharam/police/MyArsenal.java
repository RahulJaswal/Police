package com.example.dharam.police;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Dharam on 7/9/2018.
 */

public class MyArsenal
{
    private DatabaseReference mReference;
    String registrationNo;
    boolean flag=true;
    double longitude;
    double latitude;

    //returns true if device is connected to internet else return false
    public boolean connectivity( ConnectivityManager connectivityManager)
    {
        /* if(new MyArsenal().connectivity( (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)))*/

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            return true;
        }
        else
            return false;

    }

    //method to check weather the location of car exists in database or not
    public boolean ifLocationExists(String registrationNo)
    {
        this.registrationNo =registrationNo.trim().replaceAll("\\s+","");
        mReference = FirebaseDatabase.getInstance().getReference("Location");
        Query query = mReference.orderByChild("registrationNo").equalTo(registrationNo);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                DataSnapshot snapshot = null;
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    count++;
                    snapshot = shot;
                }

                if (count == 0)
                {
                    flag=false;
                }
                else
                {
                    latitude=snapshot.getValue(CarLocation.class).getLatitude();
                    longitude=snapshot.getValue(CarLocation.class).getLongitude();
                    flag=true;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  flag;
    }


    //method to get latitude
    public double getLatitude()
    {
            return latitude;
    }

    //method to get longitude
    public double getLongitude()
    {
        return longitude;
    }

    //mehtod to save location in database
    public void saveLocation(double longitude,double latitude,String registrationNo)
    {
        this.registrationNo =registrationNo.trim().replaceAll("\\s+","");
            mReference=FirebaseDatabase.getInstance().getReference("Location");
            CarLocation location=new CarLocation(longitude+"",latitude+"",registrationNo);
            mReference.child(registrationNo).setValue(location);

    }
}
