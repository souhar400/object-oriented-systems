package de.lab4inf.swt.plotter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ScriptEngineTest {
	
	static JSEngine jsEngine = new JSEngine();
	private HashMap<String, PlotterFunction> functions = new HashMap<String, PlotterFunction>();
	
	@Test 
	void engineTest(){
		assertNotNull(jsEngine);
	}
	
	@Test
	void functionWithSpaceAnfang() {
	
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("     f(x)=x*x", functions); 
		assertEquals("f(x)=x*x", result.getValue().getName());
	}
	
	@Test
	void sinTest() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("f(x)=sin(x)", functions); 
		assertEquals(Math.sin(Math.PI), result.getValue().getFunction().apply(Math.PI));
	}
	
	@Test
	void negativeTest() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("f(x)=-x*x*x", functions); 
		assertEquals(-8, result.getValue().getFunction().apply(2.0));
	}
	
	
	
	
	
}
