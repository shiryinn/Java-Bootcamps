public class Buyer {

    private int buyerId;
    private String givenName;
    private String familyName;

    // Constructors
    public Buyer(int newBuyerId) {
        this.buyerId = newBuyerId;
    }

    public Buyer(int newBuyerId, String newGivenName, String newFamilyName) {
        this.buyerId = newBuyerId;
        this.givenName = newGivenName;
        this.familyName = newFamilyName;
    }

    public void setGivenName(String newGivenName) {
        this.givenName = newGivenName;
    }

    public void setFamilyName(String newFamilyName) {
        this.familyName = newFamilyName;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public int getBuyerId() {
        return this.buyerId;
    }

    // accessor method
    public String description() {
        return this.buyerId + " " + getGivenName() + " " + getFamilyName();
    }
}