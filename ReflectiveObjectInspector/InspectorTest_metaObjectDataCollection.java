
/*
 * CPSC501 Assignment 2 Submission
 * Brett Gattinger
 * 30009390
 */


import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InspectorTest_metaObjectDataCollection {

    Inspector objectInspector;
    TestClassA baseObjectA;
    private static boolean displayingInspectionInformation = true;

    @Before
    public void initialize() {
        baseObjectA = new TestClassA();
        objectInspector = new Inspector();
    }

    @Test
    public void testA_inspectClass() {
        Class output = objectInspector.getObjectClass(baseObjectA);
        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST A: inspect class", output.toString());
        }
        assertEquals("class TestClassA", output.toString());
    }

    @Test
    public void testB_inspectSuperClass() {
        Class output = objectInspector.getObjectSuperClass(baseObjectA.getClass());
        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST B: inspect superclass", output.toString());
        }
        assertEquals("class TestClassSuperClass", output.toString());
    }

    @Test
    public void testC_inspectInterfaces() {
        Class[] output = objectInspector.getObjectInterfaces(baseObjectA.getClass());
        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST C: inspect interfaces", output.toString());
        }
    }

    @Test
    public void testD_inspectConstructors() {
        Constructor[] output = objectInspector.getObjectConstructors(baseObjectA.getClass());
        String output_info = "";
        for (Constructor c : output) {
            output_info += c.toString() + "\n";
        }
        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST D: inspect constructors", output_info);
        }
    }

    @Test
    public void testE_inspectFields() {
        Field[] output = objectInspector.getObjectFields(baseObjectA.getClass());
        String output_info = "";
        for (Field f : output) {
            output_info += f.toString() + "\n";
        }
        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST E: inspect fields", output_info);
        }
    }

    @Test
    public void testF_inspectMethods() {
        Method[] output = objectInspector.getObjectMethods(baseObjectA.getClass());
        String output_info = "";
        for (Method m : output) {
            output_info += m.toString() + "\n";
        }
        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST F: inspect methods", output_info);
        }
    }
    
    private void displayInspectionInformation(String label, String info) {
        System.out.println("\n===" + label + "===");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(info);
        System.out.println("--------------------------------------------------------------------\n");
    }
}
