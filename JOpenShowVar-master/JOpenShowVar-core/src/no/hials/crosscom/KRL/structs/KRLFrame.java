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
 * Represents a Frame struct variable from the KRL language
 *
 * @author Lars Ivar Hatledal
 */
public class KRLFrame extends KRLStruct {

    protected final HashMap<String, Double> struct = new HashMap<>();

    public KRLFrame(String name) {
        this(name, new String[]{"X", "Y", "Z", "A", "B", "C"}); 
    }

    protected KRLFrame(String name, String[] nodes) {
        super(name, nodes);
        for (String str : nodes) {
            struct.put(str, 0d);
        }
    }

    /**
     * Getter for X
     * @return the value
     */
    public double getX() {
        return struct.get("X");
    }

    /**
     * Getter for Y
     * @return the value
     */
    public double getY() {
        return struct.get("Y");
    }

    /**
     * Getter for Z
     * @return the value
     */
    public double getZ() {
        return struct.get("Z");
    }

    /**
     * Getter for A
     * @return the value
     */
    public double getA() {
        return struct.get("A");
    }

    /**
     * Getter for B
     * @return the value
     */
    public double getB() {
        return struct.get("B");
    }

    /**
     * Getter for C
     * @return the value
     */
    public double getC() {
        return struct.get("C");
    }

    /**
     * Sets X, Y and Z
     *
     * @param values the new values
     */
    public void setXToZ(double... values) {
        if (values.length != 3) {
            throw new IllegalArgumentException("The number of values should be exatly 3!");
        }
        setX(values[0]);
        setY(values[1]);
        setZ(values[2]);
    }

    /**
     * Sets A, B and C
     *
     * @param values the new values
     */
    public void setAToC(double... values) {
        if (values.length != 3) {
            throw new IllegalArgumentException("The number of values should be exatly 3!");
        }
        setA(values[0]);
        setB(values[1]);
        setC(values[2]);
    }

    /**
     * Sets the value of 'X'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLFrame setX(double d) {
        struct.put(getNodes()[0], d);
        return this;
    }

    /**
     * Sets the value of 'Y'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLFrame setY(double d) {
        struct.put(getNodes()[1], d);
        return this;
    }

    /**
     * Sets the value of 'Z'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLFrame setZ(double d) {
        struct.put(getNodes()[2], d);
        return this;
    }

    /**
     * Sets the value of 'A'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLFrame setA(double d) {
        struct.put(getNodes()[3], d);
        return this;
    }

    /**
     * Sets the value of 'B'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLFrame setB(double d) {
        struct.put(getNodes()[4], d);
        return this;
    }

    /**
     * Sets the value of 'C'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLFrame setC(double d) {
        struct.put(getNodes()[5], d);
        return this;
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

    public double[] asArrayXToZ() {
        return new double[]{struct.get("X"), struct.get("Y"), struct.get("Z")};
    }

    public double[] asArrayAToC() {
        return new double[]{struct.get("A"), struct.get("B"), struct.get("C")};
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
