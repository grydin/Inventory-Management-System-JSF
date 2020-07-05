package Beans;

import DB.DB;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@RequestScoped

public class SignUp {

    private String name;
    private String address;
    private String phone;
    private String email;
    private String username;
    private String type;
    private String password;
    private String rePassword;

    public SignUp() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String sign() {

        FacesMessage facesMessage;
        String pageTransactionMessage = "";
        try {
            if (getPassword().equals(getRePassword())) { // if the two passwords do not matches then show else message

                /**
                 * here we will insert all information to the DB in this private
                 * method called signUpHelper(); and it will return either
                 * (success) or (fail) so based on that we'll show the message
                 * of success or failure
                 *
                 */
                pageTransactionMessage = signUpHelper();

                if (pageTransactionMessage.equals("success")) {
//                    reload();
                    facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Record been inserted", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                } else {
                    facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nothing has been updated", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                }
            } else {
                throw new Exception("Entered password do not match");
            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        return pageTransactionMessage;
    }

    private String signUpHelper() throws Exception {
        FacesMessage facesMessage;
        DB db = new DB();

        try {
            if (check()) {
                String sql = "insert into users (fullname, address, phone, email, username, password, type) values (?, ?, ?, ?, ?,md5(?), ?)";
                db.update(sql, getName(), getAddress(), getPhone(), getEmail(), getUsername(), getPassword(), getType());
                return "success";
            } else {
                return "fail";
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return "fail";
        }
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public boolean check() throws Exception {

        if (password.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter the password please!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
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
