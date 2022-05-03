package de.lab4inf.swt.plotter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
	private Random rdm = new Random(); 

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
	@SuppressWarnings("unchecked")
	public Map.Entry<String, PlotterFunction> parser(String input, HashMap<String, PlotterFunction> functions) {
		Function<Double, Double> emptyDummy = x -> {
			return 0.0;
		};
		try {
			input = input.replaceAll("\\s+","");
			//input.trim();
			String parts[] = input.split("=");
			//parts[1] = replaceFunctions(parts[1], functions);
			Pattern findInner = Pattern.compile("(?<!^)(?<![a-z\\+\\*\\/\\-\\=])[a-z]\\(x{0,1}y{0,1}\\)");
			String helper = parts[1];
			String fctKey;
			List<String> fctReihenfolge = new ArrayList();
				Matcher innerFunction = findInner.matcher(parts[1]);
				while(innerFunction.find()) {
					fctKey = innerFunction.group();
					parts[1] = parts[1].replace(fctKey, "x");
					fctReihenfolge.add(fctKey);
					innerFunction = findInner.matcher(parts[1]);
				}
			if(parts[1].matches("(.*)[^x](.*)")){
				parts[1] = replaceFunctions(parts[1], functions);
			}


			javaScriptEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

			bindings.put(parts[0], parts[1]);
			bindings.put("myfunc", "(" + parts[1]+ ")");

			javaScriptEngine.eval("var regex = /(sin)|(cos)|(log)|(tan)|(sqrt)|(exp)/gi;"
					+ "var myfunc=myfunc.replace(regex, 'Math.$&');"
					+ "var func = new Function('x' ,'return ' +  myfunc);");

			Function<Double, Double> f = (Function<Double, Double>) javaScriptEngine
					.eval("new java.util.function.Function(func)");
			Function<Double, Double> rv = x ->{ return x;};
			for(String element : fctReihenfolge) {
				PlotterFunction pf = functions.get(element);
				rv = rv.andThen(pf.getFunction());
			}
			f = rv.andThen(f);
			bindings.remove("myfunc");
			
			PlotterFunction fct = new PlotterFunction(f, input, null, 0);
			fct.setReplacedVal(parts[1]);
			return Map.entry(parts[0], fct);
		} catch (Exception e) {
			e.printStackTrace();
			return Map.entry("Dummy", new PlotterFunction(emptyDummy, " ", null, 0));
		}
	}

	private String replaceFunctions(String function, Map<String, PlotterFunction> fctMap) {
		String functionRest = function;
		String fctKey;

		Pattern findInner = Pattern.compile("(?<![a-z])[a-z]\\(x{0,1}y{0,1}\\)");
		Pattern findOuter = Pattern.compile("(?<![a-z])[a-z]\\([^)]*\\)");
		Pattern xVal = Pattern.compile("\\((.*)\\)");
		while (functionRest.matches("(.*)(?<![a-z])[a-z]\\((.*)\\)(.*)")) {
			Matcher m = findInner.matcher(functionRest);
			Matcher outer = findOuter.matcher(functionRest);
			if (m.find()) {
				fctKey = m.group();

				functionRest = functionRest.replace(fctKey, fctMap.get(fctKey).getReplacedVal());
			} else {
				if (outer.find()) {
					fctKey = outer.group();
					Matcher fctSplitter = xVal.matcher(fctKey);
					if (fctSplitter.find()) {
						fctKey = fctKey.replace(fctSplitter.group(), "(x)");
						String fct = fctMap.get(fctKey).getReplacedVal();
						fct = fct.replace("x", fctSplitter.group());
						functionRest = functionRest.replace(outer.group(), fct);
					}
				}
			}
		}
		return functionRest;
	}

}
