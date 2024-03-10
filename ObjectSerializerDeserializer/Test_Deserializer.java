import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test_Deserializer {
    Deserializer deserializer;

    @Before
    public void initialize() {
        //create deserializer object
        deserializer = new Deserializer();
    }

    //PASSED
    //@Ignore
    @Test
    public void testA_primtiveObjectDeserialization() {

        // build expected object
        byte byteVal = 1;
        short shortVal = 2;
        int intVal = 200;
        long longVal = 1000000;
        float floatVal = 1.15f;
        double doubeVal = 2.39999d;
        boolean boolVal = true;
        char charVal = 'a';
        PrimitiveClass expectedObject = new PrimitiveClass(byteVal, shortVal, intVal, longVal, floatVal, doubeVal, boolVal, charVal);

        // build actual object
        Document inputJdomDoc = buildJdomDoc_primitiveClass();
        Object actualObject = deserializer.deserialize(inputJdomDoc);
    
        //display result
        System.out.println("EXPECTED:");
        System.out.println("------------------------------");
        System.out.println(expectedObject.toString());
        System.out.println();
        System.out.println("ACTUAL:");
        System.out.println("------------------------------");
        System.out.println(actualObject.toString());

        //test
        assertTrue(expectedObject.equals(actualObject));
    }

    //PASSED
    //@Ignore
    @Test
    public void testB_compositeObjectDeserialization() {
        //build expected object
        CompositeClass innerObject = new CompositeClass(1, 2.0f, 'a');
        CompositeClass expectedObject = new CompositeClass(2, 3.0f, 'b', innerObject);

        //build actual object
        Document inputJdomDoc = buildJdomDoc_CompositeClass();
        Object actualObject = deserializer.deserialize(inputJdomDoc);
    
        //display result
        System.out.println("EXPECTED:");
        System.out.println("------------------------------");
        System.out.println(expectedObject.toString());
        System.out.println();
        System.out.println("ACTUAL:");
        System.out.println("------------------------------");
        System.out.println(actualObject.toString());

        //test
        assertTrue(expectedObject.equals(actualObject));
    }

    //PASSED
    //@Ignore
    @Test
    public void testC_primitveArrayObjectDeserialization() {
        //build expected object
        PrimitiveArrayClass expectedObject = new PrimitiveArrayClass(new int[]{1,2,3,4,5,6,7,8,9,10});

        //build actual object
        Document inputJdomDoc = buildJdomDoc_primitveArrayClass();
        Object actualObject = deserializer.deserialize(inputJdomDoc);

        
        //display result
        System.out.println("EXPECTED:");
        System.out.println("------------------------------");
        System.out.println(expectedObject.toString());
        System.out.println();
        System.out.println("ACTUAL:");
        System.out.println("------------------------------");
        System.out.println(actualObject.toString());

        //test
        assertTrue(expectedObject.equals(actualObject));
    }

    //PASSED
    //@Ignore
    @Test
    public void testD_compositeArrayObjectDeserialization() {
        //build expected object
        CompositeClass innerObject1 = new CompositeClass(1, 2, 'a');
        CompositeClass innerObject2 = new CompositeClass(3, 4, 'b');
        CompositeClass innerObject3 = new CompositeClass(5, 6, 'c', innerObject1);
        CompositeArrayClass expectedObject = new CompositeArrayClass(new CompositeClass[]{innerObject1, innerObject2, innerObject3});

        //build actual object
        Document inputJdomDoc = buildJdomDoc_compositeArrayClass();
        Object actualObject = deserializer.deserialize(inputJdomDoc);
        
        //display result
        System.out.println("EXPECTED:");
        System.out.println("------------------------------");
        System.out.println(expectedObject.toString());
        System.out.println();
        System.out.println("ACTUAL:");
        System.out.println("------------------------------");
        System.out.println(actualObject.toString());

        //test
        assertTrue(expectedObject.equals(actualObject));
    }

    public Document buildJdomDoc_primitiveClass() {
        Document OutDoc = new Document(new Element("serialized"));
        Element objectElement = new Element("Object");
        objectElement.setAttribute("class", "PrimitiveClass");
        objectElement.setAttribute("id", "0");
        
        Element fieldElement;
        Element valueElement;
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a1");
        fieldElement.setAttribute("declaringclass", "PrimitiveClass");
        valueElement = new Element("value");
        valueElement.setText("1");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);

        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a2");
        fieldElement.setAttribute("declaringclass", "PrimitiveClass");
        valueElement = new Element("value");
        valueElement.setText("2");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);

        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a3");
        fieldElement.setAttribute("declaringclass", "PrimitiveClass");
        valueElement = new Element("value");
        valueElement.setText("200");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);

        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a4");
        fieldElement.setAttribute("declaringclass", "PrimitiveClass");
        valueElement = new Element("value");
        valueElement.setText("1000000");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);

        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a5");
        fieldElement.setAttribute("declaringclass", "PrimitiveClass");
        valueElement = new Element("value");
        valueElement.setText("1.15");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);

        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a6");
        fieldElement.setAttribute("declaringclass", "PrimitiveClass");
        valueElement = new Element("value");
        valueElement.setText("2.39999");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a7");
        fieldElement.setAttribute("declaringclass", "PrimitiveClass");
        valueElement = new Element("value");
        valueElement.setText("true");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);

        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a8");
        fieldElement.setAttribute("declaringclass", "PrimitiveClass");
        valueElement = new Element("value");
        valueElement.setText("a");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);

        OutDoc.getRootElement().addContent(objectElement);
        return OutDoc;
    }

    public Document buildJdomDoc_CompositeClass () {
        Document OutDoc = new Document(new Element("serialized"));
        Element objectElement;
        Element fieldElement;
        Element valueElement;
        Element referenceElement;
        
        objectElement = new Element("Object");
        objectElement.setAttribute("class", "CompositeClass");
        objectElement.setAttribute("id", "0");
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("2");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a2");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("3.0");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a3");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("b");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "object_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        referenceElement = new Element("reference");
        referenceElement.setText("1");
        fieldElement.addContent(referenceElement);
        objectElement.addContent(fieldElement);
        OutDoc.getRootElement().addContent(objectElement);

        objectElement = new Element("Object");
        objectElement.setAttribute("class", "CompositeClass");
        objectElement.setAttribute("id", "1");
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("1");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a2");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("2.0");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a3");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("a");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "object_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        referenceElement = new Element("null");
        fieldElement.addContent(referenceElement);
        objectElement.addContent(fieldElement);
        OutDoc.getRootElement().addContent(objectElement);

        return OutDoc;
    }

    public Document buildJdomDoc_primitveArrayClass () {
        Document OutDoc = new Document(new Element("serialized"));
        Element objectElement;
        Element fieldElement;
        Element valueElement;
        Element referenceElement;

        objectElement = new Element("Object");
        objectElement.setAttribute("class", "PrimitiveArrayClass");
        objectElement.setAttribute("id", "0");
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a1");
        fieldElement.setAttribute("declaringclass", "PrimitiveArrayClass");
        referenceElement = new Element("reference");
        referenceElement.setText("1");
        fieldElement.addContent(referenceElement);
        objectElement.addContent(fieldElement);
        OutDoc.getRootElement().addContent(objectElement);

        objectElement = new Element("Object");
        objectElement.setAttribute("class", "[I");
        objectElement.setAttribute("id", "1");
        objectElement.setAttribute("length", "10");
        valueElement = new Element("value");
        valueElement.setText("1");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("2");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("3");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("4");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("5");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("6");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("7");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("8");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("9");
        objectElement.addContent(valueElement);
        valueElement = new Element("value");
        valueElement.setText("10");
        objectElement.addContent(valueElement);
        OutDoc.getRootElement().addContent(objectElement);

        return OutDoc;
    }

    public Document buildJdomDoc_compositeArrayClass () {
        Document OutDoc = new Document(new Element("serialized"));
        Element objectElement;
        Element fieldElement;
        Element valueElement;
        Element referenceElement;

        objectElement = new Element("Object");
        objectElement.setAttribute("class", "CompositeArrayClass");
        objectElement.setAttribute("id", "0");
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a1");
        fieldElement.setAttribute("declaringclass", "CompositeArrayClass");
        referenceElement = new Element("reference");
        referenceElement.setText("1");
        fieldElement.addContent(referenceElement);
        objectElement.addContent(fieldElement);
        OutDoc.getRootElement().addContent(objectElement);

        objectElement = new Element("Object");
        objectElement.setAttribute("class", "[LCompositeClass;");
        objectElement.setAttribute("id", "1");
        objectElement.setAttribute("length", "3");
        referenceElement = new Element("reference");
        referenceElement.setText("2");
        objectElement.addContent(referenceElement);
        referenceElement = new Element("reference");
        referenceElement.setText("3");
        objectElement.addContent(referenceElement);
        referenceElement = new Element("reference");
        referenceElement.setText("4");
        objectElement.addContent(referenceElement);
        OutDoc.getRootElement().addContent(objectElement);

        objectElement = new Element("object");
        objectElement.setAttribute("class", "CompositeClass");
        objectElement.setAttribute("id", "2");
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("1");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a2");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("2.0");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a3");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("a");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "object_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("null");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        OutDoc.getRootElement().addContent(objectElement);

        objectElement = new Element("object");
        objectElement.setAttribute("class", "CompositeClass");
        objectElement.setAttribute("id", "3");
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("3");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a2");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("4.0");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a3");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("b");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "object_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("null");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        OutDoc.getRootElement().addContent(objectElement);

        objectElement = new Element("object");
        objectElement.setAttribute("class", "CompositeClass");
        objectElement.setAttribute("id", "4");
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("5");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a2");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("6.0");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "attribute_a3");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        valueElement = new Element("value");
        valueElement.setText("c");
        fieldElement.addContent(valueElement);
        objectElement.addContent(fieldElement);
        fieldElement = new Element("field");
        fieldElement.setAttribute("name", "object_a1");
        fieldElement.setAttribute("declaringclass", "CompositeClass");
        referenceElement = new Element("reference");
        referenceElement.setText("2");
        fieldElement.addContent(referenceElement);
        objectElement.addContent(fieldElement);
        OutDoc.getRootElement().addContent(objectElement);

        return OutDoc;
    }
}
