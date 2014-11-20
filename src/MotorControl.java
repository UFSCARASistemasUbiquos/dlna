/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elder
 */
package projetosistemasubiquos;



import org.teleal.cling.binding.annotations.*;

@UpnpService(
        serviceId = @UpnpServiceId("MotorControl"),
        serviceType = @UpnpServiceType(value = "MotorControl", version = 1)
)
public class MotorControl
{
    @UpnpStateVariable(defaultValue = "0")
    private boolean ligado = false;
    
    @UpnpStateVariable()
    private String velocidade = "0";

    
    @UpnpAction
    public void ligarMotor() {
        ligado = true;        
        System.out.println("Motor ligado!");
    }
    
    @UpnpAction
    public void desligarMotor() {
        ligado = false;        
        System.out.println("Motor desligado!");
    }    
    
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultVelocidade"))
     public String setVelocidade(@UpnpInputArgument(name = "NewTargetValue", stateVariable="velocidade")
                          String newTargetValue) {
        if(ligado)
        {
            System.out.println("Velocidade anterior: " + velocidade);
            velocidade = newTargetValue;
            System.out.println("Velocidade setada: " + velocidade);
        }
        else
        {
            System.out.println("O motor está desligado!!!");
        }
           
        return velocidade;
    }
}
