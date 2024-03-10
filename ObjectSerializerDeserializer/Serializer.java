
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;

public class Serializer {
    Serializer(){/*Default Constructor*/}

    public Document serialize(Object obj) {

        //create root element
        Element rootElement = new Element("serialized");

        //create documnent
        Document outputDocument = new Document(rootElement);

        //create map
        IdentityHashMap<Object,Integer> objectsSerialized = new IdentityHashMap<Object,Integer>();

        //this is dumb
        Document outout = null;

        //begin serialization
        try {
            outout = serializeObject(obj, outputDocument, objectsSerialized);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outout;
    } 

    private Document serializeObject(Object targetObject, Document outputDocument, IdentityHashMap<Object, Integer> objectsSerialized) throws Exception {
        
        //record encountered object being serialized so we can recall it later (avoid duplicate serialization) 
        //id's are assigned incrementally as objects are encountered
        int id = objectsSerialized.size(); //<-- Dev note: this cant be inline insed the put method like it was before, was causing issues
        objectsSerialized.put(targetObject, id);                                                           

        //create and add xml element for target object being serialized to document root
        Element objectElement = new Element("Object");
        objectElement.setAttribute("class", targetObject.getClass().getName());
        objectElement.setAttribute("id", Integer.toString(id)); 
        outputDocument.getRootElement().addContent(objectElement);

        //check if target object is an array 
        if (targetObject.getClass().isArray()) {
            //serialize array

            //REFACTOR OPPORTUNITY: extract method for serializing arrays
            
            //add additional length information
            objectElement.setAttribute("length", Integer.toString(Array.getLength(targetObject)));

            //serialize array contents
            if (targetObject.getClass().getComponentType().isPrimitive()) {
                //serialize primitive array

                //REFACTOR OPPORTUNITY: Consolidate duplicate conditional fragments
                if (targetObject.getClass().getComponentType().equals(Byte.TYPE)) {
                    for (int i=0; i<Array.getLength(targetObject); i++) {
                        Element valueElement = new Element("value");
                        valueElement.setText(String.valueOf(Array.getByte(targetObject, i)));
                        objectElement.addContent(valueElement);
                    }
                } else if (targetObject.getClass().getComponentType().equals(Short.TYPE)) {
                    for (int i=0; i<Array.getLength(targetObject); i++) {
                        Element valueElement = new Element("value");
                        valueElement.setText(String.valueOf(Array.getShort(targetObject, i)));
                        objectElement.addContent(valueElement);
                    }
                } else if (targetObject.getClass().getComponentType().equals(Integer.TYPE)) {
                    for (int i=0; i<Array.getLength(targetObject); i++) {
                        Element valueElement = new Element("value");
                        valueElement.setText(String.valueOf(Array.getInt(targetObject, i)));
                        objectElement.addContent(valueElement);
                    }
                } else if (targetObject.getClass().getComponentType().equals(Long.TYPE)) {
                    for (int i=0; i<Array.getLength(targetObject); i++) {
                        Element valueElement = new Element("value");
                        valueElement.setText(String.valueOf(Array.getLong(targetObject, i)));
                        objectElement.addContent(valueElement);
                    }
                } else if (targetObject.getClass().getComponentType().equals(Float.TYPE)) {
                    for (int i=0; i<Array.getLength(targetObject); i++) {
                        Element valueElement = new Element("value");
                        valueElement.setText(String.valueOf(Array.getFloat(targetObject, i)));
                        objectElement.addContent(valueElement);
                    }
                } else if (targetObject.getClass().getComponentType().equals(Double.TYPE)) {
                    for (int i=0; i<Array.getLength(targetObject); i++) {
                        Element valueElement = new Element("value");
                        valueElement.setText(String.valueOf(Array.getDouble(targetObject, i)));
                        objectElement.addContent(valueElement);
                    }
                } else if (targetObject.getClass().getComponentType().equals(Boolean.TYPE)) {
                    for (int i=0; i<Array.getLength(targetObject); i++) {
                        Element valueElement = new Element("value");
                        valueElement.setText(String.valueOf(Array.getBoolean(targetObject, i)));
                        objectElement.addContent(valueElement);
                    }
                } else if (targetObject.getClass().getComponentType().equals(Character.TYPE)) {
                    for (int i=0; i<Array.getLength(targetObject); i++) {
                        Element valueElement = new Element("value");
                        valueElement.setText(String.valueOf(Array.getShort(targetObject, i)));
                        objectElement.addContent(valueElement);
                    }
                }
            } else {
                //serialize non-primitve array 
                //recurse

                for (int i=0; i<Array.getLength(targetObject); i++) {

                    //create reference element for serialization
                    Element referencElement = new Element("reference");
                    if (objectsSerialized.containsKey(Array.get(targetObject, i))) {
                        //this object has already been serailized set refence element value to its id
                        referencElement.setText(objectsSerialized.get(Array.get(targetObject, i)).toString());
                    } else {
                        //this object has not yet been serialized, do so now
                        //recurse
                        
                        referencElement.setText(Integer.toString(objectsSerialized.size()));
                        serializeObject(Array.get(targetObject, i), outputDocument, objectsSerialized);
                        
                    } 

                    //add reference element to field element
                    objectElement.addContent(referencElement);
                }   
            }
        } else {
            //serialize regular object

            //get all fields of object (both declared and inherited)

            //***************
            //***REFACTOR1***
            //***************
            ArrayList<Field> gatheredFields = gatherObjectFields(targetObject);

            //now we iterate through the target objects gathered fields (both declared and inherited)
            //and serialize them into the target objects document element
            for (Field field : gatheredFields) {

                //create field element for field of target object being serialized
                Element fieldElement = new Element("field");
                fieldElement.setAttribute("name", field.getName());
                fieldElement.setAttribute("declaringclass", field.getDeclaringClass().getName());

                //check if field is public (set accessible if not)
                boolean fieldInaccessible = false;
                try {
                    if (!Modifier.isPublic(field.getModifiers())) {
                        field.setAccessible(true);
                    }
                } catch (InaccessibleObjectException e) {
                    fieldInaccessible = true;
                }
                
                //serialize value of field
                if (fieldInaccessible) {
                    Element valueElement = new Element("null");

                    //add value element to field element
                    fieldElement.addContent(valueElement);

                    //add field element to object element
                    objectElement.addContent(fieldElement);
                } else if (field.get(targetObject) == null) {
                    Element valueElement = new Element("null");

                    //add value element to field element
                    fieldElement.addContent(valueElement);

                    //add field element to object element
                    objectElement.addContent(fieldElement);

                } else if (field.getType().isPrimitive()) {
                    //create value element for serialization
                    Element valueElement = new Element("value");
                    
                    //***************
                    //***REFACTOR2***
                    //***************
                    valueElement.setText(serializePrimitive(field, targetObject));

                    //add value element to field element
                    fieldElement.addContent(valueElement);

                    //add field element to object element
                    objectElement.addContent(fieldElement);

                } else {
                    //***************
                    //***REFACTOR3***
                    //***************

                    //field type must be an object or array

                    //create reference element for serialization
                    Element referencElement = new Element("reference");
                    if (objectsSerialized.containsKey(field.get(targetObject))) {
                        //this object has already been serailized set refence element value to its id
                        referencElement.setText(objectsSerialized.get(field.get(targetObject)).toString());
                    } else {
                        //this object has not yet been serialized, do so now
                        //recurse
                        referencElement.setText(Integer.toString(objectsSerialized.size()));
                        serializeObject(field.get(targetObject), outputDocument, objectsSerialized);
                    }

                    //add reference element to field element
                    fieldElement.addContent(referencElement);

                    //add field element to object element
                    objectElement.addContent(fieldElement);
                }
            }
        }
        return outputDocument;
    }

    private ArrayList<Field> gatherObjectFields(Object targetObject) {
        ArrayList<Field> gatheredFields = new ArrayList<Field>();
        Class cls = targetObject.getClass();
        while(cls != null) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (int i=0; i<declaredFields.length; i++) {
                //check if the current field being gathered is static (only care about instance variables)
                if (! Modifier.isStatic(declaredFields[i].getModifiers())) {
                    gatheredFields.add(declaredFields[i]);
                }
            }
            cls = cls.getSuperclass();
        }
        return gatheredFields;
    }

    private String serializePrimitive(Field field, Object targetObject) throws IllegalAccessException {
        String result = null;
        if (field.getType().equals(Byte.TYPE)) {
            result = String.valueOf(field.getByte(targetObject));
        } else if (field.getType().equals(Short.TYPE)) {
            result = String.valueOf(field.getShort(targetObject));
        } else if (field.getType().equals(Integer.TYPE)) {
            result = String.valueOf(field.getInt(targetObject));
        } else if (field.getType().equals(Long.TYPE)) {
            result = String.valueOf(field.getLong(targetObject));
        } else if (field.getType().equals(Float.TYPE)) {
            result = String.valueOf(field.getFloat(targetObject));
        } else if (field.getType().equals(Double.TYPE)) {
            result = String.valueOf(field.getDouble(targetObject));
        } else if (field.getType().equals(Boolean.TYPE)) {
            result = String.valueOf(field.getBoolean(targetObject));
        } else if (field.getType().equals(Character.TYPE)) {
            result = String.valueOf(field.getChar(targetObject));
        }
        return result;
    }
}

