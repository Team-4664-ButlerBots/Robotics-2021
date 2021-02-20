// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

/** Add your docs here. */
public class LimitSwitch {
    private DigitalInput limitSwitch;
    private boolean LastState = false;

    public LimitSwitch(int port) {
        limitSwitch = new DigitalInput(port);
    }

    /**
     * returns true if the switch just got pressed down.
     * 
     * @return
     */
    public boolean getSwitchDown() {
        // if the switch wasn't pressed before this, and it is currently pressed return
        // true
        if (!LastState && limitSwitch.get()) {
            LastState = limitSwitch.get();
            return true;
        } else {
            LastState = limitSwitch.get();
            return false;
        }
    }
}
