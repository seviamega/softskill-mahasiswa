package com.sidomafit.mahasiswa.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Mahasiswa implements Parcelable {

    private String email_mhs;
    private String foto_mhs;
    private String kelas;
    private String kode_dosen;
    private String nama_mhs;
    private String nim;
    private String no_telepon;
    private String status;
    private int total_poin;

    public Mahasiswa() {
    }

    public Mahasiswa(String email_mhs, String foto_mhs, String kelas, String kode_dosen, String nama_mhs, String nim, String no_telepon, String status, int total_poin) {
        this.email_mhs = email_mhs;
        this.foto_mhs = foto_mhs;
        this.kelas = kelas;
        this.kode_dosen = kode_dosen;
        this.nama_mhs = nama_mhs;
        this.nim = nim;
        this.no_telepon = no_telepon;
        this.status = status;
        this.total_poin = total_poin;
    }

    public String getEmail_mhs() {
        return email_mhs;
    }

    public void setEmail_mhs(String email_mhs) {
        this.email_mhs = email_mhs;
    }

    public String getFoto_mhs() {
        return foto_mhs;
    }

    public void setFoto_mhs(String foto_mhs) {
        this.foto_mhs = foto_mhs;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getKode_dosen() {
        return kode_dosen;
    }

    public void setKode_dosen(String kode_dosen) {
        this.kode_dosen = kode_dosen;
    }

    public String getNama_mhs() {
        return nama_mhs;
    }

    public void setNama_mhs(String nama_mhs) {
        this.nama_mhs = nama_mhs;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public void setNo_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal_poin() {
        return total_poin;
    }

    public void setTotal_poin(int total_poin) {
        this.total_poin = total_poin;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email_mhs);
        dest.writeString(this.foto_mhs);
        dest.writeString(this.kelas);
        dest.writeString(this.kode_dosen);
        dest.writeString(this.nama_mhs);
        dest.writeString(this.nim);
        dest.writeString(this.no_telepon);
        dest.writeString(this.status);
        dest.writeInt(this.total_poin);
    }

    protected Mahasiswa(Parcel in) {
        this.email_mhs = in.readString();
        this.foto_mhs = in.readString();
        this.kelas = in.readString();
        this.kode_dosen = in.readString();
        this.nama_mhs = in.readString();
        this.nim = in.readString();
        this.no_telepon = in.readString();
        this.status = in.readString();
        this.total_poin = in.readInt();
    }

    public static final Parcelable.Creator<Mahasiswa> CREATOR = new Parcelable.Creator<Mahasiswa>() {
        @Override
        public Mahasiswa createFromParcel(Parcel source) {
            return new Mahasiswa(source);
        }

        @Override
        public Mahasiswa[] newArray(int size) {
            return new Mahasiswa[size];
        }
    };
}
