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
import java.util.Arrays;
import no.hials.crosscom.Callback;
import no.hials.crosscom.CrossComClient;
import no.hials.crosscom.KRL.structs.KRLAxis;
import no.hials.crosscom.KRL.KRLBool;
import no.hials.crosscom.KRL.structs.KRLE6Axis;
import no.hials.crosscom.KRL.KRLEnum;
import no.hials.crosscom.KRL.KRLReal;
import no.hials.crosscom.KRL.KRLVariable;
import no.hials.crosscom.KRL.structs.KRLPos;
import no.hials.crosscom.Request;

/**
 * Test program to see if everything is ok. Remember to set the IP and Port to
 * fit your own setup!
 *
 * @author Lars Ivar Hatledal
 */
public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {

        try (CrossComClient client = new CrossComClient("localhost", 7000)) {

            KRLPos pos = new KRLPos("MYPOS");
            pos.setX(2);
            pos.setY(1);  //MYPOS is defined manually in $config.dat
            pos.setXToZ(1, 2, 3);
            client.writeVariable(pos);
            System.out.println(pos);

            client.readVariable(pos);
            System.out.println(pos);

            KRLAxis axis = new KRLAxis("MYAXIS");
            client.readVariable(axis);
            System.out.println(axis);
            System.out.println(Arrays.toString(axis.asArray()));
            
            KRLE6Axis e6axis = new KRLE6Axis("MYE6AXIS");
            client.readVariable(e6axis);
            System.out.println(e6axis);
            System.out.println(Arrays.toString(e6axis.asArray()));

            KRLE6Axis axisAct = KRLVariable.AXIS_ACT(); // the same as new KRLE6Axis($AXIS_ACT)
            client.readVariable(axisAct);
            System.out.println(axisAct);
            System.out.println(Arrays.toString(axisAct.asArray()));

            KRLReal jog = KRLVariable.OV_JOG(); // the same as new KRLReal($OV_JOG)
            client.readVariable(jog);
            System.out.println(jog);

            KRLReal pro = KRLVariable.OV_PRO();
            client.readVariable(pro);
            System.out.println(pro);

            for (int i = 0; i < 25; i++) {
                pro.setValue(i);
                client.writeVariable(pro);
                System.out.println(pro);
                Thread.sleep(10);
            }

            KRLBool out1 = new KRLBool("$OUT[1]");
            client.readVariable(out1);
            System.out.println(out1);

            KRLEnum mode = new KRLEnum("$MODE_OP");
            client.readVariable(mode);
            System.out.println(mode);

            System.out.println(Arrays.toString(client.readJointAngles()));
            
            
            for (int i = 0; i < 10; i++) {
                Callback sendRequest = client.sendRequest(new Request(0, "MYPOS"));
                System.out.println(sendRequest);
            }
            
            for (int i = 0; i < 10; i++) {
                Callback sendRequest = client.sendRequest(new Request(0, "MYPOS", "{X 10,Y 10,Z 10}"));
                System.out.println(sendRequest);
            }

        }
    }
}
