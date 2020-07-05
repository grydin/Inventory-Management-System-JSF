
package Beans;

import DB.DB;
import Model.Supplier;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


@ManagedBean
@RequestScoped
public class FillSupplier {
    
    private List <Supplier> supplierInfo ;
    
    private List <Integer> supplierId;
    
    public FillSupplier() {
        // ------------ bring all supplier info -------------------//
        ResultSet rs = null;
        supplierInfo = new LinkedList<> ();
        DB db = new DB();
        String sql = "select * from suppliers";
        
        // ------------ get supplier id from DB -------------------//

        supplierId = new LinkedList<> ();
        
        try{
            
            rs = db.select(sql);            
            while(rs.next()){
                Supplier supplier = new Supplier();
                supplier.setSid(rs.getInt("sid"));
                supplier.setFullname(rs.getString("fullname"));
                supplier.setCompany(rs.getString("company"));                
                supplier.setEmail(rs.getString("email"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setDate(rs.getString("date"));
                
                supplierId.add(supplier.getSid());
                supplierInfo.add(supplier);                
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

    
    public List<Supplier> getSupplierInfo() {
        return supplierInfo;
    }

    public void setSupplierInfo(List<Supplier> supplierInfo) {
        this.supplierInfo = supplierInfo;
    }

    public List<Integer> getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(List<Integer> supplierId) {
        this.supplierId = supplierId;
    }
    
    
}
