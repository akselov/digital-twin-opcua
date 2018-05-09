/*
 * Copyright (c) 2014, Aalesund University College
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package no.hials;

import java.io.IOException;
import java.util.Scanner;
import no.hials.crosscom.CrossComClient;

/**
 * Simple console application for interfacing with KUKA robots
 *
 * @author Lars Ivar Hatledal
 */
public class ConsoleLaunch {

    /**
     * @param args the command line arguments args[0] = IP address args[1] =
     * port
     * @throws java.io.IOException on IOe error
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.out.println("Error! Both IP and port must be provided as input arguments!");
            System.out.println("Terminating..");
            System.exit(1);
        }

        String address = args[0];
        int port = -1;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            System.out.println("Error on parsing port number! Tried to convert '" + args[1] + "' to int..");
            System.out.println("Terminating..");
            System.exit(1);
        }

        CrossComClient client = null;
        try {
            client = new CrossComClient(address, port);
        } catch (IOException ex) {
            System.out.println(ex);
            System.out.println("Terminating..");
            System.exit(1);
        }

        System.out.println("");
        System.out.println("Aalesund University College - JOpenShowVar Console Application");
        System.out.println("(C) 2014 Aalesund University College");
        System.out.println("All Rights Reserved.");
        System.out.println("Author - Lars Ivar Hatledal [laht@hials.no]");
        System.out.println("");

        System.out.println("Read variable: Type the name of the variable you want to read and hit enter.");
        System.out.println("Example: $OV_JOG");
        System.out.println("Example: $AXIS_ACT");

        System.out.println("Write variable: Type the name of the variable followed by the new value to write and hit enter.");
        System.out.println("Example: $OV_JOG 90");
        System.out.println("Example: MYPOS {X 0.2,Y -0.5,Z 0} ");

        System.out.println("");
        System.out.println("Exit by typing 'q'");

        boolean run = true;
        Scanner sc = new Scanner(System.in);
        while (run) {
            System.out.println("");
            System.out.println("Insert command: ");
            String input = sc.nextLine().trim();

            if (input.equals("q") || input.equals("Q")) {
                System.out.println("Terminating...");
                run = false;
            } else {
                String[] split = input.split(" ");
                if (split.length == 1) {
                    if (!split[0].equals("")) {
                        System.out.println("Got: " + client.simpleRead(split[0]));
                    } else {
                        System.out.println("Error: Empty string!");
                    }
                } else if (split.length == 2) {
                    System.out.println("Got: " + client.simpleWrite(split[0], split[1]));
                } else {
                    System.out.println("Got: " + client.simpleWrite(split[0], input.replaceFirst(split[0], "").trim()));
                }
            }
        }
    }
}
