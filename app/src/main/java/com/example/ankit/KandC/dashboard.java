package com.example.ankit.KandC;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 03-02-2018.
 */

public class dashboard extends Fragment {
    private List<Product> productList;//List to hold products
    private Spinner sp1, sp2;// spinners
    DatabaseReference mDataRef, mchild, mProduct,mDeleteRef;
    RecyclerView recyclerView;
    ImageView Proimage;
    ImageButton deleteBtn;
    private String productname1;
    private String productname;
    ArrayList<String> BrandNames;
    ArrayList<String> productNames;
    ProductAdapter adapter;//adapter to link products
    public static String brand;
    public ImageView img;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard,container,false);
        BrandNames = new ArrayList<String>();//Brand Names
        productNames = new ArrayList<String>();//Product Names
        productList = new ArrayList<>();

        deleteBtn = (ImageButton)view.findViewById(R.id.delete);
        Proimage = (ImageView) view.findViewById(R.id.pImage);
        sp1 = (Spinner) view.findViewById(R.id.SpBrand);
        sp2 = (Spinner) view.findViewById(R.id.spin);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //creating recyclerview adapter
        mDataRef = FirebaseDatabase.getInstance().getReference();
        mchild = FirebaseDatabase.getInstance().getReference();
        mProduct = FirebaseDatabase.getInstance().getReference();
        mDataRef.child("ProductBrand").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BrandNames.clear();
                for (DataSnapshot brandSnapshot : dataSnapshot.getChildren()) {
                    String brandname = brandSnapshot.getKey();
                    BrandNames.add(brandname);
                }

                ArrayAdapter<String> BrandAdapter   = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, BrandNames);
                BrandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp1.setAdapter(BrandAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                brand = sp1.getSelectedItem().toString();


                //Toast.makeText(getActivity(), brand, Toast.LENGTH_SHORT).show();

                mchild.child("ProductBrand").child(brand).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        productNames.clear();

                        for (DataSnapshot brandSnapshot : dataSnapshot.getChildren()) {
                            productname = brandSnapshot.getKey().toString();
                            productNames.add(productname);
                        }

                        ArrayAdapter<String> BrandAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, productNames);
                        BrandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(BrandAdapter);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productname1 = sp2.getSelectedItem().toString();
                mDeleteRef = FirebaseDatabase.getInstance().getReference(brand).child(productname1);
                //Toast.makeText(getActivity(), productname1, Toast.LENGTH_LONG).show();
                mProduct.child("ProductBrand").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot psnapshot = dataSnapshot.child(brand).child(productname1);
                        productList.clear();

                        for(DataSnapshot dataSnapshot1:psnapshot.getChildren()){
                            String price = dataSnapshot1.child("pPrice").getValue().toString();
                            String description = dataSnapshot1.child("pDescription").getValue().toString();
                            String image = dataSnapshot1.child("imgurl").getValue().toString();
                            productList.add(
                                    new Product("id",
                                            productname1,
                                            description,
                                            price,
                                            image
                                            ));


                            adapter = new ProductAdapter(getContext(), productList);
                            recyclerView.setAdapter(adapter);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                /*deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Dashboard");

    }

}
