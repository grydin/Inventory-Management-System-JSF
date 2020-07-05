package Beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import DB.*;
import java.io.IOException;
import java.sql.ResultSet;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@RequestScoped
public class SelectSupplier {

    private int sid;
    private String fullname;
    private String company;
    private String phone;
    private String email;

    public SelectSupplier() {
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // -------------------------------- functions ----------------------------------------//
    /**
     * select function selects all the fields of the supplier based on the sid.
     */
    public void insert() {

        DB db = new DB();
        FacesMessage facesMessage;

        try {
            if (check()) {
                String sql = "insert into suppliers (fullname, company, email, phone) values (?,?,?,?) ";
                db.update(sql, fullname, company, email, phone);

                facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "New Record is entered", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                reload();
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        reset();

    }

    public void select() {
        DB db = new DB();
        ResultSet rs = null;
        try {

            String sql = "select * from suppliers where sid=?";

            /**
             * this function establishes connection and do the select and return
             * a result set of the result;
             */
            rs = db.select(sql, getSid());

            if (rs.next()) {
                fullname = rs.getString("fullname");
                phone = rs.getString("phone");
                email = rs.getString("email");
                company = rs.getString("company");

            } else {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is no sush supplier", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            try {
                db.releaseResources();
            } catch (Exception ex) {

            }
        }
    }

    /**
     * update function takes all the info of the supplier and inserts it in the
     * DB
     */
    public void update() {
        DB db = new DB();
        FacesMessage facesMessage;

        try {
            if (check()) {
                String sql = "update suppliers set fullname=?, company=?, email=?, phone=? where sid=? ";
                db.update(sql, fullname, company, email, phone, sid);

                facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Record is Updated", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                reload();
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        reset();
    }

//    /**
//     * delete function deletes the supplier based on his supplierID (sid)
//     */
    public void delete() {

        DB db = new DB();
        try {

            String sql = "delete from suppliers where sid=?";
            db.update(sql, sid);

            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Record is deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            reload();
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
//

    public void reset() {
        fullname = "";
        company = "";
        phone = "";
        email = "";
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public boolean check() throws Exception {
        
        if (fullname.equals("") && company.equals("") && phone.equals("") && email.equals("") ) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fill all fields, please!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        
        if (fullname.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter your Name, please!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (company.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fill Your company Name, please!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (phone.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter phone number, please!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (email.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the email please!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        return true;
    }
    /**
     * private int sid; private String fullname; private String company; private
     * String phone; private String email;
     */

}
