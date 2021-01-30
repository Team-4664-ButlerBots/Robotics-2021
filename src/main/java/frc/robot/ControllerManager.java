/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Add your docs here.
 */
public class ControllerManager {

    private Joystick gamepad = new Joystick(0);     
    private Joystick joystick = new Joystick(1);    
    // public Joystick joystick = new Joystick(1);

    private boolean speedToggled = false;

    public boolean speedToggle() {
        if (gamepad.getRawButtonPressed(5)) { // Toggle speed between fast and slow if left bumper (5) is pressed
            if (speedToggled)
                speedToggled = false;
            else
                speedToggled = true;

        }
        return speedToggled;
    }

    public double[] getDriveInput() {
        double[] input = new double[2];

        // Set inputs
        input[0] = gamepad.getRawAxis(3);
        input[1] = gamepad.getRawAxis(1);

        return input;
    }

    public double collectorInput() {
        if (gamepad.getRawButton(2)) {
            return -Constants.collectorSpeed;
        } else if (gamepad.getRawButton(1)) {
            return Constants.collectorSpeed;
        } else {
            return 0;
        }
    }

    public double getArmInput() {
        return joystick.getRawAxis(1);      
    }

    public boolean usingVision() {
        return gamepad.getRawButton(8);
    }

    public double getShootSpeed(){
        return joystick.getRawAxis(2);
    }
}
