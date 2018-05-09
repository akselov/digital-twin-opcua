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
import no.hials.crosscom.Callback;
import no.hials.crosscom.CrossComClient;
import no.hials.crosscom.KRL.KRLReal;
import no.hials.crosscom.KRL.KRLVariable;
import no.hials.crosscom.Request;

/**
 * Test program to see if everything is ok. Remember to set the IP and Port to
 * fit your own setup!
 *
 * @author Lars Ivar Hatledal
 */
public class Test2 {

    /**
     * Comparison between v0.1 and v0.2
     *
     * @param args none
     * @throws IOException on IO error
     */
    public static void main(String[] args) throws IOException {
        try (CrossComClient client = new CrossComClient("localhost", 7000)) {

            //Comparison between v0.1 and v0.2
            //v0.1 read
            Callback readRequest = client.sendRequest(new Request(0, "$OV_JOG")); //read request
            System.out.println(readRequest);

            //v0.1 write
            Callback writeRequest = client.sendRequest(new Request(1, "$OV_JOG", "50")); //write request
            System.out.println(writeRequest);

            //v0.2 read
            KRLReal jog = KRLVariable.OV_JOG();
            client.readVariable(jog);
            System.out.println(jog);

            //v0.2 write
            jog.setValue(10);
            client.writeVariable(jog);
            System.out.println(jog);
        }
    }
}
