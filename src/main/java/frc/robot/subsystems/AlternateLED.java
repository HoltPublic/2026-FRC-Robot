package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LEDConstants;
import frc.robot.Constants.LEDConstants.Patterns;
import frc.robot.Constants.LEDConstants.UniquePatterns;

import java.sql.Driver;

/**
 * A custom LED Class that allows for ease of changing colors
 * This Class is Unused as we made a collective decision to use the REV Blinkin Class
 */
public class AlternateLED extends SubsystemBase {
    private final AddressableLED m_led;
    private final AddressableLEDBuffer m_buffer;
    private boolean m_isFiring =false;

    private LEDPattern m_currentPattern = Patterns.kNotEnabledWave;

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
        if (DriverStation.isDisabled()){
            setPattern(Patterns.kNotEnabledWave);
        } else if (LEDConstants.kIs2026){
            ledSignal2026();
        } else {
            updateLEDSignals();
        }
        if (m_currentPattern != null){
            m_currentPattern.applyTo(m_buffer);
            m_led.setData(m_buffer);
        }
    }

    public void setAlliancePattern(){
        if (DriverStation.isDisabled()){
            setPattern(Patterns.kNotEnabledWave);
            return;
        }
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()){
            if (alliance.get() == DriverStation.Alliance.Red){
                setPattern(Patterns.kRedGoldWave);
            } else {
                setPattern(Patterns.kBlueGoldWave);
            }
        } else {
            setPattern(Patterns.kNotEnabledWave);
        }
    }
    public void setGoonettesPattern(){
        if (DriverStation.isDisabled()){
            setPattern(Patterns.kNotEnabledWave);
            return;
        }
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()){
            if (alliance.get() == DriverStation.Alliance.Red){
                setPattern(Patterns.kDarkHotPinkGoldWave);
            } else {
                setPattern(Patterns.kDarkPurpleGoldWave);
            }
        } else {
            setPattern(Patterns.kNotEnabledWave);
        }
    }

    public void updateLEDSignals() {
    if (DriverStation.isDisabled()){
        setPattern(Patterns.kNotEnabledWave);
    } else {
        easyChoice(LEDConstants.kChoice);
    }
    }

    /**
     * Effectively a copy of my Blinkin updateLEDSignals code, but this is just an example for
     */
    public void ledSignal2026(){
        if (DriverStation.isDisabled()){
            setPattern(Patterns.kNotEnabledWave);
            return;
        }
        else {
            if (m_isFiring){
                System.out.println("Uhh, I haven't made any cool patterns for firing");
            } else if (DriverStation.isTeleopEnabled()){
                double time = DriverStation.getMatchTime();
                String gameData = DriverStation.getGameSpecificMessage();
                if (gameData.isEmpty() || time > 130 || time <= 30){
                    easyChoice(LEDConstants.kChoice);
                    return;
                }
                var myAlliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Red);
                boolean weAreInactiveFirst = gameData.startsWith(myAlliance == DriverStation.Alliance.Red ? "B" : "R");
                int shift;
                if (time > 105) shift = 1;
                else if (time > 80) shift = 2;
                else if (time > 55) shift = 3;
                else shift = 4;
                if (shift == 1 || shift == 3){
                    if (weAreInactiveFirst) phaseChoice(LEDConstants.kChoice);
                    else easyChoice(LEDConstants.kChoice);
                } else {
                    if (weAreInactiveFirst) easyChoice(LEDConstants.kChoice);
                    else phaseChoice(LEDConstants.kChoice);
                }
            } else if (DriverStation.isAutonomousEnabled()) {
                easyChoice(LEDConstants.kChoice);
            }
        }
    }

    /**
     * In the event that there's a future game with phase changes, this code changes the pattern to one where the LEDs change when you're active
     */
    public void phaseModeStandard(){
        var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Red);

        if (alliance == DriverStation.Alliance.Red){
            setPattern(Patterns.kRedGreenWave);
        } else {
            setPattern(Patterns.kBlueGreenWave);
        }
    }

    /**
     * Basically a thing for if there's phase changes in a game, and we're at Goonettes. But effectively the same as {@link #phaseModeStandard()}
     * @see #phaseModeStandard()
     */
    public void phaseModeGoonettes(){
        var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Red);
        if (alliance == DriverStation.Alliance.Red){
            setPattern(Patterns.kDarkHotPinkGreenWave);
        } else {
            setPattern(Patterns.kDarkPurpleGreenWave);
        }
    }

    public void easyChoice(String choice){
        switch (choice){
            case "default":
                setAlliancePattern();
                break;
            case "goonettes":
                setGoonettesPattern();
                break;
            default:
                setPattern(Patterns.kNotEnabledWave);
                break;
        }
    }
    public void phaseChoice(String choice){
        switch (choice){
            case "default":
                phaseModeStandard();
                break;
            case "goonettes":
                phaseModeGoonettes();
                break;
            default:
                setPattern(Patterns.kNotEnabledWave);
                break;
        }
    }
}
