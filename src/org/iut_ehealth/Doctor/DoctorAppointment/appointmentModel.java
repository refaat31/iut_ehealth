package org.iut_ehealth.Doctor.DoctorAppointment;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class appointmentModel extends RecursiveTreeObject<appointmentModel> {
    //use stringproperty cuz it's an observable value
    private  SimpleStringProperty st_id;
    private  SimpleStringProperty time;
    private  SimpleStringProperty problem;
    private  SimpleStringProperty day;
    private  SimpleStringProperty month;
    private  SimpleStringProperty year;


    public appointmentModel(String st_id, String time, String problem, String day, String month, String year){
        this.st_id = new SimpleStringProperty(st_id);
        this.time = new SimpleStringProperty(time);
        this.problem = new SimpleStringProperty(problem);
        this.day = new SimpleStringProperty(day);
        this.month = new SimpleStringProperty(month);
        this.year = new SimpleStringProperty(year);
    }

    public String getSt_id() {
        return st_id.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getProblem() {
        return problem.get();
    }

    public String getDay() {
        return day.get();
    }

    public String getMonth() {
        return month.get();
    }

    public String getYear() {
        return year.get();
    }

//    public void setId(String st_id) {
//        this.st_id.set(st_id);
//    }

    public void setSt_id(String st_id) {
        this.st_id.set(st_id);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public void setProblem(String problem) { this.problem.set(problem); }

    public void setDay(String day) {
        this.day.set(day);
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public void setYear(String year) {
        this.year.set(year);
    }
}
