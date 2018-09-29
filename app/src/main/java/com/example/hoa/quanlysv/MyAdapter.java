package com.example.hoa.quanlysv;

/**
 * Created by hoa on 9/8/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Sinhvien> {
    private Context context;
    private int layoutID;
    ArrayList<Sinhvien> arrayList;
    public MyAdapter(Context context, int layoutID, ArrayList<Sinhvien> arrayList) {
        super(context, layoutID, arrayList);
        this.context=context;
        this.layoutID=layoutID;
        this.arrayList=arrayList;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View rooview=inflater.inflate(R.layout.hotro_listview,parent,false);
        ImageView imageView= (ImageView) rooview.findViewById(R.id.imageSex);
        TextView tvname= (TextView) rooview.findViewById(R.id.tvName);
        TextView tvmasv= (TextView) rooview.findViewById(R.id.tvMaSV);
        if(arrayList.get(position).isGioitinh())
            imageView.setImageResource(R.drawable.boy);
        else
            imageView.setImageResource(R.drawable.girl);


        tvname.setText(arrayList.get(position).getTensv());
        tvmasv.setText(""+arrayList.get(position).getMasv());



        return rooview;
    }

}
