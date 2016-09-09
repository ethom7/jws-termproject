package drpatients;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "doctorsList")
public class DoctorsList {
    private List<Doctor> docs;
    private AtomicInteger docId;


    /*  Constructor  */
    public DoctorsList() {
        docs = new CopyOnWriteArrayList<Doctor>();
        docId = new AtomicInteger();
    }


    /*  Getter  */
    @XmlElement
    @XmlElementWrapper(name = "doctors")
    public List<Doctor> getDoctors() {
        return this.docs;
    }

    /*  Setter  */
    public void setDoctors(List<Doctor> docs) {
        this.docs = docs;
    }


    /*  Methods  */
    public Doctor find(int id) {
        Doctor doc = null;
        // Search the list -- for now, the list is short enough that
        // a linear search is ok but binary search would be better if the
        // list got to be an order-of-magnitude larger in size.
        for (Doctor d : docs) {
            if (d.getId() == id) {
                doc = d;
                break;
            }
        }
        return doc;
    }

    public int add(String name, String number) {
        int id = docId.incrementAndGet();
        Doctor d = new Doctor();

        d.setName(name);
        d.setNumber(number);
        d.setId(id);
        docs.add(d);

        return id;
    }


    /*  Overridden Method  */
    @Override
    public String toString() {
        String s = "";

        for (Doctor d : docs) {
            s += d.toString();
        }

        return s;
    }

}
