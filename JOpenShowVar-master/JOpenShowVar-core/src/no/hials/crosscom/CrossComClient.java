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
package no.hials.crosscom;

import no.hials.crosscom.KRL.KRLVariable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import no.hials.crosscom.KRL.structs.KRLE6Axis;

/**
 * The Client used to communicate with KukaVarProxy
 *
 * @author Lars Ivar Hatledal
 */
public final class CrossComClient extends Socket {

    BufferedInputStream bis = new BufferedInputStream(getInputStream());
    BufferedOutputStream bos = new BufferedOutputStream(getOutputStream());

    private final KRLE6Axis axis_act = new KRLE6Axis("$AXIS_ACT");

    public CrossComClient(String host, int port) throws UnknownHostException, IOException {
        super(host, port);
    }

    /**
     * Simple read. No parsing of the return is done
     * @param name the name of the variable to read
     * @return the string we get from KUKAVARPROXY 
     * @throws IOException if something bad happens
     */
    public String simpleRead(String name) throws IOException {
        int id = 99; //just some number
        byte[] cmd = name.getBytes();
        List<Byte> header = new ArrayList<>();
        List<Byte> block = new ArrayList<>();

        byte hbyte = (byte) ((cmd.length & 0xff00)>>8); 
        byte lbyte = (byte) (cmd.length & 0x00ff);

        int index = 0;
        block.add(index++, (byte) 0);
        block.add(index++, hbyte);
        block.add(index++, lbyte);
        for (int i = 0; i < cmd.length; i++) {
            block.add(index++, cmd[i]);
        }
        hbyte = (byte) ((block.size() & 0xff00)>>8); 
        lbyte = (byte) (block.size() & 0x00ff);

        byte hbytemsg = (byte) ((id & 0xff00)>>8); 
        byte lbytemsg = (byte) (id & 0x00ff);

        index = 0;
        header.add(index++, hbytemsg);
        header.add(index++, lbytemsg);
        header.add(index++, hbyte);
        header.add(index++, lbyte);

        block.addAll(0, header);

        for (byte b : block) {
            bos.write(b);
        }
        bos.flush();

        long t0 = System.nanoTime();
        byte[] recheader = new byte[7];
        bis.read(recheader);

        byte[] recblock = new byte[getInt(recheader, 2)];
        bis.read(recblock);

        byte[] data = new byte[recheader.length + recblock.length];
        System.arraycopy(recheader, 0, data, 0, recheader.length);
        System.arraycopy(recblock, 0, data, recheader.length, recblock.length);

        long readTime = (System.nanoTime() - t0) / 1000000;

        return name + " #read time=" + readTime + "ms  #value=" + new String(Arrays.copyOfRange(data, 7, data.length)).trim();
    }

    /**
     * Simple write. No parsing of the return is done
     * @param name the name of the variable to write to
     * @param val the value to write
     * @return the string we get from KUKAVARPROXY 
     * @throws IOException if something bad happens
     */
    public String simpleWrite(String name, String val) throws IOException {
        int id = 99; //just some number
        byte[] cmd = name.getBytes();
        byte[] value = val.getBytes();
        List<Byte> header = new ArrayList<>();
        List<Byte> block = new ArrayList<>();

        byte hbyte = (byte) ((cmd.length & 0xff00)>>8); 
        byte lbyte = (byte) (cmd.length & 0x00ff);

        int index = 0;
        block.add(index++, (byte) 1);
        block.add(index++, hbyte);
        block.add(index++, lbyte);
        for (int i = 0; i < cmd.length; i++) {
            block.add(index++, cmd[i]);
        }

        hbyte = (byte) ((value.length & 0xff00)>>8); 
        lbyte = (byte) (value.length & 0x00ff);

        block.add(index++, hbyte);
        block.add(index++, lbyte);
        for (int i = 0; i < value.length; i++) {
            block.add(index++, value[i]);
        }

        hbyte = (byte) ((block.size() & 0xff00)>>8); 
        lbyte = (byte) (block.size() & 0x00ff);

        byte hbytemsg = (byte) ((id & 0xff00)>>8); 
        byte lbytemsg = (byte) (id & 0x00ff);

        index = 0;
        header.add(index++, hbytemsg);
        header.add(index++, lbytemsg);
        header.add(index++, hbyte);
        header.add(index++, lbyte);

        block.addAll(0, header);

        for (byte b : block) {
            bos.write(b);
        }
        bos.flush();

        long t0 = System.nanoTime();
        byte[] recheader = new byte[7];
        bis.read(recheader);

        byte[] recblock = new byte[getInt(recheader, 2)];
        bis.read(recblock);

        byte[] data = new byte[recheader.length + recblock.length];
        System.arraycopy(recheader, 0, data, 0, recheader.length);
        System.arraycopy(recblock, 0, data, recheader.length, recblock.length);

        long readTime = (System.nanoTime() - t0) / 1000000;

        return name + " #read time=" + readTime + "ms  #value=" + new String(Arrays.copyOfRange(data, 7, data.length)).trim();
    }

    /**
     * Sends a request to the KUKA robot
     *
     * @param request the request to send
     * @throws IOException on IO error
     * @return a Callback from the robot with an updated variable value
     */
    @Deprecated
    public Callback sendRequest(Request request) throws IOException {
        for (byte b : request.getCmd()) {
            bos.write(b);
        }
        bos.flush();
        return getCallback(request.getVariable());
    }

    /**
     * Convenience method for reading the current joint angles of the robot
     *
     * @throws IOException on IO error
     * @return the current joint angles of the robot 6 angles and 6
     */
    public double[] readJointAngles() throws IOException {
        readVariable(axis_act);
        return Arrays.copyOfRange(axis_act.asArray(), 0, 6);
    }

    /**
     * Convenience method for reading the joint torques
     *
     * @throws IOException on IO error
     * @return the joint torques of the robot, or a zero filled array if
     * something bad happens
     */
    public double[] readJointTorques() throws IOException {
        double[] jointTorques = new double[6];
        try {
            for (int i = 0; i < 6; i++) {
                Callback torque = sendRequest(new Request(0, "$TORQUE_AXIS_ACT[" + (i + 1) + "]"));
                jointTorques[i] = ((double) torque.getVariable().getValue());
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex);
            System.err.println("Error reading torques. Returning array of zeros");
            return new double[6];
        }
        return jointTorques;
    }

    /**
     * Updates var with the values retrieved from variable with the same name in
     * the controller.
     *
     * @param var the KRLVariable to read
     * @throws IOException on IO error
     */
    public void readVariable(KRLVariable var) throws IOException {
        for (byte b : var.getReadCommand()) {
            bos.write(b);
        }
        bos.flush();
        long t0 = System.nanoTime();
        byte[] header = new byte[7];
        bis.read(header);

        byte[] block = new byte[getInt(header, 2)];
        bis.read(block);

        byte[] data = new byte[header.length + block.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(block, 0, data, header.length, block.length);

        long readTime = (System.nanoTime() - t0);

        int id = getInt(data, 0);
        String strValue = new String(Arrays.copyOfRange(data, 7, data.length)).trim();
        var.update(id, strValue, readTime);
    }

    /**
     * Writes the values in var to the variable with the same name in the
     * controller
     *
     * @param var the KRLVariable to write
     * @throws IOException on IOerror
     */
    public void writeVariable(KRLVariable var) throws IOException {
        for (byte b : var.getWriteCommand()) {
            bos.write(b);
        }
        bos.flush();
        long t0 = System.nanoTime();
        byte[] header = new byte[7];
        bis.read(header);

        byte[] block = new byte[getInt(header, 2)];
        bis.read(block);

        byte[] data = new byte[header.length + block.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(block, 0, data, header.length, block.length);

        long readTime = (System.nanoTime() - t0);

        int id = getInt(data, 0);
        String strValue = new String(Arrays.copyOfRange(data, 7, data.length)).trim();
        var.update(id, strValue, readTime);

    }

    /**
     * Called internally by sendRequest
     *
     * @param variable the varibale name
     * @return a callback with the values return by the controller
     * @throws IOException
     */
    private Callback getCallback(String variable) throws IOException {
        long t0 = System.nanoTime();
        byte[] header = new byte[7];
        bis.read(header);

        byte[] block = new byte[getInt(header, 2)];
        bis.read(block);

        byte[] data = new byte[header.length + block.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(block, 0, data, header.length, block.length);

        long readTime = (System.nanoTime() - t0);
        return new Callback(variable, data, readTime);
    }

    /**
     * Converts a 2 bytes into a integer
     *
     * @param bytes a byte array of at least length 2
     * @param off index of the first byte of the integer that should be
     * converted
     * @return the integer value of the two bytes
     */
    public static int getInt(byte[] bytes, int off) {
        return bytes[off] << 8 & 0xFF00 | bytes[off + 1] & 0xFF;
    }
}
