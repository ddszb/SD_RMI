import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.io.FileReader;

public class Cliente {
    private Cliente() {}
    public static void main(String[] args) {
        System.out.println("-------------  Cliente  -----------\n\n");
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClienteServidor stub = (ClienteServidor) registry.lookup("ClienteServidor");
            //String resposta = stub.digaAloMundo();
            //System.out.println("resposta: " + resposta);
        

            Scanner input = new Scanner(System.in);
            System.out.println("Digite o nome do arquivo:");
            String nomeArquivo = input.nextLine();

            System.out.println("\nLeitura(1) ou Escrita(2)?");
            Scanner newInput = new Scanner(System.in);
            int op = newInput.nextInt();
            if(op == 1){
                System.out.println("passei aqui");
                String text = stub.readFile(nomeArquivo);
                System.out.println(text);
            }
            else if (op == 2){
                System.out.println("O que escrever?");
                String conteudo = input.nextLine();

                if(stub.writeFile(nomeArquivo,conteudo))
                    System.out.println("Escrita com sucesso");
                
            }


        }catch (NotBoundException | RemoteException e) {
            System.err.println("Capturando a exceção no Cliente: " + e.toString());
            }
    }
}