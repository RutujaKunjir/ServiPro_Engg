package com.example.servipro_engg.List;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskList implements Parcelable
{
    private String Call_No;
    private String Cust_id;
    private String Created_Date;
    private String Cust_Name;
    private String Cust_Email;
    private String Cust_Mobile1;
    private String Cust_Mobile2;
    private String Cust_Address;
    private String Model_Id;
    private String Model_Name;
    private String ROUV;
    private String Amc_Id;
    private String Amc_Details;
    private String Status_id;
    private String Status_Of_Work;
    private String Total_Collection;
    private String Comment;
    private String Service_Date;
    private String Service_Time;

    public TaskList() {
    }

    public TaskList(String call_No, String cust_id, String created_Date, String cust_Name, String cust_Email, String cust_Mobile1, String cust_Mobile2, String cust_Address, String model_Id, String model_Name, String ROUV, String amc_Id, String amc_Details, String status_id, String status_Of_Work, String total_Collection, String comment, String service_Date, String service_Time) {
        Call_No = call_No;
        Cust_id = cust_id;
        Created_Date = created_Date;
        Cust_Name = cust_Name;
        Cust_Email = cust_Email;
        Cust_Mobile1 = cust_Mobile1;
        Cust_Mobile2 = cust_Mobile2;
        Cust_Address = cust_Address;
        Model_Id = model_Id;
        Model_Name = model_Name;
        this.ROUV = ROUV;
        Amc_Id = amc_Id;
        Amc_Details = amc_Details;
        Status_id = status_id;
        Status_Of_Work = status_Of_Work;
        Total_Collection = total_Collection;
        Comment = comment;
        Service_Date = service_Date;
        Service_Time = service_Time;
    }



    public String getCall_No() {
        return Call_No;
    }

    public void setCall_No(String call_No) {
        Call_No = call_No;
    }

    public String getCust_id() {
        return Cust_id;
    }

    public void setCust_id(String cust_id) {
        Cust_id = cust_id;
    }

    public String getCreated_Date() {
        return Created_Date;
    }

    public void setCreated_Date(String created_Date) {
        Created_Date = created_Date;
    }

    public String getCust_Name() {
        return Cust_Name;
    }

    public void setCust_Name(String cust_Name) {
        Cust_Name = cust_Name;
    }

    public String getCust_Email() {
        return Cust_Email;
    }

    public void setCust_Email(String cust_Email) {
        Cust_Email = cust_Email;
    }

    public String getCust_Mobile1() {
        return Cust_Mobile1;
    }

    public void setCust_Mobile1(String cust_Mobile1) {
        Cust_Mobile1 = cust_Mobile1;
    }

    public String getCust_Mobile2() {
        return Cust_Mobile2;
    }

    public void setCust_Mobile2(String cust_Mobile2) {
        Cust_Mobile2 = cust_Mobile2;
    }

    public String getCust_Address() {
        return Cust_Address;
    }

    public void setCust_Address(String cust_Address) {
        Cust_Address = cust_Address;
    }

    public String getModel_Id() {
        return Model_Id;
    }

    public void setModel_Id(String model_Id) {
        Model_Id = model_Id;
    }

    public String getModel_Name() {
        return Model_Name;
    }

    public void setModel_Name(String model_Name) {
        Model_Name = model_Name;
    }

    public String getROUV() {
        return ROUV;
    }

    public void setROUV(String ROUV) {
        this.ROUV = ROUV;
    }

    public String getAmc_Id() {
        return Amc_Id;
    }

    public void setAmc_Id(String amc_Id) {
        Amc_Id = amc_Id;
    }

    public String getAmc_Details() {
        return Amc_Details;
    }

    public void setAmc_Details(String amc_Details) {
        Amc_Details = amc_Details;
    }

    public String getStatus_id() {
        return Status_id;
    }

    public void setStatus_id(String status_id) {
        Status_id = status_id;
    }

    public String getStatus_Of_Work() {
        return Status_Of_Work;
    }

    public void setStatus_Of_Work(String status_Of_Work) {
        Status_Of_Work = status_Of_Work;
    }

    public String getTotal_Collection() {
        return Total_Collection;
    }

    public void setTotal_Collection(String total_Collection) {
        Total_Collection = total_Collection;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getService_Date() {
        return Service_Date;
    }

    public void setService_Date(String service_Date) {
        Service_Date = service_Date;
    }

    public String getService_Time() {
        return Service_Time;
    }

    public void setService_Time(String service_Time) {
        Service_Time = service_Time;
    }

    protected TaskList(Parcel in) {
        Call_No = in.readString();
        Cust_id = in.readString();
        Created_Date = in.readString();
        Cust_Name = in.readString();
        Cust_Email = in.readString();
        Cust_Mobile1 = in.readString();
        Cust_Mobile2 = in.readString();
        Cust_Address = in.readString();
        Model_Id = in.readString();
        Model_Name = in.readString();
        ROUV = in.readString();
        Amc_Id = in.readString();
        Amc_Details = in.readString();
        Status_id = in.readString();
        Status_Of_Work = in.readString();
        Total_Collection = in.readString();
        Comment = in.readString();
        Service_Date = in.readString();
        Service_Time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Call_No);
        dest.writeString(Cust_id);
        dest.writeString(Created_Date);
        dest.writeString(Cust_Name);
        dest.writeString(Cust_Email);
        dest.writeString(Cust_Mobile1);
        dest.writeString(Cust_Mobile2);
        dest.writeString(Cust_Address);
        dest.writeString(Model_Id);
        dest.writeString(Model_Name);
        dest.writeString(ROUV);
        dest.writeString(Amc_Id);
        dest.writeString(Amc_Details);
        dest.writeString(Status_id);
        dest.writeString(Status_Of_Work);
        dest.writeString(Total_Collection);
        dest.writeString(Comment);
        dest.writeString(Service_Date);
        dest.writeString(Service_Time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TaskList> CREATOR = new Parcelable.Creator<TaskList>() {
        @Override
        public TaskList createFromParcel(Parcel source) {
            return new TaskList(source);
        }

        @Override
        public TaskList[] newArray(int size) {
            return new TaskList[size];
        }
    };


}
