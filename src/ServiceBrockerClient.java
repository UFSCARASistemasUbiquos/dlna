package projetosistemasubiquos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class ServiceBrockerClient implements Runnable
{

    static BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
    private static String xml = "<RequisicaoServiceBrocker>     <DeviceTarget>    UUID     </DeviceTarget>     <DeviceName>    NomeDispositivo    </DeviceName>     <ServiceName>    NomeServico    </ServiceName>     <ArgumentosInput>        <NomeArgumento>        Nome        </NomeArgumento>        <TipoArgumento>        Tipo        </TipoArgumento>        <ValorArgumento>        Valor        </ValorArgumento>            </ArgumentosInput>     <ArgumentosOutput>        <NomeArgumento>        Nome        </NomeArgumento>        <TipoArgumento>        Tipo        </TipoArgumento>        <ValorArgumento>        Valor        </ValorArgumento>    </ArgumentosOutput>        <Escalonamento>        <Prioridade>        Prioridade(1...9)        </Prioridade>        <TipoEscalonamento>        MK/Soft        </TipoEscalonamento>        <M>        M            </M>        <K>        K            </K>        <Deadline>        Deadline(ms)        </Deadline>    </Escalonamento></RequisicaoServiceBrocker>";

    
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

            ServiceId serviceId = new UDAServiceId("ServiceBrocker");

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device)
            {

                Service serviceBrocker;

                if ((serviceBrocker = device.findService(serviceId)) != null)
                {

                    System.out.println("Service discovered: " + serviceBrocker);
                    try
                    {
                        executeAction(upnpService, serviceBrocker);
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
                Service serviceBrocker;
                if ((serviceBrocker = device.findService(serviceId)) != null)
                {
                    System.out.println("Service disappeared: " + serviceBrocker);
                }
            }
        };
    }

    void executeAction(UpnpService upnpService, Service serviceBrockerService) throws JDOMException, IOException
    {
        ActionInvocation getTargetInvocation =
                new GetTargetActionInvocation(serviceBrockerService);
        ActionCallback ac = new ActionCallback(getTargetInvocation)
        {

            @Override
            public void success(ActionInvocation invocation)
            {
                assert invocation.getOutput().length == 0;
                
                System.out.println("Retorno: " + invocation.getOutput("ResultService").getValue().toString());
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
        //System.out.println("Retorno: " + getTargetInvocation.getOutput("ResultService").getValue().toString());

        //upnpService.getControlPoint().execute(ac);

    }

    class GetTargetActionInvocation extends ActionInvocation
    {

        GetTargetActionInvocation(Service service) throws JDOMException, IOException
        {
            super(service.getAction("GetService"));
            try
            {
                // Throws InvalidValueException 
                setInput("EntradaXml", "SwitchPower");

            } catch (InvalidValueException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(1);
            }

        }
    }
}