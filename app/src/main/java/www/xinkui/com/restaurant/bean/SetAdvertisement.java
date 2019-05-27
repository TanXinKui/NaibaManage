package www.xinkui.com.restaurant.bean;

public class SetAdvertisement {
    private int id;
    private String dishname;
    private String c3;
    private int price;
    private int state;
    private String description;

    public SetAdvertisement(int id, String dishname, String c3, int price, int state, String description) {
        this.id = id;
        this.dishname = dishname;
        this.c3 = c3;
        this.price = price;
        this.state = state;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDishname() {
        return dishname;
    }
    public void setDishname(String dishname) {
        this.dishname = dishname;
    }
    public String getC3() {
        return c3;
    }
    public void setC3(String c3) {
        this.c3 = c3;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SetAdvertisement{" +
                "id=" + id +
                ", dishname='" + dishname + '\'' +
                ", c3='" + c3 + '\'' +
                ", price=" + price +
                ", state=" + state +
                ", description='" + description + '\'' +
                '}';
    }
}
