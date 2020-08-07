package org.iut_ehealth.Doctor.DoctorBillRequests;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class refundModelDoctor extends RecursiveTreeObject<refundModelDoctor> {
    //use stringproperty cuz it's an observable value
    private SimpleStringProperty id;
    private SimpleStringProperty BillNo;
    private  SimpleStringProperty status;

    public refundModelDoctor(String id,String BillNo,String status){
        this.BillNo = new SimpleStringProperty(BillNo);
        this.status = new SimpleStringProperty(status);
    }

    public String getId() {
        return id.get();
    }

    public String getBillNo() {
        return BillNo.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setBillNo(String billNo) {
        this.BillNo.set(billNo);
    }

    public void setStatus(String status){
        this.status.set(status);
    }
}
