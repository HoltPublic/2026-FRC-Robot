package frc.robot.Utils;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NTDouble {
    private DoublePublisher pub;

    public NTDouble(String name) {
        pub = 
            NetworkTableInstance.getDefault()
                .getDoubleTopic(name)
                    .publish();
    }

    public void set(double value) {
        pub.set(value);
    } 
}
