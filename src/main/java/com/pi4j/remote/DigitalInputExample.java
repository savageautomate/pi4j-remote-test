/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pi4j.remote;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;

import java.lang.reflect.InvocationTargetException;


public class DigitalInputExample {

    private static final int GPIO_PIN = 26;
    private static final Console console = new Console();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        console.box("DIGITAL INPUT [GPIO] EXAMPLE -- USING LINUXFS DIGITAL INPUT PROVIDER");
        Context pi4j = null;
        try {
            pi4j = Pi4J.newAutoContext();
            new DigitalInputExample().run(pi4j);
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

        var config = DigitalInput.newConfigBuilder(pi4j)
                        .id("led")
                        .name("LED Flasher")
                        .address(GPIO_PIN)
                        .pull(PullResistance.PULL_DOWN)
                        .provider("linuxfs-digital-input");

        // create digital iput I/O instance using configuration
        var input = pi4j.create(config);

        int counter = 0;
        while (counter < 500) {

            System.out.println();
            System.out.println("-----------------------------------------");
            System.out.println("DIGITAL INPUT STATE : " + input.state().getName());
            System.out.println("-----------------------------------------");

            // one second rest
            Thread.sleep(1000);

            // increment loop iteration counter
            counter++;
        }
    }
}
