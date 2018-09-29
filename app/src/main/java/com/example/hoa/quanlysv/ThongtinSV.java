package com.example.hoa.quanlysv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;


public class ThongtinSV extends Activity{
    SQLiteDatabase database =null;
    String in="";
    Button btncapnhat,btnback,btnin,btninall;

    // QR
    private ImageView im;
    private TextView msv;
    private String content;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baocaothesv);
        pd = new ProgressDialog(ThongtinSV.this);

        database=openOrCreateDatabase("sinhviendatabase.sqlite",MODE_PRIVATE,null);
        Bundle bundle=getIntent().getExtras();
        in=bundle.getString("mssv");
        final Cursor cursor=database.rawQuery("SELECT * FROM sinhvien where masv= '"+in+"'",null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    TextView tvMaSv1=(TextView)findViewById(R.id.tvMaSV);
                    tvMaSv1.setText(""+cursor.getString(0));
                    EditText txtTenSV1=(EditText)findViewById(R.id.tvName);
                    txtTenSV1.setText(""+cursor.getString(1));
                    //EditText txtGT1=(EditText)findViewById(R.id.test);
                    //txtGT1.setText(""+cursor.getString(2));
                    EditText txtNamSinh1=(EditText)findViewById(R.id.tvNamSinh);
                    txtNamSinh1.setText(""+cursor.getString(3));
                    EditText txtTenLop1=(EditText)findViewById(R.id.tvTenLop);
                    txtTenLop1.setText(""+cursor.getString(4));

                    ImageView imv = (ImageView) findViewById(R.id.tvGT);
                    if (cursor.getString(2).equalsIgnoreCase("nam"))
                        imv.setImageResource(R.drawable.boy);
                    else
                        imv.setImageResource(R.drawable.girl);
                }
                while (cursor.moveToNext());
            }
            database.close();
            createQRcode();
        }



        btncapnhat=(Button)findViewById(R.id.btnCapNhat);
        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capnhatsinhvien();
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });
        btnback=(Button)findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });
        btnin = (Button)findViewById(R.id.btnIn);
        btnin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showthongbao();
            }
        });
        btninall = (Button)findViewById(R.id.btninall);
        btninall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),Testinall.class);
                startActivity(in);
            }
        });
    }

    /*public void inallthe(){

        Cursor c=database.query("sinhvien",null,null,null,null,null,null);
        c.moveToFirst();
        while (!c.isAfterLast()){

            TextView tvMaSv1=(TextView)findViewById(R.id.tvMaSV);
            tvMaSv1.setText(""+c.getString(0));
            EditText txtTenSV1=(EditText)findViewById(R.id.tvName);
            txtTenSV1.setText(""+c.getString(1));
            //EditText txtGT1=(EditText)findViewById(R.id.test);
            //txtGT1.setText(""+cursor.getString(2));
            EditText txtNamSinh1=(EditText)findViewById(R.id.tvNamSinh);
            txtNamSinh1.setText(""+c.getString(3));
            EditText txtTenLop1=(EditText)findViewById(R.id.tvTenLop);
            txtTenLop1.setText(""+c.getString(4));

            ImageView imv = (ImageView) findViewById(R.id.tvGT);
            if (c.getString(2).equalsIgnoreCase("nam"))
                imv.setImageResource(R.drawable.boy);
            else
                imv.setImageResource(R.drawable.girl);
            createQRcode();
            showthongbao();
            c.moveToNext();
        }
    }
*/

    public void capnhatsinhvien()
    {
        database=openOrCreateDatabase("sinhviendatabase.sqlite",MODE_PRIVATE,null);
        TextView tvMaSv1=(TextView)findViewById(R.id.tvMaSV);;
        EditText txtTenSV1=(EditText)findViewById(R.id.tvName);
        EditText txtGT1=(EditText)findViewById(R.id.test);
        EditText txtNamSinh1=(EditText)findViewById(R.id.tvNamSinh);
        EditText txtTenLop1=(EditText)findViewById(R.id.tvTenLop) ;
        ContentValues sv=new ContentValues();
        sv.put("tensv", txtTenSV1.getText().toString());
        sv.put("gioitinh", txtGT1.getText().toString());
        sv.put("namsinh",txtNamSinh1.getText().toString());
        sv.put("tenlop",txtTenLop1.getText().toString());
        if(database.update("sinhvien",sv,"masv="+tvMaSv1.getText().toString(),null)!=0)
        {
            Toast.makeText(this, "Đã cập nhật thành công", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Có lỗi",Toast.LENGTH_LONG).show();
        }
    }

    // QR

    public void createQRcode() {
        im = (ImageView) findViewById(R.id.tvqr);
        Intent intent=getIntent();
        content=intent.getStringExtra("mssv");
        msv = (TextView) findViewById(R.id.tvMaSV);
        msv.setText(content);
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(content, 125,125,null);
        if(qrCodeBitmap!=null) im.setImageBitmap(qrCodeBitmap);
        else Toast.makeText(this, "Loi cu phap", Toast.LENGTH_SHORT).show();
        im.setScaleType(ImageView.ScaleType.FIT_XY);
    }


    // export data
    public void showthongbao(){

        pd.show();
        RelativeLayout savingLayout =(RelativeLayout)findViewById(R.id.relativeLayout2);
        File file = saveBitMap(ThongtinSV.this, savingLayout);
        if (file != null) {
            pd.cancel();
            Toast.makeText(this, "Xuất Thẻ SV Thành Công", Toast.LENGTH_LONG).show();
        } else {
            pd.cancel();
            Toast.makeText(this, "Xuất Thẻ Thất Bại", Toast.LENGTH_LONG).show();
        }

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
                Log.i("TAG", "Không thể tạo thư mục để lưu hình");
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
            Log.i("TAG", "Có lỗi khi lưu ảnh.");
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
            Log.i("TAG", "Có lỗi khi scan thư viện.");
        }
    }

/*
    public void showthongbao(View view){
        RelativeLayout layout =(RelativeLayout)findViewById(R.id.relativeLayout2);
        File file = saveBitMap(this, layout);    //which view you want to pass that view as parameter
        if (file != null) {
            Log.i("TAG", "Drawing saved to the gallery!");
        } else {
            Log.i("TAG", "Oops! Image could not be saved.");
        }
    }

    private File saveBitMap(Context context, View drawView){
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() +File.separator+ System.currentTimeMillis()+".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap =getBitmapFromView(drawView);
        try {
            createPdf(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void scanGallery(Context context, String path) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "Có lỗi khi scan thư viện.");
        }
        }


    //create bitmap from view and returns it
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
    private void createPdf(Bitmap bitmap) throws IOException, DocumentException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image signature;
        signature = Image.getInstance(stream.toByteArray());
        signature.setAbsolutePosition(50f, 100f);
        signature.scalePercent(60f);
        //Image image = Image.getInstance(byteArray);
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
            Log.i("Created", "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(myFile);
        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
        document.add(signature);
        //document.add(new Paragraph(text.getText().toString()));
        //document.add(new Paragraph(mBodyEditText.getText().toString()));

        //Step 5: Close the document
        document.close();
    }
    */


}
