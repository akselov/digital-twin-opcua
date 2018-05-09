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
import no.hials.crosscom.KRL.KRLVariable;

/**
 * Represents a Struct variable from the KRL language
 *
 * @author Lars Ivar Hatledal
 */
public abstract class KRLStruct extends KRLVariable {

    private final String[] nodes;

    public KRLStruct(String name, String[] nodes) {
        super(name);
        this.nodes = nodes; 
    }

    @Override
    public abstract HashMap getValue();

    public abstract void setValue(String str, String obj);

    /**
     * The nodes 
     * @return the name of the variables that this struct contains
     */
    public String[] getNodes() {
        return nodes.clone();
    }

    @Override
    protected void setValueFromString(String strValue) {
        String substring;
        if (strValue.contains(":")) {
            String[] split = strValue.split(":");
            String trim = split[1].trim();
            substring = trim.substring(0, trim.length() - 1);

        } else {
            substring = strValue.substring(1, strValue.length() - 1);
        }
        String[] split1 = substring.split(",");

        for (String n : split1) {
            String[] split2 = n.trim().split(" ");
            setValue(split2[0], split2[1]);
        }
    }

    @Override
    public String getStringValue() {
        HashMap map = getValue();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        for (String str : nodes) {
             if (map.containsKey(str)) {
                Object get = map.remove(str);
                sb.append(str).append(" ").append(get);
                if (!map.isEmpty() && i != map.size()) {
                    sb.append(", ");
                }
            }
        }
 
        sb.append("}");
        return sb.toString();
    }

}
