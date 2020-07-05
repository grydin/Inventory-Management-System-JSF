package Beans;

import DB.DB;
import Model.Category;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class FillCategory {

    private List<Category> categoryInfo;

    private List<Integer> categoryId;

    public FillCategory() {
        // ------------ bring all catogory info -------------------//
        ResultSet rs = null;
        categoryInfo = new LinkedList<>();
        DB db = new DB();
        String sql = "select * from categories";

        // ------------ get category id from DB -------------------//
        categoryId = new LinkedList<>();

        try {

            rs = db.select(sql);
            while (rs.next()) {
                Category category = new Category();
                category.setCatid(rs.getInt("catid"));
                category.setName(rs.getString("name"));

                categoryId.add(category.getCatid());
                categoryInfo.add(category);
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            try {
                db.releaseResources();

            } catch (Exception ex) {
                String message = ex.getMessage();
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        }

    }

    public List<Category> getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(List<Category> categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public List<Integer> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Integer> categoryId) {
        this.categoryId = categoryId;
    }

}
