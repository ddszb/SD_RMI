import java.io.BufferedReader;
import java.io.IOException;
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
        int idClient = Integer.parseInt(args[0]);               // Definição do id do cliente
        System.out.println("-------------  Cliente " + idClient + "  -----------\n\n");
        Scanner startEvent = new Scanner(System.in);
        
        try {
            Registry registry = LocateRegistry.getRegistry(0);
            ClienteServidor stub = (ClienteServidor) registry.lookup("ClienteServidor");
            
            Random gerador = new Random(666);                   // Gerador de inteiros para as ações aleatórias
            
            int[] acoes = gerarAcoes(args[1]);

            boolean toDo = true;
            int cont = 0;
            while(toDo){
                // int op = gerador.nextInt(2) + 1;
                int op = acoes[cont];
                cont += 1;
                
                int arqNum = gerador.nextInt(3);
                // int arqNum = 1;
                String arqnome = "arquivo" + arqNum + ".txt";    //
                //System.out.println("Cliente: " + idClient);
                if(op == 1){
                    System.out.println("Fazendo LEITURA em: "+ arqnome +"\n");
                    String text = stub.readFile(arqNum, idClient);
                    System.out.println("Texto lido: " + text);

                }
                else if (op == 2){
                        System.out.println("Fazendo ESCRITA em: " + arqnome + "\n");
                        String conteudo = "Cliente " + idClient + " escreveu aqui.";
                        //String conteudo = "Cliente escreveu aqui na vez " + Integer.toString(cont);
                        if(stub.writeFile(arqNum, idClient, conteudo))
                            System.out.println("Escrita com sucesso");        
                }
                System.out.println("---------\n");
                if (cont == 10){ 
                    toDo = false;
                    System.out.println("Tudo finalizado! digite qualquer coisa para sair");
                    startEvent.nextLine();
                }
                
                
            }
        }catch (NotBoundException | RemoteException | InterruptedException e ) {
            System.err.println("Capturando a exceção no Cliente: " + e.toString());
            startEvent.nextLine();
        }
    startEvent.close();
    }

    private static int[] gerarAcoes(String nomeArqAcoes) {
        int[] acoes = new int[10];
        try {
            FileReader arq = new FileReader(nomeArqAcoes);
            BufferedReader lerArq = new BufferedReader(arq);
            for(int i = 0; i < 10; i++) {
                String linha = lerArq.readLine();
                acoes[i] = Integer.parseInt(linha);
            }
            arq.close();
        } catch (IOException e) {
            System.out.println("Não consegui abrir o arquivo de ações" + e.toString());
        }
        return acoes;
    }
}