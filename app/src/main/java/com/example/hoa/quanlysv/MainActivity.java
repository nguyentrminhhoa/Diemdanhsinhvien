package com.example.hoa.quanlysv;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    BaocaotheSV baocaotheSV;

    Button btnthem,btnIn;
    RadioGroup rdg;
    EditText txttennv,txtmanv,txtnamsinh,txttenlop;
    RadioButton rdb1,rdb2;
    ListView lvSinhVien=null;
    ArrayList<Sinhvien> arrayList=null;
    MyAdapter adapter=null;
    SQLiteDatabase database=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=openOrCreateDatabase("sinhviendatabase.sqlite",MODE_PRIVATE,null);
        if(KTBangTonTai(database,"sinhvien"))
        {
        }
        else
        {
            taobang();
            Toast.makeText(getApplicationContext(),"Tạo bảng sinh viên thành công",Toast.LENGTH_LONG).show();
        }
        btnthem= (Button) findViewById(R.id.btnThem);
        btnIn=(Button)findViewById(R.id.btnIn);
        rdg= (RadioGroup) findViewById(R.id.rdgGT);
        rdb1= (RadioButton) findViewById(R.id.rdbGTnam);
        rdb2= (RadioButton) findViewById(R.id.rdbGTnu);
        txtmanv= (EditText) findViewById(R.id.txtMaNV);
        txttennv= (EditText) findViewById(R.id.txtTenNV);
        txtnamsinh=(EditText) findViewById(R.id.txtNamSinh);
        txttenlop=(EditText) findViewById(R.id.txtTenLop);
        lvSinhVien= (ListView) findViewById(R.id.listSV);
        arrayList= new ArrayList<>();
        arrayList= xemdssinhvien();
        adapter=new MyAdapter(this,R.layout.hotro_listview,arrayList);
        lvSinhVien.setAdapter(adapter);
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemSV();
            }
        });
        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        lvSinhVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item =arrayList.get(position).getMasv()+"";
                Intent in=new Intent(getApplicationContext(),ThongtinSV.class);
                in.putExtra("mssv", item);
                startActivity(in);
            }
        });
        lvSinhVien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String item =arrayList.get(position).getMasv()+"";
                Xoasinhvien(item);
                arrayList.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        // QR



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void taobang()
    {
        String sql="CREATE TABLE  sinhvien (masv INTEGER PRIMARY KEY,tensv TEXT, gioitinh TEXT,namsinh TEXT, tenlop TEXT)";
        database.execSQL(sql);
    }
    public boolean KTBangTonTai(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    public void ThemSV()
    {
        ContentValues sv1=new ContentValues();
        int check = rdg.getCheckedRadioButtonId();
        sv1.put("masv", txtmanv.getText().toString());
        sv1.put("tensv", txttennv.getText().toString());
        sv1.put("namsinh",txtnamsinh.getText().toString());
        sv1.put("tenlop",txttenlop.getText().toString());
        if (check == R.id.rdbGTnam)
            sv1.put("gioitinh", "nam");
        else
            sv1.put("gioitinh","nữ");
        if (database.insert("sinhvien",null,sv1)==-1)
        {
            Toast.makeText(this,"Thêm sinh viên thất bại",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Thêm sinh viên thành công",Toast.LENGTH_LONG).show();
        }

        Sinhvien sv = new Sinhvien();
        sv.setMasv(Integer.parseInt(txtmanv.getText().toString()));
        sv.setTensv(txttennv.getText().toString());
        sv.setNamsinh(txtnamsinh.getText().toString());
        sv.setTenlop(txttenlop.getText().toString());
        if (check == R.id.rdbGTnam)
            sv.setGioitinh(true);
        else
            sv.setGioitinh(false);

        arrayList.add(sv);
        adapter.notifyDataSetChanged();
        // Làm mới EditText
        txtmanv.setText("");
        txttennv.setText("");
        txtnamsinh.setText("");
        txttenlop.setText("");

    }

    public ArrayList<Sinhvien> xemdssinhvien()
    {
        Cursor c=database.query("sinhvien",null,null,null,null,null,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            Sinhvien sv=new Sinhvien();
            sv.setMasv(Integer.parseInt(c.getString(0).toString()));
            sv.setTensv(c.getString(1).toString());
            sv.setNamsinh(c.getString(3).toString());
            sv.setTenlop(c.getString(4).toString());
            if(c.getString(2).equalsIgnoreCase("nam"))
                sv.setGioitinh(true);
            else
                sv.setGioitinh(false);
            c.moveToNext();
            arrayList.add(sv);
        }
        c.close();
        return  arrayList;

    }
    public  void Xoasinhvien(String masv)
    {
        if(database.delete("sinhvien", "masv=?", new String[]{masv})!=0)
        {
            Toast.makeText(this,"Xoá sinh viên thành công",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this,"Xoá thất bại",Toast.LENGTH_LONG).show();
    }
}
