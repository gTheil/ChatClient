package br.com.gtheil.frame;

import br.com.gtheil.bean.ChatMessage;
import br.com.gtheil.bean.ChatMessage.Action;
import br.com.gtheil.service.ClientService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Felipe
 */
public class ClientInterface extends javax.swing.JFrame {

    private Socket socket; // variável que contém o socket
    private ChatMessage message; // variável que contém a mensagem a ser enviada
    private ClientService service; // instancia o ClientService

    /**
     * Creates new form ClientInterface
     */
    public ClientInterface() {
        initComponents();
    }

    // implementa interface Runnable para que possa ser executada por threads
    private class ListenerSocket implements Runnable {

        private ObjectInputStream input; // objeto de entrada de dados

        public ListenerSocket(Socket socket) { // socket que fica na escuta
            try {
                this.input = new ObjectInputStream(socket.getInputStream()); // Inicializa a variável de entrada do socket recebido como parâmetro
            } catch (IOException ex) {
                Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Método executado por cada thread
        @Override
        public void run() {
            ChatMessage message = null; // variável que recebe a mensagem recebida pelo ouvinte
            try {
                while ((message = (ChatMessage) input.readObject()) != null) {
                    ChatMessage.Action action = message.getAction(); // instância de um objeto da classe Action, que recebe a ação da mensagem recebida pelo ouvinte

                    if (action.equals(ChatMessage.Action.CONNECT)) { // caso seja um pedido de conexão
                        connect(message); // é chamado o método de conexão
                    } else if (action.equals(ChatMessage.Action.DISCONNECT)) { // caso seja um pedido de desconexão do chat
                        disconnect();// é chamado o método de desconexão
                        socket.close(); // desconecta do servidor o socket do cliente
                    } else if (action.equals(ChatMessage.Action.SEND_ONE)) { // caso seja recebida um mensagem
                        receive(message); // é chamado o método de recebimento de mensagem
                    } else if (action.equals(ChatMessage.Action.USERS_ONLINE)) { // caso seja um pedido da lista de usuários online
                        refreshUsers(message); // é chamado o método que atualiza a lista de usuários online
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    // Método de conexão
    private void connect(ChatMessage message) {
        if (message.getText().equals("NO")) { // caso a conexão seja recusada
            this.nameText.setText(""); // limpa o campo de texto da área de conexão na interface
            JOptionPane.showMessageDialog(this, "Conexão recusada, tente novamente com outro nome.");
            return; // força a saída do método
        } else if (message.getText().equals("YES")) { // caso a conexão seja aceita
            this.message = message; // atribui à variável message o valor da mensagem recebida como parâmetro
            this.connBtn.setEnabled(false); // desativa o botão Conectar
            this.nameText.setEditable(false); // desativa interação com o campo de texto da área de conexão na interface
            this.dcBtn.setEnabled(true); // ativa o botão Desconectar
            this.messageText.setEnabled(true); // ativa o campo de texto de mensagem
            this.chatText.setEnabled(true); // ativa o campo de texto de chat
            this.sendBtn.setEnabled(true); // ativa o botão Enviar
            this.clearBtn.setEnabled(true); // ativa o botão Limpar

            JOptionPane.showMessageDialog(this, "Conexão realizada com sucesso!");
        }
    }

    // Método de desconexão
    private void disconnect() {
        this.connBtn.setEnabled(true); // habilita o botão Conectar
        this.nameText.setEditable(true); // habilita interação com o campo de texto da área de conexão na interface
        this.dcBtn.setEnabled(false); // desativa o botão Desconectar
        this.messageText.setEnabled(false); // desativa o campo de texto de mensagem
        this.chatText.setEnabled(false); // desativa o campo de texto de chat
        this.sendBtn.setEnabled(false); // desativa o botão Enviar
        this.clearBtn.setEnabled(false); // desativa o botão Limpar
        this.chatText.setText(""); // limpa o campo de texto do chat
        this.messageText.setText(""); // limpa o campo de texto da mensagem

        JOptionPane.showMessageDialog(this, "Conexão terminada com sucesso!");
    }

    // Método de recebimento de mensagens
    private void receive(ChatMessage message) {
        this.chatText.append(message.getName() + ": " + message.getText() + "\n"); // adiciona a mensagem recebida ao campo de texto de mensagens na interface
    }

    private void refreshUsers(ChatMessage message) {
        System.out.println(message.getSetOnline().toString());
        Set<String> setNames = message.getSetOnline(); // variável que contém a lista de usuários online
        
        setNames.remove((String) message.getName()); // remove o nome do próprio cliente da lista de usuários
        
        String[] arrayNames = (String[])setNames.toArray(new String[setNames.size()]); // converte a lista para formato array para ser lido pela interface
        
        this.listUsers.setListData(arrayNames); // seta a lista de usuários
        this.listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // faz com que somente um nome na lista possa ser selecionado por mês
        this.listUsers.setLayoutOrientation(JList.VERTICAL); // faz com que a lista exibe os nomes verticalmente
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        nameText = new javax.swing.JTextField();
        connBtn = new javax.swing.JButton();
        dcBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listUsers = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatText = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageText = new javax.swing.JTextArea();
        sendBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conexão"));

        connBtn.setText("Conectar");
        connBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connBtnActionPerformed(evt);
            }
        });

        dcBtn.setText("Desconectar");
        dcBtn.setEnabled(false);
        dcBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dcBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(nameText, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(connBtn)
                .addGap(18, 18, 18)
                .addComponent(dcBtn)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(nameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(connBtn)
                .addComponent(dcBtn))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuários"));

        jScrollPane3.setViewportView(listUsers);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chatText.setEditable(false);
        chatText.setColumns(20);
        chatText.setRows(5);
        chatText.setEnabled(false);
        jScrollPane1.setViewportView(chatText);

        messageText.setColumns(20);
        messageText.setRows(5);
        messageText.setEnabled(false);
        jScrollPane2.setViewportView(messageText);

        sendBtn.setText("Enviar");
        sendBtn.setEnabled(false);
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        clearBtn.setText("Limpar");
        clearBtn.setEnabled(false);
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(sendBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clearBtn)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendBtn)
                    .addComponent(clearBtn))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connBtnActionPerformed
        String name = nameText.getText(); // variável que recebe o nome digitado no campo de texto de conexão na interface

        // caso name não esteja vazia
        if (!name.isEmpty()) {
            this.message = new ChatMessage(); // cria nova instância do objeto ChatMessage
            this.message.setAction(Action.CONNECT); // cria uma mensagem com ação de conexão
            this.message.setName(name); // aribui à variável name o nome que foi digitado na interface

            this.service = new ClientService(); // instancia um novo objeto da classe ClientService
            this.socket = this.service.connect(); // conecta o socket com as informações especificadas no método connect() da classe ClientService

            new Thread(new ListenerSocket(this.socket)).start(); // inicializa uma nova thread com o socket ouvinte

            this.service.send(message); // envia mensagem com requisição de conexão
        }
    }//GEN-LAST:event_connBtnActionPerformed

    private void dcBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dcBtnActionPerformed
        ChatMessage message = new ChatMessage(); // cria nova instância do objeto ChatMessage
        message.setName(this.message.getName()); // cria uma mensagem com o nome do cliente
        message.setAction(Action.DISCONNECT); // seta a ação como de desconexão
        this.service.send(message); // envia mensagem com requisição de desconexão
        disconnect(); // desconecta o socket
    }//GEN-LAST:event_dcBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearBtnActionPerformed

    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed
        String text = this.messageText.getText(); // variável que contém a mensagem digitada pelo cliente
        String name = this.message.getName(); // variável que contém o nome do cliente que digitou a mensagem
        
        this.message = new ChatMessage(); // nova instancia do objeto ChatMessage
        
        if(this.listUsers.getSelectedIndex() > -1) { // verifica se algum item da lista de usuários está selecionado
            this.message.setNamePrivate((String) this.listUsers.getSelectedValue()); // seta o nome do usuário selecionado, convertido ao formato String, na mensagem
            this.message.setAction(Action.SEND_ONE); // seta a ação para ser um envio a um usuário
            this.listUsers.clearSelection(); // limpa a seleção de usuários
        } else {
            this.message.setAction(Action.SEND_ALL); // seta a ação da mensagem para ser um envio a todos os usuários
        }
        
        if(!text.isEmpty()) { // caso texto tenha sido digitado
            this.message.setName(name); // atribui o nome do usuário à mensagem
            this.message.setText(text); // atribui o texto digitado à mensagem

            this.chatText.append("Você: " + text + "\n"); // mostra no chat do cliente a mensagem que ele digitou
            
            this.service.send(this.message); // envia a mensagem
        }
        
        this.messageText.setText(""); // limpa o campo de texto de envio
    }//GEN-LAST:event_sendBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatText;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton connBtn;
    private javax.swing.JButton dcBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> listUsers;
    private javax.swing.JTextArea messageText;
    private javax.swing.JTextField nameText;
    private javax.swing.JButton sendBtn;
    // End of variables declaration//GEN-END:variables
}
