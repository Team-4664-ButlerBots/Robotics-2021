// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import frc.robot.LED.LedManager;

/** Add your docs here. */
public class VisionAI extends Vision {
    private BallCollector collector;
    private LedManager ledManager;
    private Timer time = new Timer();
    private Timer timeStart = new Timer();
    private boolean collecting = false;
    private boolean targetPresent = false;
    private Ultrasonic CollectorUltra;
    private double lastX = 0;
    private Gyro gyro;

    public VisionAI(DriveTrain dTrain, BallCollector collector, Ultrasonic ultra, LedManager ledManager, Gyro gyro) {
        super(dTrain);
        this.collector = collector;
        this.ledManager = ledManager;
        this.gyro = gyro;
        CollectorUltra = ultra;
    }

    public void AIInit() {
        lastX = xCenter.getDouble(0);
        collector.resetBallCount();
        gyro.init();
        time.reset();
        timeStart.reset();
        timeStart.start();
        targetPresent = false;
    }

    public void AIGetTarget() {
        if (timeStart.get() < 1.5) {
            dTrain.getDiffDrive().arcadeDrive(0.4, 0);
        } else {
            // System.out.println("Timer Is: " + time.get());
            ledManager.AutoUpdate(TargetExists(), xCenter.getDouble(0.5), RectSize.getDouble(0));
            if (collector.ballsFull()) {
                goToGyro();
            } else {
                if (collecting) {
                    Collect();
                } else {
                    // if a target is on camera
                    if (TargetExists()) {
                        // follow it
                        FollowTarget();
                        // if we are close enough to the target
                        if (readyToSucc()) {
                            // try to collect the target
                            startCollecting();
                        }
                    } else {
                        // if no target is found spin in place
                        dTrain.getDiffDrive().arcadeDrive(0, 0.5);
                    }
                }
            }
        }
    }

    public boolean TargetExists() {
        if (lastX != xCenter.getDouble(0)) {
            targetPresent = true;
        }
        lastX = xCenter.getDouble(0);
        // TODO Auto-generated method stub
        return targetPresent;
    }

    // if the ball is close enough to the robot to start running the collecor
    private boolean readyToSucc() {
        // TargetSize.getDouble(1000) < RectSize.getDouble(0)
        return (CollectorUltra.getRangeInches() < 15);
    }

    private void startCollecting() {
        System.out.println("------ATTEMPTING TO COLLECT-------");
        time.reset();
        time.start();
        collecting = true;
    }

    private void Collect() {
        if (time.get() > 4.5) {
            collecting = false;
            targetPresent = false;
            collector.ballCollected();
            collector.moveCollector(0);
            dTrain.getDiffDrive().arcadeDrive(0, 0);
        } else if (time.get() > 3.5) {
            collector.moveCollector(0);
            dTrain.getDiffDrive().arcadeDrive(-0.6, 0);
        } else {
            dTrain.getDiffDrive().arcadeDrive(0.3, 0);
            collector.moveCollector(-1);
        }
    }

    private void goToGyro() {
        // set robot to turn to face target from published xPosition from raspberry pi;
        pid.setPID(kp.getDouble(0), ki.getDouble(0), kd.getDouble(0));
        double currentAngle = gyro.gyro.getAngle() % 360;
        double initAngle = gyro.initalAngle % 360;
        System.out.println("Current angle: " + currentAngle + "\t Init Angle: " + initAngle);
        // dTrain.getDiffDrive().arcadeDrive(0, -clamp(pid.calculate(currentAngle,
        // initAngle), -0.5, 0.5));
        double speed = 0;
        if (Math.abs(currentAngle - initAngle) < 15) {
            speed = 0.7;
        } else {
            speed = 0;
        }
        dTrain.getDiffDrive().arcadeDrive(speed, -clamp((currentAngle - initAngle), -0.7, 0.7));

    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

}
