package id.arizalsaputro.tepe9savedata.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by muharizals on 06/11/2016.
 */

public class Hewan extends RealmObject {

    @SerializedName("nama")
    private String nama;

    @SerializedName("foto")
    private String foto;

    private int lihat;

    public Hewan(String nama, String foto) {
        this.nama = nama;
        this.foto = foto;
        this.lihat = 0;
    }

    public Hewan() {
    }

    public String getNama() {
        return nama;
    }

    public String getFoto() {
        return foto;
    }

    public int getLihat() {
        return lihat;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setLihat(int lihat) {
        this.lihat = lihat;
    }
}
