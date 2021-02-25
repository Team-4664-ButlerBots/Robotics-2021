/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.stream.Collector;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.*;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Add your docs here.
 */
public class Shooter {
    private Victor ArmMC = new Victor(5);
    private double ArmSpeed = 0.0;
    private BallCollector collector = new BallCollector();
    private double collectorSpeed;

    // pnematics
    private Compressor Compressor = new Compressor(0);
    private DoubleSolenoid StopPiston = new DoubleSolenoid(7, 5);
    private DoubleSolenoid Brake = new DoubleSolenoid(4, 6);

    // fly wheels
    private Encoder Lencoder = new Encoder(1, 2);
    private Encoder Rencoder = new Encoder(3, 4);
    private PIDController LeftPID = new PIDController(0.5, 0.1, 0);
    private PIDController RightPID = new PIDController(0.5, 0.1, 0);
    private Victor LeftShootMC = new Victor(8);
    private double LeftShootSpeed = 0.0;
    private Victor RightShootMC = new Victor(9);
    private double RightShootSpeed = 0.0;
    // target rpm is the minimum speed that the flywheels must be running at to
    // shoot
    private double TargetRpm = 0;
    private ControllerManager cManager;

    public Shooter(ControllerManager cManager) {
        this.cManager = cManager;
    }

    public void OperatorControl() {
        Lencoder.setDistancePerPulse(1/2048.0);
        Rencoder.setDistancePerPulse(1/2048.0);
        setFlyWheelSpeed(cManager.getFlyWheelSpeed());
        moveArm(cManager.getArmInput());
        Shoot(cManager.getShootState());
        collectorSpeed = cManager.collectorInput();
        Debug();
    }

    public void Debug() {
        readEncoder();
    }

    void readEncoder() {
        SmartDashboard.putNumber("LEncodeSpeed", Rencoder.getRate());
        SmartDashboard.putNumber("LEncodeDistance", Lencoder.getDistance());
        SmartDashboard.putNumber("Rotations", Lencoder.getDistance());
        SmartDashboard.putBoolean("TARGET SPEED REACHED", false);
    }

    // directly sets current flywheel speed
    private void setFlyWheelSpeed(double speed) {
        if (cManager.getArmed()) {
            TargetRpm = speed * 100;
        } else {
            TargetRpm = speed * 15;
        }
        // LeftShootSpeed = TargetRpm;
        // RightShootSpeed = TargetRpm;
        //LeftShootSpeed = (clamp(LeftPID.calculate(Lencoder.getRate(), TargetRpm), 0, 1));
        //RightShootSpeed = (clamp(RightPID.calculate(Rencoder.getRate(), TargetRpm), -1, 0));
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

    /**
     * Controls the arm of the robot. The brake should be actuated when the arm is
     * stationary and moving down. The brake should be released when the arm is
     * moving up. There should be a 5% deadband that means if the speed is less than
     * 0.05 or more than -0.05 there should be no movement.
     * 
     * @param speed sets the speed of the motor controlling the arm's up down
     *              movement
     */
    public void moveArm(double speed) {
        if (speed < -0.05) {
            Brake.set(Value.kReverse);
        } else if (speed > 0.2) {
            Brake.set(Value.kForward);
        } else {
            Brake.set(Value.kReverse);
            speed = 0;
        }
        ArmSpeed = speed;
    }

    public void Shoot(int shootState) {
        switch (shootState) {
            case 1:
                // System.out.println("Running");
                // open piston
                StopPiston.set(Value.kReverse);
                // check if the fly wheels are spinning at the target rpm
                boolean TargetRpmReached = Lencoder.getRate() > TargetRpm - 1 && Rencoder.getRate() > TargetRpm - 1;
                SmartDashboard.putBoolean("TARGET SPEED REACHED", TargetRpmReached);
                if (TargetRpmReached || cManager.ShootManualOverride()) {
                    collectorSpeed = 1;
                } else {
                    collectorSpeed = 0;
                }
                break;
            case 0:
                // System.out.println("Stopping");
                StopPiston.set(Value.kForward);
                break;
            case -1:
                // System.out.println("BackingUp");
                collectorSpeed = -1;
                break;
            default:
                System.out.println("Something is busted if this is running");
                break;
        }
    }

    /**
     * All motors will be set inside this method. A motor speed should never be set
     * outside it Motors will be set every frame of operation to prevent motor
     * safety timeout. variables inside the class will control motor speeds
     */
    public void UpdateMotors() {
        LeftShootMC.setSpeed(LeftShootSpeed);
        RightShootMC.setSpeed(RightShootSpeed);
        ArmMC.setSpeed(ArmSpeed);
        collector.moveCollector(collectorSpeed);
    }
}
