/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicebroker;

import javax.swing.JOptionPane;

/**
 *
 * @author arch
 */
public class ServiceBroker {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String valor = "";
        
        ServiceBrokerServer server = new ServiceBrokerServer();
        server.run();
        
        for(;;){
            valor = JOptionPane.showInputDialog(null,"Digite um Valor"); 
            ServiceBrokerClient client = new ServiceBrokerClient();
            client.valor = Integer.parseInt(valor);
            client.dispositivoPorTipo("SwitchPowerTest2");
        }
        
        // TODO code application logic here
    }
}
