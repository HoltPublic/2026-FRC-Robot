// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.util.Color;

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
      //CP1: Color 1 Patterns
      CP1_END_TO_END_BLEND_TO_BLACK(-0.03, "(Color Pattern 1) End to End, Blend to Black"),
      CP1_LARSON_SCANNER(-0.01, "(Color Pattern 1) Larson Scanner"),
      CP1_LIGHT_CHASE(+0.01, "(Color Pattern 1) Light Chase"),
      CP1_HEARTBEAT_SLOW(+0.03, "(Color Pattern 1) Heartbeat, Slow"),
      CP1_HEARTBEAT_MEDIUM(+0.05, "(Color Pattern 1) Heartbeat, Medium"),
      CP1_HEARTBEAT_FAST(+0.07, "(Color Pattern 1) Heartbeat, Fast"),
      CP1_BREATH_SLOW(+0.09, "(Color Pattern 1) Breath, Slow"),
      CP1_BREATH_FAST(+0.11, "(Color Pattern 1) Breath, Fast"),
      CP1_SHOT(+0.13, "(Color Pattern 1) Shot"),
      CP1_STROBE(+0.15, "(Color Pattern 1) Strobe (Please Don't)"),
      //CP2: Color 2 Patterns
      CP2_END_TO_END_BLEND_TO_BLACK(+0.17, "(Color Pattern 2) End to End, Blend to Black"),
      CP2_LARSON_SCANNER(+0.19, "(Color Pattern 2) Larson Scanner"),
      CP2_LIGHT_CHASE(+0.21, "(Color Pattern 2) Light Chase"),
      CP2_HEARTBEAT_SLOW(+0.23, "(Color Pattern 2) Heartbeat, Slow"),
      CP2_HEARTBEAT_MEDIUM(+0.25, "(Color Pattern 2) Heartbeat, Medium"),
      CP2_HEARTBEAT_FAST(+0.27, "(Color Pattern 2) Heartbeat, Fast"),
      CP2_BREATH_SLOW(+0.29, "(Color Pattern 2) Breath, Slow"),
      CP2_BREATH_FAST(+0.31, "(Color Pattern 2) Breath, Fast"),
      CP2_SHOT(+0.33, "(Color Pattern 2) Shot"),
      CP2_STROBE(+0.35, "(Color Pattern 2) Strobe (Please Don't)"),
      //CP1_2: Color 1 and 2 Patterns
      CP1_2_SPARKLE_1_ON_2(+0.37, "(Color Patterns 1 & 2) Sparkle (Color 1 on Color 2)"),
      CP1_2_SPARKLE_2_ON_1(+0.39, "(Color Patterns 1 & 2) Sparkle (Color 2 on Color 1)"),
      CP1_2_COLOR_GRADIENT(+0.41, "(Color Patterns 1 & 2) Color Gradient (Color 1 and 2)"),
      CP1_2_BEATS_PER_MINUTE(+0.43, "(Color Patterns 1 & 2) BPM (Color 1 and 2)"),
      CP1_2_END_TO_END_BLEND_1_TO_2(+0.45, "(Color Patterns 1 & 2)"),
      CP1_2_END_TO_END_BLEND(+0.47, "(Color Patterns 1 & 2) End to End (Blending Color 1 and Color 2)"),
      CP1_2_NO_BLENDING(+0.49, "(Color Patterns 1 & 2) Color 1 and Color 2 without color blending (Setup Pattern)"),
      CP1_2_TWINKLES(+0.51, "(Color Patterns 1 & 2) Twinkles (Color 1 and 2)"),
      CP1_2_COLOR_WAVES(+0.53, "(Color Patterns 1 & 2) Color Waves (Color 1 and 2"),
      CP1_2_SINELON(+0.55, "(Color Patterns 1 & 2) Sinelon (Color 1 and 2)"),
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

    /**
     * A Bunch of custom patterns to represent the LGBTQ+ Community. I, myself, am Agender, so I figured why not.
     */
    public static enum lgbtqPatterns {
      TRANS()
    }
  }

  public static class LEDConstants {
    public static final int kPWMPort = 0;
    public static final int kLEDLength = 60;

    public enum Colors {
      kGold(new Color(212, 175, 55), "Gold"),
      kRed(Color.kFirstRed, "Red"),
      kBlue(Color.kFirstBlue, "Blue"),
      kOrange(Color.kOrange, "Orange"),
      kBlack(Color.kBlack, "Off"),
      kGreen(Color.kGreen, "Green");


      public final Color color;
      public final String displayName;

      Colors(Color color, String displayName){
        this.color = color;
        this.displayName = displayName;
      }
    }
  }
}