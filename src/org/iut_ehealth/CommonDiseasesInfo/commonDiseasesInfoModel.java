package org.iut_ehealth.CommonDiseasesInfo;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class commonDiseasesInfoModel extends RecursiveTreeObject<commonDiseasesInfoModel> {
    //use stringproperty cuz it's an observable value
    private SimpleStringProperty name;
    private SimpleStringProperty symptoms;
    private SimpleStringProperty prevention;
    private SimpleStringProperty cure;

    public commonDiseasesInfoModel(String name, String symptoms, String prevention, String cure) {
        this.name = new SimpleStringProperty(name);
        this.symptoms = new SimpleStringProperty(symptoms);
        this.prevention = new SimpleStringProperty(prevention);
        this.cure = new SimpleStringProperty(cure);
    }

    public String getName() {
        return name.get();
    }

    public String getSymptoms() {
        return symptoms.get();
    }

    public String getPrevention() {
        return prevention.get();
    }

    public String getCure() {
        return cure.get();
    }

//    public void setId(String st_id) {
//        this.st_id.set(st_id);
//    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSymptoms(String symptoms) {
        this.symptoms.set(symptoms);
    }

    public void setPrevention(String prevention) { this.prevention.set(prevention); }

    public void setCure(String cure) { this.cure.set(cure); }
}
