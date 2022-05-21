package de.lab4inf.swt.plotter;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Testing {

	public static void main(String[] args) {
	JSEngine engine = new JSEngine();
	Entry<String, PlotterFunction> entry1 = engine.parser("f(x)=sin(x)", null);	
	Entry<String, PlotterFunction> entry2 = engine.parser("g(x)=sin(x)+f(x)", null);
	Entry<String, PlotterFunction> entry3 = engine.parser("h(x)=f(x)+g(x)", null);
	Entry<String, PlotterFunction> entry4 = engine.parser("j(x)=h(f(g(x)))", null);
	entry2.getValue().getFunction().apply(2.0);
	entry3.getValue().getFunction().apply(2.0);
	System.out.println(entry4.getValue().getFunction().apply(2.0));
	}	
}
		
