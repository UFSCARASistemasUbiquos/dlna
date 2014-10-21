package projetosistemasubiquos;


import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.binding.*;
import org.teleal.cling.binding.annotations.*;
import org.teleal.cling.model.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;

import java.io.IOException;

public class ServiceBrockerServer implements Runnable {

    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread serverThread = new Thread(new ServiceBrockerServer());
        serverThread.setDaemon(false);
        serverThread.start();
    }

    @Override
    public void run() {
        try {

            final UpnpService upnpService = new UpnpServiceImpl();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    upnpService.shutdown();
                }
            });
            LocalDevice ld = createDevice();
            //System.out.println("Criado: \n\n " + ld);
            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(ld);
                    
            

        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
            
    }
    }
    

LocalDevice createDevice()
        throws ValidationException, LocalServiceBindingException, IOException {

    DeviceIdentity identity =
            new DeviceIdentity(
                    UDN.uniqueSystemIdentifier("Version 1 Service Brocker")
            );

    DeviceType type =
            new UDADeviceType("ServiceBrocker", 1);

    DeviceDetails details =
            new DeviceDetails(
                    "ServiceBrockerV1",
                    new ManufacturerDetails("UFSC"),
                    new ModelDetails(
                            "ProjSistemasUbiquos",
                            "Servidor que escuta requisições externas e realiza requisições internas de serviços",
                            "v1"
                    )
            );

    Icon icon =
            new Icon(
                    "image/png", 48, 48, 8,
                    getClass().getResource("icon.png")
            );

    LocalService<ServiceBrocker> serviceBrockerService =
            new AnnotationLocalServiceBinder().read(ServiceBrocker.class);

    serviceBrockerService.setManager(
            new DefaultServiceManager(serviceBrockerService, ServiceBrocker.class)
    );

    return new LocalDevice(identity, type, details, icon, serviceBrockerService);
    //return new LocalDevice(identity, type, details, switchPowerService);

    /* Several services can be bound to the same device:
    return new LocalDevice(
            identity, type, details, icon,
            new LocalService[] {switchPowerService, myOtherService}
    );
    */
    
}



}

