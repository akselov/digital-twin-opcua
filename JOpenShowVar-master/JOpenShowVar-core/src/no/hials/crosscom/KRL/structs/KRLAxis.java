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
package no.hials.crosscom.KRL.structs;

import java.util.HashMap;

/**
 * Represents a Axis Struct variable from the KRL language
 *
 * @author Lars Ivar Hatledal
 */
public class KRLAxis extends KRLStruct {

    protected final HashMap<String, Double> struct = new HashMap<>();

    public KRLAxis(String name) {
        this(name, new String[]{"A1", "A2", "A3", "A4", "A5", "A6"});
    }

    protected KRLAxis(String name, String[] nodes) {
        super(name, nodes);
        for (String str : nodes) {
            struct.put(str, 0d);
        }
    }

    /**
     * Sets A1 to A6
     *
     * @param values the new values
     */
    public void setA1ToA6(double... values) {
        if (values.length != 6) {
            throw new IllegalArgumentException("The number of values should be exatly 6!");
        }
        setA1(values[0]);
        setA2(values[1]);
        setA3(values[2]);
        setA4(values[3]);
        setA5(values[4]);
        setA6(values[5]);
    }

    /**
     * Sets the value of 'A1'
     *
     * @param d the value to set
     */
    public void setA1(double d) {
        struct.put(getNodes()[0], d);
    }

    /**
     * Sets the value of 'A2'
     *
     * @param d the value to set
     */
    public void setA2(double d) {
        struct.put(getNodes()[1], d);
    }

    /**
     * Sets the value of 'A3'
     *
     * @param d the value to set
     */
    public void setA3(double d) {
        struct.put(getNodes()[2], d);
    }

    /**
     * Sets the value of 'A4'
     *
     * @param d the value to set
     */
    public void setA4(double d) {
        struct.put(getNodes()[3], d);
    }

    /**
     * Sets the value of 'A5'
     *
     * @param d the value to set
     */
    public void setA5(double d) {
        struct.put(getNodes()[4], d);
    }

    /**
     * Sets the value of 'A6'
     *
     * @param d the value to set
     */
    public void setA6(double d) {
        struct.put(getNodes()[5], d);
    }

    /**
     * Get a double array representation of this object
     *
     * @return a new double array with the values contained in this struct
     */
    public double[] asArray() {

        double[] arr = new double[getNodes().length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = struct.get(getNodes()[i]);
        }
        return arr;
    }

    public double[] asArrayA1ToA6() {
        return new double[]{struct.get("A1"), struct.get("A2"), struct.get("A3"), struct.get("A4"), struct.get("A5"), struct.get("A6")};
    }

    @Override
    public void setValue(String str, String obj) {
        struct.put(str, Double.parseDouble(obj));
    }

    @Override
    public HashMap<String, Double> getValue() {
        return struct;
    }

}
