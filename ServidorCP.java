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

public class ServidorCP implements ClienteServidor {

    private final Semaphore acessoArquivo1;
    private final Semaphore escritaArquivo1;
    private final Semaphore acessoArquivo2;
    private final Semaphore escritaArquivo2;
    private final Semaphore acessoArquivo3;
    private final Semaphore escritaArquivo3;

    public ServidorCP() {
        this.acessoArquivo1 = new Semaphore(3, false);
        this.acessoArquivo2 = new Semaphore(3, false);
        this.acessoArquivo3 = new Semaphore(3, false);
        this.escritaArquivo1 = new Semaphore(1, false);
        this.escritaArquivo2 = new Semaphore(1, false);
        this.escritaArquivo3 = new Semaphore(1, false);
    }


    public static void main(String args[]) {
        System.out.println("------------ Servidor Com Prioridade -----------\n\n");
        try {
            ServidorCP obj = new ServidorCP();
            ClienteServidor stub = (ClienteServidor) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("ClienteServidor", stub);
            System.out.println("Servidor CP pronto!");
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Capturando exceção no Servidor CP: " + e.toString());
        }
    }

    public String readFile(int numArq, int idClient) throws InterruptedException{
        System.out.println("Cliente " + idClient + " - [?] Pedido de leitura no arquivo " + numArq);
        String nomeArq = "arquivo" + Integer.toString(numArq) + ".txt";
        String linha;
        switch (numArq) {
            case 1:
                if(this.escritaArquivo1.availablePermits() != 1){
                    System.out.println("Cliente " + idClient + " - [X] foi bloqueado para leitura");
                }
                this.acessoArquivo1.acquire(1);
                break;
            case 2:
                if(this.escritaArquivo2.availablePermits() != 1){
                    System.out.println("Cliente " + idClient + " - [X] foi bloqueado para leitura");
                }
                this.acessoArquivo2.acquire(1);
                break;
            case 3:
                if(this.escritaArquivo3.availablePermits() != 1){
                    System.out.println("Cliente " + idClient + " - [X] foi bloqueado para leitura");
                }
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
                if (this.acessoArquivo1.availablePermits() != 3 || this.escritaArquivo1.availablePermits() != 1)
                    System.out.println("Cliente " + idClient + " - [X] foi bloqueado para escrita");
                this.acessoArquivo1.acquire(3);
                this.escritaArquivo1.acquire();
                break;
            case 2:
                if (this.acessoArquivo2.availablePermits() != 3 || this.escritaArquivo2.availablePermits() != 1)
                    System.out.println("Cliente " + idClient + " - [X] foi bloqueado para escrita");
                this.acessoArquivo2.acquire(3);
                this.escritaArquivo2.acquire();
                break;
            case 3:
                if (this.acessoArquivo3.availablePermits() != 3 || this.escritaArquivo3.availablePermits() != 1)
                    System.out.println("Cliente " + idClient + " - [X] foi bloqueado para escrita");
                this.acessoArquivo3.acquire(3);
                this.escritaArquivo3.acquire();
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
                this.escritaArquivo1.release();
                this.acessoArquivo1.release(3);
                break;
            case 2:
                this.escritaArquivo2.release();
                this.acessoArquivo2.release(3);
                break;
            case 3:
                this.escritaArquivo3.release();
                this.acessoArquivo3.release(3);
        }
        System.out.println("Cliente " + idClient + " - [!] Escrita no arquivo" + numArq + " bem sucedida:''"+ conteudo + "''");
        return true;
    }
}