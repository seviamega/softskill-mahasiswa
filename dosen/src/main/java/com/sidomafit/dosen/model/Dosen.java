package com.sidomafit.dosen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dosen implements Parcelable {
    private String kode_dosen;
    private String nama_dosen;
    private String nip;
    private String email_dosen;
    private String no_telepon;
    private String foto_dosen;
    private String status;

    public Dosen() {
    }

    public Dosen(String kode_dosen, String nama_dosen, String nip, String email_dosen, String no_telepon, String foto_dosen, String status) {
        this.kode_dosen = kode_dosen;
        this.nama_dosen = nama_dosen;
        this.nip = nip;
        this.email_dosen = email_dosen;
        this.no_telepon = no_telepon;
        this.foto_dosen = foto_dosen;
        this.status = status;
    }

    public String getKode_dosen() {
        return kode_dosen;
    }

    public void setKode_dosen(String kode_dosen) {
        this.kode_dosen = kode_dosen;
    }

    public String getNama_dosen() {
        return nama_dosen;
    }

    public void setNama_dosen(String nama_dosen) {
        this.nama_dosen = nama_dosen;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getEmail_dosen() {
        return email_dosen;
    }

    public void setEmail_dosen(String email_dosen) {
        this.email_dosen = email_dosen;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public void setNo_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public String getFoto_dosen() {
        return foto_dosen;
    }

    public void setFoto_dosen(String foto_dosen) {
        this.foto_dosen = foto_dosen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kode_dosen);
        dest.writeString(this.nama_dosen);
        dest.writeString(this.nip);
        dest.writeString(this.email_dosen);
        dest.writeString(this.no_telepon);
        dest.writeString(this.foto_dosen);
        dest.writeString(this.status);
    }

    protected Dosen(Parcel in) {
        this.kode_dosen = in.readString();
        this.nama_dosen = in.readString();
        this.nip = in.readString();
        this.email_dosen = in.readString();
        this.no_telepon = in.readString();
        this.foto_dosen = in.readString();
        this.status = in.readString();
    }

    public static final Creator<Dosen> CREATOR = new Creator<Dosen>() {
        @Override
        public Dosen createFromParcel(Parcel source) {
            return new Dosen(source);
        }

        @Override
        public Dosen[] newArray(int size) {
            return new Dosen[size];
        }
    };
}
