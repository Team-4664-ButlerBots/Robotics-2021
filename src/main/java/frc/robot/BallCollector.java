// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import java.util.stream.Collector;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

/** This class will handle tracking current state of balls inside Collector
 * it will stop the collector from moving if the balls are bunched up. Legally
 * our robot can also only hold 3 balls at a time, so this class will ensure we can't
 * pick up more than that. 
*/
public class BallCollector {
    private Victor CollectorMC = new Victor(3);
    private DigitalInput collectorSwitch = new DigitalInput(0);
    //tracks how many balls are currenty in the robot
    private int currentBallCount;

    //manual overide move collector.
    public void moveCollector(double speed){
        //if we are moving the belt forward
        if(speed > 0){
            //check if the balls won't jam
            if(!ballsWillJam()){
                //if the balls don't jam then proceed with moving the collector forward
                CollectorMC.setSpeed(speed);
            }else{
                //otherwise stop the collector
                CollectorMC.setSpeed(0);
            }
        }

        if(collectorSwitch.get()){
            currentBallCount++;
        }
        
    }

    /**
     * this is run when a ball is shot, it decrements the ball count by one. 
     */
    public void ballShot(){

    }

    /**
     * returns true when two balls are up against the shooting mechanism. 
     * in this case the belt cannont move forward without first shooting. 
     * @return
     */
    public boolean ballsWillJam(){
        return false;
    }

}
