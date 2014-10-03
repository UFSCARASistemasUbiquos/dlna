package projetosistemasubiquos;

import javax.swing.JOptionPane;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.*;
import org.teleal.cling.model.action.*;
import org.teleal.cling.model.message.*;
import org.teleal.cling.model.message.header.*;
import org.teleal.cling.model.meta.*;
import org.teleal.cling.model.types.*;
import org.teleal.cling.registry.*;

public class BinaryLightClient implements Runnable {
    public String acao, funcao;
    public static String dispositivo;        
    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        //dispositivo = JOptionPane.showInputDialog(null, "Digite o primeiro número");
        dispositivo = "SwitchPower";
        Thread clientThread = new Thread(new BinaryLightClient());
        clientThread.setDaemon(false);
        clientThread.start();
    }
    @Override
    public void run() {
        try {
            UpnpService upnpService = new UpnpServiceImpl();
            // Add a listener for device registration events
            upnpService.getRegistry().addListener(createRegistryListener(upnpService));
            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(new STAllHeader());
            
            
            
            
            
            
            
            
            //upnpService.shutdown();
        } catch (Exception ex) {
            System.out.println("Exception occured: " + ex);
            System.exit(1);
        }
    }
    RegistryListener createRegistryListener(final UpnpService upnpService) {
        System.out.println("Dispositivo: " + dispositivo);
        return new DefaultRegistryListener() {
            ServiceId serviceId = new UDAServiceId(dispositivo);
            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                /*
                //=====================================
                Service service = device.findService(new UDAServiceId("SwitchPower"));
                //=====================================
                Action action = service.getAction("GetStatus");
                ActionInvocation setTargetInvocation = new ActionInvocation(action);
                setTargetInvocation.setInput("NewName2Value", "TESTE");
                ActionCallback setTargetCallback = new ActionCallback(setTargetInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {
                        //ActionArgumentValue output = invocation.getOutput("getName");
                        ActionArgumentValue status  = invocation.getOutput("ResultStatus");
                        //assertEquals(output.length, 0);
                        //System.out.println("STATUS: " + output.toString());
                        System.out.println("STATUS: " + status.getValue());
                    }
                    @Override
                    public void failure(ActionInvocation invocation,
                                UpnpResponse operation,
                                String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                };
                upnpService.getControlPoint().execute(setTargetCallback);
                
                //=====================================
                
                Action getStatusAction = service.getAction("GetStatus");
                ActionInvocation getStatusInvocation = new ActionInvocation(getStatusAction);
                ActionCallback getStatusCallback = new ActionCallback(getStatusInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {
                        ActionArgumentValue status  = invocation.getOutput("ResultStatus");
                        assert status != null;
                        System.out.println("STATUS: " + status.getValue());
                        /*assertEquals(status.getArgument().getName(), "ResultStatus");
                        assertEquals(status.getDatatype().getClass(), BooleanDatatype.class);
                        assertEquals(status.getDatatype().getBuiltin(), Datatype.Builtin.BOOLEAN);
                        assertEquals((Boolean) status.getValue(), Boolean.valueOf(false));
                        assertEquals(status.toString(), "0"); // '0' is 'false' in UPnP*
                    }
                    @Override
                    public void failure(ActionInvocation invocation,
                                        UpnpResponse operation,
                                        String defaultMsg) {
                        System.err.println(defaultMsg);
                    }
                };
                upnpService.getControlPoint().execute(getStatusCallback);
                //=====================================
                
                
                
                Service service = device.findService(new UDAServiceId("SwitchPower"));
                Action action = service.getAction("SetTarget");
                ActionInvocation setInvocation = new ActionInvocation(action);
                setInvocation.setInput("NewTargetValue", true);
                ActionCallback ac = new ActionCallback(setInvocation) {
                    @Override
                    public void success(ActionInvocation invocation) {
                        assert invocation.getOutput().length == 0;
                        //System.out.println("STATUS: " + invocation.getOutput("RetTargetValue").getValue().toString());
                        System.out.println("Successfully called action get!");
                    }
                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        System.out.println(defaultMsg);
                    }
                };
                upnpService.getControlPoint().execute(ac);
                */
                
                
                Service testePower;
                if ((testePower = device.findService(serviceId)) != null) {
                    System.out.println("Service discovered: " + testePower);
                    executeAction(upnpService, testePower);
                }
                
            }
            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                Service testePower;
                if ((testePower = device.findService(serviceId)) != null) {
                    System.out.println("Service disappeared: " + testePower);
                }
            }
        };
    }
    void executeAction(UpnpService upnpService, Service switchPowerService) {
        /*
        SubscriptionCallback callback = new SubscriptionCallback(service, 600) {
            @Override
            public void established(GENASubscription sub) {
                System.out.println("Established: " + sub.getSubscriptionId());
            }
            @Override
            protected void failed(GENASubscription subscription,
                                  UpnpResponse responseStatus,
                                  Exception exception,
                                  String defaultMsg) {
                System.err.println(defaultMsg);
            }
            @Override
            public void ended(GENASubscription sub,
                              CancelReason reason,
                              UpnpResponse response) {
                assert reason == null;
            }
            public void eventReceived(GENASubscription sub) {
                System.out.println("Event: " + sub.getCurrentSequence().getValue());
                Map<String, StateVariableValue> values = sub.getCurrentValues();
                StateVariableValue status = values.get("Status");
                assertEquals(status.getDatatype().getClass(), BooleanDatatype.class);
                assertEquals(status.getDatatype().getBuiltin(), Datatype.Builtin.BOOLEAN);
                System.out.println("Status is: " + status.toString());
            }
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                System.out.println("Missed events: " + numberOfMissedEvents);
            }
        };
        upnpService.getControlPoint().execute(callback);
        */
        /*
        Action action = service.getAction("SetTarget");
        ActionInvocation setTargetInvocation = new ActionInvocation(action);
        setTargetInvocation.setInput("NewTargetValue", true); // Can throw InvalidValueException
        // Alternative:
        //
        // setTargetInvocation.setInput(
        //         new ActionArgumentValue(
        //                 action.getInputArgument("NewTargetValue"),
        //                 true
        //         )
        // );
        *
        ActionInvocation setTargetInvocation = new SetTargetActionInvocation(switchPowerService);
        ActionCallback setTargetCallback = new ActionCallback(setTargetInvocation) {
            @Override
            public void success(ActionInvocation invocation) {
                ActionArgumentValue[] output = invocation.getOutput();
                //assertEquals(output.length, 0);
            }
            @Override
            public void failure(ActionInvocation invocation,
                        UpnpResponse operation,
                        String defaultMsg) {
                System.err.println(defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(setTargetCallback);

        /*
        //ActionInvocation setTargetInvocation = new SetTargetActionInvocation(switchPowerService);
        ActionInvocation setTargetInvocation = new SetTargetActionInvocation(switchPowerService);
        ActionCallback ac2 = new ActionCallback(setTargetInvocation) {
            @Override
            public void success(ActionInvocation invocation) {
                assert invocation.getOutput().length == 0;
                System.out.println("Successfully called action get!");
            }
            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                System.out.println(defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(ac2);
        /*
        SubscriptionCallback callback = new SubscriptionCallback(service, 600) {
            @Override
            public void established(GENASubscription sub) {
                System.out.println("Established: " + sub.getSubscriptionId());
            }
            @Override
            protected void failed(GENASubscription subscription,
                          UpnpResponse responseStatus,
                          Exception exception,
                          String defaultMsg) {
                System.err.println(defaultMsg);
            }
            @Override
            public void ended(GENASubscription sub,
                      CancelReason reason,
                      UpnpResponse response) {
                assert reason == null;
            }
            public void eventReceived(GENASubscription sub) {
                System.out.println("Event: " + sub.getCurrentSequence().getValue());
                Map<String, StateVariableValue> values = sub.getCurrentValues();
                StateVariableValue status = values.get("Status");
                assertEquals(status.getDatatype().getClass(), BooleanDatatype.class);
                assertEquals(status.getDatatype().getBuiltin(), Datatype.Builtin.BOOLEAN);
                System.out.println("Status is: " + status.toString());
            }
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                System.out.println("Missed events: " + numberOfMissedEvents);
            }
        };
        upnpService.getControlPoint().execute(callback);
        *
        ActionInvocation getStatusInvocation = new GetTargetActionInvocation(switchPowerService);
        ActionCallback getStatusCallback = new ActionCallback(getStatusInvocation) {
            @Override
            public void success(ActionInvocation invocation) {
            ActionArgumentValue status  = invocation.getOutput("ResultStatus");
                System.out.println("STATUS: "+status.getArgument().toString());
                assert status != null;
                System.out.println("STATUS: "+status.toString());
                /*assertEquals(status.getArgument().getName(), "ResultStatus");
                assertEquals(status.getDatatype().getClass(), BooleanDatatype.class);
                assertEquals(status.getDatatype().getBuiltin(), Datatype.Builtin.BOOLEAN);
                assertEquals((Boolean) status.getValue(), Boolean.valueOf(false));
                assertEquals(status.toString(), "0"); // '0' is 'false' in UPnP*
            }
            @Override
            public void failure(ActionInvocation invocation,
                        UpnpResponse operation,
                        String defaultMsg) {
                System.err.println(defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(getStatusCallback);
        */
        
        /*
        ActionInvocation getInvocation = new GetActionInvocation(switchPowerService);        
        ActionCallback ac = new ActionCallback(getInvocation) {
            @Override
            public void success(ActionInvocation invocation) {
                assert invocation.getOutput().length == 0;
                System.out.println("STATUS: " + invocation.getOutput("ResultName").getValue().toString());
                System.out.println("Successfully called action get!");
            }
            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                System.out.println(defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(ac);
        */
        ActionInvocation setInvocation = new Set2ActionInvocation(switchPowerService);        
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
    /*
    class SetTargetActionInvocation extends ActionInvocation {
        SetTargetActionInvocation(Service service) {
            //super(service.getAction("SetName"));
            super(service.getAction("SetTarget"));
            try {
                // Throws InvalidValueException if the value is of wrong type
                setInput("NewTargetValue", false);
                //setInput("NewNameValue", "google");

            } catch (InvalidValueException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        }
    }
    
    class GetTargetActionInvocation extends ActionInvocation {
        public GetTargetActionInvocation(Service service) {
            super(service.getAction("GetStatus"));
            try{
                
            } catch (InvalidValueException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        };
    }
    */
    class Set2ActionInvocation extends ActionInvocation {
        public Set2ActionInvocation(Service service) {
            super(service.getAction("SetValor"));
            try{
                setInput("NovoValor", -10);
                //System.out.println("SAIDA: "+ getOutput("GetName").getValue().toString());
            } catch (InvalidValueException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        };
    }
    class SetActionInvocation extends ActionInvocation {
        public SetActionInvocation(Service service) {
            super(service.getAction("SetName"));
            try{
                setInput("NewNameValue", " google");
                //System.out.println("SAIDA: "+ getOutput("GetName").getValue().toString());
            } catch (InvalidValueException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        };
    }
    class GetActionInvocation extends ActionInvocation {
        public GetActionInvocation(Service service) {
            super(service.getAction("GetName"));
            try{
                setInput("NewName2Value", " google");
            } catch (InvalidValueException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        };
    }
    
}