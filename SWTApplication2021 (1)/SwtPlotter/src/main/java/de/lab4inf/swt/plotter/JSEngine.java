package de.lab4inf.swt.plotter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.script.*;

/**
 * Parser Class
 * 
 * @author Lukas Wenning
 *
 */

public class JSEngine {
	private ScriptEngineManager manager;
	private ScriptEngine javaScriptEngine;
	private Bindings bindings;

	/**
	 * Basic Constructor
	 */
	public JSEngine() {
		manager = new ScriptEngineManager();
		javaScriptEngine = manager.getEngineByName("nashorn");
		bindings = javaScriptEngine.createBindings();
		javaScriptEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
	}

	/**
	 * Parses String into Function
	 * 
	 * @throws ScriptException
	 * @param input Function declaration as a String
	 * @return Function<Double,Double>
	 */
	@SuppressWarnings("unchecked")
	public Map.Entry<String, PlotterFunction> parser(String input, HashMap<String, PlotterFunction> functions) {
		try {
			input = input.replaceAll("\\s+","");
			String functionHeader = ((Character)input.charAt(0)).toString();			
			if(!input.contains("=")) throw new IllegalArgumentException("Keine gültige Funktion");
			String functionBody = input.split("=")[1];
			String functionKey = input.split("=")[0];
			String script =
					"var "+ functionHeader+ " = new Function('x' ,'return ' +  " + functionHeader + ");"
							+"new java.util.function.Function("+ functionHeader+")";
			
			
			for (Method m : Math.class.getDeclaredMethods()) {
				javaScriptEngine.eval("function " + m.getName() + "(x){ return Math." + m.getName() + "(x);}");
				//functionBody = functionBody.replace(m.getName(), String.format("Math.%s", m.getName()));
			}
			
			bindings.put(functionHeader, functionBody);
			Function<Double, Double> f = (Function<Double, Double>) javaScriptEngine
					.eval(script);

			PlotterFunction fct = new PlotterFunction(f, input, null, 0);
			Double test = f.apply(1.0);
			return Map.entry(functionKey, fct);
		} catch (Exception e) {
			//e.printStackTrace();
			return Map.entry("Dummy", new PlotterFunction(null, null, null, 0));
		}
	}

}
