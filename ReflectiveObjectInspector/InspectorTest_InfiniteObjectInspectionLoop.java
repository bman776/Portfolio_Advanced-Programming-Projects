
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
public class InspectorTest_InfiniteObjectInspectionLoop {

    Inspector objectInspector;
    TestClassA baseObjectA;
    TestClassC baseObjectC;
    TestClassD baseObjectD;
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
            new TestClassB(0, (float)0, 'h')
        );

        baseObjectC = new TestClassC();
        baseObjectD = new TestClassD();

        baseObjectC.Dinstance = baseObjectD;
        baseObjectD.Cinstance = baseObjectC;


        objectInspector = new Inspector();
    }

    @Test 
    public void testA_infiniteObjectInspection() {
        Inspector.objectData objectData = null;
        objectData = objectInspector.inspectObject(null, baseObjectC, true);

        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST A: infinite object inpsection loop via recursive call between reciprocallly referencing classes", "see output text");

            try {
                FileWriter myWriter = new FileWriter("script2.txt", false);
                myWriter.write(objectData.getFormattedMetaData(""));
                myWriter.close();
            } catch (IOException ex) {

            } 
        }
    }

    @Ignore
    @Test
    public void testB_fullObjectInpsection_wRecursion() {
        Inspector.objectData objectData = null;
        objectData = objectInspector.inspectObject(null, baseObjectA, true);


        if (displayingInspectionInformation) {
            displayInspectionInformation("TEST H: single full basic object inspection with recursion enabled", "see output text");

            try {
                FileWriter myWriter = new FileWriter("script.txt", false);
                myWriter.write(objectData.getFormattedMetaData(""));
                myWriter.close();
            } catch (IOException ex) {

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
