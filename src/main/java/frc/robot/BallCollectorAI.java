// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.stream.Collector;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

/**
 * This class handles various autonomous modes.
 */
public class BallCollectorAI {
    private DriveTrain dTrain;
    private Ultrasonic ultrasonic = new Ultrasonic(5, 6);
    private BallCollector collector;
    // tracks the current state of the AI
    private enum State {
        Searching, Tracking, collecting
    };

    private State state = State.Searching;
    private Vision vision;
    // tracks the seconds into each state
    private Timer timer;

    public BallCollectorAI(DriveTrain dTrain, Vision vision, BallCollector collector) {
        this.dTrain = dTrain;
        this.vision = vision;
        this.collector = collector;
    }

    // the root loop for collecting balls autonomously
    public void CollectBalls() {
        switch (state) {
            case Searching:
                if(vision.TargetExists()){
                    startTracking();
                }else{
                    dTrain.getDiffDrive().arcadeDrive(0, 0.5);
                }
                break;
            case Tracking:
                if(vision.getDistance() > 1){
                    vision.FollowTarget();
                }else{
                    startCollecting();
                }
                break;
            case collecting:
                if(ultrasonic.getRangeInches() < 18){
                    collector.moveCollector(1);
                }
                if(timer.get() < 4){
                    dTrain.getDiffDrive().arcadeDrive(0.75, 0);
                }else{
                    dTrain.getDiffDrive().arcadeDrive(0, 0);
                    startSearching();
                }
                break;
            default:
                break;
        }
    }

    public void startSearching(){
        state = State.Tracking;
        timer.reset();
    }

    public void startTracking(){
        state = State.Tracking;
        timer.reset();
    }

    public void startCollecting(){
        state = State.Tracking;
        timer.reset();
    }
}
