package Beans;

import DB.DB;
import Model.Customer;
import Model.Order;
import Model.Product;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import static Beans.SignIn.userID;

@ManagedBean
@RequestScoped
public class OrderBean {

    private double finalTotalPrice;
    private String selectedCustomer;
    //Order
    List<Order> orderData;
    private int oid;
    private int opid;
    private String opname;
    private double oprice;
    private int oquantity;
    private double ototal;
    private int oinvoice;

    private int currentInvoice;

    private List<Integer> optionsOrder = new ArrayList<Integer>();
    private String selectedItemOrderID = "0";

    private int currentQuantity;
    //Customer
    List<Customer> customerData;
    private int cid;
    private String fullname;
    private String address;
    private String phone;
    private String email;
    private String company;
    private String cdate;
    private String customerNameSearch;
    private List<Integer> optionsCustomer;
    private String selectedItemCustomerID = "0";
    private String selectedCustomerID = "0";

    //Product
    List<Product> productData;
    private int pid;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String pdate;
    private int sid;
    private int catid;
    private String productNameSearch;
    private List<Integer> optionsProduct;
    private String selectedItemProductID = "0";

    private int qqq;
    private int deletedQuantity;

    //Category
    private List<String> optionsCategory = new ArrayList<String>();
    private String selectedItemCategoryName = "0";

    public OrderBean() {

        customerData = new LinkedList<>();
        optionsCustomer = new ArrayList<Integer>();
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
                optionsCustomer.add(rs.getInt("cid"));
                customerData.add(c);

            }
            dbm.releaseResources1();

            productData = new LinkedList<>();
            optionsProduct = new ArrayList<Integer>();
            DB dbm1 = new DB();
            ResultSet rs1 = dbm1.select1("SELECT * from products;");
            while (rs1.next()) {

                Product c = new Product();
                c.setPid(rs1.getInt("pid"));
                c.setName(rs1.getString("name"));
                c.setDescription(rs1.getString("description"));
                c.setPrice(rs1.getDouble("price"));
                c.setQuantity(rs1.getInt("quantity"));
                c.setCatid(rs1.getInt("catid"));
                c.setSid(rs1.getInt("sid"));
                c.setDate(rs1.getString("date"));
                optionsProduct.add(rs1.getInt("pid"));
                productData.add(c);

            }
            dbm1.releaseResources1();

            DB dbm2 = new DB();
            ResultSet rs2 = dbm2.select1("SELECT * from categories;");
            while (rs2.next()) {
                optionsCategory.add(rs2.getString("name"));
            }

            dbm2.releaseResources1();
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }

    }

    public void showProducts() {
        try {

            productData = new LinkedList<>();
            optionsProduct = new ArrayList<Integer>();
            DB dbm1 = new DB();
            ResultSet rs1 = dbm1.select1("SELECT * from products;");
            while (rs1.next()) {

                Product c = new Product();
                c.setPid(rs1.getInt("pid"));
                c.setName(rs1.getString("name"));
                c.setDescription(rs1.getString("description"));
                c.setPrice(rs1.getDouble("price"));
                c.setQuantity(rs1.getInt("quantity"));
                c.setCatid(rs1.getInt("catid"));
                c.setSid(rs1.getInt("sid"));
                c.setDate(rs1.getString("date"));
                optionsProduct.add(rs1.getInt("pid"));
                productData.add(c);

            }
            dbm1.releaseResources1();
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }

    }

    public void takeLastInvoice() {
        try {
            DB db = new DB();
            ResultSet rs = db.select1("SELECT max(invid) FROM invoice   LIMIT  1;");
            rs.next();
            currentInvoice = rs.getInt("max(invid)");
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public void confirmOrder() {
        try {
            co();
            DB dbm = new DB();

            String Q = "insert into invoice (invid,cid,soldby,total) values(" + currentInvoice + "," + selectedCustomer + "," + userID + "," + "(select sum(total) from orders where invid =" + currentInvoice + " ))";
            dbm.InsertUpdateDelete(Q);
            dbm.releaseResources1();

            FacesMessage facesMessage1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your record has inserted", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage1);

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public void showTotal() {
        ResultSet rs = null;
        try {
            co();
            DB dbm = new DB();

            String Q = "select sum(total) from orders where invid = " + currentInvoice  + ";" ;
            rs = dbm.select1(Q);
            rs.next();
            finalTotalPrice = rs.getDouble("sum(total)");
            dbm.releaseResources1();

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public void showOrder() {
        try {
            co();
            orderData = new LinkedList<>();
            optionsOrder = new ArrayList<Integer>();
            DB dbm = new DB();
            ResultSet rs1 = dbm.select1("SELECT * from orders where invid =" + currentInvoice + ";");
            while (rs1.next()) {

                Order c = new Order();
                c.setOid(rs1.getInt("oid"));
                c.setOpid(rs1.getInt("pid"));
                c.setOpname(rs1.getString("pname"));
                c.setOprice(rs1.getDouble("price"));
                c.setOquantity(rs1.getInt("quantity"));
                c.setOtotal(rs1.getDouble("total"));
                c.setOinvoice(rs1.getInt("invid"));

                optionsOrder.add(rs1.getInt("oid"));
                orderData.add(c);

            }
            dbm.releaseResources1();
            showTotal();
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }

    }

    public void co() {
        takeLastInvoice();
        currentInvoice++;
    }

    public void addBtn() {

        try {
            co();
            if (check()) {

                if (orederFound()) {

                    DB dbm2 = new DB();

                    String Q1 = "update orders set "
                            + " quantity=  (quantity +" + currentQuantity + ")"
                            + " where pid =" + getSelectedItemProductID() + ";";
                    dbm2.InsertUpdateDelete(Q1);

                    String Q2 = "update orders set "
                            + " total=  (price * quantity)"
                            + " where pid =" + getSelectedItemProductID() + ";";
                    dbm2.InsertUpdateDelete(Q2);
                    dbm2.releaseResources1();

                } else {
                    DB dbm1 = new DB();

                    String Q = "insert into orders (pid,pname,price,quantity,total,invid) values"
                            + "(" + getSelectedItemProductID() + "," + "(select name from products where pid =" + getSelectedItemProductID() + "), "
                            + "(select price from products where pid =" + getSelectedItemProductID() + ") ," + currentQuantity + ","
                            + "(select price from products where pid =" + getSelectedItemProductID() + ")*" + currentQuantity + " ,  " + currentInvoice + ");";

                    dbm1.InsertUpdateDelete(Q);
                    dbm1.releaseResources1();
                }
                DB dbm2 = new DB();

                String Q1 = "update products set "
                        + " quantity=  (quantity -" + currentQuantity + ")"
                        + " where pid =" + getSelectedItemProductID() + ";";
                dbm2.InsertUpdateDelete(Q1);
                dbm2.releaseResources1();

                showOrder();
                showProducts();

            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }

    }

    public void deleteOrder() {
        DB db = new DB();
        try {

            ResultSet rs = db.select1("SELECT quantity from orders where oid =" + getSelectedItemOrderID());
            rs.next();
            deletedQuantity = rs.getInt("quantity");

            String Q1 = "update products set "
                    + " quantity= (quantity + " + getDeletedQuantity() + ")"
                    + " where pid = (select pid from orders where oid = " + getSelectedItemOrderID() + ");";
            db.InsertUpdateDelete(Q1);

            String sql = "delete from orders where oid=" + getSelectedItemOrderID();
            db.InsertUpdateDelete(sql);
            db.releaseResources1();

            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Record is deleted", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

            showProducts();
            showOrder();
        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    public void searchCustomer() {

        customerData = new LinkedList<>();
        optionsCustomer = new ArrayList<Integer>();
        try {
            if (CustomerFound()) {
                DB dbm = new DB();
                ResultSet rs = dbm.select1("SELECT * from customers where fullname like '%" + customerNameSearch + "%' ;");
                while (rs.next()) {
                    Customer c = new Customer();
                    c.setCid(rs.getInt("cid"));
                    c.setFullname(rs.getString("fullname"));
                    c.setAddress(rs.getString("address"));
                    c.setEmail(rs.getString("email"));
                    c.setPhone(rs.getString("phone"));
                    c.setCompany(rs.getString("company"));
                    c.setDate(rs.getString("date"));
                    optionsCustomer.add(rs.getInt("cid"));
                    customerData.add(c);
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

    public void searchProduct() {

        productData = new LinkedList<>();
        optionsProduct = new ArrayList<Integer>();
        try {
            if (ProductFound()) {

                DB dbm = new DB();
                ResultSet rs = dbm.select1("SELECT * from products where name like '%" + productNameSearch + "%' ;");
                while (rs.next()) {
                    Product c = new Product();
                    c.setPid(rs.getInt("pid"));
                    c.setName(rs.getString("name"));
                    c.setDescription(rs.getString("description"));
                    c.setPrice(rs.getDouble("price"));
                    c.setQuantity(rs.getInt("quantity"));
                    c.setCatid(rs.getInt("catid"));
                    c.setSid(rs.getInt("sid"));
                    c.setDate(rs.getString("date"));
                    optionsProduct.add(rs.getInt("pid"));
                    productData.add(c);
                }

                dbm.releaseResources1();
            } else {
                throw new Exception("product Name Not Found");
            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }
    }

    public void searchProductByCategory() {

        productData = new LinkedList<>();
        optionsProduct = new ArrayList<Integer>();
        try {

            DB dbm = new DB();
            ResultSet rs = dbm.select1("SELECT * from products where catid = (select catid from categories where name = '" + getSelectedItemCategoryName() + "') ;");
            while (rs.next()) {
                Product c = new Product();
                c.setPid(rs.getInt("pid"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setPrice(rs.getDouble("price"));
                c.setQuantity(rs.getInt("quantity"));
                c.setCatid(rs.getInt("catid"));
                c.setSid(rs.getInt("sid"));
                c.setDate(rs.getString("date"));
                optionsProduct.add(rs.getInt("pid"));
                productData.add(c);

            }
            dbm.releaseResources1();

        } catch (Exception ex) {
            String message = ex.getMessage();
            FacesMessage facesMessage = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        }
    }

    private boolean CustomerFound() throws Exception {

        DB dbm = new DB();
        ResultSet rs = dbm.select1("select * from customers where fullname like '%" + customerNameSearch + "%' ;");
        boolean found = rs.next();
        dbm.releaseResources1();

        if (found) {
            return true;
        }
        return false;

    }

    private boolean orederFound() throws Exception {

        DB dbm = new DB();
        String sql = "select * from orders where pid =" + getSelectedItemProductID() + " and invid=" + currentInvoice + " ;";
        ResultSet rs = dbm.select1(sql);
        boolean found = rs.next();
        dbm.releaseResources1();

        if (found) {
            return true;
        }
        return false;

    }

    private boolean ProductFound() throws Exception {

        DB dbm = new DB();
        ResultSet rs = dbm.select1("select * from products where name like '%" + productNameSearch + "%' ;");
        boolean found = rs.next();
        dbm.releaseResources1();

        if (found) {
            return true;
        }
        return false;

    }

    public boolean check() throws Exception {

        if (currentQuantity <= 0) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "quantity cannot be zero or less", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }
        DB db = new DB();
        ResultSet rs = db.select1("SELECT quantity from products where pid =" + getSelectedItemProductID());
        rs.next();
        qqq = rs.getInt("quantity");

        if (currentQuantity > qqq) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "selected quantity cannot be more than the quantity in stock", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return false;
        }

        return true;
    }

    public int getDeletedQuantity() {
        return deletedQuantity;
    }

    public void setDeletedQuantity(int deletedQuantity) {
        this.deletedQuantity = deletedQuantity;
    }

    public String getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(String selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public List<Order> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<Order> orderData) {
        this.orderData = orderData;
    }

    public List<Integer> getOptionsOrder() {
        return optionsOrder;
    }

    public void setOptionsOrder(List<Integer> optionsOrder) {
        this.optionsOrder = optionsOrder;
    }

    public String getSelectedItemOrderID() {
        return selectedItemOrderID;
    }

    public void setSelectedItemOrderID(String selectedItemOrderID) {
        this.selectedItemOrderID = selectedItemOrderID;
    }

    public List<Customer> getCustomerData() {
        return customerData;
    }

    public void setCustomerData(List<Customer> customerData) {
        this.customerData = customerData;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCurrentInvoice() {
        return currentInvoice;
    }

    public void setCurrentInvoice(int currentInvoice) {
        this.currentInvoice = currentInvoice;
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

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCustomerNameSearch() {
        return customerNameSearch;
    }

    public void setCustomerNameSearch(String customerNameSearch) {
        this.customerNameSearch = customerNameSearch;
    }

    public List<Integer> getOptionsCustomer() {
        return optionsCustomer;
    }

    public void setOptionsCustomer(List<Integer> optionsCustomer) {
        this.optionsCustomer = optionsCustomer;
    }

    public String getSelectedItemCustomerID() {
        return selectedItemCustomerID;
    }

    public void setSelectedItemCustomerID(String selectedItemCustomerID) {
        this.selectedItemCustomerID = selectedItemCustomerID;
    }

    public List<Product> getProductData() {
        return productData;
    }

    public void setProductData(List<Product> productData) {
        this.productData = productData;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public String getSelectedCustomerID() {
        return selectedCustomerID;
    }

    public void setSelectedCustomerID(String selectedCustomerID) {
        this.selectedCustomerID = selectedCustomerID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getProductNameSearch() {
        return productNameSearch;
    }

    public void setProductNameSearch(String productNameSearch) {
        this.productNameSearch = productNameSearch;
    }

    public List<Integer> getOptionsProduct() {
        return optionsProduct;
    }

    public void setOptionsProduct(List<Integer> optionsProduct) {
        this.optionsProduct = optionsProduct;
    }

    public String getSelectedItemProductID() {
        return selectedItemProductID;
    }

    public void setSelectedItemProductID(String selectedItemProductID) {
        this.selectedItemProductID = selectedItemProductID;
    }

    public List<String> getOptionsCategory() {
        return optionsCategory;
    }

    public void setOptionsCategory(List<String> optionsCategory) {
        this.optionsCategory = optionsCategory;
    }

    public String getSelectedItemCategoryName() {
        return selectedItemCategoryName;
    }

    public void setSelectedItemCategoryName(String selectedItemCategoryName) {
        this.selectedItemCategoryName = selectedItemCategoryName;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getOpid() {
        return opid;
    }

    public void setOpid(int opid) {
        this.opid = opid;
    }

    public String getOpname() {
        return opname;
    }

    public void setOpname(String opname) {
        this.opname = opname;
    }

    public double getOprice() {
        return oprice;
    }

    public int getQqq() {
        return qqq;
    }

    public void setQqq(int qqq) {
        this.qqq = qqq;
    }

    public void setOprice(double oprice) {
        this.oprice = oprice;
    }

    public int getOquantity() {
        return oquantity;
    }

    public void setOquantity(int oquantity) {
        this.oquantity = oquantity;
    }

    public double getOtotal() {
        return ototal;
    }

    public void setOtotal(double ototal) {
        this.ototal = ototal;
    }

    public int getOinvoice() {
        return oinvoice;
    }

    public void setOinvoice(int oinvoice) {
        this.oinvoice = oinvoice;
    }

    public double getFinalTotalPrice() {
        return finalTotalPrice;
    }

    public void setFinalTotalPrice(double finalTotalPrice) {
        this.finalTotalPrice = finalTotalPrice;
    }

}
