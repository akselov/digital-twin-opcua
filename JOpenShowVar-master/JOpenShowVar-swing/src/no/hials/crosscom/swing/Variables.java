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
package no.hials.crosscom.swing;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import no.hials.crosscom.variables.Variable;

/**
 *
 * @author Lars Ivar Hatledal
 */
public class Variables  {
    private final EventList<Variable> variables = new BasicEventList<>();
    
    
   public EventList<Variable> getVariables;
    
    public Variable getByID(int id) {
        for (Variable var : variables) {
            if (var.getId() == id) {
                return var;
            }
        }
        
        return null;
    }

    public Variable getByName(String name) {
        for (Variable var : variables) {
            if (var.getName().equals(name)) {
                return var;
            }
        }
        return null;
    }

    public String getIDs() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Variable var : variables) {
            sb.append(var.getId());
            if (i != variables.size() - 1) {
                sb.append("\n");
            }
            i++;
        }
        return sb.toString();
    }

    public String getDataTypes() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Variable var : variables) {
            sb.append(var.getDataType());
            if (i != variables.size() - 1) {
                sb.append("\n");
            }
            i++;
        }
        return sb.toString();
    }

    public String getValues() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Variable var : variables) {
            sb.append(var.getValue());
            if (i != variables.size() - 1) {
                sb.append("\n");
            }
            i++;
        }
        return sb.toString();
    }

    public String getReadTimes() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Variable var : variables) {
            sb.append(var.getReadTime());
            if (i != variables.size() - 1) {
                sb.append("\n");
            }
            i++;
        }
        return sb.toString();
    }
}
