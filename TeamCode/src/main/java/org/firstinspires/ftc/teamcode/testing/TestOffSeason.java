package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Test: Off-Season", group = "Testing")
public class TestOffSeason extends OpMode {

    @Override
    public void init() {
        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        telemetry.addLine("Running");
        telemetry.addData("Runtime", String.format("%.2f", getRuntime()));
        telemetry.update();
    }
}
