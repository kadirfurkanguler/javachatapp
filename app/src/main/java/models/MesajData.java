package models;

public class MesajData {
    private String userId, mesajAdi, mesaj;

    public MesajData(String userId, String mesajAdi, String mesaj) {
        this.userId = userId;
        this.mesajAdi = mesajAdi;
        this.mesaj = mesaj;
    }

    public MesajData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMesajAdi() {
        return mesajAdi;
    }

    public void setMesajAdi(String mesajAdi) {
        this.mesajAdi = mesajAdi;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
}
