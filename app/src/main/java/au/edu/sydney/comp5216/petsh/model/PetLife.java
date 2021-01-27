package au.edu.sydney.comp5216.petsh.model;

public class PetLife {
    String petName;
    String petCategory;
    String adoptPerson;
    String petLifePhoto;
    String petLifeDescription;

    public PetLife() {
    }

    public PetLife(String petName, String petCategory, String adoptPerson, String petLifePhoto) {
        this.petName = petName;
        this.petCategory = petCategory;
        this.adoptPerson = adoptPerson;
        this.petLifePhoto = petLifePhoto;
    }

    public PetLife(String petName, String petCategory, String adoptPerson, String petLifePhoto, String petLifeDescription) {
        this.petName = petName;
        this.petCategory = petCategory;
        this.adoptPerson = adoptPerson;
        this.petLifePhoto = petLifePhoto;
        this.petLifeDescription = petLifeDescription;
    }

    public String getPetLifeDescription() {
        return petLifeDescription;
    }

    public void setPetLifeDescription(String petLifeDescription) {
        this.petLifeDescription = petLifeDescription;
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

    public String getAdoptPerson() {
        return adoptPerson;
    }

    public void setAdoptPerson(String adoptPerson) {
        this.adoptPerson = adoptPerson;
    }

    public String getPetLifePhoto() {
        return petLifePhoto;
    }

    public void setPetLifePhoto(String petLifePhoto) {
        this.petLifePhoto = petLifePhoto;
    }
}
