
package Beans;


import Model.User;
import DB.*;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class FillUser {

    List <User> userInfo ;
    
    List <Integer> userId;

    public FillUser() {
        // ------------ select user info -------------------//
        ResultSet rs = null;
        userInfo = new LinkedList<> ();
        DB db = new DB();
        String sql = "select * from users";
        
        // ------------ select user id -------------------//

        userId = new LinkedList<> ();       
        
        try{
            
            rs = db.select(sql);            
            while(rs.next()){
                User us = new User();
                us.setId(rs.getInt("id"));
                us.setName(rs.getString("fullname"));
                us.setAddress(rs.getString("address"));
                us.setPhone(rs.getString("phone"));
                us.setEmail(rs.getString("email"));
                us.setUsername(rs.getString("username"));
                us.setType(rs.getString("type"));
                
                userId.add(us.getId());
                
                userInfo.add(us);
            }
            
            
            
            
        }catch(Exception ex){
            String  message = ex.getMessage(); 
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null,  facesMessage);
        }
        
        finally {
            try{
                db.releaseResources();
            }
            catch (Exception ex) {
                String message = ex.getMessage();
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        }        
    }   

    public List<Integer> getUserId() {
        return userId;
    }

    public void setUserId(List<Integer> userId) {
        this.userId = userId;
    }
    
    public List<User> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<User> userInfo) {
        this.userInfo = userInfo;
    }
}
