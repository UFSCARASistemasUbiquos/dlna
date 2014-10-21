/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosistemasubiquos;

/**
 *
 * @author elder
 */
public class Client
{
    public static void main(String[] args) throws Exception
    {
        //(new ServiceHandler("SwitchPower", "SetTarget", "INPUT", "true", "NewTargetValue", "aiai")).findAndExecute();
        //(new ServiceHandler("SwitchPower", "GetName", "INPUTANDRETURN", "AAAA", "NewTargetValue", "ResultName")).findAndExecute();
        
        Thread serverThread = new Thread(new CallbackServer());
        serverThread.setDaemon(false);
        serverThread.start();
        Thread.sleep(1000); // Aguarda 1 segundo para garantir que está sendo levantado o serviço que aguarda resposta
        
        
        //Realiza a busca e requisçao do serviço GetService do ServiceBrocker. A entrada, chamada "EntradaXml" deve ser o arquivo XML que contem as informaçoes do servico
        (new ServiceHandler("ServiceBrocker", "GetService", "INPUT", "SwitchPower", "EntradaXml", "empty")).findAndExecute();
        
    }
}
