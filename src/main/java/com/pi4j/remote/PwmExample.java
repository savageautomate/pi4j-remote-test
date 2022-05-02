/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pi4j.remote;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmPolarity;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;
import com.pi4j.util.Frequency;

import java.lang.reflect.InvocationTargetException;

public class PwmExample {

    private static final int PWM_CHANNEL = 0;  // (only supports channel 1 & 1 on RPI)
    private static final Console console = new Console();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        console.box("PWM EXAMPLE -- USING LINUXFS PWM PROVIDER");
        Context pi4j = null;
        try {
            pi4j = Pi4J.newAutoContext();
            //pi4j.config().properties().put("linux.pwm.chip", "2");
            new PwmExample().run(pi4j);
        } catch (InvocationTargetException e) {
            console.println("Error: " + e.getTargetException().getMessage());
        } catch (Exception e) {
            console.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (pi4j != null) {
                pi4j.shutdown();
            }
        }
    }

    private void run(Context pi4j) throws Exception {
        Platforms platforms = pi4j.platforms();

        console.box("Pi4J PLATFORMS");
        console.println();
        platforms.describe().print(System.out);
        console.println();

        var config = Pwm.newConfigBuilder(pi4j)
                .id("my-pwm")
                .name("My PWM Pin")
                .address(PWM_CHANNEL)
                .polarity(PwmPolarity.NORMAL)
                .frequency(Frequency.kilohertz(1))
                .dutyCycle(50)
//                .shutdown(25)
                .pwmType(PwmType.HARDWARE)
                .provider("linuxfs-pwm");

        var pwm = pi4j.create(config);

        // TURN PWM CHANNEL TO [ON]
        pwm.on();

        int counter = 0;
        while (counter < 50) {

            // increase by 1 kHz on each loop iteration
            pwm.on(pwm.dutyCycle(), pwm.frequency() + 1000);

            System.out.println("PWM FREQUENCY        : " + pwm.frequency() + " (HZ)");
            System.out.println("PWM ACTUAL FREQUENCY : " + pwm.actualFrequency() + " (HZ)");
            System.out.println("PWM DUTY-CYCLE       : " + pwm.dutyCycle() + " (%)");
            System.out.println("PWM CHANNEL ON       : " + pwm.isOn());
            System.out.println("-----------------------------------------");

            Thread.sleep(5000);
            counter++;
        }
    }

}
