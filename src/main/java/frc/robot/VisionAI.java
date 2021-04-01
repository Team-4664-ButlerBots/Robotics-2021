// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

/** Add your docs here. */
public class VisionAI extends Vision {
    private BallCollector collector;
    private Timer time = new Timer();
    private boolean collecting = false;
    private boolean targetPresent = false;
    private Ultrasonic CollectorUltra;
    private double lastX = 0;

    public VisionAI(DriveTrain dTrain, BallCollector collector, Ultrasonic ultra) {
        super(dTrain);
        this.collector = collector;
        CollectorUltra = ultra;
    }

    public void AIInit(){
        lastX = xCenter.getDouble(0);
        targetPresent = false;
    }

    public void AIGetTarget() {
        if (collecting) {
            Collect();
        } else {
            //if a target is on camera
            if (TargetExists()) {
                //follow it
                FollowTarget();
                // if we are close enough to the target
                if (readyToSucc()) {
                    //try to collect the target
                    startCollecting();
                }
            }else{
                //if no target is found spin in place
                dTrain.getDiffDrive().arcadeDrive(0, 0.5);
            }
        }
    }

    
    public boolean TargetExists() {
        if(lastX != xCenter.getDouble(0)){
            targetPresent = true;
        }
        lastX = xCenter.getDouble(0);
        // TODO Auto-generated method stub
        return targetPresent;
    }

    //if the ball is close enough to the robot to start running the collecor
    private boolean readyToSucc(){
        //TargetSize.getDouble(1000) < RectSize.getDouble(0)
        return (CollectorUltra.getRangeInches() < 15);
    }

    private void startCollecting() {
        System.out.println("------ATTEMPTING TO COLLECT-------");
        time.reset();
        collecting = true;
    }

    private void Collect() {
        if (time.get() > 2 || !readyToSucc()) {
            collecting = false;
            targetPresent = false;
            dTrain.getDiffDrive().arcadeDrive(0, 0);
            collector.moveCollector(0);
        } else {
            dTrain.getDiffDrive().arcadeDrive(0.3, 0);
            collector.moveCollector(-1);
        }
    }

}
