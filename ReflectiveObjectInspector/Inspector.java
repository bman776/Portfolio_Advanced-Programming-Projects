
/*
 * CPSC501 Assignment 2 Submission
 * Brett Gattinger
 * 30009390
 */

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Objects;

public class Inspector {

    public ArrayList<Integer> inspectedObjectHashCodes;

    private static final int fieldVal_primitive = 1;
    private static final int fieldVal_array = 2;
    private static final int fieldVal_obj = 3;

    Inspector() {
        inspectedObjectHashCodes = new ArrayList<Integer>();
    }
    
    //REQUIRED FUNCTION====================================================================
    public void inspect(Object obj, boolean recursive) {
        objectData inspectedObjectData = inspectObject(null, obj, recursive);

        System.out.println(inspectedObjectData.getFormattedMetaData(""));

        //Used this as alternative to UNIX script command 
        /*try {
            FileWriter myWriter = new FileWriter("script.txt", true);
            myWriter.write(inspectedObjectData.getFormattedMetaData(""));
            myWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }
    //=====================================================================================










    //META OBJECT RETRIEVAL FUNCTIONS====================================================================
    public Class getObjectClass(Object obj) {
        Class cls = obj.getClass();
        return cls;
    }
    public Class getObjectSuperClass(Class obj_cls) {
        Class spr_cls = obj_cls.getSuperclass();
        return spr_cls;
    }
    public Class[] getObjectInterfaces(Class obj_cls) {
        Class[] intrfcs = obj_cls.getInterfaces();
        return intrfcs;
    }
    public Constructor[] getObjectConstructors(Class obj_cls) {
        Constructor[] cnstctrs = obj_cls.getDeclaredConstructors();
        return cnstctrs;
    }
    public Field[] getObjectFields(Class obj_cls) {
        Field[] flds = obj_cls.getDeclaredFields();
        return flds;
    }
    public Method[] getObjectMethods(Class obj_cls) {
        Method[] mthds = obj_cls.getMethods();
        return mthds;
    }
    //====================================================================










    //META OBJECT INSPECTION FUNCTIONS====================================================================    
    public objectData inspectObject(Object parentObject, Object targetObject, boolean recursive) {
        objectData objData = new objectData();

        if (Objects.isNull(targetObject)) {
            return objData;
        }

        Object inspectedObject;
        Class inspectedObject_Class;
        Class inspectedObject_SuperClass;
        Class[] inspectedObject_Interfaces;
        Constructor[] inspectedObject_Constructors;
        Field[] inspectedObject_Fields;
        Method[] inspectedObject_Methods;

        //collect metaObject Data on given object
        inspectedObject = targetObject;
        inspectedObject_Class = getObjectClass(inspectedObject);
        inspectedObject_SuperClass = getObjectSuperClass(inspectedObject_Class);
        inspectedObject_Interfaces = getObjectInterfaces(inspectedObject_Class);
        inspectedObject_Constructors = getObjectConstructors(inspectedObject_Class);
        inspectedObject_Fields = getObjectFields(inspectedObject_Class);
        inspectedObject_Methods = getObjectMethods(inspectedObject_Class);

        //get object class
        objData.classDeclared = targetObject.getClass().getName();

        //get object hash code
        objData.objHash = String.valueOf(targetObject.hashCode());

        //inspect object superclass (get inheritance hierarchy)
        if ( ! Objects.isNull(inspectedObject_SuperClass)) {
            objData.superclassExtended.add(inspectSuperClass(inspectedObject_SuperClass, targetObject));
        }
        //else class of object being inspected doesn't have a super class 
        

        //inspect interfaces
        for (Class intrfc : inspectedObject_Interfaces) {
            objData.interfacesImplemented.add(inspectInterface(intrfc, inspectedObject_Methods));
        }

        //inspect constructors
        for (Constructor constructor : inspectedObject_Constructors) {
            objData.constructorsDeclared.add(inspectObjectConstructor(inspectedObject_Class, constructor));
        }

        //inspect fields
        for (Field field : inspectedObject_Fields) {
            try {
                objData.fieldsDeclared.add(inspectObjectField(inspectedObject_Class, inspectedObject, field, recursive));
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        //inspect methods
        for (Method method : inspectedObject_Methods) {
            objData.methodsDeclared.add(inspectObjectMethod(method));
        }

        return objData;
    }

    public constructorMetaObjectData inspectObjectConstructor(Class parentObjCls, Constructor c) {
        constructorMetaObjectData constructorData = new constructorMetaObjectData();

        //get constructor name (which I suppose is just class name)
        constructorData.constructorName = parentObjCls.getName();

        //get constructor parameters
        for (Class paramter : c.getParameterTypes()) {
            constructorData.constructorParameters.add(paramter.toString());
        }

        //get constructor modifiers
        int constructorModifierCode = c.getModifiers();
        constructorData.constructorModifiers.add(Modifier.toString(constructorModifierCode));

        return constructorData;
    }

    public fieldMetaObjectData inspectObjectField(Class parentObjCls, Object parentObj, Field f, boolean recursive) throws IllegalAccessException {
        fieldMetaObjectData fieldData = new fieldMetaObjectData();

        //ensure field is accessible
        try {
            f.setAccessible(true);
        } catch (InaccessibleObjectException ex) {
            fieldData.fieldValueCode = fieldVal_primitive;
            fieldData.primitiveFieldValue = "InaccessibleObjectException: Field / Object not accessible";
        }
        
        //get field name
        fieldData.fieldName = f.getName();

        //get the modifiers the field uses
        int fieldModifierCode = f.getModifiers();
        fieldData.fieldModifiers.add(Modifier.toString(fieldModifierCode));

        //get the field type and value
        Class ft = f.getType();
        fieldData.fieldType = ft.toString();
        if (ft.isPrimitive()) {
            //handle primitive
            assert(Objects.isNull(fieldData.arrayFieldValue));
            assert(Objects.isNull(fieldData.objectFieldValue));

            fieldData.fieldValueCode = fieldVal_primitive;

            //determine type of primitive
            try {
                if (ft.equals(Byte.TYPE)) {
                    fieldData.primitiveFieldValue = String.valueOf(f.getByte(parentObj)); 
                } else if (ft.equals(Short.TYPE)) {
                    fieldData.primitiveFieldValue = String.valueOf(f.getShort(parentObj)); 
                } else if (ft.equals(Integer.TYPE)) {
                    fieldData.primitiveFieldValue = String.valueOf(f.getInt(parentObj)); 
                } else if (ft.equals(Long.TYPE)) {
                    fieldData.primitiveFieldValue = String.valueOf(f.getLong(parentObj)); 
                } else if (ft.equals(Float.TYPE)) {
                    fieldData.primitiveFieldValue = String.valueOf(f.getFloat(parentObj)); 
                } else if (ft.equals(Double.TYPE)) {
                    fieldData.primitiveFieldValue = String.valueOf(f.getDouble(parentObj)); 
                } else if (ft.equals(Character.TYPE)) {
                    fieldData.primitiveFieldValue = String.valueOf(f.getChar(parentObj)); 
                } else if (ft.equals(Boolean.TYPE)) {
                    fieldData.primitiveFieldValue = String.valueOf(f.getBoolean(parentObj)); 
                } 
            } catch (InaccessibleObjectException ex) {
                fieldData.fieldValueCode = fieldVal_primitive;
                fieldData.primitiveFieldValue = "InaccessibleObjectException: Field / Object not accessible";
            } catch (IllegalAccessException ex) {
                fieldData.fieldValueCode = fieldVal_primitive;
                fieldData.primitiveFieldValue = "InaccessibleObjectException: Field / Object not accessible";
            } 
            
        } else if (ft.isArray()) {
            //handle array
            assert(Objects.isNull(fieldData.primitiveFieldValue));
            assert(Objects.isNull(fieldData.objectFieldValue));

            //get array object
            //Object arrayObject = f.get(parentObj);

            try {
                //get array object
                Object arrayObject = f.get(parentObj); //<--throws access exceptions

                //call inspectArray()
                fieldData.fieldValueCode = fieldVal_array;
                fieldData.arrayFieldValue.add(inspectArray(parentObj, arrayObject, ft, recursive));

            } catch (InaccessibleObjectException ex) {
                fieldData.fieldValueCode = fieldVal_primitive;
                fieldData.primitiveFieldValue = "InaccessibleObjectException: Field / Object not accessible";
            } catch (IllegalAccessException ex) {
                fieldData.fieldValueCode = fieldVal_primitive;
                fieldData.primitiveFieldValue = "InaccessibleObjectException: Field / Object not accessible";
            } 

            //call inspectArray()
            //fieldData.fieldValueCode = fieldVal_array;
            //fieldData.arrayFieldValue.add(inspectArray(parentObj, arrayObject, ft, recursive));
        } else {
            //handle object
            assert(Objects.isNull(fieldData.primitiveFieldValue));
            assert(Objects.isNull(fieldData.arrayFieldValue));

            //get object
            Object innerObject = f.get(parentObj);

            //check if object is unitialized (null)
            if (Objects.isNull(innerObject)) {
                fieldData.fieldValueCode = fieldVal_primitive;
                fieldData.primitiveFieldValue = "instance of: " + f.getClass().getName() + " | object value is null";
            } else {
                //inner object is not null

                //check recursion
                if (recursive) {
                    //check if object has already been inspected
                    if ( ! inspectedObjectHashCodes.contains(innerObject.hashCode())) {
                        //object has not been inspected yet

                        //track the object
                        inspectedObjectHashCodes.add(innerObject.hashCode());

                        //fully inspect the object
                        fieldData.fieldValueCode = fieldVal_obj;
                        fieldData.objectFieldValue.add(inspectObject(parentObj, innerObject, true));
                    } else {
                        //object has already been inspected

                        //perform surface level inspection of the object
                        fieldData.fieldValueCode = fieldVal_primitive;
                        fieldData.primitiveFieldValue = "Object already inspected | hash code: " + String.valueOf(innerObject.hashCode());
                    }
                } else {
                    //perform surface level inspection of the object
                    fieldData.fieldValueCode = fieldVal_primitive;
                    fieldData.primitiveFieldValue = "instance of: " + f.getClass().getName() + " | hash code: " + String.valueOf(innerObject.hashCode());
                }
            }

            
        }
        return fieldData;
    }

    public arrayMetaObjectData inspectArray(Object parentObj, Object arrayObject, Class arrayClass, boolean recursive) {
        arrayMetaObjectData arrayData = new arrayMetaObjectData();

        //get array name
        arrayData.arrayName = arrayClass.getName();

        //get array component type
        Class arType = arrayClass.getComponentType();
        arrayData.componentType = arType.toString();

        //get array length
        int arLen = Array.getLength(arrayObject);
        arrayData.arrayLength = String.valueOf(arLen);

        //get component values
        if (arType.isPrimitive()) {
            //inpsect primitive array
            assert(Objects.isNull(arrayData.arrayComponentValues));
            assert(Objects.isNull(arrayData.objectComponentValues));
    
            arrayData.fieldValueCode = fieldVal_primitive;

            if (arType.equals(Byte.TYPE)) {
                for (int i = 0; i < arLen; i++) {
                    arrayData.primitiveComponentValues.add(String.valueOf(Array.getByte(arrayObject, i)));
                }
            } else if (arType.equals(Short.TYPE)) {
                for (int i = 0; i < arLen; i++) {
                    arrayData.primitiveComponentValues.add(String.valueOf(Array.getShort(arrayObject, i)));
                }
            } else if (arType.equals(Integer.TYPE)) {
                for (int i = 0; i < arLen; i++) {
                    arrayData.primitiveComponentValues.add(String.valueOf(Array.getInt(arrayObject, i)));
                }
            } else if (arType.equals(Long.TYPE)) {
                for (int i = 0; i < arLen; i++) {
                    arrayData.primitiveComponentValues.add(String.valueOf(Array.getLong(arrayObject, i)));
                }
            } else if (arType.equals(Float.TYPE)) {
                for (int i = 0; i < arLen; i++) {
                    arrayData.primitiveComponentValues.add(String.valueOf(Array.getFloat(arrayObject, i)));
                }
            } else if (arType.equals(Double.TYPE)) {
                for (int i = 0; i < arLen; i++) {
                    arrayData.primitiveComponentValues.add(String.valueOf(Array.getDouble(arrayObject, i)));
                }
            } else if (arType.equals(Character.TYPE)) {
                for (int i = 0; i < arLen; i++) {
                    arrayData.primitiveComponentValues.add(String.valueOf(Array.getChar(arrayObject, i)));
                }
            } else if (arType.equals(Boolean.TYPE)) {
                for (int i = 0; i < arLen; i++) {
                    arrayData.primitiveComponentValues.add(String.valueOf(Array.getBoolean(arrayObject, i)));
                }
            }

        } else if (arType.isArray()) {
            //inspect multidimensional array
            assert(Objects.isNull(arrayData.primitiveComponentValues));
            assert(Objects.isNull(arrayData.objectComponentValues));
            
            arrayData.fieldValueCode = fieldVal_array;

            //recursively call inspect array
            for (int i = 0; i < arLen; i++) {
                Object innerArrayObj = Array.get(arrayObject, i);
                Class innerArrayClass = innerArrayObj.getClass();

                arrayData.arrayComponentValues.add(inspectArray(arrayObject, innerArrayObj, innerArrayClass, recursive));
            }
            

        } else {
            //inspect object array
            assert(Objects.isNull(arrayData.primitiveComponentValues));
            assert(Objects.isNull(arrayData.arrayComponentValues));
            
            arrayData.fieldValueCode = fieldVal_obj;

            for (int i = 0; i < arLen; i++) {
                Object componentObject = Array.get(arrayObject, i);
                arrayData.objectComponentValues.add(inspectObject(arrayObject, componentObject, recursive));
            }

        }
        return arrayData;
    }

    public methodMetaObjectData inspectObjectMethod(Method m) {
        methodMetaObjectData methodData = new methodMetaObjectData();

        //get method name
        methodData.methodName = m.getName();
        
        //get exception types that can be thrown by method
        for (Class exception : m.getExceptionTypes()) {
            methodData.methodExceptions.add(exception.toString());
        }

        //get parameter types that the method takes
        for (Class parameter : m.getParameterTypes()) {
            methodData.methodParameters.add(parameter.toString());
        }

        //get the modifiers the method uses
        int methodModifierCode = m.getModifiers();
        methodData.methodModifiers.add(Modifier.toString(methodModifierCode));

        //get the return type of the method
        methodData.returnType = m.getReturnType().toString();
    
        return methodData;
    }
    //====================================================================










    //INHERITANCE HIERARCHY TRAVERSAL FUNCTIONS=====================================================
    public superClassMetaObjectData inspectSuperClass(Class spr_cls, Object originalChildObj) {
        superClassMetaObjectData superClassData = new superClassMetaObjectData();

        Class inspectedSuperClass_Class;
        Class inspectedSuperClass_SuperClass;
        Class[] inspectedSuperClass_Interfaces;
        Constructor[] inspectedSuperClass_Constructors;
        Field[] inspectedSuperClass_Fields;
        Method[] inspectedSuperClass_Methods;

        //collect metaObject Data on given superclass
        inspectedSuperClass_Class = spr_cls;
        inspectedSuperClass_SuperClass = spr_cls.getSuperclass();
        inspectedSuperClass_Interfaces = getObjectInterfaces(inspectedSuperClass_Class);
        inspectedSuperClass_Constructors = getObjectConstructors(inspectedSuperClass_Class);
        inspectedSuperClass_Fields = getObjectFields(inspectedSuperClass_Class);
        inspectedSuperClass_Methods = getObjectMethods(inspectedSuperClass_Class);


        //get superclass name
        superClassData.superClassName = inspectedSuperClass_Class.getName();

        //inspect superclass of superclass
        if ( ! Objects.isNull(inspectedSuperClass_SuperClass)) {
            superClassData.superclassExtended.add(inspectSuperClass(inspectedSuperClass_SuperClass, originalChildObj));
        }
        //else superclass being inspected doesn't have a superclass
        

        //inspect interfaces
        for (Class _interface : inspectedSuperClass_Interfaces) {
            superClassData.interfacesImplemented.add(inspectInterface(_interface, originalChildObj));
        }

        //inspect constructors
        for (Constructor constructor : inspectedSuperClass_Constructors) {
            superClassData.constructorsDeclared.add(inspectObjectConstructor(inspectedSuperClass_Class, constructor));
        }

        //inspect fields
        for (Field field : inspectedSuperClass_Fields) {
            try {
                superClassData.fieldsDeclared.add(inspectObjectField(inspectedSuperClass_Class, originalChildObj, field, false));
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        //inspect methods
        for (Method method : inspectedSuperClass_Methods) {
            superClassData.methodsDeclared.add(inspectObjectMethod(method));
        }

        return superClassData;
    }

    public interfaceMetaObjectData inspectInterface(Class intrfc, Object originalChildObj) {
        interfaceMetaObjectData interfaceData = new interfaceMetaObjectData();

        Class inspectedInterface_Class;
        Class[] inspectedInterface_Interfaces;
        Field[] inspectedInterface_Fields;
        Method[] inspectedInterface_Methods;

        //collect metaObject Data on given superclass
        inspectedInterface_Class = intrfc;
        inspectedInterface_Interfaces = getObjectInterfaces(inspectedInterface_Class);
        inspectedInterface_Fields = getObjectFields(inspectedInterface_Class);
        inspectedInterface_Methods = getObjectMethods(inspectedInterface_Class);

        //get interface name
        interfaceData.interfaceName = inspectedInterface_Class.getName();

        //inspect interfaces 
        for (Class _interface : inspectedInterface_Interfaces) {
            interfaceData.interfacesExtended.add(inspectInterface(_interface, originalChildObj));
        }

        //inspect fields
        for (Field field : inspectedInterface_Fields) {
            try {
                interfaceData.constantsDeclared.add(inspectObjectField(inspectedInterface_Class, originalChildObj, field, false));
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        //inspect methods
        for (Method method : inspectedInterface_Methods) {
            interfaceData.methodsDeclared.add(inspectObjectMethod(method));
        }

        return interfaceData;
    }
    //==============================================================================================
    









    //DATA ENCAPSULATION CLASSES====================================================================
    public class superClassMetaObjectData {
        String superClassName;
        ArrayList<superClassMetaObjectData> superclassExtended;
        ArrayList<interfaceMetaObjectData> interfacesImplemented;
        ArrayList<constructorMetaObjectData> constructorsDeclared;
        ArrayList<fieldMetaObjectData> fieldsDeclared;
        ArrayList<methodMetaObjectData> methodsDeclared;

        superClassMetaObjectData() {
            superClassName = "";
            superclassExtended = new ArrayList<superClassMetaObjectData>();
            interfacesImplemented = new ArrayList<interfaceMetaObjectData>();
            constructorsDeclared = new ArrayList<constructorMetaObjectData>();
            fieldsDeclared = new ArrayList<fieldMetaObjectData>();
            methodsDeclared = new ArrayList<methodMetaObjectData>();
        }

        public String getFormattedMetaData(String indent) {
            String localIndent = new String(indent);
            String output = "";
            output = localIndent + "SUPERCLASS: \n";
            output += localIndent + "\tClass Name: " + superClassName + "\n";
            for (superClassMetaObjectData sprClsVal : superclassExtended) {
                output += localIndent + "\tSuperClass Extended: \n" + sprClsVal.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tINTERFACES IMPLEMENTED: \n";
            for (interfaceMetaObjectData intrfc: interfacesImplemented) {
                output += intrfc.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tCONSTRUCTORS DECLARED: \n";
            for (constructorMetaObjectData cnstrct : constructorsDeclared) {
                output += cnstrct.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tMETHODS DECLARED: \n";
            for (methodMetaObjectData mthd : methodsDeclared) {
                output += mthd.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tFIELDS DECLARED: \n";
            for (fieldMetaObjectData fld : fieldsDeclared) {
                output += fld.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            return output;
        }
    }

    public class interfaceMetaObjectData {
        String interfaceName;
        ArrayList<interfaceMetaObjectData> interfacesExtended;
        ArrayList<fieldMetaObjectData> constantsDeclared;
        ArrayList<methodMetaObjectData> methodsDeclared;

        interfaceMetaObjectData() {
            interfaceName = "";
            interfacesExtended = new ArrayList<interfaceMetaObjectData>();
            constantsDeclared = new ArrayList<fieldMetaObjectData>();
            methodsDeclared = new ArrayList<methodMetaObjectData>();
        }

        public String getFormattedMetaData(String indent) {
            String localIndent = new String(indent);
            String output = "";
            output += localIndent + "INTERFACE: \n";
            output += localIndent + "\t Interface Name: " + interfaceName + "\n";
            output += localIndent + "\tINTERFACES IMPLEMENTED: \n";
            for (interfaceMetaObjectData intrfc: interfacesExtended) {
                output += intrfc.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tMETHODS DECLARED: \n";
            for (methodMetaObjectData mthd : methodsDeclared) {
                output += mthd.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tFIELDS DECLARED: \n";
            for (fieldMetaObjectData fld : constantsDeclared) {
                output += fld.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }

            return output;
        }
    }

    public class objectData {
        String classDeclared;
        String objHash;
        ArrayList<superClassMetaObjectData> superclassExtended;               
        ArrayList<interfaceMetaObjectData> interfacesImplemented;                       
        ArrayList<constructorMetaObjectData> constructorsDeclared;
        ArrayList<fieldMetaObjectData> fieldsDeclared;
        ArrayList<methodMetaObjectData> methodsDeclared;

        objectData() {
            classDeclared = "";
            objHash = "";
            superclassExtended = new ArrayList<superClassMetaObjectData>();
            interfacesImplemented = new ArrayList<interfaceMetaObjectData>();
            constructorsDeclared = new ArrayList<constructorMetaObjectData>();
            methodsDeclared = new ArrayList<methodMetaObjectData>();
            fieldsDeclared = new ArrayList<fieldMetaObjectData>();
        }

        public String getFormattedMetaData(String indent) {
            String localIndent = new String(indent);
            String output = "";
            output = localIndent + "OBJECT: \n";
            output += localIndent + "\tObject Hash Code: " + objHash + "\n";
            output += localIndent + "\tClass Declared: " + classDeclared + "\n";
            for (superClassMetaObjectData sprClsVal : superclassExtended) {
                output += localIndent + "\tSuperClass Extended: \n" + sprClsVal.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tInterfaces Implemented: \n";
            output += localIndent + "\tINTERFACES IMPLEMENTED: \n";
            for (interfaceMetaObjectData intrfc: interfacesImplemented) {
                output += intrfc.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tCONSTRUCTORS DECLARED: \n";
            for (constructorMetaObjectData cnstrct : constructorsDeclared) {
                output += cnstrct.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tMETHODS DECLARED: \n";
            for (methodMetaObjectData mthd : methodsDeclared) {
                output += mthd.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            output += localIndent + "\tFIELDS DECLARED: \n";
            for (fieldMetaObjectData fld : fieldsDeclared) {
                output += fld.getFormattedMetaData(localIndent + "\t\t") + "\n";
            }
            return output;
        } 
    }

    public class constructorMetaObjectData {
        String constructorName;
        ArrayList<String> constructorParameters;
        ArrayList<String> constructorModifiers;

        constructorMetaObjectData(){
            constructorName = "";
            constructorParameters = new ArrayList<String>();
            constructorModifiers = new ArrayList<String>();
        }

        public String getFormattedMetaData(String indent) {
            String localIndent = new String(indent);
            String output = "";
            output = localIndent + "Constructor: " + constructorName + "\n";
            output += localIndent + "\tParameters: \n";
            for (String parameter : constructorParameters) {
                output += localIndent + "      " + parameter + "\n";
            }
            output += localIndent + "\tModifiers: \n";
            for (String modifier : constructorModifiers) {
                output += localIndent + "\t\t" + modifier + "\n";
            }
            return output;
        }
    }

    public class fieldMetaObjectData {
        String fieldName;
        ArrayList<String> fieldModifiers;
        String fieldType;
        String primitiveFieldValue;
        ArrayList<arrayMetaObjectData> arrayFieldValue;
        ArrayList<objectData> objectFieldValue;
        int fieldValueCode;

        fieldMetaObjectData(){
            fieldName = "";
            fieldModifiers = new ArrayList<String>();
            fieldType = "";
            primitiveFieldValue = "";
            arrayFieldValue = new ArrayList<arrayMetaObjectData>();
            objectFieldValue = new ArrayList<objectData>();
            fieldValueCode = 0;
        }

        public String getFormattedMetaData(String indent) {
            String localIndent = new String(indent);
            String output = "";
            output = localIndent + "Field: " + fieldName + "\n";
            output += localIndent + "\tModifiers: \n";
            for (String modifier : fieldModifiers) {
                output += localIndent + "\t\t" + modifier + "\n";
            }
            output += localIndent + "\tField Type: " + fieldType + "\n";
            if (fieldValueCode == fieldVal_primitive) {
                output += localIndent + "\tField Value: " + primitiveFieldValue + "\n";
            } else if (fieldValueCode == fieldVal_array) {
                for (arrayMetaObjectData arVal : arrayFieldValue) {
                    output += localIndent + "\tField Value: \n" + arVal.getFormattedMetaData(localIndent + "\t\t") + "\n";
                }
            } else if (fieldValueCode == fieldVal_obj) {
                for (objectData objVal : objectFieldValue) {
                    output += localIndent + "\tField Value: \n" + objVal.getFormattedMetaData(localIndent + "\t\t") + "\n";
                }
            }
            return output;
        }
    }

    public class arrayMetaObjectData {
        String arrayName;
        String componentType;
        ArrayList<String> primitiveComponentValues;
        ArrayList<arrayMetaObjectData> arrayComponentValues;
        ArrayList<objectData> objectComponentValues; 
        String arrayLength;
        int fieldValueCode;

        arrayMetaObjectData() {
            arrayName = "";
            componentType = "";
            primitiveComponentValues = new ArrayList<String>();
            arrayComponentValues = new ArrayList<arrayMetaObjectData>();
            objectComponentValues = new ArrayList<objectData>();
            fieldValueCode = 0;
        }

        public String getFormattedMetaData(String indent) {
            String output = "";
            output = indent + "Array: " + arrayName + "\n";
            output += indent + "\tComponent Type: " + componentType + "\n";
            output += indent + "\tArray Length: " + arrayLength + "\n";
            

            if (fieldValueCode == fieldVal_primitive) {
                output += indent + "\tComponent Values: [";
                int i = 0;
                for (String val : primitiveComponentValues) {
                    if (i++ == primitiveComponentValues.size() - 1) {
                        output += val + "]\n";
                    } else {
                        output += val + ", "; 
                    }
                } 
            } else if (fieldValueCode == fieldVal_array) {
                output += indent + "\tComponent Values: [\n";
                for (arrayMetaObjectData val : arrayComponentValues) {
                    output += val.getFormattedMetaData(indent + "\t\t") + "\n";
                }
                output += indent + "\t]";
            } else if (fieldValueCode == fieldVal_obj) {
                output += indent + "\tComponent Values: [\n";
                for (objectData val : objectComponentValues) {
                    output += val.getFormattedMetaData(indent + "\t\t") + "\n";
                }
                output += indent + "\t]";
            }
            return output;
        }
    }

    public class methodMetaObjectData {
        String methodName;
        ArrayList<String> methodExceptions;
        ArrayList<String> methodParameters;
        ArrayList<String> methodModifiers;
        String returnType;

        methodMetaObjectData(){
            methodName = "";
            methodExceptions = new ArrayList<String>();
            methodParameters = new ArrayList<String>();
            methodModifiers = new ArrayList<String>();
            returnType = "";
        }

        public String getFormattedMetaData(String indent) {
            String output = "";
            output = indent + "Method: " + methodName + "\n";
            output += indent + "\tExceptions: \n";
            for (String exception : methodExceptions) {
                output += indent + "      " + exception + "\n";
            }
            output += indent + "\tParameters: \n";
            for (String parameter : methodParameters) {
                output += indent + "      " + parameter + "\n";
            }
            output += indent + "\tModifiers: \n";
            for (String modifier : methodModifiers) {
                output += indent + "      " + modifier + "\n";
            }
            output += indent + "\tReturn Type: " + returnType + "\n";
            return output;
        }   
    } 
    //====================================================================
}