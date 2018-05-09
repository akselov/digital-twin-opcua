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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import no.hials.crosscom.variables.Struct;
import no.hials.crosscom.variables.StructNode;
import no.hials.crosscom.variables.Variable;

/**
 *
 * @author Lars Ivar Hatledal
 */
public class EditPanel extends JPanel {

    private final VarModel model;
    private final JFrame parent;

    public EditPanel(JFrame parent, VarModel model) {
        this.parent = parent;
        this.model = model;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createTitledBorder(null, "Edit variable", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.BLUE));
    }

    public void addVariable(final Variable variable) {
        this.removeAll();
        List<Component> components = new ArrayList<>();
        components.add(new JLabel(variable.getName() + " (" + variable.getDataType() + ")"));
        final List<String> names = new ArrayList<>();
        final List<JTextField> text = new ArrayList<>();
        if (variable instanceof Struct) {
            Struct struct = (Struct) variable;
            for (StructNode node : struct.getValue()) {
                final JLabel label = new JLabel("Write new value for " + node.getName() + ":");
                final JTextField textField = new JTextField(node.getValue().toString(), 10);
                textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
                components.add(label);
                components.add(textField);
                text.add(textField);
                names.add(node.getName());
            }
            final JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    for (int i = 0; i < names.size(); i++) {
                        sb.append(names.get(i)).append(" ").append(text.get(i).getText());
                        if (i != names.size() - 1) {
                            sb.append(",");
                        }

                    }
                    sb.append("}");
                    try {
                        model.editVariable(variable.getId(), sb.toString());
                    } catch (IOException ex) {
                        Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            components.add(okButton);
        } else {

            final JLabel label = new JLabel("Write a new value:");
            final JTextField textField = new JTextField(variable.getValue().toString(), 10);
            textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            textField.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        model.editVariable(variable.getId(), textField.getText());
                    } catch (IOException ex) {
                        Logger.getLogger(EditPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    textField.setText(null);
                }
            });
            components.add(label);
            components.add(textField);

        }

        for (Component c : components) {
            this.add(c);
        }

        revalidate();
        parent.revalidate();
    }

}
