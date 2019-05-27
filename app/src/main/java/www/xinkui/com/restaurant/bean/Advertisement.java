package www.xinkui.com.restaurant.bean;

public class Advertisement {
    private String dishname;
    private String c3;
    private int price;
    private int state;
    private String description;

    public Advertisement(String dishname, String c3, int price, int state, String description) {
        this.dishname = dishname;
        this.c3 = c3;
        this.price = price;
        this.state = state;
        this.description = description;
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

    public int getPrice() {
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
}
