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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a STRUCT value in KRL
 * @author Lars Ivar Hatledal
 */
 @Deprecated 
public class Struct extends Variable<List<StructNode>> {
    
     @Deprecated 
    public Struct(int id, String name, List<StructNode> value, long readTime) {
        super(id, name, "Struct", readTime);
        this.value = value;
    }

    /**
     * Parses a String encoded KRL Struct to a JOpenShowVar Struct
     * @param value the KRL String encoded Struct
     * @return a JOpenShowVar Struct 
     */
    public static List<StructNode> parseString(String value) {
        if (value.contains(":")) {
            value = value.substring(value.indexOf(":") + 1, value.indexOf("}") + 1);
        } else {
            value = value.substring(value.indexOf("{") + 1, value.indexOf("}") + 1);
            value = " " + value;
        }
        value = value.replaceAll("\\{", "").replaceAll("\\}", "");
        List<StructNode> nodes = new ArrayList<>();
        String[] split1 = value.split(",");
        for (String str1 : split1) {
            String[] split2 = str1.split(" ");
            String name = split2[1];
            Object val;
            Scanner sc = new Scanner(split2[2]);
            if (sc.hasNextInt()) {
                val = sc.nextInt();
            } else if (sc.hasNextDouble()) {
                val = sc.nextDouble();
            } else if (sc.hasNextBoolean()) {
                val = sc.nextBoolean();
            } else {
                val = sc.next();
            }
            StructNode node = new StructNode(name, val);
            nodes.add(node);
        }
        return nodes;
    }
}
