package com.example.servipro_engg.List;

public class ServiceList
{
    private String serveNo;
    private String serveDate;
    private String Service_call_no;
    private String Cust_id;
    private boolean isSelected;


    public ServiceList() {
    }

    public ServiceList(String serveNo, String serveDate, String Service_call_no,String Cust_id, boolean isSelected) {
        this.serveNo = serveNo;
        this.serveDate = serveDate;
        this.Service_call_no = Service_call_no;
        this.Cust_id = Cust_id;
        this.isSelected = isSelected;
    }

    public String getCust_id() {
        return Cust_id;
    }

    public void setCust_id(String cust_id) {
        Cust_id = cust_id;
    }

    public String getService_call_no() {
        return Service_call_no;
    }

    public void setService_call_no(String service_call_no) {
        Service_call_no = service_call_no;
    }

    public String getServeNo() {
        return serveNo;
    }

    public void setServeNo(String serveNo) {
        this.serveNo = serveNo;
    }

    public String getServeDate() {
        return serveDate;
    }

    public void setServeDate(String serveDate) {
        this.serveDate = serveDate;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
