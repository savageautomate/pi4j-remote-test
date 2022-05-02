/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pi4j.remote;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;

import java.lang.reflect.InvocationTargetException;

public class DigitalOutputExample {

    private static final int GPIO_PIN = 26;
    private static final Console console = new Console();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        console.box("DIGITAL OUTPUT [GPIO] EXAMPLE -- USING LINUXFS DIGITAL OUTPUT PROVIDER");
        Context pi4j = null;
        try {
            pi4j = Pi4J.newAutoContext();
            new DigitalOutputExample().run(pi4j);
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

        // create digital output I/O configuration
        var config = DigitalOutput.newConfigBuilder(pi4j)
                        .id("my-dout")
                        .name("My Digital Output")
                        .address(GPIO_PIN)
                        .shutdown(DigitalState.LOW)
                        .initial(DigitalState.HIGH)
                        .provider("linuxfs-digital-output");

        // create digital output I/O instance using configuration
        var output = pi4j.create(config);

        int counter = 0;
        while (counter < 500) {

            System.out.println();
            System.out.println("-----------------------------------------");
            System.out.println("DIGITAL OUTPUT STATE : " + output.state().getName());
            System.out.println("-----------------------------------------");

            // toggle output state
            output.toggle();

            // one second rest
            Thread.sleep(1000);

            // increment loop iteration counter
            counter++;
        }
    }

}
