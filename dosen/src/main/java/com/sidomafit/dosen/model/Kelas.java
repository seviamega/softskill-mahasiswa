package com.sidomafit.dosen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Kelas implements Parcelable {
    private String id;
    private String kelas;

    public Kelas() {
    }

    public Kelas(String id, String kelas) {
        this.id = id;
        this.kelas = kelas;
    }

    //test

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.kelas);
    }

    protected Kelas(Parcel in) {
        this.id = in.readString();
        this.kelas = in.readString();
    }

    public static final Creator<Kelas> CREATOR = new Creator<Kelas>() {
        @Override
        public Kelas createFromParcel(Parcel source) {
            return new Kelas(source);
        }

        @Override
        public Kelas[] newArray(int size) {
            return new Kelas[size];
        }
    };
}
