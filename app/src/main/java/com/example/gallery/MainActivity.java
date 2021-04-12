package com.example.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
   // private GridView gridview = null;
    private Cursor cc = null;//zapewnia losowy dostęp do odczytu i zapisu do wyników zwróconego przez zapytanie do bazy danych.

    private ProgressDialog myProgressDialog = null;

    public Uri[] getmUrls(){
        return mUrls;
    }
    public Cursor getCc(){
        return cc;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /* if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            setContentView(R.layout.activity_main);
            return;
        }//sprawdza czy sa permissions w manifest
        //else {
            Log.e("setContent", "przed adapterem");

        setContentView(R.layout.activity_main);

        cc = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null) ;//pobiera zdj z pamieci(obu) w postaci sciezek
      /*  cc2=this.getContentResolver().query(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,null,null,null,
                null);*/
        // MediaStore.Images.Media.INTERNAL_CONTENT_URI
        // File[] files=f.listFiles();
        if (cc != null) {

            myProgressDialog = new ProgressDialog(MainActivity.this);
            myProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myProgressDialog.setMessage(getResources().getString(R.string.pls_wait_txt));
            Log.i("Wait_Dialog","Please wait dialog");
            //myProgressDialog.setIcon(R.drawable.blind);
            myProgressDialog.show();

            new Thread() {
                public void run() {
                    try {
                      cc.moveToFirst();//cc2.moveToFirst();Przenosi kursor do pierwszego wiersza
                        mUrls = new Uri[cc.getCount()];//+cc2.getCount()
                        strUrls = new String[cc.getCount()];
                        mNames = new String[cc.getCount()];
                        for (int i = 0; i < cc.getCount(); i++) {//+2
                            cc.moveToPosition(i);
                            mUrls[i] = Uri.parse(cc.getString(1));
                            strUrls[i] = cc.getString(1);//Zwraca wartość żądanej kolumny jako ciąg.
                            mNames[i] = cc.getString(3);
                            //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
                        }

                    } catch (Exception e) {
                    }
                    myProgressDialog.dismiss();
                }
            }.start();

       GridView gridview = (GridView) findViewById(R.id.gridview);
        Log.e("adapter", "przed adapterem");
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {

                Intent i = new Intent(getApplicationContext(), BigImageActivity.class);

                i.putExtra("id", position);
               // i.putExtra("mUrls",mUrls);
                startActivity(i);
            }
        });

        TextView textview =(TextView) findViewById(R.id.textView);
        textview.setText("Lczba zdjęć: "+cc.getCount());
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                GridView gridview = (GridView) findViewById(R.id.gridview);
                Log.e("adapter", "przed adapterem");
                gridview.setAdapter(new ImageAdapter(this));

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View v, int position, long id) {


                        Intent i = new Intent(getApplicationContext(), BigImageActivity.class);

                        i.putExtra("id", position);
                        //i.putExtra("mUrls",mUrls);
                        //i.set.setData(mUrls);
                      //  i.putExtra("strUrls",strUrls);
                        startActivity(i);
                    }
                });
                TextView textview =(TextView) findViewById(R.id.textView);
                textview.setText("Lczba zdjęć: "+cc.getCount());
            } else {
                // User refused to grant permission.
            }
        }
    }

    public  class ImageAdapter extends BaseAdapter {
        private Context mContext;

        // Constructor
        public ImageAdapter(Context c) {
            mContext = c;
            Log.e("adapter","w adapter");
        }

        public int getCount() {
           // MainActivity mA=new MainActivity();
            return cc.getCount();//mThumbIds.length; Zwraca liczbę wierszy w kursorze
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 400));
                //imageView.setMaxHeight(150);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            }
            else
            {
                imageView = (ImageView) convertView;
            }

         //  imageView.setImageURI(mUrls[position+1080]);
         //  Picasso.get().load(strUrls[1]).fit().into(imageView);
            //Picasso instance = new Picasso.Builder(mContext).executor(Executors.newSingleThreadExecutor()).memoryCache(Cache.NONE).indicatorsEnabled(true).build();
            //File[] files= new File[strUrls.length];
            ArrayList<File> urlFiles= new ArrayList();
            for(int i=0;i<strUrls.length;i++){
                Log.i("File index", String.valueOf(i));
               urlFiles.add(new File(strUrls[i]));
            }
           // File f = new File(strUrls[0]);
           //Picasso.get().load("https://i.imgur.com/DvpvklR.png").placeholder(R.drawable.ic_launcher_background).into(imageView);
            Picasso.get().load(urlFiles.get(position)).into(imageView);
            Log.i("picasso","picasso done"+ strUrls[1] +" i "+mNames[1]);
            //imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }


    }
}
