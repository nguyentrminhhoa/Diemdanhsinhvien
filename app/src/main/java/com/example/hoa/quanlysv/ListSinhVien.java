package com.example.hoa.quanlysv;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class ListSinhVien extends AppCompatActivity {

    ListView lvSinhVien1=null;
    ArrayList<Sinhvien> arrayList1=null;
    MyAdapter adapter=null;
    SQLiteDatabase database=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sinh_vien);

        lvSinhVien1= (ListView) findViewById(R.id.listSV1);
        arrayList1= new ArrayList<>();
        arrayList1= xemdssinhvien1();
        adapter=new MyAdapter(this,R.layout.hotro_listview,arrayList1);
        lvSinhVien1.setAdapter(adapter);

        lvSinhVien1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item =arrayList1.get(position).getMasv()+"";
                Intent in=new Intent(getApplicationContext(),ThongtinSV.class);
                in.putExtra("mssv", item);
                startActivity(in);
            }
        });

    }
    public ArrayList<Sinhvien> xemdssinhvien1()
    {
        Cursor c=database.query("sinhvien",null,null,null,null,null,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            Sinhvien sv1=new Sinhvien();
            sv1.setMasv(Integer.parseInt(c.getString(0).toString()));
            sv1.setTensv(c.getString(1).toString());
            sv1.setNamsinh(c.getString(3).toString());
            sv1.setTenlop(c.getString(4).toString());
            if(c.getString(2).equalsIgnoreCase("nam"))
                sv1.setGioitinh(true);
            else
                sv1.setGioitinh(false);
            c.moveToNext();
            arrayList1.add(sv1);
        }
        c.close();
        return  arrayList1;

    }
}
