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

/**
 * Represents a E6Pos struct variable from the KRL language
 *
 * @author Lars Ivar Hatledal
 */
public final class KRLE6Pos extends KRLPos {

    public KRLE6Pos(String name) {
        super(name, new String[]{"X", "Y", "Z", "A", "B", "C", "E1", "E2", "E3", "E4", "E5", "E6", "S", "T"});
    }

    /**
     * Sets E1 to E6
     *
     * @param values the new values
     */
    public void setE1ToE6(double... values) {
        if (values.length != 6) {
            throw new IllegalArgumentException("The number of values should be exatly 6!");
        }
        setE1(values[0]);
        setE2(values[1]);
        setE3(values[2]);
        setE4(values[3]);
        setE5(values[4]);
        setE6(values[5]);
    }

    /**
     * Getter for X
     *
     * @return the value
     */
    public double getE1() {
        return struct.get("E1");
    }

    /**
     * Getter for Y
     *
     * @return the value
     */
    public double getE2() {
        return struct.get("E2");
    }

    /**
     * Getter for Z
     *
     * @return the value
     */
    public double getE3() {
        return struct.get("E3");
    }

    /**
     * Getter for A
     *
     * @return the value
     */
    public double getE4() {
        return struct.get("E4");
    }

    /**
     * Getter for B
     *
     * @return the value
     */
    public double getE5() {
        return struct.get("E5");
    }

    /**
     * Getter for C
     *
     * @return the value
     */
    public double getE6() {
        return struct.get("E6");
    }

    /**
     * Sets the value of 'E1'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLE6Pos setE1(double d) {
        struct.put(getNodes()[6], d);
        return this;
    }

    /**
     * Sets the value of 'E2'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLE6Pos setE2(double d) {
        struct.put(getNodes()[7], d);
        return this;
    }

    /**
     * Sets the value of 'E3'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLE6Pos setE3(double d) {
        struct.put(getNodes()[8], d);
        return this;
    }

    /**
     * Sets the value of 'E4'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLE6Pos setE4(double d) {
        struct.put(getNodes()[9], d);
        return this;
    }

    /**
     * Sets the value of 'E5'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLE6Pos setE5(double d) {
        struct.put(getNodes()[10], d);
        return this;
    }

    /**
     * Sets the value of 'E6'
     *
     * @param d the value to set
     * @return this - so that method call chaining is possible
     */
    public KRLE6Pos setE6(double d) {
        struct.put(getNodes()[11], d);
        return this;
    }

    public double[] asArrayE1ToE6() {
        return new double[]{struct.get("E1"), struct.get("E2"), struct.get("E3"), struct.get("E4"), struct.get("E5"), struct.get("E6")};
    }
}
