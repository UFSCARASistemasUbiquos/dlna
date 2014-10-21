/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosistemasubiquos;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.JDOMException;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.*;
import org.teleal.cling.model.action.*;
import org.teleal.cling.model.message.header.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.registry.*;

/**
 *
 * @author elder
 */


public class ServiceHandler
{
    String serviceType, action, returnValue;
    String mode = ""; //RETURN, INPUT, RETURN AND INPUT, VOID
    String inputValue;
    String inputName;
    String outputName;
    boolean finished = false;

    public ServiceHandler(String service, String action, String mode, String inputValue, String inputName, String outputName)
    {
        serviceType = service;
        this.action = action;
        this.mode = mode;
        this.inputValue = inputValue;
        this.outputName = outputName;
        this.inputName = inputName;
    }

    public final void findAndExecute()
    {        
        System.out.println("Buscando o servico " + serviceType);
        UpnpService upnpService = new UpnpServiceImpl();
        
        // Add a listener for device registration events
        upnpService.getRegistry().addListener(
                createRegistryListener(upnpService));


        // Broadcast a search message for all devices
        //upnpService.getControlPoint().search(
        //       new STAllHeader());

        //Find for devices which provide SwitchPower services
        UDAServiceType udaType = new UDAServiceType(serviceType);
        upnpService.getControlPoint().search(new UDAServiceTypeHeader(udaType));
        
    }
    
    RegistryListener createRegistryListener(final UpnpService upnpService)
    {
        return new DefaultRegistryListener()
        {
            ServiceId serviceId = new UDAServiceId(serviceType);

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device)
            {

                Service service;

                if ((service = device.findService(serviceId)) != null)
                {

                    System.out.println("Service discovered: " + service);
                    try
                    {
                        executeAction(upnpService, service);
                    } catch (JDOMException ex)
                    {
                        Logger.getLogger(ServiceBrockerClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex)
                    {
                        Logger.getLogger(ServiceBrockerClient.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device)
            {
                Service service;
                if ((service = device.findService(serviceId)) != null)
                {
                    System.out.println("Service disappeared: " + service);
                }
            }
        };
    }
   

    
    void executeAction(UpnpService upnpService, Service newService) throws JDOMException, IOException
    {

        ActionInvocation genericActionInvocation =
                new GenericActionInvocation(newService, action);
        

        //upnpService.getControlPoint().execute(ac);
        //upnpService.shutdown();
        
        // Executes asynchronous in the background
        new ActionCallback.Default(genericActionInvocation, upnpService.getControlPoint()).run();
        if(mode.contains("RETURN"))
            returnValue = genericActionInvocation.getOutput(outputName).getValue().toString();
        finished = true;
    }

    class GenericActionInvocation extends ActionInvocation
    {

        GenericActionInvocation(Service service, String action) throws JDOMException, IOException
        {
            super(service.getAction(action));
            try
            {
                if(mode.contains("INPUT"))
                {
                    setInput(inputName, inputValue);
                }

            } catch (InvalidValueException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }
    
    
}