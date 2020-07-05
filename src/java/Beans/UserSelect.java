package Beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import DB.*;
import Model.User;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;
import java.util.LinkedList;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@RequestScoped
public class UserSelect {

    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String username;
    private String type;
    private List<Integer> fillId;

    public UserSelect() {
        ResultSet rs = null;
        fillId = new LinkedList<>();
        DB db = new DB();

        try {

            String sql = "select id from users";
            rs = db.select(sql);
            while (rs.next()) {
                fillId.add(rs.getInt("id"));
            }
            db.releaseResources();

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

    }

    public List<Integer> getFillId() {
        return fillId;
    }

    public void setFillId(List<Integer> fillId) {
        this.fillId = fillId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // -------------------------------- functions ----------------------------------------//
    /**
     * select function selects all the fields of the employee based on the
     * UserName. and they should be displayed on the text fields.
     */
    public void select() {
        DB db = new DB();
        ResultSet rs = null;
        try {

            String sql = "select * from users where id=?";

            /**
             * this function establishes connection and do the select based on
             * the sql statement operation and return a result set;
             */
            rs = db.select(sql, getId());

            if (rs.next()) {

                name = rs.getString("fullname");
                address = rs.getString("address");
                phone = rs.getString("phone");
                email = rs.getString("email");
                username = rs.getString("username");
                type = rs.getString("type");
            } else {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is no sush employee", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            try {

            } catch (Exception ex) {

            }
        }
    }

    /**
     * update function takes all the info of the user and inserts it in the DB
     */
    public void update() {
        DB db = new DB();
        FacesMessage facesMessage;
        
        try {
            if (check()) {
                String sql = "update users set fullname=?, address=?, email=?, phone=?, username=?, type=? where id=? ";
                db.update(sql, name, address, email, phone, username, getType(), id);

                facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Record is Updated", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                reload();
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
//        reset();
    }

    /**
     * delete function deletes the user based on his UserName
     */
    public void delete() {

        DB db = new DB();
        try {

            String sql = "delete from users where id=? ";
            db.update(sql, id);

            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Record is deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            reload();
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public void reset() {
        name = "";
        address = "";
        phone = "";
        email = "";
        username = "";
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public boolean check() throws Exception {

        if (username.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the username please!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (name.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the name, please!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (address.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the address, please!", null);
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
}
