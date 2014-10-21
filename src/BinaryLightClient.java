package projetosistemasubiquos;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.*;
import org.teleal.cling.model.action.*;
import org.teleal.cling.model.message.*;
import org.teleal.cling.model.message.header.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.registry.*;

public class BinaryLightClient implements Runnable
{
    static BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception
    {
        // Start a user thread that runs the UPnP stack
        Thread clientThread = new Thread(new BinaryLightClient());
        clientThread.setDaemon(false);
        clientThread.start();

    }

    @Override
    public void run()
    {
        try
        {

            UpnpService upnpService = new UpnpServiceImpl();

            // Add a listener for device registration events
            upnpService.getRegistry().addListener(
                    createRegistryListener(upnpService));
            

            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(
                    new STAllHeader());
            //upnpService.shutdown();

        } catch (Exception ex)
        {
            System.err.println("Exception occured: " + ex);
            System.exit(1);
        }
    }

    RegistryListener createRegistryListener(final UpnpService upnpService)
    {
        return new DefaultRegistryListener()
        {

            ServiceId serviceId = new UDAServiceId("SwitchPower");

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device)
            {

                Service switchPower;
                
                if ((switchPower = device.findService(serviceId)) != null)
                {

                    System.out.println("Service discovered: " + switchPower);
                    executeAction(upnpService, switchPower);
                    
                }

            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device)
            {
                Service switchPower;
                if ((switchPower = device.findService(serviceId)) != null)
                {
                    System.out.println("Service disappeared: " + switchPower);
                }
            }
        };
    }

    void executeAction(UpnpService upnpService, Service switchPowerService)
    {

        ActionInvocation getTargetInvocation =
                new GetTargetActionInvocation(switchPowerService);        
        ActionCallback ac = new ActionCallback(getTargetInvocation)
                {

                    @Override
                    public void success(ActionInvocation invocation)
                    {
                        assert invocation.getOutput().length == 0;
                        System.out.println("Name: "+ invocation.getOutput("ResultName").getValue().toString());
                        System.out.println("Successfully called action set!");
                    }

                    @Override
                    public void failure(ActionInvocation invocation,
                            UpnpResponse operation,
                            String defaultMsg)
                    {
                        System.err.println(defaultMsg);
                    }
                };
        
        // Executes asynchronous in the background
            
        new ActionCallback.Default(getTargetInvocation, upnpService.getControlPoint()).run();


        upnpService.getControlPoint().execute(ac); 
        

    }
    class GetTargetActionInvocation extends ActionInvocation
    {

        GetTargetActionInvocation(Service service)
        {
            super(service.getAction("GetName"));
            try
            {
                // Throws InvalidValueException if the value is of wrong type
                setInput("NewTargetValue", "cacuercoisa");

            } catch (InvalidValueException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
        }
        
    }
    
    class SetTargetActionInvocation extends ActionInvocation
    {

        SetTargetActionInvocation(Service service)
        {            
            super(service.getAction("SetValor"));
            try
            {
                // Throws InvalidValueException if the value is of wrong type
                setInput("NovoValor", "Falei que dava certo");
                

            } catch (InvalidValueException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(1);
            }
            
        }
        
    }
}