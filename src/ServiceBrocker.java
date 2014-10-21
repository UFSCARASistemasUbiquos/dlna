/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosistemasubiquos;

/**
 *
 * @author elder
 */
import org.teleal.cling.binding.annotations.*;
//import org.teleal.cling.UpnpService;

@UpnpService(serviceId =
@UpnpServiceId("ServiceBrocker"),
serviceType =
@UpnpServiceType(value = "ServiceBrocker", version = 1))
public class ServiceBrocker
{

    private static String file1 = "src/projetosistemasubiquos/RequisicaoServiceBrocker.xml";
    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;
    @UpnpStateVariable(defaultValue = "idle")
    private String service;
    

    @UpnpAction(out =
    @UpnpOutputArgument(name = "ResultStatus"))
    public boolean getStatus()
    {
        return status;
    }

    @UpnpAction
    public void getService(@UpnpInputArgument(name = "EntradaXml") String entradaXml)
    {
        new Thread(new SBT(entradaXml)).start();        
    }
    /*@UpnpAction(out =
    @UpnpOutputArgument(name = "ResultWrapped", stateVariable="service"))
    public String getWrapped()
    {
        System.out.println("Entrou na acao retrieve");
        b.busca();
        
        service = "teste";//b.retorno;
        return service;
    }
    
    @UpnpAction(name = "GetService",
    out =
    @UpnpOutputArgument(name = "ResultService",
    getterName = "getWrapped"))
    public void getService(@UpnpInputArgument(name = "EntradaXml") String entradaXml) throws JDOMException, IOException
    {

        b = new Buscador(entradaXml, "teste");
        //b.busca();

        //while(!b.terminou);
        System.out.println("Fim da acao get");

    }*/

}
