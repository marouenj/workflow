package sample;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VarsAndValsCombinedParser {
	
	public static void main(String[] args) throws MalformedURLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		JsonNode template = read("file:///marouenj/git/test-suite-gen/combiner/vagrant-init/in/complex.json");
		JsonNode _cases = read("file:///marouenj/git/test-suite-gen/combiner/out/complex_exploded.json");
        
        Iterator<JsonNode> _casesElmts = _cases.iterator();
        while (_casesElmts.hasNext()) {
        	JsonNode _case = _casesElmts.next();
        	
        	JsonNode vals = _case.get("Case");
        	Iterator<JsonNode> _valsElmts = vals.iterator();
        	
            VarsAndValsParser.parseTemplateHelper(template, _valsElmts);
            
            System.out.println();
        }
    }
	
	/*
	 * unmarshalls a json file into a tree-based data structure
	 */
	public static JsonNode read(String url) {
		// pattern-match the url
		String json = null;
		try {
			json = IOUtils.toString(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
        // create an ObjectMapper instance.
        ObjectMapper mapper = new ObjectMapper();
        
        // use the ObjectMapper to read the json string and create a tree
        JsonNode _cases = null;
		try {
			_cases = mapper.readTree(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return _cases;
	}
}
