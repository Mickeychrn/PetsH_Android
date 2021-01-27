package au.edu.sydney.comp5216.petsh.model;

public class FavouriteRecord {
    private String petId;
    private String petName;
    private String petCategory;
    private String petGender;
    private String petAge;
    private String petDescription;
    private String adoptPerson;
    private String petPhotoName;
    private String UserId;



    public FavouriteRecord(String petId, String petName, String petCategory, String petGender, String petAge, String petDescription, String adoptPerson, String petPhotoName, String userId) {
        this.petId = petId;
        this.petName = petName;
        this.petCategory = petCategory;
        this.petGender = petGender;
        this.petAge = petAge;
        this.petDescription = petDescription;
        this.adoptPerson = adoptPerson;
        this.petPhotoName = petPhotoName;
        UserId = userId;
    }

    public FavouriteRecord() {
    }


    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getPetDescription() {
        return petDescription;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public String getAdoptPerson() {
        return adoptPerson;
    }

    public void setAdoptPerson(String adoptPerson) {
        this.adoptPerson = adoptPerson;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setPetCategory(String petCategory) {
        this.petCategory = petCategory;
    }

    public void setPetPhotoName(String petPhotoName) {
        this.petPhotoName = petPhotoName;
    }

    public String getPetCategory() {
        return petCategory;
    }

    public String getPetPhotoName() {
        return petPhotoName;
    }


}
