package br.com.gtheil.service;

import br.com.gtheil.bean.ChatMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class ClientService {
    private Socket socket; // socket do cliente, que recebe as informações a serem enviadas ao servidor
    private ObjectOutputStream output; // objeto de saída de dados para o envio de mensagens
    
    // Método de conexão ao servidor
    public Socket connect() {
        try {
            this.socket = new Socket("localhost", 5555); // cria um novo socket com estas informações de conexão (endereço IP e porta)
            this.output = new ObjectOutputStream(socket.getOutputStream()); // inicializa a variável de saída do socket recebido como parâmetro
        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return socket; // retorna o socket
    }
    
    public void send(ChatMessage message) {
        try {
            output.writeObject(message); // realiza o envio da mensagem ao servidor
        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
