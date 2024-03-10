import java.net.*;
import java.io.*;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class Receiver {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream client_out;
    private ObjectInputStream client_in;

    private Deserializer deserializer;
    private Inspector inspector;

    Receiver() {
        serverSocket = null;
        clientSocket = null;
        client_in = null;

        deserializer = new Deserializer();
        inspector = new Inspector();
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Must provide the port number of Receiver");
            return;
        }

        int receiver_portNumber = Integer.parseInt(args[0]);

        Receiver receiverServer = new Receiver();
        receiverServer.start(receiver_portNumber);
    }

    public void start(int portNum) {
        try {
            //initialize server socket
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 4321));

            while(true) {
                System.out.println("\nAwaiting client connection...\n");
                clientSocket = serverSocket.accept();
                System.out.println("\nConnected to Client!\n");

                client_out = new ObjectOutputStream(clientSocket.getOutputStream());
                client_in = new ObjectInputStream(clientSocket.getInputStream());

                //recieve Jdom document object from client Sender
                Document JdomDoc = null;
                JdomDoc = (Document) client_in.readObject();

                //acknowledge receival of Jdom document object by returning it to client 
                client_out.writeObject(JdomDoc);

                //deserialize object
                Object obj = deserializer.deserialize(JdomDoc);

                //inspect object
                inspector.inspect(obj, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
