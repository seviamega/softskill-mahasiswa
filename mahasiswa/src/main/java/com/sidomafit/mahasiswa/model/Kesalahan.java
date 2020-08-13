package com.sidomafit.mahasiswa.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Kesalahan implements Parcelable {
    private String deskripsi_kesalahan;
    private String id_dosen_wali;
    private String id_kesalahan;
    private String id_mahasiswa;
    private String id_pengirim;
    private String nama_pelanggar;
    private String nama_pengirim;
    private String poin_kesalahan;
    private String waktu_pengiriman;
    private String foto_mhs;
    private String status;
    private String pending_foto;

    public Kesalahan() {
    }

    public String getDeskripsi_kesalahan() {
        return deskripsi_kesalahan;
    }

    public String getId_dosen_wali() {
        return id_dosen_wali;
    }

    public String getId_kesalahan() {
        return id_kesalahan;
    }

    public String getId_mahasiswa() {
        return id_mahasiswa;
    }

    public String getId_pengirim() {
        return id_pengirim;
    }

    public String getNama_pelanggar() {
        return nama_pelanggar;
    }

    public String getNama_pengirim() {
        return nama_pengirim;
    }

    public String getPoin_kesalahan() {
        return poin_kesalahan;
    }

    public String getWaktu_pengiriman() {
        return waktu_pengiriman;
    }

    public String getFoto_mhs() {
        return foto_mhs;
    }

    public String getStatus() {
        return status;
    }

    public String getPending_foto() {
        return pending_foto;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deskripsi_kesalahan);
        dest.writeString(this.id_dosen_wali);
        dest.writeString(this.id_kesalahan);
        dest.writeString(this.id_mahasiswa);
        dest.writeString(this.id_pengirim);
        dest.writeString(this.nama_pelanggar);
        dest.writeString(this.nama_pengirim);
        dest.writeString(this.poin_kesalahan);
        dest.writeString(this.waktu_pengiriman);
        dest.writeString(this.foto_mhs);
        dest.writeString(this.status);
        dest.writeString(this.pending_foto);
    }

    protected Kesalahan(Parcel in) {
        this.deskripsi_kesalahan = in.readString();
        this.id_dosen_wali = in.readString();
        this.id_kesalahan = in.readString();
        this.id_mahasiswa = in.readString();
        this.id_pengirim = in.readString();
        this.nama_pelanggar = in.readString();
        this.nama_pengirim = in.readString();
        this.poin_kesalahan = in.readString();
        this.waktu_pengiriman = in.readString();
        this.foto_mhs = in.readString();
        this.status = in.readString();
        this.pending_foto = in.readString();
    }

    public static final Creator<Kesalahan> CREATOR = new Creator<Kesalahan>() {
        @Override
        public Kesalahan createFromParcel(Parcel source) {
            return new Kesalahan(source);
        }

        @Override
        public Kesalahan[] newArray(int size) {
            return new Kesalahan[size];
        }
    };
}
