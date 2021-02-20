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
    private Victor LeftShoot = new Victor(8);
    private Victor RightShoot = new Victor(9);
    private PIDController LeftPID = new PIDController(0, 0, 0);
    private PIDController RightPID = new PIDController(0, 0, 0);
    private ControllerManager cManager;

    public Shooter(ControllerManager cManager){
        this.cManager = cManager;
    }

    public void OperatorControl(){
        arm.set(cManager.getArmInput());
        setSpeed(cManager.getShootSpeed());
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

    private void setSpeed(double speed){
        LeftShoot.setSpeed(LeftPID.calculate(LShoot.getRate() , speed*100.0));
        RightShoot.setSpeed(RightPID.calculate(RShoot.getRate() , speed*100.0));
        
        
        /*L_rpm = (LShoot.getRate()/2048.0)/60.0;
        R_rpm = (RShoot.getRate()/2048.0)/60.0;
        // calculate target
        TargetRpm = speed;
        //compare l and r rpm to target
        if(L_rpm > TargetRpm){
            LeftShoot.setSpeed(0.0);
        }else{
            LeftShoot.setSpeed(1.0);
        }

        if(R_rpm > TargetRpm){
            RightShoot.setSpeed(0.0);
        }
        else{
            RightShoot.setSpeed(1.0);
        }
        */
           

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
}
