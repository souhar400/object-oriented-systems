package de.lab4inf.swt.plotter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Function;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class NewParser {
	private ScriptEngineManager manager;
	private ScriptEngine javaScriptEngine;
	private Bindings bindings;

	/**
	 * Basic Constructor
	 */
	public NewParser() {
		manager = new ScriptEngineManager();
		javaScriptEngine = manager.getEngineByName("nashorn");
		bindings = javaScriptEngine.createBindings();
		javaScriptEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
	}

	public HashMap<String, PlotterFunction> parser(String input) {
		HashMap mapi = new HashMap<>();
//		String myScript = " a=0.25; b=(1-a); c=0.1; s(x)=sin(x); h(x)=1/x; f(x)=(a*s(x) + b*h(x))*exp(-c*x); ";
//		myScript ="f(x)=cos(x)"; 
		input = input.replaceAll("\\s+", "");
		HashMap<String, PlotterFunction> myMap = new HashMap<String, PlotterFunction>();
		try {
			for (Method m : Math.class.getDeclaredMethods()) {
				javaScriptEngine.eval("function " + m.getName() + "(x){ return Math." + m.getName() + "(x);}");
			}
			for (String part : input.split(";")) {
				if(!part.isEmpty()) {
					StringBuilder validScript = new StringBuilder();
					if (!part.contains("x")) {
						validScript.append(part);
						validScript.append(";");
						javaScriptEngine.eval(validScript.toString());
					} else {
						String functionHeader = ((Character) part.charAt(0)).toString();
						String functionBody = part.split("=")[1];
						String functionString = "var " + functionHeader + "= new Function('x', 'return " + functionBody
								+ "');";
						validScript.append(functionString);
						validScript.append(";");
						validScript.append("new java.util.function.Function(" + functionHeader + ")");
						Function<Double, Double> myFunction = (Function<Double, Double>) javaScriptEngine.eval(validScript.toString());
						PlotterFunction fct = new PlotterFunction(myFunction, part, null, 0);
						myMap.put(functionHeader+"(x)", fct);
					}
				}
			}
			
			return myMap; 
			

		} catch (Exception e) {
			myMap.put("Dummy", null);
			return myMap;
		}
	}
}
