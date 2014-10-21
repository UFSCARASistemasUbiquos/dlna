/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosistemasubiquos;


/**
 *
 * @author elder
 */
public class SBT implements Runnable
{
    ServiceHandler sh;
    public SBT(String entradaXml)
    {
        sh = new ServiceHandler("SwitchPower", "GetName", "INPUTANDRETURN", entradaXml,"NewTargetValue","ResultName");
    }
    @Override
    public void run()
    {        
        sh.findAndExecute();
        while(!sh.finished);
        System.out.println("Fim da execução da ação. Retorno: " +  sh.returnValue);
        devolveRetorno();
        System.out.println("Entrou na rotina de retornar");
    }

    private void devolveRetorno()
    {
        ServiceHandler novo = new ServiceHandler("Callback", "SetRetorno", "INPUT", sh.returnValue, "EntradaXml","empty");
        novo.findAndExecute(); 
    }
    
}
