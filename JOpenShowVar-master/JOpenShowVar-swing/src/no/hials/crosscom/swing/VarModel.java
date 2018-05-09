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
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import static no.hials.crosscom.ApplicationLauncher.FILELOCATION_VAR_LIST;
import no.hials.crosscom.Callback;
import no.hials.crosscom.CrossComClient;
import no.hials.crosscom.Request;
import no.hials.crosscom.variables.Variable;

/**
 *
 * @author Lars Ivar
 */
public final class VarModel  {

   private final EventList<Variable> variables = new BasicEventList<>();
    private final AtomicInteger ID = new AtomicInteger(0);
    private final CrossComClient client;
    private final Timer timer;

    public VarModel(CrossComClient client) {
        this.client = client;
        this.timer = new Timer(250, (ActionEvent e) -> {
            try {
                update();
            } catch (IOException ex) {
                Logger.getLogger(VarModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        timer.start();
    }

    public void addVariable(String var) throws Exception {
        if (getByName(var) != null) {
            throw new Exception("A variable with the name '" + var + "' is already beeing tracked!");
        }
        int id = ID.getAndAdd(1);
        Request request = new Request(id, var, null);
        try {
            Callback callback = client.sendRequest(request);
            Variable variable = Variable.parseVariable(callback);
            variables.add(variable);
        } catch (NumberFormatException ex) {
            Logger.getLogger(VarModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editVariable(int id, String value) throws IOException {
        client.sendRequest(new Request(id, getByID(id).getName(), value));
    }

    public EventList<Variable> getVariables() {
        return variables;
    }

    /**
     * Updates the variable view
     */
    public void update() throws IOException {
        for (Variable var : variables) {
            Request request = new Request(var.getId(), var.getName());
            Callback callback = client.sendRequest(request);
            int id = callback.getId();
            Variable variable = callback.getVariable();
            Variable oldVar = getByID(id);
            if (oldVar != null) {
                oldVar.update(variable.getValue(), callback.getReadTime());
                int indexOf = variables.indexOf(oldVar);
                variables.set(indexOf, oldVar);
            } else {
                variables.add(id, variable);
            }
        }
    }

    /**
     * Get a Variable by searching for the ID
     *
     * @param id the ID of the variable
     * @return the variable in the list with the same ID, or null if there are
     * no such variable
     */
    public Variable getByID(int id) {
        for (Variable var : variables) {
            if (var.getId() == id) {
                return var;
            }
        }
        return null;
    }

    /**
     * Get a Variable by searching for the name
     *
     * @param name the name of the variable
     * @return the variable in the list with the same name, or null if there are
     * no such variable
     */
    public Variable getByName(String name) {
        for (Variable var : variables) {
            if (var.getName().equals(name)) {
                return var;
            }
        }
        return null;
    }

    /**
     * Reads all the variable names from a text file, and adds them to the
     * monitoring
     */
    public void restore()  {
        try (
                FileInputStream fis = new FileInputStream(FILELOCATION_VAR_LIST);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {

            String var = "";
            while ((var = br.readLine()) != null) {
                addVariable(var);
            }

        } catch (Exception ex) {
            Logger.getLogger(VarModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saves the currently monitored variable names to a text file, so that they
     * can later be retrieved Each variable is separated by a newline
     */
    public void save() {
        try (
                FileOutputStream fos = new FileOutputStream(new File(FILELOCATION_VAR_LIST));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < variables.size(); i++) {
                sb.append(variables.get(i).getName());
                if (i != variables.size() - 1) {
                    sb.append("\n");
                }
            }
            bw.write(sb.toString());

        } catch (IOException ex) {
            Logger.getLogger(VarModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
