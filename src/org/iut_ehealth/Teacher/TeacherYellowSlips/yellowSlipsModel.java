package org.iut_ehealth.Teacher.TeacherYellowSlips;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class yellowSlipsModel extends RecursiveTreeObject<yellowSlipsModel> {
    //use stringproperty cuz it's an observable value
    private  SimpleStringProperty SlipNo;
    private  SimpleStringProperty Id;


    public yellowSlipsModel(String SlipNo, String Id){
        this.SlipNo = new SimpleStringProperty(SlipNo);
        this.Id = new SimpleStringProperty(Id);
    }

    public String getSlipNo() {
        return SlipNo.get();
    }

    public String getId() {
        return Id.get();
    }

    public void setSlipNo(String SlipNo) { this.SlipNo.set(SlipNo); }

    public void setId(String Id){
        this.Id.set(Id);
    }
}
