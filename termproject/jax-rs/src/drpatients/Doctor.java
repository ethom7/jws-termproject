package drpatients;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;



@XmlRootElement(name = "doctor")
public class Doctor {
    private String name;
    private String number;
    private PatientsList patients;
    private int id;

    public Doctor() {  }


    /*  Setters  */
    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPatients(PatientsList patients) {
        this.patients = patients;
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
    public String getNumber() {
        return this.number;
    }

    @XmlElement
    public PatientsList getPatients() {
        return this.patients;
    }

    @XmlElement
    public String getId() {
        return this.id;
    }



    /*  Overridden Method  */
    @Override
    public String toString() {
        return String format("%6d: ", id) + name + " : " + '\n' + patients;
    }
}
