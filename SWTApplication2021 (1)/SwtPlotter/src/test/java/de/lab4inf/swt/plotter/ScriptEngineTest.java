package de.lab4inf.swt.plotter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ScriptEngineTest {
	
	static JSEngine jsEngine = new JSEngine();
	private HashMap<String, PlotterFunction> functions = new HashMap<String, PlotterFunction>();
	
	@Test 
	public void testengineTest(){
		assertNotNull(jsEngine);
	}
	
	@Test
	public void functionWithSpaceAnfang() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("     f(x)=sin(x)", functions); 
		assertEquals(Math.sin(Math.PI/2), result.getValue().getFunction().apply(Math.PI/2));
	}
	
	@Test
	public void testfunctionWithSpaceEnde() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("f(x)=x*x   ", functions); 
		assertEquals(4.0, result.getValue().getFunction().apply(2.0));
	}
	
	@Test
	public void testwithoutMathDotSin() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("f(x)=sin(x)", functions); 
		assertEquals(Math.sin(Math.PI), result.getValue().getFunction().apply(Math.PI));
	}
	
	@Test
	public void testnegativeTest() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("f(x)= - x*x*x", functions); 
		assertEquals(-8, result.getValue().getFunction().apply(2.0));
	}
	@Test
	public void testUnsinn() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("unsinn", functions); 
		assertEquals("Dummy", result.getKey());
	}
	@Test
	public void testUnsinn1() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("f(x)=sin;", functions); 
		assertEquals("Dummy", result.getKey());
	}
	@Test
	public void testUnsinn2() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("f(x)=u", functions); 
		assertEquals("Dummy", result.getKey());
	}

	@Test
	public void testUnsinn3() {
		Map.Entry<String, PlotterFunction> result = jsEngine.parser("f(x)=?", functions); 
		assertEquals("Dummy", result.getKey());
	}

	
}
