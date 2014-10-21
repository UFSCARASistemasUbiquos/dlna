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
import org.teleal.cling.model.message.*;
import org.teleal.cling.model.message.header.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.registry.*;

/**
 *
 * @author elder
 */
public class Buscador
{

    String serviceType, action, retorno;
    boolean terminou = false;

    public Buscador(String service, String action)
    {
        serviceType = service;
        this.action = action;
    }

    public final void buscaEExecuta()
    {
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
    
    public final void buscaEExecuta(String valor)
    {
        UpnpService upnpService = new UpnpServiceImpl();


        // Add a listener for device registration events
        upnpService.getRegistry().addListener(
                createRegistryListener(upnpService,valor));



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
    
    RegistryListener createRegistryListener(final UpnpService upnpService, final String valor)
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
                        executeAction(upnpService, service, valor);
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

    void executeAction(UpnpService upnpService, Service newService, String valor) throws JDOMException, IOException
    {

        ActionInvocation setRetornoActionInvocation =
                new SetRetornoActionInvocation(newService, action, valor);
        ActionCallback ac = new ActionCallback(setRetornoActionInvocation)
        {

            @Override
            public void success(ActionInvocation invocation)
            {
                assert invocation.getOutput().length == 0;
                System.out.println("Successfully called action setRetorno!");
            }

            @Override
            public void failure(ActionInvocation invocation,
                    UpnpResponse operation,
                    String defaultMsg)
            {
                System.err.println(defaultMsg);
            }
        };

        //upnpService.getControlPoint().execute(ac);
        //upnpService.shutdown();

        // Executes asynchronous in the background
        new ActionCallback.Default(setRetornoActionInvocation, upnpService.getControlPoint()).run();
        
    }
    
    void executeAction(UpnpService upnpService, Service newService) throws JDOMException, IOException
    {

        ActionInvocation genericActionInvocation =
                new GenericActionInvocation(newService, action);
        

        //upnpService.getControlPoint().execute(ac);
        //upnpService.shutdown();

        // Executes asynchronous in the background
        new ActionCallback.Default(genericActionInvocation, upnpService.getControlPoint()).run();
        //retorno = genericActionInvocation.getOutput("ResultName").getValue().toString();
        terminou = true;
    }

    class GenericActionInvocation extends ActionInvocation
    {

        GenericActionInvocation(Service service, String action) throws JDOMException, IOException
        {
            super(service.getAction(action));
            try
            {
                // Throws InvalidValueException if the value is of wrong type
                
                setInput("NewTargetValue", "30");

            } catch (InvalidValueException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }
    
    class SetRetornoActionInvocation extends ActionInvocation
    {

        SetRetornoActionInvocation(Service service, String action, String valor) throws JDOMException, IOException
        {
            super(service.getAction(action));
            try
            {
                // Throws InvalidValueException if the value is of wrong type
                setInput("EntradaXml", valor);

            } catch (InvalidValueException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
    }
}