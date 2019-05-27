package www.xinkui.com.restaurant.bean;
public class TranscationDetail {
    private Menu menu;
    private int sumup;
    private String username;
    private int deskNum;
    private int clientNum;
    private long currentTime;
    private int confirmState;
    private String phone;
    private int clientBalance;
    public TranscationDetail() {

    }
    public TranscationDetail(Menu menu, int sumup, String username, int deskNum, int clientNum, long currentTime,
                             int confirmState, String phone, int clientBalance) {
        super();
        this.menu = menu;
        this.sumup = sumup;
        this.username = username;
        this.deskNum = deskNum;
        this.clientNum = clientNum;
        this.currentTime = currentTime;
        this.confirmState = confirmState;
        this.phone = phone;
        this.clientBalance = clientBalance;
    }
    public Menu getMenu() {
        return menu;
    }
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    public int getSumup() {
        return sumup;
    }
    public void setSumup(int sumup) {
        this.sumup = sumup;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getDeskNum() {
        return deskNum;
    }
    public void setDeskNum(int deskNum) {
        this.deskNum = deskNum;
    }
    public int getClientNum() {
        return clientNum;
    }
    public void setClientNum(int clientNum) {
        this.clientNum = clientNum;
    }
    public long getCurrentTime() {
        return currentTime;
    }
    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
    public int getConfirmState() {
        return confirmState;
    }
    public void setConfirmState(int confirmState) {
        this.confirmState = confirmState;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getClientBalance() {
        return clientBalance;
    }
    public void setClientBalance(int clientBalance) {
        this.clientBalance = clientBalance;
    }
    @Override
    public String toString() {
        return "TranscationDetail [menu=" + menu + ", sumup=" + sumup + ", username=" + username + ", deskNum="
                + deskNum + ", clientNum=" + clientNum + ", currentTime=" + currentTime + ", confirmState="
                + confirmState + ", phone=" + phone + ", clientBalance=" + clientBalance + "]";
    }
}
