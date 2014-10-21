package projetosistemasubiquos;



import org.teleal.cling.binding.annotations.*;

@UpnpService(
        serviceId = @UpnpServiceId("SwitchPower"),
        serviceType = @UpnpServiceType(value = "SwitchPower", version = 1)
)
public class SwitchPower {

    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private boolean target = false;

    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;
    
    @UpnpStateVariable(defaultValue = "vazio")
    private String name = "a";

    @UpnpAction
    public void setTarget(@UpnpInputArgument(name = "NewTargetValue")
                          boolean newTargetValue) {
        target = newTargetValue;
        status = newTargetValue;
        
        System.out.println("Switch is: " + status);
    }
    

    @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
    public boolean getTarget() {
        return target;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public boolean getStatus() {
        return status;
    }
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultName"))
    public String getName(@UpnpInputArgument(name = "NewTargetValue")
                          String newTargetValue) {
        name = "a" + newTargetValue;
        System.out.println(name);
        return name;
    }
    
    

}

