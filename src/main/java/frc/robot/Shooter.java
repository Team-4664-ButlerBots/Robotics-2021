/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.*;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class Shooter {
    private Encoder LShoot = new Encoder(1,2);
    private Encoder RShoot = new Encoder(3,4);
    private Spark arm = new Spark(2);
    private double leftShootSpeed = 0;
    private Victor LeftShoot = new Victor(8);
    private double rightShootSpeed = 0;
    private Victor RightShoot = new Victor(9);
    private PIDController LeftPID = new PIDController(0, 0, 0);
    private PIDController RightPID = new PIDController(0, 0, 0);
    private ControllerManager cManager;

    public Shooter(ControllerManager cManager){
        this.cManager = cManager;
    }

    public void OperatorControl(){
        arm.set(cManager.getArmInput());
        setFlyWheelSpeed(cManager.getShootSpeed());
    }

    public void Debug(){
        readEncoder();
    }

    void readEncoder(){
        SmartDashboard.putNumber("LEncodeSpeed", LShoot.getRate());
        SmartDashboard.putNumber("LEncodeDistance", LShoot.getDistance());
        SmartDashboard.putNumber("Rotations", LShoot.getDistance()/2048.0);
    }

    private double L_rpm, R_rpm, TargetRpm;

    private void setFlyWheelSpeed(double speed){
        LeftShoot.setSpeed(LeftPID.calculate(LShoot.getRate() , speed*100.0));
        RightShoot.setSpeed(RightPID.calculate(RShoot.getRate() , speed*100.0));
    }

    private double clamp(double in, double low, double high){
        if(in < low){
            return low;
        }else if(in > high){
            return high;
        }else{
            return in;
        }
    }

    /**
     * Controls the arm of the robot. The brake should be actuated when the arm is stationary and 
     * moving down. The brake should be released when the arm is moving up. There should be a 5% deadband
     * @param speed sets the speed of the motor controlling the arm's up down movement
     */
    public void moveArm(double speed){
        
    }

    /**
     * All motors will be set inside this method. A motor speed should never be set outside it
     * Motors will be set every frame of operation to prevent motor safety timeout. 
     * variables inside the class will control motor speeds
     */
    public void UpdateMotors(){

    }
}
