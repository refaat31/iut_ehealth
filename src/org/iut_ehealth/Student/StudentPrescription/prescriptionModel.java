package org.iut_ehealth.Student.StudentPrescription;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class prescriptionModel extends RecursiveTreeObject<prescriptionModel> {
    //use stringproperty cuz it's an observable value
    private  SimpleStringProperty slipNo;
    private  SimpleStringProperty status;

    public prescriptionModel(String SlipNo,String status){
        this.slipNo = new SimpleStringProperty(SlipNo);
        this.status = new SimpleStringProperty(status);
    }

    public String getSlipNo() {
        return slipNo.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setSlipNo(String slipNo) {
        this.slipNo.set(slipNo);
    }

    public void setStatus(String status){
        this.status.set(status);
    }
}
