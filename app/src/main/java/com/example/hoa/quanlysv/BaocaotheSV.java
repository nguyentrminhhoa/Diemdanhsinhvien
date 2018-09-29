package com.example.hoa.quanlysv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.codec.Base64;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.lang.String;

import com.itextpdf.*;

/**
 * Created by hoa on 9/9/2018.
 */

public class BaocaotheSV extends Activity {
    Button btnttt;
    SQLiteDatabase database =null;
    private ImageView im;
    private String content;
    private TextView msv,tvMaSV;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baocaothesv);
        pd = new ProgressDialog(BaocaotheSV.this);

        database = openOrCreateDatabase("sinhviendatabase.sqlite", MODE_PRIVATE, null);
        Cursor cursor = database.query("sinhvien", null, null, null, null, null, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                TextView tvMaSv1 = (TextView) findViewById(R.id.tvMaSV);
                tvMaSv1.setText("" + cursor.getString(0));
                TextView txtTenSV1 = (TextView) findViewById(R.id.tvName);
                txtTenSV1.setText("" + cursor.getString(1));
                TextView txtNamSinh1 = (TextView) findViewById(R.id.tvNamSinh);
                txtNamSinh1.setText("" + cursor.getString(3));
                TextView txtTenLop1 = (TextView) findViewById(R.id.tvTenLop);
                txtTenLop1.setText("" + cursor.getString(4));
                ImageView imv = (ImageView) findViewById(R.id.tvGT);
                if (cursor.getString(2).equalsIgnoreCase("nam"))
                    imv.setImageResource(R.drawable.boy);
                else
                    imv.setImageResource(R.drawable.girl);
                cursor.moveToNext();
            }
            while (cursor.moveToNext());
        }
        database.close();
        btnttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showthongbao();
            }
        });

    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private File saveBitMap(Context context, View drawView){
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"AnhTheSinhVien");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() +File.separator+ System.currentTimeMillis() +".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap =getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }

    public void showthongbao(){

        pd.show();
        RelativeLayout savingLayout =(RelativeLayout)findViewById(R.id.relativeLayout2);
        File file = saveBitMap(BaocaotheSV.this, savingLayout);
        if (file != null) {
            pd.cancel();
            Toast.makeText(this, "Đã luu anh thanh cong", Toast.LENGTH_LONG).show();
        } else {
            pd.cancel();
            Toast.makeText(this, "Luu anh that bai", Toast.LENGTH_LONG).show();
        }

    }

}
