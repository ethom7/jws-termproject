package drpatients;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patientsList")
public class PatientsList {
    private List<Patient> pats;
    private AtomicInteger patId;


    /*  Constructor  */
    public PatientsList() {
        pats = new CopyOnWriteArrayList<Patient>();
        patId = new AtomicInteger();
    }


    /*  Getter  */
    @XmlElement
    @XmlElementWrapper(name = "patients")
    public List<Patient> getPatients() {
        return this.pats;
    }

    /*  Setter  */
    public void setPatients(List<Patient> pats) {
        this.pats = pats;
    }


    /*  Methods  */
    public Patient find(int id) {
        Patient pat = null;
        // Search the list -- for now, the list is short enough that
        // a linear search is ok but binary search would be better if the
        // list got to be an order-of-magnitude larger in size.
        for (Patient p : pats) {
            if (p.getId() == id) {
                pat = p;
                break;
            }
        }
        return pat;
    }

    public int add(String name, String insuranceNumber) {
        int id = patId.incrementAndGet();
        Patient p = new Patient();

        p.setName(name);
        p.setInsuranceNumber(insuranceNumber);
        p.setId(id);
        pats.add(p);

        return id;
    }


    /*  Overridden Method  */
    @Override
    public String toString() {
        String s = "";

        for (Patient p : pats) {
            s += p.toString();
        }

        return s;
    }

}
