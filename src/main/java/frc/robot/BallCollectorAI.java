// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/** 
 * This class handles various autonomous modes. 
 */
public class BallCollectorAI {
    private DriveTrain dTrain;
    //tracks the current state of the AI
    private enum state {Searching, Tracking, collecting};
    //tracks the seconds into each state
    private double seconds;

    public BallCollectorAI(DriveTrain dTrain){
        this.dTrain = dTrain;
    }

    //the root loop for collecting balls autonomously
    public void CollectBalls(){

    }
}
