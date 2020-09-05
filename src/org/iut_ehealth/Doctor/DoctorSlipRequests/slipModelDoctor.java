package org.iut_ehealth.Doctor.DoctorSlipRequests;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class slipModelDoctor extends RecursiveTreeObject<slipModelDoctor> {
    //use stringproperty cuz it's an observable value
    private SimpleStringProperty id;
    private SimpleStringProperty SlipNo;
    private  SimpleStringProperty status;

    public slipModelDoctor(String id, String BillNo, String status){
        this.id = new SimpleStringProperty(id);
        this.SlipNo = new SimpleStringProperty(BillNo);
        this.status = new SimpleStringProperty(status);
    }

    public String getId() {
        return id.get();
    }

    public String getSlipNo() {
        return SlipNo.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setSlipNo(String slipNo) {
        this.SlipNo.set(slipNo);
    }

    public void setStatus(String status){
        this.status.set(status);
    }
}
