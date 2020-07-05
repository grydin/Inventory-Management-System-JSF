
package DB;

import java.sql.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
public class DB {
    
    private final String DB_URL="jdbc:mysql://localhost/inventory";
    private final String USER="root";
    private final String PASS="root";
    
    Connection conn = null ;
    PreparedStatement stmnt = null ;
    ResultSet rs = null ;
    
    //------------------D7---------------------------//
    
    Statement stmt = null;
   
    //------------------Functions---------------------//
    
    public void connect () throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }
    
    
     
    
    public ResultSet select(String sql, String p1, String p2) throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        stmnt.setString(1, p1);
        stmnt.setString(2, p2);
        rs = stmnt.executeQuery();

        return rs ;
    }

    public ResultSet select(String sql, String p1) throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        stmnt.setString(1, p1);
        rs = stmnt.executeQuery();      
      
        return rs ;
    }
    
        // this method used in the userSelect.java to get the values of the user.
    public ResultSet select(String sql, int p1) throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        stmnt.setInt(1, p1);
        rs = stmnt.executeQuery();
        
        return rs ;
    }
    
      
    public ResultSet select(String sql) throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        rs = stmnt.executeQuery();       
      
        return rs ;
    }
    
    public void update(String sql, String name, String address, String phone, String email, String username, String password, String type)throws Exception{
        connect();
        stmnt = conn.prepareStatement(sql);
        stmnt.setString(1, name);
        stmnt.setString(2, address);
        stmnt.setString(3, phone);
        stmnt.setString(4, email);
        stmnt.setString(5, username);
        stmnt.setString(6, password);
        stmnt.setString(7, type);
        stmnt.executeUpdate();
        releaseResourcesNo();        
    }
    
    //    this update statement used in UserSelect.java to update the user info in DB
    //    update users set name=?,address=?, email=?, phone=?, username=?,type=? where id=?;
    public void update(String sql, String p1, String p2, String p3, String p4, String p5, String p6, int p7)throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        
        stmnt.setString(1, p1);
        stmnt.setString(2, p2);
        stmnt.setString(3, p3);
        stmnt.setString(4, p4);
        stmnt.setString(5, p5);
        stmnt.setString(6, p6);
        stmnt.setInt(7,p7);
        
        stmnt.executeUpdate();
        releaseResourcesNo();        
    }
    
    //  this update statement used in SelectSupplier.java to update the supplier info in DB
    // "update suppliers set fullname=?, company=?, email=?, phone=? where sid=? "
     public void update(String sql, String p1, String p2, String p3, String p4, int p5) throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        
        stmnt.setString(1, p1);
        stmnt.setString(2, p2);
        stmnt.setString(3, p3);
        stmnt.setString(4, p4);
        stmnt.setInt(5,p5);
        
        stmnt.executeUpdate();
        releaseResourcesNo();        
    }
     
      //  this insert statement used in SelectSupplier.java to insert to the supplier table in DB
     // "insert into suppliers (fullname, company, email, phone) values (?,?,?,?) "
    public void update(String sql, String p1, String p2, String p3, String p4) throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        
        stmnt.setString(1, p1);
        stmnt.setString(2, p2);
        stmnt.setString(3, p3);
        stmnt.setString(4, p4);
                
        stmnt.executeUpdate();
        releaseResourcesNo();        
    }
    
    // used in SelectCategory.java for update
    public void update(String sql, String p1,int p2)throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        
        stmnt.setString(1, p1);
        stmnt.setInt(2, p2);
       
        stmnt.executeUpdate();
        releaseResourcesNo();        
    }
    
    // used in SelectCategory.java 
    public void update(String sql, String p1)throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        
        stmnt.setString(1, p1);
       
        stmnt.executeUpdate();
        releaseResourcesNo();        
    }
    
    // used in UserSelect.java, SelectSupplier.java to delete 
    public void update(String sql, int p1)throws Exception {
        connect();
        stmnt = conn.prepareStatement(sql);
        
        stmnt.setInt(1, p1);
       
        stmnt.executeUpdate();
        releaseResourcesNo();        
    }
    
    public void releaseResources() throws Exception{
        
        if(conn != null)
            conn.close();
        if(stmnt != null)
            stmnt.close();
        if(rs != null)
            rs.close();
    }
    
    public void releaseResourcesNo() throws Exception{
        if(conn != null)
            conn.close();
        if(stmnt != null)
            stmnt.close();
        
    }
    
    //-----------------------------D7-----------------------------------//
    
    public void createConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }
    
    public ResultSet select1(String Q) throws Exception {
        createConnection();
        stmt = conn.createStatement();
        rs = stmt.executeQuery(Q);
        return rs;
    }
        
    public void releaseResources1() throws Exception {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }

        }

    }

    public void InsertUpdateDelete(String Q) throws Exception {
        createConnection();
        stmt = conn.createStatement();
        stmt.execute(Q);

        if (stmt != null) {
            stmt.close();
        }
        if (conn != null) {
            conn.close();
        }

    }
}
