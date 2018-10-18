import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Servidor implements ClienteServidor {
    private final Semaphore disponivel;
    public Servidor() {
        this.disponivel = new Semaphore(1, true);
    }
    public String digaAloMundo() {
        System.out.println("Chamada de aplicacao Cliente recebida!");
        return "Agora nao tem desculpa. Tu jah sabe RMI. Bora programar! Beleza?";
    }
    public static void main(String args[]) {
        System.out.println("------------ Servidor -----------\n\n");
        try {
            Servidor obj = new Servidor();
            ClienteServidor stub = (ClienteServidor) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("ClienteServidor", stub);
            System.out.println("Servidor pronto!");
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Capturando exceção no Servidor: " + e.toString());
        }
    }

    public String readFile(String arquivo) throws InterruptedException{
        try {
            this.disponivel.acquire();
            FileReader arq = new FileReader(arquivo);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine(); 
            arq.close();
            System.out.println("Linha lida com sucesso:" + linha);
            this.disponivel.release();
            return new String(linha);
        } catch (IOException e) {
            return "Erro na abertura do arquivo ";
        }
    }

    public boolean writeFile(String arq, String conteudo) throws InterruptedException {
    System.out.println("Chamada de escrita de arquivo! Escrevendo: " + conteudo);
    try {
        this.disponivel.acquire();
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(arq));
        buffWrite.append(conteudo);
        buffWrite.close();
        this.disponivel.release();
        return true;
    } catch (IOException e) {
        return false;
        }
    }
}