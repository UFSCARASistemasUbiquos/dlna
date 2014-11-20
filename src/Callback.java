/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosistemasubiquos;

import org.teleal.cling.binding.annotations.*;

/**
 *
 * @author elder
 */
@UpnpService(serviceId =
@UpnpServiceId("Callback"),
serviceType =
@UpnpServiceType(value = "Callback", version = 1))
public class Callback
{
    
    @UpnpStateVariable(defaultValue = "vazio")
    private String retorno;

    @UpnpAction(out =
    @UpnpOutputArgument(name = "ResultStatus"))
    public String getRetorno()
    {
        return retorno;
    }

    @UpnpAction
    public void setRetorno(@UpnpInputArgument(name = "EntradaXml") String entradaXml)
    {
        retorno = entradaXml;
        if(retorno != null)
            System.out.println("Setou callback, retorno: " + retorno);
    }
}
