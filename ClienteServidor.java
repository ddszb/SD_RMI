import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteServidor extends Remote {

	boolean writeFile(int numArq, int idClient, String conteudo) throws RemoteException, InterruptedException;

	String readFile(int arquivo, int idClient) throws RemoteException, InterruptedException;
 
}