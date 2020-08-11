package com.sidomafit.dosen.model;

public class SearchModel {
    private String id_kesalahan;
    private String kesalahan;
    private String status;
    private String poin_kesalahan;

    public SearchModel() {
    }

    public String getId_kesalahan() {
        return id_kesalahan;
    }

    public void setId_kesalahan(String id_kesalahan) {
        this.id_kesalahan = id_kesalahan;
    }

    public String getKesalahan() {
        return kesalahan;
    }

    public void setKesalahan(String kesalahan) {
        this.kesalahan = kesalahan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoin_kesalahan() {
        return poin_kesalahan;
    }

    public void setPoin_kesalahan(String poin_kesalahan) {
        this.poin_kesalahan = poin_kesalahan;
    }
}
