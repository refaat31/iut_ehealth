package org.iut_ehealth.Student.StudentRefunds;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class refundModel extends RecursiveTreeObject<refundModel> {
    //use stringproperty cuz it's an observable value
    private  SimpleStringProperty BillNo;
    private  SimpleStringProperty status;

    public refundModel(String BillNo,String status){
        this.BillNo = new SimpleStringProperty(BillNo);
        this.status = new SimpleStringProperty(status);
    }

    public String getBillNo() {
        return BillNo.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setBillNo(String billNo) {
        this.BillNo.set(billNo);
    }

    public void setStatus(String status){
        this.status.set(status);
    }
}
