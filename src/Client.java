/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosistemasubiquos;

import java.util.ArrayList;
import java.util.List;
import projetosistemasubiquos.type.Argumento;
import projetosistemasubiquos.type.Requisicao;

/**
 *
 * @author elder
 */
public class Client
{
    public static void main(String[] args) throws Exception
    {
        //(new ServiceHandler("BinaryLight", "SwitchPower", "SetTarget", "INPUT", "true", "NewTargetValue", "aiai")).findAndExecute();
        //(new ServiceHandler("BinaryLight", "SwitchPower", "GetName", "INPUTANDRETURN", "AAAA", "NewTargetValue", "ResultName")).findAndExecute();
        
        
        Thread serverThread = new Thread(new CallbackServer("cliente1"));
        serverThread.setDaemon(false);
        serverThread.start();
        Thread.sleep(1000); // Aguarda 1 segundo para garantir que está sendo levantado o serviço que aguarda resposta
        
        //String s = "<RequisicaoServiceBrocker>     <DeviceTarget>    UUID     </DeviceTarget>     <DeviceName>    NomeDispositivo    </DeviceName>     <ServiceName>    MotorControl    </ServiceName>     <ArgumentosInput>        <NomeArgumento>        LigarMotor        </NomeArgumento>        <TipoArgumento>        Tipo        </TipoArgumento>        <ValorArgumento>        Valor        </ValorArgumento>            </ArgumentosInput>     <ArgumentosOutput>        <NomeArgumento>        Nome        </NomeArgumento>        <TipoArgumento>        Tipo        </TipoArgumento>        <ValorArgumento>        Valor        </ValorArgumento>    </ArgumentosOutput>        <Escalonamento>        <Prioridade>        Prioridade(1...9)        </Prioridade>        <TipoEscalonamento>        MK/Soft        </TipoEscalonamento>        <M>        M            </M>        <K>        K            </K>        <Deadline>        Deadline(ms)        </Deadline>    </Escalonamento></RequisicaoServiceBrocker>";
        Requisicao xml = new Requisicao();
        xml.setDeviceName("JanelaAutomatica");
        xml.setServiceName("MotorControl");
        //xml.setOperationName("LigarMotor");
        
        xml.setOperationName("SetVelocidade");
        Argumento a1 = new Argumento();
        a1.setNomeArgumento("NewTargetValue");
        a1.setValorArgumento("90");
        List<Argumento> lInput = new ArrayList<Argumento>();
        lInput.add(a1);
        xml.setArgumentosInput(lInput);
        
        Argumento a2 = new Argumento();
        a2.setNomeArgumento("ResultVelocidade");
        List<Argumento> lOutput = new ArrayList<Argumento>();
        lOutput.add(a2);
        xml.setArgumentosOutput(lOutput);
        
        
        //--------------
        Requisicao xmlSB = new Requisicao();
        xmlSB.setDeviceName("ServiceBrocker");
        xmlSB.setServiceName("ServiceBrocker");
        xmlSB.setOperationName("GetService");
        Argumento a3 = new Argumento();
        a3.setNomeArgumento("EntradaXml");
        a3.setValorArgumento(xml.toXML()); //incluindo xml principal como entrada no xml para Service Brocker
        List<Argumento> lInput2 = new ArrayList<Argumento>();
        lInput2.add(a3);
        xmlSB.setArgumentosInput(lInput2);
        
        
        
        //Realiza a busca e requisçao do serviço GetService do ServiceBrocker. A entrada, chamada "EntradaXml" deve ser o arquivo XML que contem as informaçoes do servico
        //(new ServiceHandler("ServiceBrocker", "ServiceBrocker", "GetService", "INPUT", "SwitchPower", "EntradaXml", "empty")).findAndExecute();
        //(new ServiceHandler("ServiceBrocker", "ServiceBrocker", "GetService", "INPUT", xml2.toXML(), "EntradaXml", "")).findAndExecute();
        (new ServiceHandler(xmlSB.toXML())).findAndExecute();
    }
}
