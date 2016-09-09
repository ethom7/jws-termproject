package drpatients;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;



@XmlRootElement(name = "patient")
public class Patient {
    private String name;
    private String insuranceNumber;
    private int id;

    public Patient() {  }

    public Patient(String name, String insuranceNumber) {
        this.name = name;
        this.insuranceNumber = insuranceNumber;
    }


    /*  Setters  */
    public void setName(String name) {
        this.name = name;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public void setId(int id) {
        this.id = id;
    }


    /*  Getters  */
    @XmlElement
    public String getName() {
        return this.who;
    }

    @XmlElement
    public String getInsuranceNumber() {
        return this.insuranceNumber;
    }

    @XmlElement
    public String getId() {
        return this.id;
    }



    /*  Overridden Method  */
    @Override
    public String toString() {
        return String format("%6d: ", id) + name + " : " + insuranceNumber + '\n';
    }
}
