package com.example.dharam.police;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
public class BlackListed extends Fragment
{
    private ListView blackList;
    private ArrayList<Data> arraayList=new ArrayList<>();
    private DatabaseReference mDatabase;
    private ProgressBar loading;

    public BlackListed()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_black_listed, container, false);
        loading=view.findViewById(R.id.loading);

        blackList=view.findViewById(R.id.blackListView);

        loading.setVisibility(View.VISIBLE);
        loadData();

        //loading.setVisibility(View.INVISIBLE);

        return view;

    }


    void loadData()
    {
        //Log.d("Msg","Into Load Data");

        mDatabase= FirebaseDatabase.getInstance().getReference("Cars");

        Query query=mDatabase.orderByChild("status").equalTo("Black_List");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
               arraayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                  arraayList.add(new Data(snapshot.getValue(Car.class).getRegistration_no(),
                           snapshot.getValue(Car.class).getBrand(),snapshot.getValue(Car.class).getModel(),
                           snapshot.getValue(Car.class).getColor()));
                  // Log.d("Msg","Loading data");
                }
                blackList.setAdapter(new PopulateDataAdapter(getActivity(),arraayList));
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

class Data
{
    public String registration_no;
    public String brand;
    public String model;
    public String color;

    public Data(String registration_no, String brand, String model, String color) {
        this.registration_no = registration_no;
        this.brand = brand;
        this.model = model;
        this.color = color;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }
}

//Custom-ArrayAdapter
class PopulateDataAdapter extends ArrayAdapter<Data>
{
    Context context;
    ArrayList arrayList;

    public PopulateDataAdapter(Context context, ArrayList<Data> objects)
    {
        super(context,R.layout.carview,objects);
        this.arrayList=objects;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.carview,parent,false);
        TextView regNo=convertView.findViewById(R.id.regnoTextView);
        TextView brand=convertView.findViewById(R.id.brandTextView);
        TextView model=convertView.findViewById(R.id.modelTextView);
        TextView color=convertView.findViewById(R.id.colorTextView);





        Data obj=(Data) this.arrayList.get(position);
        regNo.setText(obj.getRegistration_no());
        brand.setText(obj.getBrand());
        model.setText(obj.getModel());
        color.setText(obj.getColor());

        return convertView;
    }
}

