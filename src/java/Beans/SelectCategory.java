package Beans;

import DB.DB;
import java.io.IOException;
import java.sql.ResultSet;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@RequestScoped
public class SelectCategory {

    private int catid;
    private String name;

    public SelectCategory() {

    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //--------------------------------- Functions ------------------------------//
    public void insert() {

        DB db = new DB();
        FacesMessage facesMessage;

        try {
            if (check()) {
                String sql = "insert into categories (name) values (?) ";
                db.update(sql, name);

                facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "New Record is Entered", null);
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
            if (catid != 0) {
                String sql = "select * from categories where catid=?";
                rs = db.select(sql, getCatid());

                if (rs.next()) {
                    name = rs.getString("name");

                } else {
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "There is no sush category", null);
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                }
            } else {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "choose id, please", null);
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

    public void update() {
        DB db = new DB();
        FacesMessage facesMessage;

        try {
            if (check()) {
                String sql = "update categories set name=? where catid=? ";
                db.update(sql, name, catid);

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

    public void delete() {

        DB db = new DB();
        try {

            String sql = "delete from categories where catid=?";
            db.update(sql, catid);

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
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public boolean check() throws Exception {

        if (name.equals("")) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fill The Name Field!", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        return true;
    }
}
