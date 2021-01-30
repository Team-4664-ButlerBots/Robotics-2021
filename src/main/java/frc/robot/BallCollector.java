/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;

public class BallCollector {

    private ControllerManager cManager;

    public BallCollector(ControllerManager cManager){
        this.cManager = cManager;
    }

    private Victor collector = new Victor(3);

    //meathod to run collector motor from controller
    public void opRunCollector(){
        collector.set(cManager.collectorInput());
    }

    



}
