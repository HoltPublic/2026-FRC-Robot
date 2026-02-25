// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.util.Color;

import java.util.Map;

import static edu.wpi.first.units.Units.Hertz;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  /**
   * Constants related to the Drive Team
   */
  public static class OIConstants {
    public static final int kDriverControllerPort = 0;
  }

  /**
   * Constants related to the REV Blinkin
   */
  public static class BlinkinConstants {

    /**
     * PWM Port on the RoboRIO that the Blinkin is plugged into. The below image points out the PWM area <br>
     * <img src="doc-files/roborioPWM.png">
     */
    public static final int kPwmPort = 0;
    /**
     * A specified LED Choice for the Blinkin, it pretty much allows for the choice between Default (Red & Blue), Goonettes (Pink & Purple), or a Custom Color
     */
    public static final String kLedChoice = "default"; //Have this value set to either default, goonettes, or custom

    /**
     * Pretty much a bunch of pre-made patterns
     * @see <a href="https://1166281274-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2F-ME3KPEhFI6-MDoP9nZD%2Fuploads%2FMOYJvZmWgxCVKJhcV5fn%2FREV-11-1105-LED-Patterns.pdf?alt=media&token=e8227890-6dd3-498d-834a-752fa43413fe">REV Blinkin Documentation</a>
     */
    public static enum blinkinPattern {
      //Fixed Palette Patterns
      RAINBOW_RAINBOW_PALETTE(-0.99, "(Fixed) Rainbow, Rainbow"),
      RAINBOW_PARTY_PALETTE(-0.97, "(Fixed) Rainbow, Party"),
      RAINBOW_OCEAN_PALETTE(-0.95, "(Fixed) Rainbow, Ocean"),
      RAINBOW_LAVA_PALETTE(-0.93, "(Fixed) Rainbow, Lava"),
      RAINBOW_FOREST_PALETTE(-0.91, "(Fixed) Rainbow, Forest"),
      RAINBOW_WITH_GLITTER(-0.89, "(Fixed) Rainbow with Glitter"),
      CONFETTI(-0.87, "(Fixed) Confetti"),
      SHOT_RED(-0.85, "(Fixed) Shot, Red"),
      SHOT_BLUE(-0.83, "(Fixed) Shot, Blue"),
      SHOT_WHITE(-0.81, "(Fixed) Shot, White"),
      SINELON_RAINBOW_PALETTE(-0.79, "(Fixed) Sinelon, Rainbow"),
      SINELON_PARTY_PALETTE(-0.77, "(Fixed) Sinelon, Party"),
      SINELON_OCEAN_PALETTE(-0.75, "(Fixed) Sinelon, Ocean"),
      SINELON_LAVA_PALETTE(-0.73, "(Fixed) Sinelon, Lava"),
      SINELON_FOREST_PALETTE(-0.71, "(Fixed) Sinelon, Forest"),
      BEATS_PER_MINUTE_RAINBOW_PALETTE(-0.69, "(Fixed) BPM, Rainbow"),
      BEATS_PER_MINUTE_PARTY_PALETTE(-0.67, "(Fixed) BPM, Party"),
      BEATS_PER_MINUTE_OCEAN_PALETTE(-0.65, "(Fixed) BPM, Ocean"),
      BEATS_PER_MINUTE_LAVA_PALETTE(-0.63, "(Fixed) BPM, Lava"),
      BEATS_PER_MINUTE_FOREST_PALETTE(-0.61, "(Fixed) BPM, Forest"),
      FIRE_MEDIUM(-0.59, "(Fixed) Medium Fire"),
      FIRE_LARGE(-0.57, "(Fixed) Large Fire"),
      TWINKLES_RAINBOW_PALETTE(-0.55, "(Fixed) Twinkles, Rainbow"),
      TWINKLES_PARTY_PALETTE(-0.53, "(Fixed) Twinkle, Party"),
      TWINKLES_OCEAN_PALETTE(-0.51, "(Fixed) Twinkle, Ocean"),
      TWINKLES_LAVA_PALETTE(-0.49, "(Fixed) Twinkles, Lava"),
      TWINKLES_FOREST_PALETTE(-0.47, "(Fixed) Twinkles, Forest"),
      COLOR_WAVES_RAINBOW_PALETTE(-0.45, "(Fixed) Color Waves, Rainbow"),
      COLOR_WAVES_PARTY_PALETTE(-0.43, "(Fixed) Color Waves, Party"),
      COLOR_WAVES_OCEAN_PALETTE(-0.41, "(Fixed) Color Waves, Ocean"),
      COLOR_WAVES_LAVA_PALETTE(-0.39, "(Fixed) Color Waves, Lava"),
      COLOR_WAVES_FOREST_PALETTE(-0.37, "(Fixed) Color Waves, Forest"),
      LARSON_SCANNER_RED(-0.35, "(Fixed) Larson Scanner, Red"),
      LARSON_SCANNER_GRAY(-0.33, "(Fixed) Larson Scanner, Gray"),
      LIGHT_CHASE_RED(-0.31, "(Fixed) Light Chase, Red"),
      LIGHT_CHASE_BLUE(-0.29, "(Fixed) Light Chase, Blue"),
      LIGHT_CHASE_GRAY(-0.27, "(Fixed) Light Chase, Gray (Or Grey if you prefer British)"),
      HEARTBEAT_RED(-0.25, "(Fixed) Heartbeat, Red"),
      HEARTBEAT_BLUE(-0.23, "(Fixed) Heartbeat, Blue"),
      HEARTBEAT_WHITE(-0.21, "(Fixed) Heartbeat, White"),
      HEARTBEAT_GRAY(-0.19, "(Fixed) Heartbeat, Gray"),
      BREATH_RED(-0.17, "(Fixed) Breath, Red"),
      BREATH_BLUE(-0.15, "(Fixed) Breath, Blue"),
      BREATH_GRAY(-0.13, "(Fixed) Breath, Gray (Or Grey if you prefer British)"),
      STROBE_RED(-0.11, "(Fixed) Strobe, Red (Probably don't use this one)"),
      STROBE_BLUE(-0.09, "(Fixed) Strobe, Blue (Probably don't use this one)"),
      STROBE_GOLD(-0.07, "(Fixed) Strobe, Gold (Even though this is our team color, please don't use this one)"),
      STROBE_WHITE(-0.05, "(Fixed) Stobe, White (I feel this one would require an epilepsy warning)"),
      //Solid Color
      HOT_PINK(+0.57, "(Solid) Hot Pink"),
      DARK_RED(+0.59, "(Solid) Dark Red"),
      RED(+0.61, "(Solid) Red"),
      RED_ORANGE(+0.63, "(Solid) Red-Orange"),
      ORANGE(+0.65, "(Solid) Orange"),
      GOLD(+0.67, "(Solid) Gold"), //Oh come on, why does gold have a value of 0.67?
      YELLOW(+0.69, "(Solid) Yellow"),
      LAWN_GREEN(+0.71, "(Solid) Lawn Green"),
      LIME(+0.73, "(Solid) Lime"),
      DARK_GREEN(+0.75, "(Solid) Dark Green"),
      GREEN(+0.77, "(Solid) Green"),
      BLUE_GREEN(+0.79, "(Solid) Blue-Green"),
      AQUA(+0.81, "(Solid) Aqua"),
      SKY_BLUE(+0.83, "(Solid) Sky Blue"),
      DARK_BLUE(+0.85, "(Solid) Dark Blue"),
      BLUE(+0.87, "(Solid) Blue"),
      BLUE_VIOLET(+0.89, "(Solid) Somewhere between Blue and Purple"),
      VIOLET(+0.91, "(Solid) Purple (Technically Violet)"),
      WHITE(+0.93, "(Solid) White"),
      GRAY(+0.95, "(Solid) Gray (Or Grey if you prefer British spelling)"),
      DARK_GRAY(+0.97, "(Solid) Dark Gray (Or Dark Grey if you prefer British spelling)"),
      BLACK(+0.99, "Off");

      public final double value;
      public final String displayName;

      /**
       * Constructor that allows you to see the value and display name of the Blinkin Pattern
       * @param value A double that represents the actual value of each pattern, it's the roboRIO SPARK Value
       * @param displayName Pretty much just a string so that the Options are more or less human-readable
       */
      private blinkinPattern(double value, String displayName){
        this.value = value;
        this.displayName = displayName;
      }
    }

  }

  /**
   * Constants for if we wanted to use the {@link frc.robot.subsystems.AlternateLED AddressableLED} option. Not used in the 2026 season, but if anyone wants to use it in the future, I'll leave this here for future students - Riley
   */
  public static class LEDConstants {
    public static final int kPWMPort = 0;
    public static final int kLEDLength = 60;

    public enum Colors {
      kGold(new Color(212, 175, 55), "Gold"),
      kRed(Color.kFirstRed, "Red"),
      kBlue(Color.kFirstBlue, "Blue"),
      kOrange(Color.kOrange, "Orange"),
      kBlack(Color.kBlack, "Off"),
      kGreen(Color.kGreen, "Green"),
      kWhite(Color.kWhite, "White"),
      kGray(new Color(163, 163, 163), "Gray"),
      kLime(new Color(185, 251, 106), "Lime");


      public final Color color;
      public final String displayName;

      Colors(Color color, String displayName){
        this.color = color;
        this.displayName = displayName;
      }
    }

    /**
     * A class to hold a bunch of fairly standard patterns I made
     */
    public static final class Patterns{
      /**
       * A Red-Gold Wave Pattern
       */
      public static final LEDPattern kRedGoldWave = LEDPattern.gradient(
              LEDPattern.GradientType.kContinuous,
              Colors.kRed.color,
              Colors.kGold.color
      ).scrollAtRelativeSpeed(Hertz.of(0.5));
      /**
       * A Blue-Gold Wave Pattern
       */
      public static final LEDPattern kBlueGoldWave = LEDPattern.gradient(
              LEDPattern.GradientType.kContinuous,
              Colors.kBlue.color,
              Colors.kGold.color
      ).scrollAtRelativeSpeed(Hertz.of(0.5));
      /**
       * A Red-Green Wave Pattern
       */
      public static final LEDPattern kRedGreenWave = LEDPattern.gradient(
              LEDPattern.GradientType.kContinuous,
              Colors.kRed.color,
              Colors.kGreen.color
      );
      /**
       * A Blue-Green Wave Pattern
       */
      public static final LEDPattern kBlueGreenWave = LEDPattern.gradient(
        LEDPattern.GradientType.kContinuous,
        Colors.kBlue.color,
        Colors.kGreen.color
      );

    }

    /**
     * Unique Patterns, I would assume that these would be used for Rainbow Rumble, but honestly, if you want to make any other unique patterns, I suppose use this as a template
     */
    public static final class UniquePatterns{
      /**
       * A Pattern of the Agender Flag :)
       */
      public static final LEDPattern kAgenderPattern = LEDPattern.steps(Map.of(0.0, Colors.kBlack.color, 0.14, Colors.kGray.color, 0.28, Colors.kWhite.color, 0.42, Colors.kLime.color, 0.57, Colors.kWhite.color, 0.71, Colors.kGray.color, 0.85, Colors.kBlack.color));
      /**
       * A Wave pattern of {@link #kAgenderPattern}
       */
      public static final LEDPattern kAgenderWave = kAgenderPattern.scrollAtRelativeSpeed(Hertz.of(0.5));


    }
  }
}