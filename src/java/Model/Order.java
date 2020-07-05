
package Model;


public class Order {
    
    private int oid;
    private int opid;
    private String opname;
    private double oprice;
    private int oquantity;
    private double ototal;
    private int oinvoice;

    public Order() {
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getOpid() {
        return opid;
    }

    public void setOpid(int opid) {
        this.opid = opid;
    }

    public String getOpname() {
        return opname;
    }

    public void setOpname(String opname) {
        this.opname = opname;
    }

    public double getOprice() {
        return oprice;
    }

    public void setOprice(double oprice) {
        this.oprice = oprice;
    }

    public int getOquantity() {
        return oquantity;
    }

    public void setOquantity(int oquantity) {
        this.oquantity = oquantity;
    }

    public double getOtotal() {
        return ototal;
    }

    public void setOtotal(double ototal) {
        this.ototal = ototal;
    }

    public int getOinvoice() {
        return oinvoice;
    }

    public void setOinvoice(int oinvoice) {
        this.oinvoice = oinvoice;
    }
    
}
