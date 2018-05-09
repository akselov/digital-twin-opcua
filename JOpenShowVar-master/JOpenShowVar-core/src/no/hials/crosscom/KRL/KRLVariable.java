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
package no.hials.crosscom.KRL;

import no.hials.crosscom.KRL.structs.KRLE6Axis;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a variable from the KRL language
 * @author Lars Ivar Hatledal
 */
public abstract class KRLVariable {

    private final int id;
    private final String name;

    private long readTime = -1;

    private static final AtomicInteger idFactory = new AtomicInteger();

    public KRLVariable(String name) {
        this.name = name;
        this.id = idFactory.getAndIncrement();
    }

    /**
     * The unique id of this variable. Can be used for sorting
     *
     * @return the id of this variable
     */
    public int getId() {
        return id;
    }

    /**
     * The variable name of this variable. Should match a variable from the KUKA
     * robot
     *
     * @return the name of this variable
     */
    public String getName() {
        return name;
    }
    
    /**
    * Return the Type of Variable that implements this class
    *
    * @return the Type of variable that implements this class
    */
    public String getType(){
        return this.getClass().getSimpleName();
    }

    /**
     * The time it took to read/write this variable
     *
     * @return the time it took to read/write this variable in nanosesconds
     */
    public long getReadTimeNano() {
        return readTime;
    }

    /**
     * The time it took to read/write this variable
     *
     * @return the time it took to read/write this variable in milliseconds
     */
    public long getReadTimeMillis() {
        return readTime / 1000000;
    }

    /**
     * The time it took to read/write this variable
     *
     * @return the time it took to read/write this variable in seconds
     */
    public double getReadTimeSec() {
        return ((double) readTime / 1000000000d);
    }

    /**
     * NOTE! You should not use this method yourself. 
     * @param id the variable id
     * @param strValue the string value
     * @param readTime the time it took to read the variable
     */
    public void update(int id, String strValue, long readTime) {
        if (this.id != id) {
            throw new RuntimeException("The returned id does not match the variable id! Should not happen...");
        }
        this.readTime = readTime;
        setValueFromString(strValue);
    }

    /*
     * The read command. This is what's actually beeing sent to the robot.
     * It's a implementation of the OpenShowVar c++ source
     * @return the read command
     */
    public Byte[] getReadCommand() {
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

        return block.toArray(new Byte[block.size()]);
    }

    /*
     * The write command. This is what's actually beeing sent to the robot.
     * It's a implementation of the OpenShowVar c++ source
     * @return the write command
     */
    public Byte[] getWriteCommand() {
        byte[] cmd = name.getBytes();
        byte[] value = getStringValue().getBytes();
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

        return block.toArray(new Byte[block.size()]);
    }

    public abstract Object getValue();

    public abstract String getStringValue();

    protected abstract void setValueFromString(String strValue);


    /**
     * Convenience method for getting the $OV_JOG variable
     * @return the OV_JOG variable
     */
    public static KRLReal OV_JOG() {
        return new KRLReal("$OV_JOG");
    }

    /**
     * Convenience method for getting the $OV_PRO variable
     * @return the OV_PRO variable
     */
    public static KRLReal OV_PRO() {
        return new KRLReal("$OV_PRO");
    }

    /**
     * Convenience method for getting the $AXIS_ACT variable
     * @return the AXIS_ACT variable
     */
    public static KRLE6Axis AXIS_ACT() {
        return new KRLE6Axis("$AXIS_ACT");
    }

    /**
     * Convenience method for getting the $POS_ACT variable
     * @return the POS_ACT variable
     */
    public static KRLReal POS_ACT() {
        return new KRLReal("$POS_ACT");
    }

    @Override
    public String toString() {
        return "KRLVariable{" + "id=" + id + ", name=" + name + ", value=" + getValue() + ", readTime=" + getReadTimeMillis() + "ms" + '}';
    }

}
