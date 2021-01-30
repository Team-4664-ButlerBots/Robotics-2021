/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.controller.*;

/**
 * Add your docs here.
 */
public class Vision {

    private DriveTrain dTrain;
    private NetworkTableInstance ntist = NetworkTableInstance.getDefault();
    private NetworkTable visionTable;
    private NetworkTableEntry xCenter, yCenter, RectSize, noTarget, kp, ki, kd, targetDistance, followSpeed;
    PIDController pid = new PIDController(0, 0, 0);

    // interval between calls to track target. This
    private double pidPeriod = 20;

    public Vision(DriveTrain dTrain) {
        this.dTrain = dTrain;
        visionTable = ntist.getTable("vision");
        xCenter = visionTable.getEntry("Xposition");
        yCenter = visionTable.getEntry("Yposition");
        RectSize = visionTable.getEntry("Size");
        noTarget = visionTable.getEntry("NoTarget");
        targetDistance = visionTable.getEntry("TargetDistance");
        followSpeed = visionTable.getEntry("followSpeed");

        // instantiate the values
        kp = visionTable.getEntry("kp");
        ki = visionTable.getEntry("ki");
        kd = visionTable.getEntry("kd");
        // attempt to grab existing values from network tables otherwise use default of
        // 0
        kp.setDouble(kp.getDouble(0));
        ki.setDouble(ki.getDouble(0));
        kd.setDouble(kd.getDouble(0));
        pid.setPID(kp.getDouble(0), ki.getDouble(0), kd.getDouble(0));
        targetDistance.setDouble(targetDistance.getDouble(0));
        followSpeed.setDouble(followSpeed.getDouble(0));
    }

    // used to track time since tracking is lost
    long time;

    public void LookAtTarget() {
        if (!noTarget.getBoolean(true)) {
            // set robot to turn to face target from published xPosition from raspberry pi;
            pid.setPID(kp.getDouble(0), ki.getDouble(0), kd.getDouble(0));
            dTrain.getDiffDrive().arcadeDrive(0, pid.calculate(0, (xCenter.getDouble(0) - 0.5) * 2));
        } else {
            dTrain.getDiffDrive().tankDrive(0, 0);
        }
    }

    public void FollowTarget() {
        if (!noTarget.getBoolean(true)) {
            double speed = (RectSize.getDouble(0) - targetDistance.getDouble(0)) * followSpeed.getDouble(0);

            // set robot to turn to face target from published xPosition from raspberry pi;
            pid.setPID(kp.getDouble(0), ki.getDouble(0), kd.getDouble(0));
            dTrain.getDiffDrive().arcadeDrive(speed * yCenter.getDouble(0), pid.calculate(0, (xCenter.getDouble(0) - 0.5) * 2));
        } else {
            dTrain.getDiffDrive().tankDrive(0, 0);
        }
    }

}
