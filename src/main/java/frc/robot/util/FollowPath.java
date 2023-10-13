package frc.robot.util;

import java.util.HashMap;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.Drivetrain;

public class FollowPath {  
    PathPlannerTrajectory examplePath = PathPlanner.loadPath("Example Path", new PathConstraints(4, 3));
    Drivetrain sDrivetrain = Drivetrain.getInstance();

    FollowPathWithEvents command = new FollowPathWithEvents(
        sDrivetrain.followTrajectoryCommand(examplePath, false),
        examplePath.getMarkers(),
        (HashMap<String, Command>)AutoConstants.eventMap
    );

    public Command getFollowCommand(){return command;}
}