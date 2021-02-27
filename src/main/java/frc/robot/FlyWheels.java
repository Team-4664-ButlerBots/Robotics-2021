// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.PIDBase.Tolerance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Should have a variable that tracks set speed. Should have an amrmed state.
 * When armed wheels run at full speed, when not at 1/4th should make each
 * flywheel go to specified speed should have method that returns if ready to
 * fire
 */
public class FlyWheels {
    private double speed;
    private boolean armed;
    private double rpmTolerance = 4;

    // fly wheels
    private Encoder Lencoder = new Encoder(1, 2);
    private Encoder Rencoder = new Encoder(3, 4);
    private Victor LeftShootMC = new Victor(8);
    private Victor RightShootMC = new Victor(9);

    public FlyWheels() {
        Lencoder.setDistancePerPulse(1 / 2048.0);
        Rencoder.setDistancePerPulse(1 / 2048.0);
    }

    public void setArmed(boolean armed) {
        this.armed = armed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean readyToFire() {
        double leftDiff, rightDiff;
        leftDiff = speed - Lencoder.getRate();
        rightDiff = speed - Rencoder.getRate();
        boolean ready = leftDiff < rpmTolerance && rightDiff < rpmTolerance;
        SmartDashboard.putBoolean("TARGET SPEED REACHED", ready);
        return ready;
    }

    public void updateMotors() {
        double currentSpeed = 0;
        if (armed) {
            currentSpeed = speed;
        }else{
            currentSpeed = speed / 4;
        }

        double pMultiplier = 0.2;

        double leftSpeed = clamp(((currentSpeed - Lencoder.getRate()) * pMultiplier), 0, 1);
        LeftShootMC.setSpeed(leftSpeed);
        double rightSpeed = clamp(((currentSpeed - Rencoder.getRate()) * pMultiplier), 0, 1);
        RightShootMC.setSpeed(rightSpeed);
    }

    private double clamp(double in, double low, double high) {
        if (in < low) {
            return low;
        } else if (in > high) {
            return high;
        } else {
            return in;
        }
    }

    public void Debug() {
        readEncoder();
    }

    void readEncoder() {
        SmartDashboard.putNumber("LEncodeSpeed", Rencoder.getRate());
        SmartDashboard.putNumber("LEncodeDistance", Lencoder.getDistance());
        SmartDashboard.putNumber("Rotations", Lencoder.getDistance());
    }
}
