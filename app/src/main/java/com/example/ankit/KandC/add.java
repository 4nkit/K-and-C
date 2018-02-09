package com.example.ankit.KandC;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ankit on 31-01-2018.
 */

public class add extends Fragment{
    public static String url=null;
    public static final int GALLERY_INTENT=2;
    EditText pName,pBrand,pPrice,pDescription;
    Button addBtn,uploadBtn;
    DatabaseReference databasepBrand;
    StorageReference mStorageReference;
    ImageView pImage;
    private ProgressDialog mProgress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add,container,false);
        pBrand = (EditText) view.findViewById(R.id.pBrand);
        mStorageReference = FirebaseStorage.getInstance().getReference("Photos");
        pName = (EditText) view.findViewById(R.id.pName);
        pImage = (ImageView)view.findViewById(R.id.pImage);
        uploadBtn = (Button)view.findViewById(R.id.uploadBtn);
        pPrice = (EditText) view.findViewById(R.id.pPrice);
        mProgress = new ProgressDialog(getContext());
        pDescription= (EditText) view.findViewById(R.id.pDescription);
        addBtn = (Button) view.findViewById(R.id.addButton);
        databasepBrand = FirebaseDatabase.getInstance().getReference("ProductBrand");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });


    return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK ){
            mProgress.setMessage("Uploading...");
            mProgress.show();
            Uri uri = data.getData();
            pImage.setImageURI(uri);
            pImage.setVisibility(View.VISIBLE);
            StorageReference filepath = mStorageReference.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"Upload Done",Toast.LENGTH_LONG).show();
                    url = taskSnapshot.getDownloadUrl().toString();
                    mProgress.dismiss();
                }
            });
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add");
    }
    public void add(){
        String brand = pBrand.getText().toString();
        String name = pName.getText().toString();
        String description = pDescription.getText().toString();
        String price = pPrice.getText().toString();
        if(!TextUtils.isEmpty((CharSequence) name)){
            String id = databasepBrand.push().getKey();
            Product product = new Product(id,name,price,description,url);
            databasepBrand.child(brand).child(name).child(id).setValue(product);
            Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getContext(),"Enter name first",Toast.LENGTH_SHORT).show();
        }
    }

}


