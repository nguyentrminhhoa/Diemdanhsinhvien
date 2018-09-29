package com.example.hoa.quanlysv;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Testinall extends AppCompatActivity {
    SQLiteDatabase database =null;
    ThongtinSV thongtinSV;
    private ImageView imv;
    private TextView msv;
    private String content,in=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testinall);
        database=openOrCreateDatabase("sinhviendatabase.sqlite",MODE_PRIVATE,null);

        Bundle bundle=getIntent().getExtras();
        in=bundle.getString("mssv");
        Cursor c=database.query("sinhvien",null,null,null,null,null,null);
        c.moveToFirst();
        while (!c.isAfterLast()){

            TextView tvMaSv1=(TextView)findViewById(R.id.tvMaSV);
            tvMaSv1.setText("mssv"+c.getString(0));
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
           //thongtinSV.showthongbao();
            c.moveToNext();

        }
    }

    public void createQRcode(){
        imv = (ImageView) findViewById(R.id.tvqr);
        Intent intent=getIntent();
        content=intent.getStringExtra("mssv");
        msv = (TextView) findViewById(R.id.tvMaSV);
        msv.setText(content);
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(content, 125,125,null);
        if(qrCodeBitmap!=null) imv.setImageBitmap(qrCodeBitmap);
        else Toast.makeText(this, "Loi cu phap", Toast.LENGTH_SHORT).show();
        imv.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
