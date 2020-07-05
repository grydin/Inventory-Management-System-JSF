
package Beans;

import DB.DB;
import Model.Invoice;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


@ManagedBean
@RequestScoped
public class FillInvoice {

  private List<Invoice> invoiceInfo;

    private List<Integer> invid;

    public List<Invoice> getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(List<Invoice> invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public List<Integer> getInvid() {
        return invid;
    }

    public void setInvid(List<Integer> invid) {
        this.invid = invid;
    }

    
    public FillInvoice() {
        ResultSet rs = null;
        invoiceInfo = new LinkedList<>();
        DB db = new DB();
        

        // ------------ get invoice id from DB -------------------//
        invid = new LinkedList<>();

        try {
            String sql = "select * from invoice";
            rs = db.select(sql);

            while (rs.next()) {

                Invoice invoice = new Invoice();

                invoice.setInvoiceID(rs.getInt("invid"));
                invoice.setSoldby(rs.getInt("soldby"));
                invoice.setCid(rs.getInt("cid"));
                invoice.setPurchaseDate(rs.getString("date"));
                invoice.setTotalPrice(rs.getDouble("total"));
                
                invoice.setcName(CNameInDB(invoice.getCid()));
                invoice.seteName(ENameInDB(invoice.getSoldby()));

                invoiceInfo.add(invoice);
                invid.add(invoice.getInvoiceID());
            }
            db.releaseResources();

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    private String CNameInDB(int cId) throws Exception {

        DB db = new DB();
        ResultSet rs = null;
        String user = "false fullname";
        try {
            String sql = "select fullname from customers where cid=" + cId + ";";
            rs = db.select1(sql);

            if (rs.next()) {
                user = rs.getString("fullname");
            }
            db.releaseResources1();

        } catch (Exception ex) {

        }

        return user;
    }

    private String ENameInDB(int eId) throws Exception {

        DB db = new DB();
        ResultSet rs = null;
        String user = "false user name";
        try {
            String sql = "select username from users where id=" + eId + ";";
            rs = db.select1(sql);

            if (rs.next()) {
                user = rs.getString("username");
            }
            db.releaseResources1();

        } catch (Exception ex) {

        }
        return user;
    }
}