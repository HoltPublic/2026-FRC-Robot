package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LEDConstants;
import frc.robot.Constants.LEDConstants.Colors;
import static edu.wpi.first.units.Units.*;

/**
 * A custom LED Class that allows for ease of changing colors
 * This Class is Unused as we made a collective decision to use the REV Blinkin Class
 */
public class AlternateLED extends SubsystemBase {
    private final AddressableLED m_led;
    private final AddressableLEDBuffer m_buffer;

    private LEDPattern m_currentPattern = LEDConstants.Patterns.kRedGoldWave;

    public AlternateLED() {
        m_led = new AddressableLED(LEDConstants.kPWMPort);
        m_buffer = new AddressableLEDBuffer(LEDConstants.kLEDLength);
        m_led.setLength(m_buffer.getLength());
        m_led.setData(m_buffer);
        m_led.start();
    }

    /**
     * Updates the currently displayed pattern
     * @param pattern A {@link LEDPattern pattern} of which you'll want to grab from {@link LEDConstants.Patterns the Patterns Class}
     */
    public void setPattern(LEDPattern pattern){
        m_currentPattern = pattern;
    }

    @Override
    public void periodic(){
        m_currentPattern.applyTo(m_buffer);
        m_led.setData(m_buffer);
    }



}
