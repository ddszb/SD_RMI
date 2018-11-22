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
import java.util.concurrent.Semaphore;

public class ServidorSP implements ClienteServidor {

    private final Semaphore acessoArquivo1;
    private final Semaphore acessoArquivo2;
    private final Semaphore acessoArquivo3;

    public ServidorSP() {
        this.acessoArquivo1 = new Semaphore(3, true);
        this.acessoArquivo2 = new Semaphore(3, true);
        this.acessoArquivo3 = new Semaphore(3, true);
    }


    public static void main(String args[]) {
        System.out.println("------------ Servidor Sem Prioridade -----------\n\n");
        try {
            ServidorSP obj = new ServidorSP();
            ClienteServidor stub = (ClienteServidor) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("ClienteServidor", stub);
            System.out.println("Servidor SP pronto!");
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Capturando exceção no Servidor SP: " + e.toString());
        }
    }

    public String readFile(int numArq, int idClient) throws InterruptedException{
        System.out.println("Cliente " + idClient + " - [?] Pedido de leitura no arquivo " + numArq);
        String nomeArq = "arquivo" + Integer.toString(numArq) + ".txt";
        String linha;
        switch (numArq) {
            case 1:
                this.acessoArquivo1.acquire(1);
                break;
            case 2:
                this.acessoArquivo2.acquire(1);
                break;
            case 3:
                this.acessoArquivo3.acquire(1);
                break;
        }
        Thread.sleep(5000);
        try {
            FileReader arq = new FileReader(nomeArq);
            BufferedReader lerArq = new BufferedReader(arq);
            linha = lerArq.readLine();
            arq.close();
        } catch (IOException e) {
            System.out.println("Cliente " + idClient + " - Leitura no arquivo" + numArq + " deu erro: " + e.toString());
            return "Erro na abertura do arquivo ";
        }
        switch (numArq) {
            case 1:
                this.acessoArquivo1.release();
                break;
            case 2:
                this.acessoArquivo2.release();
                break;
            case 3:
                this.acessoArquivo3.release();
                break;
        }
        System.out.println("Cliente " + idClient + " - [!] Leitura no arquivo" + numArq + " realizada! Lido: " + linha);
        return linha;
    }

    public boolean writeFile(int numArq, int idClient, String conteudo) throws InterruptedException {
        System.out.println("Cliente " + idClient + " - [?] Pedido de escrita no arquivo " + numArq);
        String nomeArq = "arquivo" + Integer.toString(numArq) + ".txt";
        switch (numArq) {
            case 1:
                this.acessoArquivo1.acquire(3);
                System.out.println("Cliente " + idClient + " iniciou a escrita na arquivo " + numArq);
                break;
            case 2:
                this.acessoArquivo2.acquire(3);
                System.out.println("Cliente " + idClient + " iniciou a escrita na arquivo " + numArq);
                break;
            case 3:
                this.acessoArquivo3.acquire(3);
                System.out.println("Cliente " + idClient + " iniciou a escrita na arquivo " + numArq);
                break;
        }
        Thread.sleep(10000);
        try {
            BufferedWriter buffWrite = new BufferedWriter(new FileWriter(nomeArq));
            buffWrite.append(conteudo);
            buffWrite.close();
        } catch (IOException e) {
            System.out.println("Cliente " + idClient + " - Escrita no arquivo" + numArq + " deu erro: " + e.toString());
            return false;
        }
        switch (numArq) {
            case 1:
                this.acessoArquivo1.release(3);
                break;
            case 2:
                this.acessoArquivo2.release(3);
                break;
            case 3:
                this.acessoArquivo3.release(3);
        }
        System.out.println("Cliente " + idClient + " - [!] Escrita no arquivo" + numArq + " bem sucedida:''"+ conteudo + "''");
        return true;
    }
}