// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * Should have a variable that tracks set speed.
 * Should have an amrmed state. When armed wheels run at full speed, when not at 1/4th
 * should make each flywheel go to specified speed
 * should have method that returns if ready to fire
 * */
public class FlyWheels {
    private double speed;
    private boolean armed;
    

    public void setArmed(boolean armed){
        this.armed = armed;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public boolean readyToFire(){
        return false;
    }

    public void updateMotors(){
        
    }
}
