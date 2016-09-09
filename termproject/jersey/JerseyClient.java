import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.representation.Form;

/* Jersey Client for places3 */

public class JerseyClient {
    private static final String baseUrl = "http://localhost:8080/places3/resourcesC";

    public static void main(String[ ] args) {
	new JerseyClient().demo();
    }

    private void demo() {
	Client client = Client.create();
	client.setFollowRedirects(true); // in case the service redirects

	WebResource resource = client.resource(baseUrl);

                // getALL methods are commented out as they are not needed for results run
	//getAllXMLDemo(resource);
             //getAllJSONDemo(resource);


	String url = baseUrl;
	resource = client.resource(url);
	getOneXMLDemo(resource, "3"); // 1) get place 3 in xml
             postDemo(resource); // 2) post new place place
	deleteDemo(resource, "3"); // 3) delete place 3
    }

    // GET XML method
    private void getAllXMLDemo(WebResource resource) {
	// GET all XML
	String response =
	    resource.path("/xml").accept(MediaType.APPLICATION_XML_TYPE).get(String.class);
	report("GET all in XML:\n", response);
    }

    // GET JSON method
    private void getAllJSONDemo(WebResource resource) {
	// GET all JSON
	String response =
	    resource.path("/json").accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
	report("GET all in JSON:\n", response);
    }

    // GET XML method
    private void getOneXMLDemo(WebResource resource, String id) {
	String response =
	    resource.path("/xml/" + id).accept(MediaType.APPLICATION_XML_TYPE).get(String.class);
	report("GET one in XML:\n", response);
    }

    // GET JSON method
    private void getOneJSONDemo(WebResource resource, String id) {
        String response =
        resource.path("/json/" + id).accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
        report("GET one in JSON:\n", response);
    }



    // POST
    private void postDemo(WebResource resource) {
	Form form = new Form(); // HTTP body, a simple hash
	form.add("who", "Minneapolis");
	form.add("what1", "Minneapolis Sculpture Garden, is one of the largest urban sculpture gardens in the US.");
             form.add("what2", "Lake Harriet, offers outdoor recreation including bike and pedestrian trails, sailing, and the bandshell a public pavilion that often features live music.");

	String response =
	    resource.path("/create").type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
	    .accept(MediaType.TEXT_PLAIN_TYPE)
	    .post(String.class, form);
	report("POST:\n", response);
    }

    // DELETE
    private void deleteDemo(WebResource resource, String id) {
	String response =
	    resource.path("/delete/" + id).accept(MediaType.TEXT_PLAIN_TYPE).delete(String.class);
	report("DELETE:\n", response);
    }

    private void report(String msg, String response) {
	System.out.println("\n" + msg + response);
    }
}
