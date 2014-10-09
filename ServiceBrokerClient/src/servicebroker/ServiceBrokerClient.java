package servicebroker;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.*;
import org.teleal.cling.model.action.*;
import org.teleal.cling.model.message.*;
import org.teleal.cling.model.message.header.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.registry.*;
import org.teleal.cling.registry.DefaultRegistryListener;

public class ServiceBrokerClient implements Runnable {

    public int valor = 0;
    public UpnpService upnpService = new UpnpServiceImpl();

    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread clientThread = new Thread(new ServiceBrokerClient());
        clientThread.setDaemon(false);
        clientThread.start();
    }

    @Override
    public void run() {
        try {
            //upnpService.getRegistry().addListener(createRegistryListener(upnpService));
            //upnpService.getControlPoint().search(new STAllHeader());

            upnpService.getRegistry().addListener(createRegistryListener(upnpService));
            UDAServiceType udaType = new UDAServiceType("SwitchPowerTest2");
            upnpService.getControlPoint().search(new UDAServiceTypeHeader(udaType));

            //upnpService.shutdown();            
        } catch (Exception ex) {
            System.out.println("Exception occured: " + ex);
            System.exit(1);
        }
    }
    
    public void dispositivoPorTipo(String dispositivo) {
        try {
            //upnpService.getRegistry().addListener(createRegistryListener(upnpService));
            //upnpService.getControlPoint().search(new STAllHeader());

            upnpService.getRegistry().addListener(createRegistryListenerDispositivo(upnpService, dispositivo));
            UDAServiceType udaType = new UDAServiceType(dispositivo);
            upnpService.getControlPoint().search(new UDAServiceTypeHeader(udaType));

            //upnpService.shutdown();            
        } catch (Exception ex) {
            System.out.println("Exception occured: " + ex);
            System.exit(1);
        }
    }
    
    RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {
            ServiceId serviceId = new UDAServiceId("SwitchPowerTest2");
            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                /*System.out.println("Service discovered: " + device.getDetails().getFriendlyName());
                for (RemoteService service : device.findServices()) {
                    System.out.println("Actions: " + service.toString());
                    System.out.println("Variables: " + service.getStateVariables().toString());
                }*/
                //executeAction(upnpService, testePower);
                Service switchPower;
                switchPower = device.findService(serviceId);
                executeAction(upnpService, switchPower);
                
            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {

            }
        };
    }

    RegistryListener createRegistryListenerDispositivo(final UpnpService upnpService, final String dispositivo) {
        return new DefaultRegistryListener() {
            ServiceId serviceId = new UDAServiceId(dispositivo);
            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                /*System.out.println("Service discovered: " + device.getDetails().getFriendlyName());
                for (RemoteService service : device.findServices()) {
                    System.out.println("Actions: " + service.toString());
                    System.out.println("Variables: " + service.getStateVariables().toString());
                }*/
                //executeAction(upnpService, testePower);
                Service switchPower;
                switchPower = device.findService(serviceId);
                executeAction(upnpService, switchPower);
                
            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {

            }
        };
    }

    void executeAction(UpnpService upnpService, Service service) {
        ActionInvocation setInvocation = new SetActionInvocation(service);
        ActionCallback a = new ActionCallback(setInvocation) {
            @Override
            public void success(ActionInvocation invocation) {
                assert invocation.getOutput().length == 0;
                System.out.println("SAIDA: " + invocation.getOutput("RetValor").getValue().toString());
                System.out.println("Successfully called action get!");
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                System.out.println(defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(a);
    }
    
    class SetActionInvocation extends ActionInvocation {

        public SetActionInvocation(Service service) {
            super(service.getAction("SetValor"));
            try {
                setInput("NovoValor", valor);
                //System.out.println("SAIDA: "+ getOutput("GetName").getValue().toString());
            } catch (InvalidValueException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        }
    ;
}

}
