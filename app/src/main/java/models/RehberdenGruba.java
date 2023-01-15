package models;

public class RehberdenGruba {
    private String userId, grupId, contactPhone;

    public RehberdenGruba(String userId, String grupId, String contactPhone) {
        this.userId = userId;
        this.grupId = grupId;
        this.contactPhone = contactPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrupId() {
        return grupId;
    }

    public void setGrupId(String grupId) {
        this.grupId = grupId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
