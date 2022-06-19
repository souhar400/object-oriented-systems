package de.lab4inf.swt.plotter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NewEngineTest {

	static NewParser jsEngine = new NewParser();
	private HashMap<String, PlotterFunction> functions;

	@BeforeEach
	void setUp() throws Exception {
		functions = new HashMap<String, PlotterFunction>();
	}

	@Test
	public void testengineTest() {
		assertNotNull(jsEngine);
	}

	@Test
	public void functionWithSpaceAnfang() {
		HashMap<String, PlotterFunction> map = jsEngine.parser("     f(x)=sin(x)");
		assertEquals(map.get("f(x)").getName(), "f(x)=sin(x)");
		assertEquals(Math.sin(Math.PI / 2), map.get("f(x)").getFunction().apply(Math.PI / 2));
	}

	@Test
	public void testfunctionWithSpaceEnde() {
		HashMap<String, PlotterFunction> map = jsEngine.parser("g(x)=x*x   ");
		assertEquals(map.get("g(x)").getName(), "g(x)=x*x");
		assertEquals(4.0, map.get("g(x)").getFunction().apply(2.0));
	}

	@Test
	public void testwithoutMathDotSin() {
		HashMap<String, PlotterFunction> map = jsEngine.parser("h(x)=sin(x)");
		assertEquals(map.get("h(x)").getName(), "h(x)=sin(x)");
		assertEquals(Math.sin(Math.PI / 2), map.get("h(x)").getFunction().apply(Math.PI / 2));
	}

	@Test
	public void testnegativeTest() {
		HashMap<String, PlotterFunction> map = jsEngine.parser("n(x)= - x*x*x");
		assertEquals(map.get("n(x)").getName(), "n(x)=-x*x*x");
		assertEquals(-8, map.get("n(x)").getFunction().apply(2.0));
	}

	@Test
	public void testUnsinn() {
		HashMap<String, PlotterFunction> map = jsEngine.parser("unsinn"); 
		assertTrue(map.containsKey("Dummy")); 
	}


	@Test
	public void testUnsinn1() {
		HashMap<String, PlotterFunction> map = jsEngine.parser("f(x)=?;"); 
		assertTrue(map.containsKey("Dummy")); 
	}
	
	@Test 
	public void testLinearKombination() {
		HashMap<String, PlotterFunction> map = jsEngine.parser(" a=0.25; b=(1-a); c=0.1; s(x)=sin(x); h(x)=1/x; f(x)=(a*s(x) + b*h(x))*exp(-c*x);");
		assertEquals(map.get("s(x)").getName(), "s(x)=sin(x)");
		assertEquals(map.get("h(x)").getName(), "h(x)=1/x");
		assertEquals(map.get("f(x)").getName(), "f(x)=(a*s(x)+b*h(x))*exp(-c*x)");
		Function<Double, Double> fct = map.get("f(x)").getFunction(); 
		assertEquals(0.4931, fct.apply(2.0), 0.0001);
		assertEquals(-0.7356, fct.apply(-2.0), 0.0001);
	}

	
	@Test 
	public void testLinearKombination2() {
		HashMap<String, PlotterFunction> map = jsEngine.parser(" s(x)=sin(x);  a=0.5; b=0.1; h(x)=1/x; f(x)=a*x*s(h(x)) +b; ");
		assertEquals(map.get("s(x)").getName(), "s(x)=sin(x)");
		assertEquals(map.get("h(x)").getName(), "h(x)=1/x");
		assertEquals(map.get("f(x)").getName(), "f(x)=a*x*s(h(x))+b");
		Function<Double, Double> fct = map.get("f(x)").getFunction(); 
		assertEquals(0.5794, fct.apply(2.0), 0.0001);
		assertEquals(0.5991, fct.apply(-10.0), 0.0001);
	}
}
