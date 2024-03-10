
/*Brett Gattinger 30009390 */

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.jdom2.Document;

public class ObjectCreator extends JFrame {

    private JTabbedPane mainNavigatioPane;
    private Serializer serializer;
    private Sender sender;
    private String receiverIP;
    private int receiverPort;

    /*
     * Launch application
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Must provide the IP address and port number of Receiver");
            return;
        }

        String receiver_IPaddress = args[0];
        int receiver_portNumber = Integer.parseInt(args[1]);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ObjectCreator objectCreator = new ObjectCreator(receiver_IPaddress, receiver_portNumber);
                    objectCreator.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ObjectCreator(String IP, int Port) {

        this.receiverIP = IP;
        this.receiverPort = Port;

        this.serializer = new Serializer();
        this.sender = new Sender();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Object Creator");
        setBounds(50, 50, 950, 750);
        setLayout(null);

        //initialize main tabbed navigation pane
        mainNavigatioPane = new JTabbedPane();
        mainNavigatioPane.setBounds(10, 10, 880, 680);

        //add tabs to main navigation pane
        JComponent primtiveobjectCreator = new PrimitiveObjectCreator();
        mainNavigatioPane.addTab("Primitive Object Creator", primtiveobjectCreator);

        JComponent compositeObjectCreator = new CompositeObjectCreator();
        mainNavigatioPane.addTab("Composite Object Creator", compositeObjectCreator);

        JComponent primitiveArrayObjectCreator = new PrimitiveArrayObjectCreator();
        mainNavigatioPane.addTab("Primitive Array Object Creator", primitiveArrayObjectCreator);

        JComponent compositeArrayObjectCreator = new CompositeArrayObjectCreator();
        mainNavigatioPane.addTab("Composite Array Object Creator", compositeArrayObjectCreator);

        JComponent collectionObjectCreator = new CollectionObjectCreator();
        mainNavigatioPane.addTab("Collection Object Creator", collectionObjectCreator);

        add(mainNavigatioPane);
    }

    public class PrimitiveObjectCreator extends JPanel {
        public JTextField byteValueEntryField;
        public JTextField shortValueEntryField;
        public JTextField intValueEntryField;
        public JTextField longValueEntryField;
        public JTextField floatValueEntryField;
        public JTextField doubleValueEntryField;
        public JTextField booleanValueEntryField;
        public JTextField charValueEntryField;

        public JButton createPrimitiveObjectButton;

        //REFACTOR OPPORTUNITY: Extract method to create border around each textfield
        private Border borderByteField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "byte value");
        private Border borderShortField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "short value");
        private Border borderIntField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "int value");
        private Border borderLongField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "long value");
        private Border borderFloatField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "float value");
        private Border borderDoubleField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "double value");
        private Border borderBooleanField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "boolean value");
        private Border borderCharField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "char value");
        
        /*
         * Constructor
         */
        PrimitiveObjectCreator() {
            //configure the JPanel
            setLayout(null);

            //REFACTOR OPPORTUNITY: Extract method to init each text field

            //initialize and add data entry fields to JPanel
            byteValueEntryField = new JTextField();
            byteValueEntryField.setBounds(10, 10, 200, 50);
            byteValueEntryField.setBorder(borderByteField);
            byteValueEntryField.setEditable(true);
            add(byteValueEntryField);

            shortValueEntryField = new JTextField();
            shortValueEntryField.setBounds(10, 70, 200, 50);
            shortValueEntryField.setBorder(borderShortField);
            shortValueEntryField.setEditable(true);
            add(shortValueEntryField);

            intValueEntryField = new JTextField();
            intValueEntryField.setBounds(10, 130, 200, 50);
            intValueEntryField.setBorder(borderIntField);
            intValueEntryField.setEditable(true);
            add(intValueEntryField);

            longValueEntryField = new JTextField();
            longValueEntryField.setBounds(10, 190, 200, 50);
            longValueEntryField.setBorder(borderLongField);
            longValueEntryField.setEditable(true);
            add(longValueEntryField);

            floatValueEntryField = new JTextField();
            floatValueEntryField.setBounds(10, 240, 200, 50);
            floatValueEntryField.setBorder(borderFloatField);
            floatValueEntryField.setEditable(true);
            add(floatValueEntryField);

            doubleValueEntryField = new JTextField();
            doubleValueEntryField.setBounds(10, 300, 200, 50);
            doubleValueEntryField.setBorder(borderDoubleField);
            doubleValueEntryField.setEditable(true);
            add(doubleValueEntryField);

            booleanValueEntryField = new JTextField();
            booleanValueEntryField.setBounds(10, 360, 200, 50);
            booleanValueEntryField.setBorder(borderBooleanField);
            booleanValueEntryField.setEditable(true);
            add(booleanValueEntryField);

            charValueEntryField = new JTextField();
            charValueEntryField.setBounds(10, 420, 200, 50);
            charValueEntryField.setBorder(borderCharField);
            charValueEntryField.setEditable(true);
            add(charValueEntryField);

            createPrimitiveObjectButton = new JButton("Create Primtive Object");
            createPrimitiveObjectButton.setBounds(10, 480, 200, 50);
            createPrimitiveObjectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    Byte byteVal = 0;
                    Short shortVal = 0;
                    int intVal = 0;
                    long longVal = 0;
                    float floatVal = 0.0f;
                    double doubleVal = 0.0d;
                    boolean boolVal = true;
                    char charVal = 'a';

                    //REFACTOR OPPORTUNITY HERE: extract method to make this whole thing shorter 

                    //validate/collect entered data for primitive object creation
                    try {
                        byteVal = Byte.parseByte(byteValueEntryField.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Byte Value");
                        return;
                    }
                    
                    try {
                        shortVal = Short.parseShort(shortValueEntryField.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Short Value");
                        return;
                    }

                    try {
                        intVal = Integer.parseInt(intValueEntryField.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Integer Value");
                        return;
                    }

                    try {
                        longVal = Long.parseLong(longValueEntryField.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Long Value");
                        return;
                    }

                    try {
                        floatVal = Float.parseFloat(floatValueEntryField.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Float Value");
                        return;
                    }

                    try {
                        doubleVal = Double.parseDouble(doubleValueEntryField.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Double Value");
                        return;
                    }

                    try {
                        boolVal = Boolean.parseBoolean(booleanValueEntryField.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Boolean Value");
                        return;
                    }

                    if (charValueEntryField.getText().length() > 1 ||
                        charValueEntryField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive");
                            return;
                    } else {
                        charVal = charValueEntryField.getText().charAt(0);
                    }

                    //ASSERTION:
                    //execution in event handler will only reach this point if all provided data is of valid type

                    //create primitive object
                    PrimitiveClass primitiveObject = new PrimitiveClass(byteVal, shortVal, intVal, longVal, floatVal, doubleVal, boolVal, charVal);

                    //serialize object
                    Document serializedObject = serializer.serialize(primitiveObject);

                    //send object to receiver
                    sender.sendDocument(receiverIP, receiverPort, serializedObject);
                }
            });
            createPrimitiveObjectButton.setEnabled(true);
            add(createPrimitiveObjectButton);
        }
    }

    public class CompositeObjectCreator extends JPanel {

        public JLabel Object1Label;
        public JLabel Object1Note;
        public JTextField intValueEntryField_group1;
        public JTextField floatValueEntryField_group1;
        public JTextField charValueEntryField_group1;
        public JComboBox<String> objectReferenceEntryField_group1;

        public JLabel Object2Label;
        public JCheckBox object2Check;
        public JTextField intValueEntryField_group2;
        public JTextField floatValueEntryField_group2;
        public JTextField charValueEntryField_group2;
        public JComboBox<String> objectReferenceEntryField_group2;

        public JLabel Object3Label;
        public JCheckBox object3Check;
        public JTextField intValueEntryField_group3;
        public JTextField floatValueEntryField_group3;
        public JTextField charValueEntryField_group3;
        public JComboBox<String> objectReferenceEntryField_group3;

        public JButton createCompositeObjectButton;

        public JLabel readme;

        private Border borderIntField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "int value");
        private Border borderFloatField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "float value");
        private Border borderCharField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "char value");
        private Border borderRefField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "object reference");

        CompositeObjectCreator() {
            setLayout(null);

            String[] objectRefOptions = {"null", "Object1", "Object2", "Object3"};

            Object1Label = new JLabel("Object 1:");
            Object1Label.setBounds(10, 10, 200, 50);
            add(Object1Label);
            Object1Note = new JLabel("Must enter values for object 1");
            Object1Note.setBounds(10, 70, 200, 50);
            add(Object1Note);
            intValueEntryField_group1 = new JTextField();
            intValueEntryField_group1.setBounds(10, 130, 200, 50);
            intValueEntryField_group1.setBorder(borderIntField);
            intValueEntryField_group1.setEditable(true);
            add(intValueEntryField_group1);
            floatValueEntryField_group1 = new JTextField();
            floatValueEntryField_group1.setBounds(10, 190, 200, 50);
            floatValueEntryField_group1.setBorder(borderFloatField);
            floatValueEntryField_group1.setEditable(true);
            add(floatValueEntryField_group1);
            charValueEntryField_group1 = new JTextField();
            charValueEntryField_group1.setBounds(10, 250, 200, 50);
            charValueEntryField_group1.setBorder(borderCharField);
            charValueEntryField_group1.setEditable(true);
            add(charValueEntryField_group1);
            objectReferenceEntryField_group1 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group1.setBounds(10,310, 200, 50);
            objectReferenceEntryField_group1.setBorder(borderRefField);
            add(objectReferenceEntryField_group1);

            Object2Label = new JLabel("Object 2:");
            Object2Label.setBounds(220, 10, 200, 50);
            add(Object2Label);
            object2Check = new JCheckBox("Enable Object2", false);
            object2Check.setBounds(220, 70, 200, 50);
            object2Check.setSelected(true);
            object2Check.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (object2Check.isSelected()) {
                        intValueEntryField_group2.setEnabled(true);
                        floatValueEntryField_group2.setEnabled(true);
                        charValueEntryField_group2.setEnabled(true);
                        objectReferenceEntryField_group2.setEnabled(true);
                    } else {
                        intValueEntryField_group2.setEnabled(false);
                        floatValueEntryField_group2.setEnabled(false);
                        charValueEntryField_group2.setEnabled(false);
                        objectReferenceEntryField_group2.setEnabled(false);
                    }
                }
            });
            add(object2Check);
            intValueEntryField_group2 = new JTextField();
            intValueEntryField_group2.setBounds(220, 130, 200, 50);
            intValueEntryField_group2.setBorder(borderIntField);
            intValueEntryField_group2.setEditable(true);
            add(intValueEntryField_group2);
            floatValueEntryField_group2 = new JTextField();
            floatValueEntryField_group2.setBounds(220, 190, 200, 50);
            floatValueEntryField_group2.setBorder(borderFloatField);
            floatValueEntryField_group2.setEditable(true);
            add(floatValueEntryField_group2);
            charValueEntryField_group2 = new JTextField();
            charValueEntryField_group2.setBounds(220, 250, 200, 50);
            charValueEntryField_group2.setBorder(borderCharField);
            charValueEntryField_group2.setEditable(true);
            add(charValueEntryField_group2);
            objectReferenceEntryField_group2 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group2.setBounds(220, 310, 200, 50);
            objectReferenceEntryField_group2.setBorder(borderRefField);
            add(objectReferenceEntryField_group2);

            Object3Label = new JLabel("Object 3:");
            Object3Label.setBounds(430, 10, 200, 50);
            add(Object3Label);
            object3Check = new JCheckBox("Enable Object2", false);
            object3Check.setBounds(430, 70, 200, 50);
            object3Check.setSelected(true);
            object3Check.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (object3Check.isSelected()) {
                        intValueEntryField_group3.setEnabled(true);
                        floatValueEntryField_group3.setEnabled(true);
                        charValueEntryField_group3.setEnabled(true);
                        objectReferenceEntryField_group3.setEnabled(true);
                    } else {
                        intValueEntryField_group3.setEnabled(false);
                        floatValueEntryField_group3.setEnabled(false);
                        charValueEntryField_group3.setEnabled(false);
                        objectReferenceEntryField_group3.setEnabled(false);
                    }
                }
            });
            add(object3Check);
            intValueEntryField_group3 = new JTextField();
            intValueEntryField_group3.setBounds(430, 130, 200, 50);
            intValueEntryField_group3.setBorder(borderIntField);
            intValueEntryField_group3.setEditable(true);
            add(intValueEntryField_group3);
            floatValueEntryField_group3 = new JTextField();
            floatValueEntryField_group3.setBounds(430, 190, 200, 50);
            floatValueEntryField_group3.setBorder(borderFloatField);
            floatValueEntryField_group3.setEditable(true);
            add(floatValueEntryField_group3);
            charValueEntryField_group3 = new JTextField();
            charValueEntryField_group3.setBounds(430, 250, 200, 50);
            charValueEntryField_group3.setBorder(borderCharField);
            charValueEntryField_group3.setEditable(true);
            add(charValueEntryField_group3);
            objectReferenceEntryField_group3 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group3.setBounds(430, 310, 200, 50);
            objectReferenceEntryField_group3.setBorder(borderRefField);
            add(objectReferenceEntryField_group3);

            createCompositeObjectButton = new JButton("Create Composite Object");
            createCompositeObjectButton.setBounds(10, 370, 200, 50);
            createCompositeObjectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int int1 = 0;
                    float float1 = 0.0f;
                    char char1 = 'a';
                    CompositeClass ref1 = null;
                    int int2 = 0;
                    float float2 = 0.0f;
                    char char2 = 'a';
                    CompositeClass ref2 = null;
                    int int3 = 0;
                    float float3 = 0.0f;
                    char char3 = 'a';
                    CompositeClass ref3 = null;

                    CompositeClass compositeObject1 = null;
                    CompositeClass compositeObject2 = null;
                    CompositeClass compositeObject3 = null;

                    //get data for object 1
                    try {
                        int1 = Integer.parseInt(intValueEntryField_group1.getText());
                        float1 = Float.parseFloat(floatValueEntryField_group1.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse values for object 1");
                        return;
                    }
                    if (charValueEntryField_group1.getText().length() > 1 ||
                        charValueEntryField_group1.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive of Object 1");
                            return;
                    } else {
                       char1 = charValueEntryField_group1.getText().charAt(0);
                    }
                    //createcompostie object 1
                    compositeObject1 = new CompositeClass(int1, float1, char1);
                    
                    //check and create object 2
                    if (object2Check.isSelected()) {
                        //get data for object 2
                        try {
                            int2 = Integer.parseInt(intValueEntryField_group2.getText());
                            float2 = Float.parseFloat(floatValueEntryField_group2.getText());
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Error could not parse values for object 2");
                            return;
                        }
                        if (charValueEntryField_group2.getText().length() > 1 ||
                            charValueEntryField_group2.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive of Object 2");
                                return;
                        } else {
                            char2 = charValueEntryField_group2.getText().charAt(0);
                        }
                        //create object 2
                        compositeObject2 = new CompositeClass(int2, float2, char2);
                    }

                    //check and create object 3
                    if (object3Check.isSelected()) {
                        //get data for object 3
                        try {
                            int3 = Integer.parseInt(intValueEntryField_group3.getText());
                            float3 = Float.parseFloat(floatValueEntryField_group3.getText());
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Error could not parse values for Object 3");
                            return;
                        }
                        if (charValueEntryField_group3.getText().length() > 1 ||
                            charValueEntryField_group3.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive of Object 3");
                                return;
                        } else {
                            char3 = charValueEntryField_group3.getText().charAt(0);
                        }
                        //create object 3
                        compositeObject3 = new CompositeClass(int3, float3, char3);
                    }

                    //set reference for object 1
                    if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object1")) {
                        compositeObject1.set_object_a1(compositeObject1);
                    } else if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object2") && 
                        object2Check.isSelected()) {
                           compositeObject1.set_object_a1(compositeObject2); 
                    } else if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object3") && 
                        object3Check.isSelected()) {
                           compositeObject1.set_object_a1(compositeObject3); 
                    } else {
                        compositeObject1.set_object_a1(null);
                    }

                    //set reference for object 2
                    if (object2Check.isSelected()) {
                        if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object1")) {
                            compositeObject2.set_object_a1(compositeObject1);
                        } else if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object2") && 
                            object2Check.isSelected()) {
                            compositeObject2.set_object_a1(compositeObject2); 
                        } else if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object3") && 
                            object3Check.isSelected()) {
                            compositeObject2.set_object_a1(compositeObject3); 
                        }else {
                            compositeObject2.set_object_a1(null);
                        }

                    }

                    //set reference for object 3
                    if (object3Check.isSelected()) {
                        if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object1")) {
                            compositeObject3.set_object_a1(compositeObject1);
                        } else if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object2") && 
                            object2Check.isSelected()) {
                            compositeObject3.set_object_a1(compositeObject2); 
                        } else if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object3") && 
                            object3Check.isSelected()) {
                            compositeObject3.set_object_a1(compositeObject3); 
                        } else {
                            compositeObject3.set_object_a1(null);
                        }
                    }

                    //serialize composite object 1
                    Document serializedObject = serializer.serialize(compositeObject1);

                    //send object to receiver
                    sender.sendDocument(receiverIP, receiverPort, serializedObject);
                }
            });
            createCompositeObjectButton.setEnabled(true);
            add(createCompositeObjectButton);

            readme = new JLabel("<html>Object 1 is the primary composite object that will always be serialized and so you must provide values for it.<br>" +
            "Object 1 consists of 3 primitve values and a 4th object reference value that can be set to refer to either itself or Objects 2 and 3<br>" +    
            "Objects 2 and 3 are optional composite objects of the same type as Object 1 that can be used as optional values to set Object 1's object reference value to.<br>" +
            "the reference values for Objects 2 and 3 can also be set to refere to one another or object 1</html>");
            readme.setBounds(10, 400, 800, 300);
            add(readme);

        }
    }

    public class PrimitiveArrayObjectCreator extends JPanel {

        public JTextField intValueEntryField1;
        public JTextField intValueEntryField2;
        public JTextField intValueEntryField3;
        public JTextField intValueEntryField4;
        public JTextField intValueEntryField5;
        public JTextField intValueEntryField6;
        public JTextField intValueEntryField7;
        public JTextField intValueEntryField8;
        public JTextField intValueEntryField9;
        public JTextField intValueEntryField10;

        public JButton createPrimitiveArrayObject;

        private Border borderIntField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "int value");

        PrimitiveArrayObjectCreator() {
            setLayout(null);

            intValueEntryField1 = new JTextField();
            intValueEntryField1.setBounds(10, 10, 200, 50);
            intValueEntryField1.setBorder(borderIntField);
            intValueEntryField1.setEditable(true);
            add(intValueEntryField1);

            intValueEntryField2 = new JTextField();
            intValueEntryField2.setBounds(10, 70, 200, 50);
            intValueEntryField2.setBorder(borderIntField);
            intValueEntryField2.setEditable(true);
            add(intValueEntryField2);

            intValueEntryField3 = new JTextField();
            intValueEntryField3.setBounds(10, 130, 200, 50);
            intValueEntryField3.setBorder(borderIntField);
            intValueEntryField3.setEditable(true);
            add(intValueEntryField3);

            intValueEntryField4 = new JTextField();
            intValueEntryField4.setBounds(10, 190, 200, 50);
            intValueEntryField4.setBorder(borderIntField);
            intValueEntryField4.setEditable(true);
            add(intValueEntryField4);

            intValueEntryField5 = new JTextField();
            intValueEntryField5.setBounds(10, 250, 200, 50);
            intValueEntryField5.setBorder(borderIntField);
            intValueEntryField5.setEditable(true);
            add(intValueEntryField5);

            intValueEntryField6 = new JTextField();
            intValueEntryField6.setBounds(10, 310, 200, 50);
            intValueEntryField6.setBorder(borderIntField);
            intValueEntryField6.setEditable(true);
            add(intValueEntryField6);

            intValueEntryField7 = new JTextField();
            intValueEntryField7.setBounds(10, 370, 200, 50);
            intValueEntryField7.setBorder(borderIntField);
            intValueEntryField7.setEditable(true);
            add(intValueEntryField7);
            
            intValueEntryField8 = new JTextField();
            intValueEntryField8.setBounds(10, 430, 200, 50);
            intValueEntryField8.setBorder(borderIntField);
            intValueEntryField8.setEditable(true);
            add(intValueEntryField8);

            intValueEntryField9 = new JTextField();
            intValueEntryField9.setBounds(10, 490, 200, 50);
            intValueEntryField9.setBorder(borderIntField);
            intValueEntryField9.setEditable(true);
            add(intValueEntryField9);

            intValueEntryField10 = new JTextField();
            intValueEntryField10.setBounds(10, 550, 200, 50);
            intValueEntryField10.setBorder(borderIntField);
            intValueEntryField10.setEditable(true);
            add(intValueEntryField10);

            createPrimitiveArrayObject = new JButton("Create Primitive Array Object");
            createPrimitiveArrayObject.setBounds(300, 10, 200, 50);
            createPrimitiveArrayObject.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int int1 = 0;
                    int int2 = 0;
                    int int3 = 0;
                    int int4 = 0;
                    int int5 = 0;
                    int int6 = 0;
                    int int7 = 0;
                    int int8 = 0;
                    int int9 = 0;
                    int int10 = 0;

                    try {
                        int1 = Integer.parseInt(intValueEntryField1.getText());
                        int2 = Integer.parseInt(intValueEntryField2.getText());
                        int3 = Integer.parseInt(intValueEntryField3.getText());
                        int4 = Integer.parseInt(intValueEntryField4.getText());
                        int5 = Integer.parseInt(intValueEntryField5.getText());
                        int6 = Integer.parseInt(intValueEntryField6.getText());
                        int7 = Integer.parseInt(intValueEntryField7.getText());
                        int8 = Integer.parseInt(intValueEntryField8.getText());
                        int9 = Integer.parseInt(intValueEntryField9.getText());
                        int10 = Integer.parseInt(intValueEntryField10.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Integer Value");
                        return;
                    }

                    //create primitive array object
                    int[] array = new int[] {int1, int2, int3, int4, int5, int6, int7, int8, int9, int10};
                    PrimitiveArrayClass primitiveArrayObject = new PrimitiveArrayClass(array);

                    //serialize object
                    Document serializedObject = serializer.serialize(primitiveArrayObject);

                    sender.sendDocument(receiverIP, receiverPort, serializedObject);
                }
            });
            createPrimitiveArrayObject.setEnabled(true);
            add(createPrimitiveArrayObject);
        }
    }

    public class CompositeArrayObjectCreator extends JPanel {
        public JLabel Object1Label;
        public JTextField intValueEntryField_group1;
        public JTextField floatValueEntryField_group1;
        public JTextField charValueEntryField_group1;
        public JComboBox<String> objectReferenceEntryField_group1;

        public JLabel Object2Label;
        public JTextField intValueEntryField_group2;
        public JTextField floatValueEntryField_group2;
        public JTextField charValueEntryField_group2;
        public JComboBox<String> objectReferenceEntryField_group2;

        public JLabel Object3Label;
        public JTextField intValueEntryField_group3;
        public JTextField floatValueEntryField_group3;
        public JTextField charValueEntryField_group3;
        public JComboBox<String> objectReferenceEntryField_group3;

        public JButton createCompositeArrayObjectButton;

        private Border borderIntField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "int value");
        private Border borderFloatField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "float value");
        private Border borderCharField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "char value");
        private Border borderRefField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "object reference");

        CompositeArrayObjectCreator() {
            setLayout(null);

            String[] objectRefOptions = {"null", "Object1", "Object2", "Object3"};

            Object1Label = new JLabel("Object 1:");
            Object1Label.setBounds(10, 10, 200, 50);
            add(Object1Label);
            intValueEntryField_group1 = new JTextField();
            intValueEntryField_group1.setBounds(10, 70, 200, 50);
            intValueEntryField_group1.setBorder(borderIntField);
            intValueEntryField_group1.setEditable(true);
            add(intValueEntryField_group1);
            floatValueEntryField_group1 = new JTextField();
            floatValueEntryField_group1.setBounds(10, 130, 200, 50);
            floatValueEntryField_group1.setBorder(borderFloatField);
            floatValueEntryField_group1.setEditable(true);
            add(floatValueEntryField_group1);
            charValueEntryField_group1 = new JTextField();
            charValueEntryField_group1.setBounds(10, 190, 200, 50);
            charValueEntryField_group1.setBorder(borderCharField);
            charValueEntryField_group1.setEditable(true);
            add(charValueEntryField_group1);
            objectReferenceEntryField_group1 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group1.setBounds(10,250, 200, 50);
            objectReferenceEntryField_group1.setBorder(borderRefField);
            add(objectReferenceEntryField_group1);

            Object2Label = new JLabel("Object 2:");
            Object2Label.setBounds(220, 10, 200, 50);
            add(Object2Label);
            intValueEntryField_group2 = new JTextField();
            intValueEntryField_group2.setBounds(220, 70, 200, 50);
            intValueEntryField_group2.setBorder(borderIntField);
            intValueEntryField_group2.setEditable(true);
            add(intValueEntryField_group2);
            floatValueEntryField_group2 = new JTextField();
            floatValueEntryField_group2.setBounds(220, 130, 200, 50);
            floatValueEntryField_group2.setBorder(borderFloatField);
            floatValueEntryField_group2.setEditable(true);
            add(floatValueEntryField_group2);
            charValueEntryField_group2 = new JTextField();
            charValueEntryField_group2.setBounds(220, 190, 200, 50);
            charValueEntryField_group2.setBorder(borderCharField);
            charValueEntryField_group2.setEditable(true);
            add(charValueEntryField_group2);
            objectReferenceEntryField_group2 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group2.setBounds(220,250, 200, 50);
            objectReferenceEntryField_group2.setBorder(borderRefField);
            add(objectReferenceEntryField_group2);

            Object3Label = new JLabel("Object 3:");
            Object3Label.setBounds(430, 10, 200, 50);
            add(Object3Label);
            intValueEntryField_group3 = new JTextField();
            intValueEntryField_group3.setBounds(430, 70, 200, 50);
            intValueEntryField_group3.setBorder(borderIntField);
            intValueEntryField_group3.setEditable(true);
            add(intValueEntryField_group3);
            floatValueEntryField_group3 = new JTextField();
            floatValueEntryField_group3.setBounds(430, 130, 200, 50);
            floatValueEntryField_group3.setBorder(borderFloatField);
            floatValueEntryField_group3.setEditable(true);
            add(floatValueEntryField_group3);
            charValueEntryField_group3 = new JTextField();
            charValueEntryField_group3.setBounds(430, 190, 200, 50);
            charValueEntryField_group3.setBorder(borderCharField);
            charValueEntryField_group3.setEditable(true);
            add(charValueEntryField_group3);
            objectReferenceEntryField_group3 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group3.setBounds(430,250, 200, 50);
            objectReferenceEntryField_group3.setBorder(borderRefField);
            add(objectReferenceEntryField_group3);

            createCompositeArrayObjectButton = new JButton("Create Composite Array Object");
            createCompositeArrayObjectButton.setBounds(10, 310, 250, 50);
            createCompositeArrayObjectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int int1 = 0;
                    float float1 = 0.0f;
                    char char1 = 'a';
                    CompositeClass ref1 = null;
                    int int2 = 0;
                    float float2 = 0.0f;
                    char char2 = 'a';
                    CompositeClass ref2 = null;
                    int int3 = 0;
                    float float3 = 0.0f;
                    char char3 = 'a';
                    CompositeClass ref3 = null;

                    CompositeClass compositeObject1 = null;
                    CompositeClass compositeObject2 = null;
                    CompositeClass compositeObject3 = null;

                    try {
                        int1 = Integer.parseInt(intValueEntryField_group1.getText());
                        int2 = Integer.parseInt(intValueEntryField_group2.getText());
                        int3 = Integer.parseInt(intValueEntryField_group3.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Integer Value");
                        return;
                    }

                    try {
                        float1 = Float.parseFloat(floatValueEntryField_group1.getText());
                        float2 = Float.parseFloat(floatValueEntryField_group2.getText());
                        float3 = Float.parseFloat(floatValueEntryField_group3.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Float Value");
                        return;
                    }

                    if (charValueEntryField_group1.getText().length() > 1 ||
                        charValueEntryField_group1.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive");
                            return;
                    } else {
                        char1 = charValueEntryField_group1.getText().charAt(0);
                    }
                    if (charValueEntryField_group2.getText().length() > 1 ||
                        charValueEntryField_group2.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive");
                            return;
                    } else {
                        char2 = charValueEntryField_group2.getText().charAt(0);
                    }
                    if (charValueEntryField_group3.getText().length() > 1 ||
                        charValueEntryField_group3.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive");
                            return;
                    } else {
                        char3 = charValueEntryField_group3.getText().charAt(0);
                    }

                    //create composite objects
                    compositeObject1 = new CompositeClass(int1, float1, char1);
                    compositeObject2 = new CompositeClass(int2, float2, char2);
                    compositeObject3 = new CompositeClass(int3, float3, char3);

                    //set references
                    if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object1")) {
                        compositeObject1.set_object_a1(compositeObject1);
                    } else if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object2")) {
                        compositeObject1.set_object_a1(compositeObject2); 
                    } else if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object3")) {
                        compositeObject1.set_object_a1(compositeObject3); 
                    } else {
                        compositeObject1.set_object_a1(null);
                    }
                    if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object1")) {
                        compositeObject2.set_object_a1(compositeObject1);
                    } else if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object2")) {
                        compositeObject2.set_object_a1(compositeObject2); 
                    } else if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object3")) {
                        compositeObject2.set_object_a1(compositeObject3); 
                    } else {
                        compositeObject2.set_object_a1(null);
                    }
                    if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object1")) {
                        compositeObject3.set_object_a1(compositeObject1);
                    } else if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object2")) {
                        compositeObject3.set_object_a1(compositeObject2); 
                    } else if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object3")) {
                        compositeObject3.set_object_a1(compositeObject3); 
                    } else {
                        compositeObject3.set_object_a1(null);
                    }

                    //create composite array object
                    CompositeClass[] compArray = new CompositeClass[] {compositeObject1, compositeObject2, compositeObject3};
                    CompositeArrayClass compArrayObject = new CompositeArrayClass(compArray);

                    //serialize composite object 1
                    Document serializedObject = serializer.serialize(compArrayObject);

                    //send object to receiver
                    sender.sendDocument(receiverIP, receiverPort, serializedObject);
                }
            });
            createCompositeArrayObjectButton.setEnabled(true);
            add(createCompositeArrayObjectButton);
        }
    }

    public class CollectionObjectCreator extends JPanel {
        public JLabel Object1Label;
        public JTextField intValueEntryField_group1;
        public JTextField floatValueEntryField_group1;
        public JTextField charValueEntryField_group1;
        public JComboBox<String> objectReferenceEntryField_group1;

        public JLabel Object2Label;
        public JTextField intValueEntryField_group2;
        public JTextField floatValueEntryField_group2;
        public JTextField charValueEntryField_group2;
        public JComboBox<String> objectReferenceEntryField_group2;

        public JLabel Object3Label;
        public JTextField intValueEntryField_group3;
        public JTextField floatValueEntryField_group3;
        public JTextField charValueEntryField_group3;
        public JComboBox<String> objectReferenceEntryField_group3;

        public JButton createCollectionObjectButton;

        private Border borderIntField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "int value");
        private Border borderFloatField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "float value");
        private Border borderCharField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "char value");
        private Border borderRefField = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "object reference");

        CollectionObjectCreator () {
            setLayout(null);

            String[] objectRefOptions = {"null", "Object1", "Object2", "Object3"};

            Object1Label = new JLabel("Object 1:");
            Object1Label.setBounds(10, 10, 200, 50);
            add(Object1Label);
            intValueEntryField_group1 = new JTextField();
            intValueEntryField_group1.setBounds(10, 70, 200, 50);
            intValueEntryField_group1.setBorder(borderIntField);
            intValueEntryField_group1.setEditable(true);
            add(intValueEntryField_group1);
            floatValueEntryField_group1 = new JTextField();
            floatValueEntryField_group1.setBounds(10, 130, 200, 50);
            floatValueEntryField_group1.setBorder(borderFloatField);
            floatValueEntryField_group1.setEditable(true);
            add(floatValueEntryField_group1);
            charValueEntryField_group1 = new JTextField();
            charValueEntryField_group1.setBounds(10, 190, 200, 50);
            charValueEntryField_group1.setBorder(borderCharField);
            charValueEntryField_group1.setEditable(true);
            add(charValueEntryField_group1);
            objectReferenceEntryField_group1 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group1.setBounds(10,250, 200, 50);
            objectReferenceEntryField_group1.setBorder(borderRefField);
            add(objectReferenceEntryField_group1);

            Object2Label = new JLabel("Object 2:");
            Object2Label.setBounds(220, 10, 200, 50);
            add(Object2Label);
            intValueEntryField_group2 = new JTextField();
            intValueEntryField_group2.setBounds(220, 70, 200, 50);
            intValueEntryField_group2.setBorder(borderIntField);
            intValueEntryField_group2.setEditable(true);
            add(intValueEntryField_group2);
            floatValueEntryField_group2 = new JTextField();
            floatValueEntryField_group2.setBounds(220, 130, 200, 50);
            floatValueEntryField_group2.setBorder(borderFloatField);
            floatValueEntryField_group2.setEditable(true);
            add(floatValueEntryField_group2);
            charValueEntryField_group2 = new JTextField();
            charValueEntryField_group2.setBounds(220, 190, 200, 50);
            charValueEntryField_group2.setBorder(borderCharField);
            charValueEntryField_group2.setEditable(true);
            add(charValueEntryField_group2);
            objectReferenceEntryField_group2 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group2.setBounds(220,250, 200, 50);
            objectReferenceEntryField_group2.setBorder(borderRefField);
            add(objectReferenceEntryField_group2);

            Object3Label = new JLabel("Object 3:");
            Object3Label.setBounds(430, 10, 200, 50);
            add(Object3Label);
            intValueEntryField_group3 = new JTextField();
            intValueEntryField_group3.setBounds(430, 70, 200, 50);
            intValueEntryField_group3.setBorder(borderIntField);
            intValueEntryField_group3.setEditable(true);
            add(intValueEntryField_group3);
            floatValueEntryField_group3 = new JTextField();
            floatValueEntryField_group3.setBounds(430, 130, 200, 50);
            floatValueEntryField_group3.setBorder(borderFloatField);
            floatValueEntryField_group3.setEditable(true);
            add(floatValueEntryField_group3);
            charValueEntryField_group3 = new JTextField();
            charValueEntryField_group3.setBounds(430, 190, 200, 50);
            charValueEntryField_group3.setBorder(borderCharField);
            charValueEntryField_group3.setEditable(true);
            add(charValueEntryField_group3);
            objectReferenceEntryField_group3 = new JComboBox<String>(objectRefOptions);
            objectReferenceEntryField_group3.setBounds(430,250, 200, 50);
            objectReferenceEntryField_group3.setBorder(borderRefField);
            add(objectReferenceEntryField_group3);

            createCollectionObjectButton = new JButton("Create Collection Object");
            createCollectionObjectButton.setBounds(10, 310, 200, 50);
            createCollectionObjectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int int1 = 0;
                    float float1 = 0.0f;
                    char char1 = 'a';
                    CompositeClass ref1 = null;
                    int int2 = 0;
                    float float2 = 0.0f;
                    char char2 = 'a';
                    CompositeClass ref2 = null;
                    int int3 = 0;
                    float float3 = 0.0f;
                    char char3 = 'a';
                    CompositeClass ref3 = null;

                    CompositeClass compositeObject1 = null;
                    CompositeClass compositeObject2 = null;
                    CompositeClass compositeObject3 = null;

                    try {
                        int1 = Integer.parseInt(intValueEntryField_group1.getText());
                        int2 = Integer.parseInt(intValueEntryField_group2.getText());
                        int3 = Integer.parseInt(intValueEntryField_group3.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Integer Value");
                        return;
                    }

                    try {
                        float1 = Float.parseFloat(floatValueEntryField_group1.getText());
                        float2 = Float.parseFloat(floatValueEntryField_group2.getText());
                        float3 = Float.parseFloat(floatValueEntryField_group3.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error could not parse Float Value");
                        return;
                    }

                    if (charValueEntryField_group1.getText().length() > 1 ||
                        charValueEntryField_group1.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive");
                            return;
                    } else {
                        char1 = charValueEntryField_group1.getText().charAt(0);
                    }
                    if (charValueEntryField_group2.getText().length() > 1 ||
                        charValueEntryField_group2.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive");
                            return;
                    } else {
                        char2 = charValueEntryField_group2.getText().charAt(0);
                    }
                    if (charValueEntryField_group3.getText().length() > 1 ||
                        charValueEntryField_group3.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Error: please provide a single character for the char primitive");
                            return;
                    } else {
                        char3 = charValueEntryField_group3.getText().charAt(0);
                    }

                    //create composite objects
                    compositeObject1 = new CompositeClass(int1, float1, char1);
                    compositeObject2 = new CompositeClass(int2, float2, char2);
                    compositeObject3 = new CompositeClass(int3, float3, char3);

                    //set references
                    if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object1")) {
                        compositeObject1.set_object_a1(compositeObject1);
                    } else if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object2")) {
                        compositeObject1.set_object_a1(compositeObject2); 
                    } else if (((String)objectReferenceEntryField_group1.getSelectedItem()).equals("Object3")) {
                        compositeObject1.set_object_a1(compositeObject3); 
                    } else {
                        compositeObject1.set_object_a1(null);
                    }
                    if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object1")) {
                        compositeObject2.set_object_a1(compositeObject1);
                    } else if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object2")) {
                        compositeObject2.set_object_a1(compositeObject2); 
                    } else if (((String)objectReferenceEntryField_group2.getSelectedItem()).equals("Object3")) {
                        compositeObject2.set_object_a1(compositeObject3); 
                    } else {
                        compositeObject2.set_object_a1(null);
                    }
                    if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object1")) {
                        compositeObject3.set_object_a1(compositeObject1);
                    } else if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object2")) {
                        compositeObject3.set_object_a1(compositeObject2); 
                    } else if (((String)objectReferenceEntryField_group3.getSelectedItem()).equals("Object3")) {
                        compositeObject3.set_object_a1(compositeObject3); 
                    } else {
                        compositeObject3.set_object_a1(null);
                    }

                    //create collections object
                    CompositeClass[] compArrayList = new CompositeClass[] {
                        compositeObject1,
                        compositeObject2,
                        compositeObject3
                    };
                    CollectionClass collectionObject = new CollectionClass(compArrayList);


                    //serialize object
                    Document serializedObject = serializer.serialize(collectionObject);

                    //send object to reciever
                    sender.sendDocument(receiverIP, receiverPort, serializedObject);
                }
            });
            createCollectionObjectButton.setEnabled(true);
            add(createCollectionObjectButton);
        }
    }
}
