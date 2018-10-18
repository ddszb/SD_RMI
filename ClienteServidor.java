import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteServidor extends Remote {
	String digaAloMundo() throws RemoteException;

	boolean writeFile(String arquivo, String texto) throws RemoteException, InterruptedException;

	String readFile(String arquivo) throws RemoteException, InterruptedException;
 
}