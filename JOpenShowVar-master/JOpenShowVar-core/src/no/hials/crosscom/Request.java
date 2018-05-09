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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request which is sent to the KUKAVARPROXY server
 *
 * @author Lars Ivar Hatledal
 */
@Deprecated
public class Request {

    public static final char READ = 0;
    public static final char WRITE = 1;

    private final String var, val;
    private final int id;
    private final Byte[] cmd;

    /**
     * Read constructor
     *
     * @param id the message id
     * @param var the variable to write to
     */
    @Deprecated
    public Request(int id, String var) {
        this(id, var, null);
    }

    /**
     * Write constructor
     *
     * @param id the message id
     * @param var the variable to write to
     * @param val the new value of the variable
     */
    public Request(int id, String var, String val) {
        this.id = id;
        this.var = var;
        this.val = val;
        if (val == null) {
            cmd = getReadCommand();
        } else {
            cmd = getWriteCommand();
        }
    }

    /**
     * Get the name of the variable
     *
     * @return the name of the variable
     */
    public String getVariable() {
        return var;
    }

    /**
     * Get the command to send (byte array)
     *
     * @return the command to send to the KUKA
     */
    public Byte[] getCmd() {
        return cmd.clone();
    }

    /**
     * The read command
     *
     * @return The read command
     */
    private Byte[] getReadCommand() {
        byte[] cmd = var.getBytes();
        List<Byte> header = new ArrayList<>();
        List<Byte> block = new ArrayList<>();

        byte hbyte = (byte) ((cmd.length >> 8) & 0xff00);
        byte lbyte = (byte) (cmd.length & 0x00ff);

        int index = 0;
        block.add(index++, (byte) READ);
        block.add(index++, hbyte);
        block.add(index++, lbyte);
        for (int i = 0; i < cmd.length; i++) {
            block.add(index++, cmd[i]);
        }
        hbyte = (byte) ((block.size() >> 8) & 0xff00);
        lbyte = (byte) (block.size() & 0x00ff);

        byte hbytemsg = (byte) ((id >> 8) & 0xff00);
        byte lbytemsg = (byte) (id & 0x00ff);

        index = 0;
        header.add(index++, hbytemsg);
        header.add(index++, lbytemsg);
        header.add(index++, hbyte);
        header.add(index++, lbyte);

        block.addAll(0, header);

        return block.toArray(new Byte[block.size()]);
    }

    /**
     * The write command
     *
     * @return the write command
     */
    private Byte[] getWriteCommand() {
        byte[] cmd = var.getBytes();
        byte[] value = val.getBytes();
        List<Byte> header = new ArrayList<>();
        List<Byte> block = new ArrayList<>();

        byte hbyte = (byte) ((cmd.length >> 8) & 0xff00);
        byte lbyte = (byte) (cmd.length & 0x00ff);

        int index = 0;
        block.add(index++, (byte) WRITE);
        block.add(index++, hbyte);
        block.add(index++, lbyte);
        for (int i = 0; i < cmd.length; i++) {
            block.add(index++, cmd[i]);
        }

        hbyte = (byte) ((value.length >> 8) & 0xff00);
        lbyte = (byte) (value.length & 0x00ff);

        block.add(index++, hbyte);
        block.add(index++, lbyte);
        for (int i = 0; i < value.length; i++) {
            block.add(index++, value[i]);
        }

        hbyte = (byte) ((block.size() >> 8) & 0xff00);
        lbyte = (byte) (block.size() & 0x00ff);

        byte hbytemsg = (byte) ((id >> 8) & 0xff00);
        byte lbytemsg = (byte) (id & 0x00ff);

        index = 0;
        header.add(index++, hbytemsg);
        header.add(index++, lbytemsg);
        header.add(index++, hbyte);
        header.add(index++, lbyte);

        block.addAll(0, header);

        return block.toArray(new Byte[block.size()]);
    }
}
