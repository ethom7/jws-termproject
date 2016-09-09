package drpatients;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.servlet.ServletContext;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
public class DrPatientsRS {
    @Context
    private ServletContext sctx;          // dependency injection
    private static DoctorsList dlist;  //set in populate()

    public DrPatientsRS() { }

    @GET
    @Path("/xml")
    @Produces({MediaType.APPLICATION_XML})
    public Response getXml() {
        checkContext();
        return Response.ok(dlist, "application/xml").build();
    }

    @GET
    @Path("/xml/{id: \\d+}")
    @Produces({MediaType.APPLICATION_XML}) // could use "application/xml" instead
    public Response getXml(@PathParam("id") int id) {
        checkContext();
        return toRequestedType(id, "application/xml");
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/json")
    public Response getJson() {
        checkContext();
        return Response.ok(toJson(dlist), "application/json").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/json/{id: \\d+}")
    public Response getJson(@PathParam("id") int id) {
        checkContext();
        return toRequestedType(id, "application/json");
    }

    @GET
    @Path("/plain")
    @Produces({MediaType.TEXT_PLAIN})
    public String getPlain() {
        checkContext();
        return dlist.toString();
    }

    @POST
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/create")
    public Response create(@FormParam("name") String name,
               @FormParam("number") String number, @FormParam("patients") String patients) {

        // input for patients formatted as patients=Jim Johnson,CC456!
        // each patient is separated by a '!' and the name and insuranceNumber separated by ','

        checkContext();
        String msg = null;
        // Require all three properties to create.
        if (name == null || number == null || patients == null) {
            msg = "Property 'name', 'number', or 'patients' is missing.\n";
            return Response.status(Response.Status.BAD_REQUEST).
                                           entity(msg).
                                           type(MediaType.TEXT_PLAIN).
                                           build();
        }

        // Otherwise, create the Doctor and add it to the collection.
        PatientsList patsList = new PatientsList();
        String[] patient = patients.split("!");

        for (String p : patient) {
            String[] pfield = p.split(",");
            int pid = patsList.add(pfield[0], pfield[1]);

        }



        int did = addDoctor(name, number, patsList.getPatients());
        msg = "Doctor " + did + " created: (name = " + name + " number = " + number + " patients " + ").\n";
        return Response.ok(msg, "text/plain").build();
    }

    @PUT
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/update")
    public Response update(@FormParam("id") int id, @FormParam("name") String name) {
        checkContext();

        // Check that sufficient data are present to do an edit.
        String msg = null;
        if (name == null && id == null)
            msg = "Neither name nor id is given: not enough information to edit.\n";

        Doctor d = dlist.find(id);
        if (d == null)
            msg = "There is no doctor with ID " + id + "\n";

        if (msg != null)
            return Response.status(Response.Status.BAD_REQUEST).
                                           entity(msg).
                                           type(MediaType.TEXT_PLAIN).
                                           build();
        // Update.
        if (name != null) d.setName(name);

        msg = "Doctor " + id + " has been updated.\n";

        return Response.ok(msg, "text/plain").build();
    }

    @DELETE
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/delete/{id: \\d+}")
    public Response delete(@PathParam("id") int id) {
        checkContext();

        String msg = null;
        Doctor d = dlist.find(id);
        if (d == null) {
            msg = "There is no Doctor with ID " + id + ". Cannot delete.\n";
            return Response.status(Response.Status.BAD_REQUEST).
                                           entity(msg).
                                           type(MediaType.TEXT_PLAIN).
                                           build();
        }

        dlist.getDoctors().remove(d);
        msg = "Place " + id + " deleted.\n";

        return Response.ok(msg, "text/plain").build();
    }

    //** utilities
    private void checkContext() {
        if (dlist == null) populate();
    }

    private void populate() {
        dlist = new DoctorsList();
        plist = new PatientsList();

        String dfilename = "/WEB-INF/data/drs.db";
        InputStream din = sctx.getResourceAsStream(dfilename);

        String pfilename = "/WEB-INF/data/patients.db";
        InputStream pin = sctx.getResourceAsStream(pfilename);

        // Read the data into the array of Predictions.
        if (din != null && pin != null) {
            try {
                BufferedReader dreader = new BufferedReader(new InputStreamReader(din));
                BufferedReader preader = new BufferedReader(new InputStreamReader(pin));


                String drecord = null;
                String precord = null;
                while ((drecord = dreader.readLine()) != null) {
                    String[] dparts = drecord.split("!");
                    int numberOfPatients = 0;

                    while ((precord = preader.readLine()) != null) {
                        String[] pparts = precord.split("!");
                        if (numberOfPatients <= Integer.parseInt(dparts[1])) {

                            // Need to figure out way to add patient list to doctor, then to add doctor
                            addDoctor(dparts[0], dparts[1], addPatients(pparts[0], pparts[1]));
                            numberOfPatients++;
                        }

                    }
                    // from there to here

                }
            } catch (Exception e) {
                throw new RuntimeException("I/O failed!");
            }
        }
    }

    // Add a new doctor to the list.
    private int addDoctor(String name, String number, PatientsList patients) {
        int id = dlist.add(name, number, patients);
        return id;
    }


    private int addPatient(String name, String insuranceNumber) {
        int id = plist.add(name, insuranceNumber);
        return id;
    }

    // Doctor --> JSON document
    private String toJson(Doctor doc) {
    String json = "If you see this, there's a problem.";
        try {
            json = new ObjectMapper().writeValueAsString(doc);
        } catch(Exception e) { }

        return json;
    }

    // PredictionsList --> JSON document
    private String toJson(DoctorsList dlist) {
        String json = "If you see this, there's a problem.";
        try {
            json = new ObjectMapper().writeValueAsString(dlist);
        } catch(Exception e) { }

        return json;
    }

    // Generate an HTTP error response or typed OK response.
    private Response toRequestedType(int id, String type) {
        Doctor doc = dlist.find(id);

        if (doc == null) {
            String msg = id + " is a bad ID.\n";
            return Response.status(Response.Status.BAD_REQUEST).
                                           entity(msg).
                                           type(MediaType.TEXT_PLAIN).
                                           build();
        }
        else if (type.contains("json"))
            return Response.ok(toJson(doc), type).build();
        else
            return Response.ok(doc, type).build(); // toXml is automatic
    }
}
