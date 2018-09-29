package com.example.hoa.quanlysv;

/**
 * Created by hoa on 9/8/2018.
 */

public class Sinhvien {
    private int masv;
    private String tensv;
    private String namsinh;
    private String tenlop;
    private boolean gioitinh;

    public int getMasv() {
        return masv;
    }

    public void setMasv(int masv) {
        this.masv = masv;
    }

    public String getTensv() {
        return tensv;
    }

    public void setTensv(String tensv) {
        this.tensv = tensv;
    }
    public String getNamsinh(){
        return namsinh;
    }
    public void setNamsinh(String namsinh){
        this.namsinh= namsinh;
    }
    public String getTenlop() {
        return tenlop;
    }

    public void setTenlop(String tenlop) {
        this.tenlop = tenlop;
    }
    public boolean isGioitinh() {
        return gioitinh;
    }
    public void setGioitinh(boolean gioitinh) {
        this.gioitinh = gioitinh;
    }
    @Override
    public String toString()
    {
        return masv+"-"+tensv+"-"+gioitinh+"-"+namsinh+"-"+tenlop;
    }
}
