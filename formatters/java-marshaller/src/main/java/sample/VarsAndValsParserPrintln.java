package sample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class VarsAndValsParserPrintln {
	
	public static void parseHelper(JsonNode curr, Iterator<JsonNode> itrVals) {
		if (curr.getNodeType() == JsonNodeType.ARRAY) { // root should start with an array of objects
			Iterator<JsonNode> currElmts = curr.iterator();
	        while (currElmts.hasNext()) {
	        	JsonNode currElmt = currElmts.next();
	        	if (currElmt.getNodeType() == JsonNodeType.OBJECT) { // under there should be an object encompassing, among other things, the vals of the var
	    			JsonNode prefix = currElmt.get("prefix");
	    			JsonNode suffix = currElmt.get("suffix");
	    			if (prefix != null && suffix != null) { // complex object
	    				System.out.println("instantiating " + prefix + "." + suffix);
	    				JsonNode vals = currElmt.get("vals");
	    				if (vals != null && vals.getNodeType() == JsonNodeType.ARRAY) {
	    					parse(vals, prefix + "." + suffix, itrVals);
	    				}
		        	} else { // primitive object (int, boolean, string, ...)
		        		System.out.println("setting val " + itrVals.next() + " to var " + currElmt.get("name"));
		        	}
		        }
	        }
		}
	}
	
	public static void parse(JsonNode curr, String parent, Iterator<JsonNode> itrVals) {
		if (curr.getNodeType() == JsonNodeType.ARRAY) {
			Iterator<JsonNode> currElmts = curr.iterator();
	        while (currElmts.hasNext()) {
	        	JsonNode currSub = currElmts.next();
	        	parse(currSub, parent, itrVals);
	        }
		} else if (curr.getNodeType() == JsonNodeType.OBJECT) {
			JsonNode prefix = curr.get("prefix");
			JsonNode suffix = curr.get("suffix");
			if (prefix != null && suffix != null) { // complex object
				System.out.println("instantiating " + prefix + "." + suffix);
				System.out.println("setting instantiated " + prefix + "." + suffix + " to object " + parent + " through setter " + curr.get("setter"));
				JsonNode vals = curr.get("vals");
				if (vals != null && vals.getNodeType() == JsonNodeType.ARRAY) {
					parse(vals, prefix + "." + suffix, itrVals);
				}
        	} else { // primitive object (int, boolean, string, ...)
        		System.out.println("setting val " + itrVals.next() + " to object " + parent + " through setter " + curr.get("setter"));
        	}
		}
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		String url = "file:///Users/marouen.jilani/dev/vagrant/test-suite-gen/combiner/vagrant-init/in/complex.json";
		String json = IOUtils.toString(new URL(url));
 
        // create an ObjectMapper instance.
        ObjectMapper mapper = new ObjectMapper();
        
        // use the ObjectMapper to read the json string and create a tree
        JsonNode objs = mapper.readTree(json);
    	System.out.println(objs.size());
 
        // print node type
        System.out.println(objs.getNodeType());
        
//        parseHelper(objs);
    }
}
