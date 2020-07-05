package Beans;

import DB.*;
import java.sql.ResultSet;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class SignIn {

    public static int userID;
    private String name;
    private String password;

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        SignIn.userID = userID;
    }

    public SignIn() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String checkLogin() {
        FacesMessage facesMessage;
        DB db = new DB();
        ResultSet rs = null;

        String pageTransactionMessage = "";
        try {
            String sql = "SELECT * FROM users WHERE username=? AND password = md5(?)";

            rs = db.select(sql, getName(), getPassword());

            if (rs.next()) {
                userID = rs.getInt("id");
                String type = rs.getString("type");

                if (type.equals("user")) {
                    pageTransactionMessage = "user";
                } else {
                    pageTransactionMessage = "admin";
                }
            } else {
                pageTransactionMessage = "fail";

                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "invalid username, password", null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            pageTransactionMessage = "fail";
        } finally {
            try {
                db.releaseResources();
            } catch (Exception ex) {
                String message = ex.getMessage();
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        }
        return pageTransactionMessage;
    }
}
