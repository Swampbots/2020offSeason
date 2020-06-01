package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.OffSeasonHardware;

@Autonomous(name = "Test: Robot Drive Square", group = "Testing")
public class TestRobotDriveSquare extends LinearOpMode {

    // Hardware
    OffSeasonHardware hardware = new OffSeasonHardware();
    private final boolean INIT_IMU = true;

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing");
        telemetry.update();

        hardware.init(hardwareMap, INIT_IMU);

        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        telemetry.addLine("Running");
        telemetry.update();


        while(opModeIsActive()) {
            telemetry.addLine("Finished");
            telemetry.update();
        }


    }



}
