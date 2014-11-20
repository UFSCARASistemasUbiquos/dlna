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
import org.teleal.cling.model.message.header.UDAServiceTypeHeader;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.registry.*;
import projetosistemasubiquos.type.Argumento;
import projetosistemasubiquos.type.Requisicao;

/**
 *
 * @author elder
 */
public class ServiceHandler
{

    String device, serviceName, action, returnValue;
    String mode = ""; //RETURN, INPUT, RETURN AND INPUT, VOID
    String inputValue;
    String inputName;
    String outputName;
    boolean finished = false;
    Requisicao r = null;

    //public ServiceHandler(String device, String service, String action, String mode, String inputValue, String inputName, String outputName)
    public ServiceHandler(String xml)
    {
        r = Requisicao.create(xml);
        this.device = r.getDeviceName();   //device;
        this.serviceName = r.getServiceName();  //service;
        this.action = r.getOperationName();//action;
        this.mode = "VOID";
        if (!r.getArgumentosInput().isEmpty())
        {
            this.mode = "INPUT";
        }
        if (!r.getArgumentosOutput().isEmpty())
        {
            this.mode += "RETURN";
            this.outputName = r.getArgumentosOutput().get(0).getNomeArgumento();    //outputName;
        }
    }

    public final void findAndExecute()
    {
        System.out.println("Buscando o servico " + serviceName);
        UpnpService upnpService = new UpnpServiceImpl();

        // Add a listener for device registration events
        upnpService.getRegistry().addListener(
                createRegistryListener(upnpService));

        // Broadcast a search message for all devices
        //upnpService.getControlPoint().search(
        //       new STAllHeader());

        //Find for devices which provide SwitchPower services
        /*UDADeviceType udaType = new UDADeviceType(device);
        upnpService.getControlPoint().search(new UDADeviceTypeHeader(udaType));*/


        UDAServiceType udaType = new UDAServiceType(serviceName);
        upnpService.getControlPoint().search(new UDAServiceTypeHeader(udaType));



    }

    RegistryListener createRegistryListener(final UpnpService upnpService)
    {
        return new DefaultRegistryListener()
        {

            ServiceId serviceId = new UDAServiceId(serviceName);

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

        GenericActionInvocation genericActionInvocation =
                new GenericActionInvocation(newService, action);


        //upnpService.getControlPoint().execute(ac);
        //upnpService.shutdown();

        // Executes asynchronous in the background
        new ActionCallback.Default(genericActionInvocation, upnpService.getControlPoint()).run();
        if (mode.contains("RETURN"))
        {
            returnValue = genericActionInvocation.getOutput(outputName).getValue().toString();
        }
        finished = true;
    }

    class GenericActionInvocation extends ActionInvocation
    {

        GenericActionInvocation(Service service, String action) throws JDOMException, IOException
        {
            super(service.getAction(action));
            try
            {
                if (mode.contains("INPUT"))
                {
                    for(Argumento a:r.getArgumentosInput())
                    {
                        setInput(a.getNomeArgumento(), a.getValorArgumento());
                    }                    
                }

            } catch (InvalidValueException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }
}