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

import java.util.Arrays;
import no.hials.crosscom.variables.Variable;

/**
 * When querying the KUKAVARPROXY, a Callback with a updated value of the
 * requested variable is sent which this class represents
 *
 * @author Lars Ivar Hatledal
 */
@Deprecated
public final class Callback {

    private final String variableName;
    private final int id, option;
    private final long readTime;
    private final String strValue;

    @Deprecated
    public Callback(String variable, byte[] data, long readTime) {
        this.variableName = variable;
        this.id = CrossComClient.getInt(data, 0);
        this.readTime = readTime;
        this.option = (int) data[4];
        this.strValue = new String(Arrays.copyOfRange(data, 7, data.length)).trim(); 
    }

    /**
     * Getter for the variable name
     *
     * @return the name of the variable
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Getter for the ID of the callback variable
     *
     * @return the id if the variable
     */
    public int getId() {
        return id;
    }

    /**
     * 0 means that the request was for reading, 1 means that the request was
     * for writing
     *
     * @return the option type, should be 0 or 1
     */
    public int getOption() {
        return option;
    }

    /**
     * The time it took from sending the request to get the Callback
     *
     * @return The time it took from sending the request to get the Callback in
     * nanoseconds
     */
    public long getReadTime() {
        return readTime;
    }

    /**
     * Get the value of the Variable
     *
     * @return value of the Variable (This is a String) The Variable class has a
     * static method for parsing this
     */
    public String getStringValue() {
        return strValue;
    }

    /**
     * Getter for the callback variable
     *
     * @return the callback variable
     */
    public Variable getVariable() {
        return Variable.parseVariable(this);
    }

    @Override
    public String toString() {
        return "Callback{" + "variable=" + variableName + ", id=" + id + ", option=" + option + ", readTime=" + (readTime / 1000000) + "ms, value=" + strValue + '}';
    }
}
