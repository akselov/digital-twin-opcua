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

import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import no.hials.crosscom.swing.AddVarPanel;
import no.hials.crosscom.swing.BasicMenuBar;
import no.hials.crosscom.swing.EditPanel;
import no.hials.crosscom.swing.VarBrowser;
import no.hials.crosscom.swing.VarModel;

/**
 * Main class
 *
 * @author Lars Ivar Hatledal
 */
public class ApplicationLauncher {

    public static final String FILELOCATION_VAR_LIST = "Resources//Var_list.txt";
    public static final String FILELOCATION_ROBOT_IP = "Resources//Robot_IP.txt";
    public static final String GUI_TITLE = "JOpenShowVar";

    public static void main(String[] args) throws IOException {
        CrossComClient client;

        while (true) {
            String answer = JOptionPane.showInputDialog(null, "Insert robot IP in the form '192.168.2.2:7000'", "Robot IP", JOptionPane.QUESTION_MESSAGE);
            if (answer == null) {
                JOptionPane.showMessageDialog(null, "Application will terminate..");
                System.exit(0);
            }
            String[] split = answer.split(":");
            if (split.length != 2) {
                JOptionPane.showMessageDialog(null, "Wrong input format. Try again..");
                continue;
            }
            String IP = split[0];
            int port;
            try {
                port = Integer.parseInt(split[1]);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error parsing the port number. Try again..");
                continue;
            }
            try {
                client = new CrossComClient(IP, port);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex);
                continue;
            }
            break;
        }

        final JFrame frame = new JFrame(GUI_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final VarModel model = new VarModel(client);
        model.restore();
        frame.setJMenuBar(new BasicMenuBar(model));
        frame.setLayout(new BorderLayout());
        final EditPanel editPanel = new EditPanel(frame, model);
        final VarBrowser browser = new VarBrowser(model, editPanel);
        frame.getContentPane().add(new JScrollPane(browser), BorderLayout.CENTER);
        frame.getContentPane().add(new AddVarPanel(model), BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(editPanel), BorderLayout.EAST);
        frame.pack();

        frame.setVisible(true);
    }
}
