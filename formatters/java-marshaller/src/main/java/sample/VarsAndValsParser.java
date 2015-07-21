package sample;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class VarsAndValsParser {
	
	public static void parseTemplateHelper(JsonNode curr, Iterator<JsonNode> itrVals) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (curr.getNodeType() == JsonNodeType.ARRAY) { // root should start with an array of objects
			Iterator<JsonNode> currElmts = curr.iterator();
			
	        while (currElmts.hasNext()) {
	        	JsonNode currElmt = currElmts.next();
	        	if (currElmt.getNodeType() == JsonNodeType.OBJECT) { // under there should be an object encompassing, among other things, the vals of the var
	    			JsonNode prefix = currElmt.get("prefix");
	    			JsonNode suffix = currElmt.get("suffix");
	    			if (prefix != null && suffix != null) { // complex object
	    				String clazz = prefix.asText() + "." + suffix.asText();
	    				Object obj = Class.forName(clazz).newInstance();
	    				JsonNode vals = currElmt.get("vals");
	    				if (vals != null && vals.getNodeType() == JsonNodeType.ARRAY) {
	    					parseTemplate(vals, obj, itrVals);
	    				}
	    				System.out.println(obj.toString());
		        	} else { // primitive object (int, boolean, string, ...)
		        		System.out.println("setting val " + itrVals.next() + " to var " + currElmt.get("name"));
		        	}
		        }
	        }
		}
	}
	
	public static void parseTemplate(JsonNode curr, Object parent, Iterator<JsonNode> itrVals) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (curr.getNodeType() == JsonNodeType.ARRAY) {
			Iterator<JsonNode> currElmts = curr.iterator();
	        while (currElmts.hasNext()) {
	        	JsonNode currSub = currElmts.next();
	        	parseTemplate(currSub, parent, itrVals);
	        }
		} else if (curr.getNodeType() == JsonNodeType.OBJECT) {
			JsonNode prefix = curr.get("prefix");
			JsonNode suffix = curr.get("suffix");
			if (prefix != null && suffix != null) { // complex object
				String clazz = prefix.asText() + "." + suffix.asText();
				Object obj = Class.forName(clazz).newInstance();
				
				String method = curr.get("setter").asText();
				
			    Class<?>[] paramTypes = new Class<?>[1];
			    paramTypes[0] = Class.forName(clazz);
			    
			    Object[] args = new Object[1];
			    args[0] = obj;
			    
				parent.getClass().getMethod(method, paramTypes).invoke(parent, args);
				
				JsonNode vals = curr.get("vals");
				if (vals != null && vals.getNodeType() == JsonNodeType.ARRAY) {
					parseTemplate(vals, obj, itrVals);
				}
        	} else { // primitive object (int, boolean, string, ...)
				String method = curr.get("setter").asText();
				
			    Class<?>[] paramTypes = new Class<?>[1];
			    Object[] args = new Object[1];
			    
			    JsonNode val = itrVals.next();
			    
			    if (val.getNodeType() == JsonNodeType.STRING) {
				    paramTypes[0] = String.class;
				    args[0] = val.asText();
			    } else if (val.getNodeType() == JsonNodeType.NUMBER) {
				    paramTypes[0] = int.class;
				    args[0] = val.asInt();
			    }
			    
				parent.getClass().getMethod(method, paramTypes).invoke(parent, args);
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
