
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.HashMap;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;

public class Deserializer {
    Deserializer() {/*Defualt Constructor*/}

    public Object deserialize(Document inputDocument) {
        Object result = deserializeObject(inputDocument);
        return result;
    }

    private Object deserializeObject(Document inputDocument) {
        //get object list
        List<Element> serializedObjects = inputDocument.getRootElement().getChildren();

        //create hash map to keep track of already deserialized objects
        HashMap<Integer, Object> deserializedObjects = new HashMap<Integer, Object>();

        //***************
        //***REFACTOR3***
        //***************
        createObjectInstance(deserializedObjects, serializedObjects);

        //***************
        //***REFACTOR4***
        //***************
        restoreFieldValues(deserializedObjects, serializedObjects);
        
        return deserializedObjects.get(0);
    }
    
    private void createObjectInstance(HashMap<Integer, Object> deserializedObjects, List<Element> serializedObjects) {
        for (int i=0; i < serializedObjects.size(); i++) {
            Element objectElement = (Element) serializedObjects.get(i);

            //get class from current serialized object
            Class objectClass = null;
            try {
                objectClass = Class.forName(objectElement.getAttributeValue("class"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            if (objectClass.isArray()) {
                //create blank array instance
                Class arrayType = objectClass.getComponentType();
                int arrayLength = Integer.parseInt(objectElement.getAttributeValue("length"));
                try {
                    deserializedObjects.put(objectElement.getAttribute("id").getIntValue(),Array.newInstance(arrayType, arrayLength));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            } else {
                //create blank object instance
                
                //get uninitializing constructor for serialized object
                Constructor objectConstructor = null;
                try {
                    objectConstructor = objectClass.getDeclaredConstructor(null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
    
                //make sure this constructor is accessible
                if (!Modifier.isPublic(objectConstructor.getModifiers())) {
                    objectConstructor.setAccessible(true);
                }

                //create unitialized instance and store/track using hashmap
                try {
                    deserializedObjects.put(objectElement.getAttribute("id").getIntValue(), objectConstructor.newInstance(null));
                } catch (Exception e) {
                    //IllegalAccess or IllegalArgument or Instantiation or InvocationTarget or ExceptionInInitializer or DataConversion Exceptions thrown
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    private void restoreFieldValues(HashMap<Integer, Object> deserializedObjects, List<Element> serializedObjects) {
        for (int i=0; i < serializedObjects.size(); i++) {

            //ASSERTION: we have a collection of unitialized objects in deserializedObjects
            // so now we go through each element object and and use its id to get the reference to the 
            // actual object that we've initialized at runtime on this end of socket

            //we iterate through each object element again and use its ID to retrieve the actual unititalized object
            //so that we may restore its field values
            Element objectElement = (Element)serializedObjects.get(i);

            //get field elements for corresponding object element
            List<Element> serializedFields = objectElement.getChildren();

            Object objectInstance = null;
            try {
                objectInstance = deserializedObjects.get(objectElement.getAttribute("id").getIntValue());
            } catch (DataConversionException e) {
                e.printStackTrace();
                System.exit(-1);
            } 

            if (objectInstance.getClass().isArray()) {
                //restore array

                Class arrayType = objectInstance.getClass().getComponentType();

                for (int j=0; j < serializedFields.size(); j++) {
                    Element valueElement = serializedFields.get(j);
                    if (valueElement.getName().equals("value")) {
                        //restore value
                        if (arrayType.equals(Byte.TYPE)) {
                            Array.set(objectInstance, j, Byte.valueOf(valueElement.getText()));
                        } else if (arrayType.equals(Short.TYPE)) {
                            Array.set(objectInstance, j, Short.valueOf(valueElement.getText()));
                        } else if (arrayType.equals(Integer.TYPE)) {
                            Array.set(objectInstance, j, Integer.valueOf(valueElement.getText()));
                        } else if (arrayType.equals(Long.TYPE)) {
                            Array.set(objectInstance, j, Long.valueOf(valueElement.getText()));
                        } else if (arrayType.equals(Float.TYPE)) {
                            Array.set(objectInstance, j, Float.valueOf(valueElement.getText()));
                        } else if (arrayType.equals(Double.TYPE)) {
                            Array.set(objectInstance, j, Double.valueOf(valueElement.getText()));
                        } else if (arrayType.equals(Boolean.TYPE)) {
                            Array.set(objectInstance, j, Boolean.valueOf(valueElement.getText()));
                        } else if (arrayType.equals(Character.TYPE)) {
                            Array.set(objectInstance, j, valueElement.getText().charAt(0));
                        } else {
                            Array.set(objectInstance, j, valueElement.getText());
                        }
                    } else if (valueElement.getName().equals("reference")) {
                        //restore reference
                        int temp = Integer.valueOf(valueElement.getText());
                        Array.set(objectInstance, j, deserializedObjects.get(Integer.valueOf(valueElement.getText())));
                    } else {
                        //restore null
                        Array.set(objectInstance, j, null);
                    }
                }
            } else {
                //restore object
                
                for (int j=0; j < serializedFields.size(); j++) {
                    //restore field value
                    Element fieldElement = serializedFields.get(j);

                    // get field's declaring class (this is differnt from the type of the field)
                    Class fieldDeclaringClass = null;
                    try {
                        fieldDeclaringClass = Class.forName(fieldElement.getAttributeValue("declaringclass"));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }

                    // get field meta object
                    Field field = null;
                    try {
                        field = fieldDeclaringClass.getDeclaredField(fieldElement.getAttributeValue("name"));
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }

                    //make sure field meta object is accessible
                    boolean fieldInaccessible = false;
                    try {
                        if (!Modifier.isPublic(field.getModifiers())) {
                            field.setAccessible(true);
                        }
                    } catch (InaccessibleObjectException e) {
                        fieldInaccessible = true;
                    }

                    //get value element within field element
                    Element valueElement = (Element) fieldElement.getChildren().get(0);

                    //restore either reference or value
                    //REFACTOR OPPORTUNITY: extract method to reduce size of long method
                    try {
                        if (fieldInaccessible) {
                            //move on to next object we cant restore innaccessible JDK internal
                            continue;
                        } else if (valueElement.getName().equals("value")) {
                            //restore value
                            if (field.getType().equals(Byte.TYPE)) {
                                field.set(objectInstance, Byte.valueOf(valueElement.getText()));
                            } else if (field.getType().equals(Short.TYPE)) {
                                field.set(objectInstance, Short.valueOf(valueElement.getText()));
                            } else if (field.getType().equals(Integer.TYPE)) {
                                field.set(objectInstance, Integer.valueOf(valueElement.getText()));
                            } else if (field.getType().equals(Long.TYPE)) {
                                field.set(objectInstance, Long.valueOf(valueElement.getText()));
                            } else if (field.getType().equals(Float.TYPE)) {
                                field.set(objectInstance, Float.valueOf(valueElement.getText()));
                            } else if (field.getType().equals(Double.TYPE)) {
                                field.set(objectInstance, Double.valueOf(valueElement.getText()));
                            } else if (field.getType().equals(Boolean.TYPE)) {
                                field.set(objectInstance, Boolean.valueOf(valueElement.getText()));
                            } else if (field.getType().equals(Character.TYPE)) {
                                field.set(objectInstance, valueElement.getText().charAt(0));
                            } else {
                                field.set(objectInstance, valueElement.getText());
                            }
                        } else if (valueElement.getName().equals("reference")) {
                            //restore reference
                            int temp = Integer.valueOf(valueElement.getText());
                            field.set(objectInstance, deserializedObjects.get(Integer.valueOf(valueElement.getText())));
                        } else {
                            //restore null
                            field.set(objectInstance, null);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
            }   
        }
    }
}
