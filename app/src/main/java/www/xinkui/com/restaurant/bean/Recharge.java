package www.xinkui.com.restaurant.bean;

public class Recharge {
    private String username;
    private int addBalance;

    public Recharge(String username, int addBalace) {
        this.username = username;
        this.addBalance = addBalace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAddBalace() {
        return addBalance;
    }

    public void setAddBalace(int addBalace) {
        this.addBalance = addBalace;
    }
}
