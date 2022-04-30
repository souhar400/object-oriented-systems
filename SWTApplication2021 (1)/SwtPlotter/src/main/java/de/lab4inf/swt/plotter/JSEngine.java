package de.lab4inf.swt.plotter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	}

	/**
	 * Parses String into Function
	 * 
	 * @throws ScriptException
	 * @param input Function declaration as a String
	 * @return Function<Double,Double>
	 */
	public Map.Entry<String, PlotterFunction> parser(String input, HashMap<String, PlotterFunction> functions) {
		Function<Double, Double> emptyDummy = x -> {
			return 0.0;
		};
		try {
			input.trim();
			String parts[] = input.split("=");
			parts[1] = replaceFunctions(parts[1], functions);
//			functions.forEach((key, value) -> {
//				if (parts[0].equals(key))
//					functions.remove(key);
//				if (parts[1].matches("(.*)[a-z]\\((.*)\\)(.*)")) {
//					// TODO : mehrfach verschachtelt
//					String functionReplacement = value.getName().split("=")[1];
//					parts[1] = parts[1].replace(key, functionReplacement);
//				}
//			});
			javaScriptEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
			try {
				for (String key : bindings.keySet()) {
					if (parts[1].contains(key)) {
						parts[1] = parts[1].replace(key, (String) bindings.get(key));
					}
				}
				bindings.put(parts[0], parts[1]);
				bindings.put("myfunc", parts[1]);
			} catch (Exception e) {
				e.printStackTrace();
				return Map.entry("", new PlotterFunction());
			}
			javaScriptEngine.eval("var regex = /(sin)|(cos)|(log)|(tan)|(sqrt)|(exp)/gi;"
					+ "var myfunc=myfunc.replace(regex, 'Math.$&');"
					+ "var func = new Function('x' ,'return ' +  myfunc);");

			Function<Double, Double> f = (Function<Double, Double>) javaScriptEngine
					.eval("new java.util.function.Function(func)");
			bindings.remove("myfunc");
			PlotterFunction fct = new PlotterFunction(f, input, null, 0);

			return Map.entry(parts[0], fct);
		} catch (Exception e) {
			e.printStackTrace();
			return Map.entry("", new PlotterFunction());
		}
	}

	private String replaceFunctions(String function, Map<String, PlotterFunction> fctMap) {
		String functionRest = function;
		int counter = 0;
		for (Character c : functionRest.toCharArray()) {
			if (Character.isAlphabetic(c) && c != 'x') {
				counter++;
			}
		}
		String[] fctKeys = new String[counter];
		Pattern findInner = Pattern.compile("(?<![a-z])[a-z]\\(x{0,1}y{0,1}\\)");
		int i = 0;
		while (functionRest.matches("(.*)(?<![a-z])[a-z]\\((.*)\\)(.*)")) {
			Matcher m = findInner.matcher(functionRest);
			if (m.find()) {
				fctKeys[i] = m.group();
				functionRest = functionRest.replace(fctKeys[i], "y");
				fctKeys[i] = fctKeys[i].replace("y", "x");
			}
			i++;
		}
		for (int j = 0; j < fctKeys.length-1; j++) {
			if (fctKeys[j] != null ) {
				String fct1 = fctMap.get(fctKeys[j]).getName().split("=")[1];
				String fct2 = new String();
				if(fctKeys[j+1] != null) fct2 = fctMap.get(fctKeys[j+1]).getName().split("=")[1];
				else fct2 = null;
				if(fct2 != null) {
					if(fct2.contains(fctKeys[j])) fct2 = fct2.replace(fctKeys[j], fct1);
					fct2 = fct2.replace("x", fct1);
					functionRest = functionRest.replace("y", fct2);
					function = functionRest;
				}
				else
					function = function.replace(fctKeys[j], fct1);
			}
		}
		return function;
	}

}