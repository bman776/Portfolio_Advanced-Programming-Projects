
/*
 * CPSC501 Assignment 2 Submission
 * Brett Gattinger
 * 30009390
 */


import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InspectorTest_basicObjectInspection {

    Inspector objectInspector;
    TestClassA baseObjectA;
    ClassB baseObjectB;
    private static boolean displayingInspectionInformation = true;

    @Before
    public void initialize() {
        baseObjectA = new TestClassA(
            (byte)100,
            (short)100, 
            3, 
            (long)4000, 
            (float)404.4, 
            6056.78, 
            true, 
            'a', 
            new int[] {1,2,3,4},
            new int[][] {{1,2,3},{4,5,6}},
            new TestClassB[] {new TestClassB(0, 0, 'a'), new TestClassB(1, 1, 'b')},
            new TestClassB(0, (float)0, 'h'));

        try {
            baseObjectB = new ClassB();
        } catch(Exception e) {
            e.printStackTrace();
        }
        objectInspector = new Inspector();
    }

    @Ignore
    @Test
    public void testA_fullMethodInspection() {
        Inspector.methodMetaObjectData methodData = null;
        try {
            methodData = objectInspector.inspectObjectMethod(baseObjectA.getClass().getDeclaredMethod("method_A1", new Class[]{int.class}));
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } 
        
        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST A: single full method inspection", methodData.toString());
        }
    }

    @Ignore
    @Test 
    public void testB_fullPrimitiveFieldInspection() {
        Inspector.fieldMetaObjectData fieldData = null;
        try {
            fieldData = objectInspector.inspectObjectField(baseObjectA.getClass(), baseObjectA , baseObjectA.getClass().getDeclaredField("attribute_A1"), false);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }

        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST B: single full primitive field inspection", fieldData.toString());
        }
    }

    @Ignore
    @Test
    public void testC_fullConstructorInspection() {
        Inspector.constructorMetaObjectData constructorData = null;
        Class[] parameterSignature = null;
        try {
            constructorData = objectInspector.inspectObjectConstructor(baseObjectA.getClass(), baseObjectA.getClass().getDeclaredConstructor(parameterSignature));
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST C: single full constructor inspection", constructorData.toString());
        }
    }

    @Ignore
    @Test 
    public void testD_fullPrimitiveArrayInspection() {
        Inspector.arrayMetaObjectData arrayData = null;
        try {
            arrayData = objectInspector.inspectArray(
                baseObjectA, 
                baseObjectA.getClass().getDeclaredField("attribute_A9").get(baseObjectA), 
                baseObjectA.getClass().getDeclaredField("attribute_A9").getType(),
                false
            );
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }

        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST D: single full primitive array inspection", arrayData.toString());
        }
        
    }

    @Ignore
    @Test
    public void testE_fullPrimitiveArrayInspection2() {
        Inspector.fieldMetaObjectData fieldData = null;
        try {
            fieldData = objectInspector.inspectObjectField(baseObjectA.getClass(), baseObjectA, baseObjectA.getClass().getDeclaredField("attribute_A9"), false);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }

        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST E: single full primitive field inspection", fieldData.toString());
        }
    }

    @Ignore
    @Test
    public void testF_fullObjectInspection_nonRecursive() {
        Inspector.objectData objectData = null;
        objectData = objectInspector.inspectObject(null, baseObjectA, false);


        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST F: single full basic object inspection", objectData.toString());
        }
    }

    @Ignore
    @Test
    public void testG_fullObjectInspection_wFormattedOutStrings() {
        Inspector.objectData objectData = null;
        objectData = objectInspector.inspectObject(null, baseObjectA, false);


        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST G: single full basic object inspection with new formatted output strings", objectData.getFormattedMetaData(""));
        }
    }

    @Test
    public void testH_fullObjectInpsection_wRecursion() {
        Inspector.objectData objectData = null;
        objectData = objectInspector.inspectObject(null, baseObjectA, true);


        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST H: single full basic object inspection with recursion enabled", "see output text");

            try {
                FileWriter myWriter = new FileWriter("script.txt", false);
                myWriter.write(objectData.getFormattedMetaData(""));
                myWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        }
    }

    @Ignore
    @Test 
    public void testI_testingTAsClassBObject() {
        Inspector.objectData objectData = null;
        objectData = objectInspector.inspectObject(null, baseObjectB, true);


        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST H: single full basic object inspection with recursion enabled", "see output text");

            try {
                FileWriter myWriter = new FileWriter("script.txt", false);
                myWriter.write(objectData.getFormattedMetaData(""));
                myWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        }
    }
    
    private void displayInspectionInformation(String label, String info) {
        System.out.println("\n===" + label + "===");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(info);
        System.out.println("--------------------------------------------------------------------\n");
    }



}
