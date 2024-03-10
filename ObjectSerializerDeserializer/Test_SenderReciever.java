import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runners.MethodSorters;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test_SenderReciever {
    Thread recieverThread;
    Sender sender;
    Receiver receiver;

    @Before 
    public void initialize() {

        //start reciever server in a seperate thread
        /*Thread receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                receiver = new Receiver();
                receiver.start(4321);
            }
        });
        receiverThread.start();*/

        //or run Receiver in seperate terminal
    }

    @Test
    public void testA_basicDocumentTransfer() {

        sender = new Sender();
        Document sentDoc = buildJdomDoc();
        Document ackDoc = sender.sendDocument("127.0.0.1", 4321, sentDoc);

        assertEquals(new XMLOutputter().outputString(sentDoc), new XMLOutputter().outputString(ackDoc));
    }

    public Document buildJdomDoc() {
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
}