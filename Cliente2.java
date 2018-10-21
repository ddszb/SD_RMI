import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.io.FileReader;
import java.util.Random;

public class Cliente2 {

    private Cliente2() {}

    public static void main(String[] args) {
        int idClient = Integer.parseInt(args[0]);               // Definição do id do cliente
        System.out.println("-------------  Cliente2 " + idClient + "  -----------\n\n");
        Scanner startEvent = new Scanner(System.in);
        
        try {
            Registry registry = LocateRegistry.getRegistry(0);
            ClienteServidor stub = (ClienteServidor) registry.lookup("ClienteServidor");
            
            Thread.sleep(1000);

            leitura(stub, idClient);
            leitura(stub, idClient);
            leitura(stub, idClient);
            leitura(stub, idClient);
            leitura(stub, idClient);

            System.out.println("---------\n");
            System.out.println("Tudo finalizado! digite qualquer coisa para sair");
            startEvent.nextLine();
                
        }catch (NotBoundException | RemoteException | InterruptedException e ) {
            System.err.println("Capturando a exceção no Cliente: " + e.toString());
            startEvent.nextLine();
        }
        startEvent.close();
    }

    public static void leitura(ClienteServidor stub, int idClient) throws RemoteException, InterruptedException {
        int arqNum = 1;
        String arqnome = "arquivo" + arqNum + ".txt";    //
        System.out.println("Fazendo LEITURA em: "+ arqnome +"\n");
        String text = stub.readFile(arqNum, idClient);
        System.out.println("Texto lido: " + text);
    }

    public static void escrita(ClienteServidor stub, int idClient) throws RemoteException, InterruptedException {
        int arqNum = 1;
        String arqnome = "arquivo" + arqNum + ".txt";    //
        System.out.println("Fazendo ESCRITA em: " + arqnome + "\n");
        String conteudo = "Cliente " + idClient + " escreveu aqui.";
        //String conteudo = "Cliente escreveu aqui na vez " + Integer.toString(cont);
        if(stub.writeFile(arqNum, idClient, conteudo))
            System.out.println("Escrita com sucesso");
    }

}