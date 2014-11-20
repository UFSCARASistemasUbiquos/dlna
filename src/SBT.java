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
public class SBT implements Runnable
{
    ServiceHandler sh;
    String clienteCallback;
    public SBT(String entradaXml)
    {
        //sh = new ServiceHandler("SwitchPower", "GetName", "INPUTANDRETURN", entradaXml,"NewTargetValue","ResultName");
        //sh = new ServiceHandler("JanelaAutomatica", "MotorControl", "LigarMotor", "VOID", "  ","  ","  ");
        //sh = new ServiceHandler("JanelaAutomatica", "MotorControl", "SetVelocidade", "INPUTANDRETURN", "90","NewTargetValue","ResultVelocidade");
        sh = new ServiceHandler(entradaXml);
        clienteCallback = "cliente1";
    }
    @Override
    public void run()
    {        
        sh.findAndExecute();
        while(!sh.finished);
        System.out.println("Fim da execução da ação.");
        if(sh.returnValue != null)
            System.out.println("Retorno: " +  sh.returnValue);
        devolveRetorno();
        System.out.println("Entrou na rotina de retornar");
    }

    private void devolveRetorno()
    {
        //ServiceHandler novo = new ServiceHandler(("Callback" + clienteCallback), "Callback", "SetRetorno", "INPUT", sh.returnValue, "EntradaXml","empty");
        Requisicao xmlResposta = new Requisicao();
        xmlResposta.setDeviceName("Callback" + clienteCallback);
        xmlResposta.setServiceName("Callback");
        xmlResposta.setOperationName("SetRetorno");
        if(sh.returnValue != null)
        {
            Argumento a1 = new Argumento();
            a1.setNomeArgumento("EntradaXml");
            a1.setValorArgumento(sh.returnValue);
            List<Argumento> lInput = new ArrayList<Argumento>();
            lInput.add(a1);
            xmlResposta.setArgumentosInput(lInput);
        }
        ServiceHandler novo = new ServiceHandler(xmlResposta.toXML());
        novo.findAndExecute(); 
    }
    
}
