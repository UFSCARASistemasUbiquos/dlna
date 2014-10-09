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

public class ServiceBrokerServerClient implements Runnable {
    
    public UpnpService upnpService = new UpnpServiceImpl();
    
    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread clientThread = new Thread(new ServiceBrokerServerClient());
        clientThread.setDaemon(false);
        clientThread.start();
    }
    @Override
    public void run() {
        try {
            //upnpService.getRegistry().addListener(createRegistryListener(upnpService));
            //upnpService.getControlPoint().search(new STAllHeader());
            
            upnpService.getRegistry().addListener(createRegistryListener(upnpService));
            UDAServiceType udaType = new UDAServiceType("SwitchPower");
            upnpService.getControlPoint().search(new UDAServiceTypeHeader(udaType));
            
            //upnpService.shutdown();            
        } catch (Exception ex) {
            System.out.println("Exception occured: " + ex);
            System.exit(1);
        }
    }
    
    RegistryListener createRegistryListener(final UpnpService upnpService) {
        return new DefaultRegistryListener() {
            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                System.out.println("Service discovered: " + device.getDetails().getFriendlyName());
                for (RemoteService service: device.findServices()) {
                    System.out.println("Actions: "+ service.toString());
                    System.out.println("Variables: "+ service.getStateVariables().toString());
                }
                //executeAction(upnpService, testePower);
            }
            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                
            }
        };
    }
    
    void executeAction(UpnpService upnpService, Service switchPowerService) {
        ActionInvocation setInvocation = new SetActionInvocation(switchPowerService);        
        ActionCallback ac = new ActionCallback(setInvocation) {
            @Override
            public void success(ActionInvocation invocation) {
                assert invocation.getOutput().length == 0;
                System.out.println("SAIDA: "+ invocation.getOutput("RetValor").getValue().toString());
                System.out.println("Successfully called action get!");
            }
            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                System.out.println(defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(ac);
    }
    
    class SetActionInvocation extends ActionInvocation {
        public SetActionInvocation(Service service) {
            super(service.getAction("SetValor"));
            try{
                setInput("NovoValor", 11);
                //System.out.println("SAIDA: "+ getOutput("GetName").getValue().toString());
            } catch (InvalidValueException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        };
    }
    
}
