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
package no.hials.crosscom.variables;

import java.util.Scanner;
import no.hials.crosscom.Callback;

/**
 * Represents a KRL variable (deprecated)
 *
 * @author Lars Ivar Hatledal
 * @param <E> Variable type
 */
 @Deprecated 
public abstract class Variable<E> implements Comparable<Variable<E>> {

    private final int id;
    private final String name;
    private final String dataType;
    private long readTime;
    protected E value;

    @Deprecated 
    public Variable(int id, String name, String dataType, long readTime) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
        this.readTime = readTime;
    }

    /**
     * Get the name of the variable
     *
     * @return the name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * Get the datatype: 'INT', 'REAL', 'BOOL', etc.
     *
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Get the time it took to read the variable from the robot
     *
     * @return the time it took to read the variable from the robot in nanoseconds
     */
    public long getReadTime() {
        return readTime;
    }

    /**
     * Get the assigned ID of the variable
     *
     * @return the assigned ID of the variable
     */
    public int getId() {
        return id;
    }

    /**
     * Get the value of the variable
     *
     * @return the value of the variable
     */
    public E getValue() {
        return value;
    }

    /**
     * Sets the value of the variable
     *
     * @param value the value to set
     */
    protected void setValue(E value) {
        this.value = value;
    }

    /**
     * Updates the value
     *
     * @param value the new value
     * @param readTime the new read time
     */
    public void update(E value, long readTime) {
        setValue(value);
        this.readTime = readTime;
    }

    @Override
    public int compareTo(Variable<E> o) {
        if (toString().length() == o.toString().length()) {
            return 0;
        } else if (toString().length() < o.toString().length()) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "ID = " + id + "\t" + name + " = " + getValue() + "\t#ReadTime: " + readTime;
    }

    /**
     * Parses a string encoded KRL variable to a JOpenShowVar Variable
     *
     * @param callback the Callback from the robot
     * @return the JOpenShowVar Variable
     * @throws NumberFormatException on parsing error
     */
    public static Variable parseVariable(Callback callback) throws NumberFormatException {
        String variable = callback.getVariableName();
        String value = callback.getStringValue();
        int id = callback.getId();
        long readTime = callback.getReadTime();
//        int option = callback.getOption();

        Scanner sc = new Scanner(value);
        Variable var;
        if (sc.hasNextInt()) {
            var = new Int(id, variable, sc.nextInt(), readTime);
            sc.close();
        } else if (sc.hasNextFloat()) {
            var = new Real(id, variable, (double) sc.nextFloat(), readTime);
            sc.close();
        } else if (sc.hasNextDouble()) {
            var = new Real(id, variable, sc.nextDouble(), readTime);
            sc.close();
        } else if (sc.hasNextBoolean()) {
            var = new Bool(id, variable, sc.nextBoolean(), readTime);
            sc.close();
        }else if (value.contains("#")) {
            var = new Enum(id, variable, sc.nextLine(), readTime);
            sc.close();
        } else if (value.contains("{")) {
            sc.close();
            var = new Struct(id, variable, Struct.parseString(value), readTime);
        } else {
            var = new Real(id, variable, Double.parseDouble(value), readTime);
            sc.close();
        }
        return var;
    }

}
