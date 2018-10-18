import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.io.FileReader;
import java.util.Random;

public class Cliente {
    private Cliente() {}
    public static void main(String[] args) {
        System.out.println("-------------  Cliente  -----------\n\n");
        int idClient = Integer.parseInt(args[0]);               // Definição do id do cliente

        
        try {
            Registry registry = LocateRegistry.getRegistry(0);
            ClienteServidor stub = (ClienteServidor) registry.lookup("ClienteServidor");
            
            Random gerador = new Random(666);                   // Gerador de inteiros para as ações aleatórias           
            
            Scanner startEvent = new Scanner(System.in);
            System.out.println("Digite qualquer coisa para iniciar o cliente ");
            int startFlag = startEvent.nextInt();
            boolean toDo = true;
            int cont = 0;
            while(toDo){
                cont += 1;
                
                String arqNum = Integer.toString(gerador.nextInt(3));
                String arqnome = "arquivo" +arqNum + ".txt";    //
                System.out.println("Cliente: " + args[0]);
                //System.out.println("Cliente: 1");
                int op = gerador.nextInt(2) + 1;
                Thread.sleep(1000);
                if(op == 1){
                    System.out.println("Fazendo LEITURA em: "+ arqnome +"\n");
                    String text = stub.readFile(arqnome);
                    System.out.println("Texto lido: " + text);

                }
                else if (op == 2){
                        System.out.println("Fazendo ESCRITA em:" + arqnome + "\n");
                        String conteudo = "Cliente " + args[0] + "escreveu aqui.";
                        //String conteudo = "Cliente escreveu aqui na vez " + Integer.toString(cont);
                        if(stub.writeFile(arqnome, conteudo))
                            System.out.println("Escrita com sucesso");        
                }
                System.out.println("---------\n");
                if (cont == 10){ 
                    toDo = false;
                    System.out.println("Tudo finalizado! digite qualquer coisa para sair");
                    startFlag = startEvent.nextInt();
                }
                
                
            }
        }catch (NotBoundException | RemoteException | InterruptedException e ) {
            System.err.println("Capturando a exceção no Cliente: " + e.toString());
            }
    }
}