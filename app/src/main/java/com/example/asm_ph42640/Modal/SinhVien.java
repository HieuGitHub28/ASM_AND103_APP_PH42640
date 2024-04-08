package com.example.asm_ph42640.Modal;

public class SinhVien {
    private String _id;

    private String masv;
    private String name;

    private Double point;
    private String avatar;

    public SinhVien(String masv, String name, Double point, String avatar) {
        this.masv = masv;
        this.name = name;
        this.point = point;
        this.avatar = avatar;
    }

    public SinhVien(String _id, String name, String masv, Double point, String avatar) {
        this._id = _id;
        this.name = name;
        this.masv = masv;
        this.point = point;
        this.avatar = avatar;
    }

    public SinhVien(String masv, String name, Double point) {
        this.masv = masv;
        this.name = name;
        this.point = point;
    }

    public SinhVien() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }


}
