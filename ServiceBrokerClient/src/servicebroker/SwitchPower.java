/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicebroker;

import org.teleal.cling.binding.annotations.*;

/*
 *
 * Getting output values from a JavaBean
 * <p>
 * Here the action method does not return the output argument value directly,
 * but a JavaBean instance is returned which offers a getter method to obtain
 * the output argument value:
 * </p>
 * <a class="citation" href="javacode://this" style="include:INC1"/>
 * <p>
 * Cling will detect that you mapped a getter name in the output argument
 * and that the action method is not <code>void</code>. It now expects that
 * it will find the getter method on the returned JavaBean. If there are
 * several output arguments, all of them have to be mapped to getter methods
 * on the returned JavaBean.
 * </p>
 */
@UpnpService(
        serviceId = @UpnpServiceId("SwitchPowerTest2"),
        serviceType = @UpnpServiceType(value = "SwitchPowerTest2", version = 1)
)
public class SwitchPower {

    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private boolean target = false;
    @UpnpStateVariable(defaultValue = "0")
    private boolean status = false;
    @UpnpStateVariable(defaultValue = "")
    private String name = "teste";
    @UpnpStateVariable(defaultValue = "0")
    private int valor;

    @UpnpAction(out = @UpnpOutputArgument(name = "RetNameValue"))
    public String setName(@UpnpInputArgument(name = "NewNameValue") String newNameValue) {
        name = newNameValue;
        System.out.println("Switch is: " + name);
        return " ---- " + name + " ---- ";
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "RetValor"))
    public int setValor(@UpnpInputArgument(name = "NovoValor") int novoValor) {
        valor += novoValor;
        //System.out.println("Switch is: " + valor);
        return valor;
    }

    @UpnpAction
    public boolean setTarget(@UpnpInputArgument(name = "NewTargetValue") boolean newTargetValue) {
        target = newTargetValue;
        status = newTargetValue;
        System.out.println("Switch is: " + status);
        return target;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
    public boolean getTarget() {
        return target;
    }

    @UpnpAction(name = "GetStatus", out = @UpnpOutputArgument(name = "ResultStatus", getterName = "getWrapped"))
    public StatusHolder getStatus() {
        return new StatusHolder(status);
    }

    public class StatusHolder {

        boolean wrapped;

        public StatusHolder(boolean wrapped) {
            this.wrapped = wrapped;
        }

        public boolean getWrapped() {
            return wrapped;
        }
    }
}
