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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Add your docs here.
 */
public class Shooter {
    private Spark ArmMC = new Spark(2);
    private double ArmSpeed = 0.0;
    BallCollector collector = new BallCollector();

    // pnematics
    private Compressor Compressor = new Compressor(0);
    private Solenoid StopPiston = new Solenoid(1);
    private Solenoid Brake = new Solenoid(2);

    // fly wheels
    private Encoder Lencoder = new Encoder(3, 4);
    private Encoder Rencoder = new Encoder(1, 2);
    private PIDController LeftPID = new PIDController(0, 0, 0);
    private PIDController RightPID = new PIDController(0, 0, 0);
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
        setFlyWheelSpeed(cManager.getFlyWheelSpeed());
        moveArm(cManager.getArmInput());
        collector.moveCollector(cManager.collectorInput());
        Shoot(cManager.getShootState());
        Debug();
    }

    public void Debug() {
        readEncoder();
    }

    void readEncoder() {
        SmartDashboard.putNumber("LEncodeSpeed", Lencoder.getRate() / 2048.0);
        SmartDashboard.putNumber("LEncodeDistance", Lencoder.getDistance());
        SmartDashboard.putNumber("Rotations", Lencoder.getDistance() / 2048.0);
    }

    // directly sets current flywheel speed
    private void setFlyWheelSpeed(double speed) {
        speed += 1.0;
        speed /= 2.0;
        TargetRpm = speed * 10;
        LeftShootSpeed = TargetRpm;
        RightShootSpeed = TargetRpm;
        //LeftShootSpeed = (LeftPID.calculate(Lencoder.getRate(), TargetRpm));
        //RightShootSpeed = (RightPID.calculate(Rencoder.getRate(), TargetRpm));
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
        if (speed < -0.5) {
            Brake.set(true);
        } else if (speed > 0.5) {
            Brake.set(false);
        } else {
            Brake.set(true);
            speed = 0;
        }
        ArmSpeed = speed;
    }

    public void Shoot(int shootState) {
        switch (shootState) {
            case 1:
                //System.out.println("Running");
                // open piston
                StopPiston.set(false);
                // check if the fly wheels are spinning at the target rpm
                if (Lencoder.getRate() > TargetRpm - 1 && Rencoder.getRate() > TargetRpm - 1) {
                    collector.moveCollector(1);
                } else {
                    collector.moveCollector(0);
                }
                break;
            case 0:
                //System.out.println("Stopping");
                StopPiston.set(true);
                break;
            case -1:
                //System.out.println("BackingUp");
                collector.moveCollector(-1);
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
    }
}
