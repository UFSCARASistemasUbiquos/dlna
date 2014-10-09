package servicebroker;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.binding.*;
import org.teleal.cling.binding.annotations.*;
import org.teleal.cling.model.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import java.io.IOException;

public class ServiceBrokerServer implements Runnable {
    
    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread serverThread = new Thread(new ServiceBrokerServer());
        serverThread.setDaemon(false);
        serverThread.start();
    }
    @Override
    public void run() {
        try {
            final UpnpService upnpService = new UpnpServiceImpl();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() { upnpService.shutdown(); }
            });
            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(createDevice());
        } catch (Exception ex) {
            System.out.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    LocalDevice createDevice()
        throws ValidationException, LocalServiceBindingException, IOException {
        DeviceIdentity identity = 
                new DeviceIdentity(UDN.uniqueSystemIdentifier("Demo 2"));
        DeviceType type = new UDADeviceType("Contador", 1);
        DeviceDetails details = 
                new DeviceDetails("Contador Amigavel", 
                        new ManufacturerDetails("ACME"), 
                        new ModelDetails("Contador2000", "teste.", "v9.9")
                );
        Icon icon = new Icon("image/png", 48, 48, 8, getClass().getResource("icon.png"));
        LocalService<SwitchPower> switchPowerService = new AnnotationLocalServiceBinder().read(SwitchPower.class);
        switchPowerService.setManager(new DefaultServiceManager(switchPowerService, SwitchPower.class));
        return new LocalDevice(identity, type, details, icon, switchPowerService);
        //return new LocalDevice(identity, type, details, switchPowerService);
        /* Several services can be bound to the same device:
        return new LocalDevice(
                identity, type, details, icon,
                new LocalService[] {switchPowerService, myOtherService}
        );
        */
    }
}