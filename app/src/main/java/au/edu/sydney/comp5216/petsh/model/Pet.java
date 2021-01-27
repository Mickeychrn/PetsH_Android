package au.edu.sydney.comp5216.petsh.model;

public class Pet {
    private String petId;
    private String petName;
    private String petCategory;
    private String petGender;
    private String petAge;
    private String petDescription;
    private String petPhotoName;
    private String adoptPerson;
    private String addPerson;

    public Pet(String petName, String petCategory, String petGender, String petAge, String petDescription, String petPhotoName, String adoptPerson, String addPerson) {
        this.petName = petName;
        this.petCategory = petCategory;
        this.petGender = petGender;
        this.petAge = petAge;
        this.petDescription = petDescription;
        this.petPhotoName = petPhotoName;
        this.adoptPerson = adoptPerson;
        this.addPerson = addPerson;
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

    public String getAddPerson() {
        return addPerson;
    }

    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson;
    }

    public String getAdoptPerson() {
        return adoptPerson;
    }

    public void setAdoptPerson(String adoptPerson) {
        this.adoptPerson = adoptPerson;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetCategory() {
        return petCategory;
    }

    public void setPetCategory(String petCategory) {
        this.petCategory = petCategory;
    }

    public String getPetPhotoName() {
        return petPhotoName;
    }

    public void setPetPhotoName(String petPhotoName) {
        this.petPhotoName = petPhotoName;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public Pet(String petName, String petCategory, String petPhotoName) {
        this.petName = petName;
        this.petCategory = petCategory;
        this.petPhotoName = petPhotoName;
    }

    public Pet() {
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId='" + petId + '\'' +
                ", petName='" + petName + '\'' +
                ", petCategory='" + petCategory + '\'' +
                ", petGender='" + petGender + '\'' +
                ", petAge='" + petAge + '\'' +
                ", petDescription='" + petDescription + '\'' +
                ", petPhotoName='" + petPhotoName + '\'' +
                ", adoptPerson='" + adoptPerson + '\'' +
                ", addPerson='" + addPerson + '\'' +
                '}';
    }
}
