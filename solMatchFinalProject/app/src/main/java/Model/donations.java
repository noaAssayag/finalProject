package Model;

public class donations {


    private String name;
    private String adress;
    private String catagory;
    private String description;
    private int img;

    public donations(String name, String adress, String catagory, int img,String description) {
        this.name = name;
        this.adress = adress;
        this.catagory = catagory;
        this.img = img;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
