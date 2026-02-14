package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LEDConstants;
import frc.robot.Constants.LEDConstants.Colors;
import static edu.wpi.first.units.Units.*;

public class AlternateLED extends SubsystemBase {
    private final AddressableLED m_led;
    private final AddressableLEDBuffer m_buffer;
    /**
     * Tracks Wave Movement
     */
    private double m_offset = 0;
    private static final Color kRed = Color.kFirstRed;
    private static final Color kBlue = Color.kFirstBlue;
    private static final Color kGold = new Color(255, 215, 0);

    public AlternateLED() {
        m_led = new AddressableLED(LEDConstants.kPWMPort);
        m_buffer = new AddressableLEDBuffer(LEDConstants.kLEDLength);
        m_led.setLength(m_buffer.getLength());
        m_led.setData(m_buffer);
        m_led.start();
    }

    public void setRedGoldWave() {
        LEDPattern wave = LEDPattern.gradient(
                LEDPattern.GradientType.kContinuous,
                Colors.kRed.color,
                Colors.kGold.color
        );

        wave.scrollAtRelativeSpeed(Hertz.of(0.5))
                .applyTo(m_buffer);
        m_led.setData(m_buffer);
    }
}
