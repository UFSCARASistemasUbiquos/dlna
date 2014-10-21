/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosistemasubiquos;

import java.io.IOException;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.binding.*;
import org.teleal.cling.binding.annotations.*;
import org.teleal.cling.model.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;

class CallbackServer implements Runnable
{

    @Override
    public void run()
    {
        try
        {

            final UpnpService upnpService = new UpnpServiceImpl();

            Runtime.getRuntime().addShutdownHook(new Thread()
            {

                @Override
                public void run()
                {
                    upnpService.shutdown();
                }
            });
            LocalDevice ld = createDevice();
            
            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(ld);



        } catch (Exception ex)
        {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);

        }
    }

    LocalDevice createDevice()
            throws ValidationException, LocalServiceBindingException, IOException
    {

        DeviceIdentity identity =
                new DeviceIdentity(
                UDN.uniqueSystemIdentifier("Callback Service"));

        DeviceType type =
                new UDADeviceType("Callback", 1);

        DeviceDetails details =
                new DeviceDetails(
                "First Callback Service",
                new ManufacturerDetails("UFSC"),
                new ModelDetails(
                "Callbackv1",
                "A callback service for clients recieve responses.",
                "v1"));

        Icon icon =
                new Icon(
                "image/png", 48, 48, 8,
                getClass().getResource("icon.png"));
        LocalService<Callback> CallbackService =
                new AnnotationLocalServiceBinder().read(Callback.class);

        CallbackService.setManager(
                new DefaultServiceManager(CallbackService, Callback.class));

        return new LocalDevice(identity, type, details, icon, CallbackService);
        //return new LocalDevice(identity, type, details, switchPowerService);

        /* Several services can be bound to the same device:
        return new LocalDevice(
        identity, type, details, icon,
        new LocalService[] {switchPowerService, myOtherService}
        );
         */

    }
}
