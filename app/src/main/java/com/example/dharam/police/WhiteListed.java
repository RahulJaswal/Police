package com.example.dharam.police;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WhiteListed extends Fragment
{
    private ArrayList<Data> arrayList=new ArrayList<>();
    private DatabaseReference mDatabase;
    ListView whiteList;
    private ProgressBar loading;

    public WhiteListed()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_white_listed, container, false);

        loading=view.findViewById(R.id.whiteLoading);
        whiteList=view.findViewById(R.id.whiteListView);

        loading.setVisibility(View.VISIBLE);
        loadData();

        return view;

    }


    void loadData()
    {
        mDatabase= FirebaseDatabase.getInstance().getReference("Cars");

        Query query=mDatabase.orderByChild("status").equalTo("White_List");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                    arrayList.add(new Data(snapshot.getValue(Car.class).getRegistration_no(),
                            snapshot.getValue(Car.class).getBrand(),snapshot.getValue(Car.class).getModel(),
                            snapshot.getValue(Car.class).getColor()));
                }
                whiteList.setAdapter(new PopulateDataAdapter(getActivity(),arrayList));
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e("Msg","Error Occurred");
            }
        });

    }
}


