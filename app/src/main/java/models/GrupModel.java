package models;

public class GrupModel {
    private  String userId, grupName, grupDesc, link, grupId;

    public GrupModel(String userId, String grupName, String grupDesc, String link, String grupId) {
        this.userId = userId;
        this.grupName = grupName;
        this.grupDesc = grupDesc;
        this.link = link;
        this.grupId = grupId;
    }

    public GrupModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrupName() {
        return grupName;
    }

    public void setGrupName(String grupName) {
        this.grupName = grupName;
    }

    public String getGrupDesc() {
        return grupDesc;
    }

    public void setGrupDesc(String grupDesc) {
        this.grupDesc = grupDesc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGrupId() {
        return grupId;
    }

    public void setGrupId(String grupId) {
        this.grupId = grupId;
    }
}
