import java.net.*;
import java.util.Objects;
import java.io.*;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class Sender {

    private Socket serverSocket;
    private ObjectInputStream server_in;
    private ObjectOutputStream server_out;

    Sender() {
        serverSocket = null;
        server_in = null;
        server_out = null;
    }

    public Document sendDocument(String ip, int port, Document doc) {
        Document JdomDoc_ack = null;

        //establish connection to receiver server
        try {
            InetSocketAddress serverAddress = new InetSocketAddress(ip, port);
            serverSocket = new Socket();
            serverSocket.connect(serverAddress);

            //give feedback on connection establishment
            System.out.println("\nConnection to Reciever server established!\n");

            //DEV NOTE: object output stream must be instanitated before object input stream (see documenation)
            server_out = new ObjectOutputStream(serverSocket.getOutputStream());
            server_in = new ObjectInputStream(serverSocket.getInputStream());

            //send Jdom document object to receiver
            server_out.writeObject(doc);

            //wait for Receiver to finish reading sent document (Receiver will return document as ack)
            while (Objects.isNull(JdomDoc_ack)) {
                JdomDoc_ack = (Document) server_in.readObject();
            }

            
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        //document transfer to Reciever server successfull
        return JdomDoc_ack;
    }
}