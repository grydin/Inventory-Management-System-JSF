/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import DB.DB;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import Model.Customer;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ِِACER-PC
 */
@ManagedBean
@RequestScoped
public class SelectCustomer {

    private List<Integer> options;
    private String selectedItem = "0";

    List<Customer> cusData;
    private int cid;
    private String fullname;
    private String address;
    private String phone;
    private String email;
    private String company;
    private String date;

    private String nameSearch;

    public SelectCustomer() {
        cusData = new LinkedList<>();
        options = new ArrayList<Integer>();
        try {
            DB dbm = new DB();
            ResultSet rs = dbm.select1("SELECT * from customers;");
            while (rs.next()) {
                Customer c = new Customer();
                c.setCid(rs.getInt("cid"));
                c.setFullname(rs.getString("fullname"));
                c.setAddress(rs.getString("address"));
                c.setEmail(rs.getString("email"));
                c.setPhone(rs.getString("phone"));
                c.setCompany(rs.getString("company"));
                c.setDate(rs.getString("date"));
                options.add(rs.getInt("cid"));
                cusData.add(c);

            }

            dbm.releaseResources1();
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }

    }

    public void selectCustomerBtn() {
        try {
            if (check2()) {
                DB dbm = new DB();
                ResultSet rs = dbm.select1("select * from customers where cid=" + getSelectedItem());
                rs.next();
                fullname = rs.getString("fullname");
                address = rs.getString("address");
                email = rs.getString("email");
                phone = rs.getString("phone");
                company = rs.getString("company");
                dbm.releaseResources1();
            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }
    }

    public void selectCustomerByName() {
        options = new ArrayList<Integer>();
        cusData = new LinkedList<>();

        try {
            if (Found()) {
                DB dbm = new DB();
                ResultSet rs = dbm.select1("SELECT * from customers where fullname like '%" + nameSearch + "%' ;");
                while (rs.next()) {
                    Customer c = new Customer();
                    c.setCid(rs.getInt("cid"));
                    c.setFullname(rs.getString("fullname"));
                    c.setAddress(rs.getString("address"));
                    c.setEmail(rs.getString("email"));
                    c.setPhone(rs.getString("phone"));
                    c.setCompany(rs.getString("company"));
                    c.setDate(rs.getString("date"));
                    options.add(rs.getInt("cid"));
                    cusData.add(c);
                }

                dbm.releaseResources1();
            } else {
                throw new Exception("Customer Name Not Found");
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }
    }

    public void selectCustomerInsert() {

        try {
            if (check()) {
                DB dbm = new DB();

                String Q = "insert into customers (fullname,address,email,phone,company) values"
                        + "('" + fullname + "','" + address + "', '" + email + "','" + phone + "','" + company + "');";
                dbm.InsertUpdateDelete(Q);
                dbm.releaseResources1();
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your record has inserted", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                reload();
            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }

    }

    public void selectCustomerUpdate() {
        try {

            if (check()) {
                DB dbm = new DB();

                String Q = "update customers set "
                        + " fullname = '" + fullname + "',"
                        + " address = '" + address + "',"
                        + " email=  '" + email + "',"
                        + " phone = '" + phone + "',"
                        + " company = '" + company + "' "
                        + " where cid =" + selectedItem + ";";
                dbm.InsertUpdateDelete(Q);
                dbm.releaseResources1();
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your record has updated", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                reload();
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    /**
     * delete function deletes the user based on his UserName
     */
    public void selectCustomerDelete() {

        DB db = new DB();
        try {

            String sql = "delete from customers where cid=" + selectedItem;
            db.InsertUpdateDelete(sql);
            db.releaseResources1();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Record is deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            reload();
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public boolean check2() throws Exception {
        if (getSelectedItem().equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select ID!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        return true;
    }

    public boolean check() throws Exception {

        if (fullname.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fill The Name Field!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (email.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fill The email Field!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (address.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fill The address Field!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (phone.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fill The phone Field!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        if (company.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fill The company Field!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }

        return true;
    }

    public void reset() {
        fullname = "";
        address = "";
        phone = "";
        email = "";
        company = "";
        nameSearch = "";

    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    private boolean Found() throws Exception {

        DB dbm = new DB();
        ResultSet rs = dbm.select1("select * from customers where fullname like'%" + nameSearch + "%' ;");
        boolean found = rs.next(); //if there is a row will return true
        dbm.releaseResources1();

        if (found) {
            return true;
        }
        return false;

    }

    public String getNameSearch() {
        return nameSearch;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }

    public List<Customer> getCusData() {
        return cusData;
    }

    public void setCusData(List<Customer> cusData) {
        this.cusData = cusData;
    }

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
