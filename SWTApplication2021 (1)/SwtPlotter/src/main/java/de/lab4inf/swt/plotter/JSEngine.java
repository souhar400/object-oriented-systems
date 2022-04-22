package de.lab4inf.swt.plotter;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.script.*;






/**
 * Parser Class
 * @author Lukas Wenning
 *
 */

public class JSEngine {
	private ScriptEngineManager manager;
	private ScriptEngine javaScriptEngine;
	Bindings bindings;
	/**
	 * Basic Constructor
	 */
	public JSEngine() {
		manager = new ScriptEngineManager();
		javaScriptEngine = manager.getEngineByName("nashorn");
		bindings = javaScriptEngine.createBindings();
	}

	/**
	 * Parses String into Function
	 * @throws ScriptException
	 * @param input Function declaration as a String
	 * @return Function<Double,Double>
	 */
	public Map.Entry<String, Function<Double, Double>> parser(String input){
		Function<Double, Double> emptyDummy = x -> {
			return 0.0;};
		try {
			input.trim();
			String parts[] = input.split("=");
			javaScriptEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
			try {
	    	for(String key : bindings.keySet()) {
	    		if(parts[1].contains(key)) {
	    			parts[1] = parts[1].replace(key, (String) bindings.get(key));
	    		}
	    	}
	    	bindings.put(parts[0], parts[1]);
	    	bindings.put("myfunc", parts[1]);
			}
			catch(Exception e) {
				e.printStackTrace();
				return Map.entry("", emptyDummy);
			}
			javaScriptEngine.eval("var regex = /(sin)|(cos)|(tan)|(sqrt)|(exp)/gi;"
					+"var myfunc=myfunc.replace(regex, 'Math.$&');"
					+"var func = new Function('x' ,'return ' +  myfunc);");
			Function<Double,Double> f = (Function<Double,Double>)javaScriptEngine.eval("new java.util.function.Function(func)");
			bindings.remove("myfunc");

			Map<String, Function<Double, Double>> retVal = new HashMap<>();
			retVal.put(input, f);
			
			return Map.entry(input, f);
		}
		catch(Exception e){
			e.printStackTrace();
			return Map.entry("", emptyDummy);
			}
		}
	
}