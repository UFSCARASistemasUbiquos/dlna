package projetosistemasubiquos;


import org.teleal.cling.binding.annotations.*;
import java.beans.PropertyChangeSupport;

@UpnpService(
        serviceId = @UpnpServiceId("SwitchPower"),
        serviceType = @UpnpServiceType(value = "SwitchPower", version = 1)
)

public class SwitchPowerWithPropertyChangeSupport {

    private final PropertyChangeSupport propertyChangeSupport;

    public SwitchPowerWithPropertyChangeSupport() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private boolean target = false;

    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;
    
    @UpnpStateVariable()
    private String name = "teste";

    @UpnpAction
    public void setName(@UpnpInputArgument(name = "NewNameValue") String newNameValue) {

        name = newNameValue;
        
        System.out.println("Novo nome: " + name);
        
    }
    
    @UpnpAction
    public void setTarget(@UpnpInputArgument(name = "NewTargetValue") boolean newTargetValue) {

        target = newTargetValue;
        status = newTargetValue;
        
        System.out.println("Switch is: " + status);
        
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
    public boolean getTarget() {
        return target;
    }
    
    

    @UpnpAction(name = "GetStatus", out = @UpnpOutputArgument(name = "ResultStatus", stateVariable = "Status"))
    public boolean retrieveStatus() {
        return status;
    }
    
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultName"))
    public String getName(@UpnpInputArgument(name = "NewName2Value") String newNameValue) {
        
        return name+" "+newNameValue;
    }
    

}


