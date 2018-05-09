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

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import no.hials.crosscom.variables.Variable;

/**
 *
 * @author Lars Ivar Hatledal
 */
public class VarBrowser extends JPanel {

    private final EventList<Variable> eventList;
    private final JTable table;

    public VarBrowser(final VarModel model, final EditPanel editPanel) {
        this.eventList = model.getVariables();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        SortedList<Variable> sortedIssues = new SortedList<>(eventList, new VarComparator());
        JTextField filterEdit = new JTextField(10);
        filterEdit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        MatcherEditor<Variable> textMatcherEditor = new TextComponentMatcherEditor<>(filterEdit, new VarTextFilterator());
        FilterList<Variable> textFilteredVariables = new FilterList<>(sortedIssues, textMatcherEditor);

        AdvancedTableModel<Variable> varTableModel = GlazedListsSwing.eventTableModelWithThreadProxyList(textFilteredVariables, new VarTableFormat());
        table = new JTable(varTableModel);
        TableComparatorChooser<Variable> tableSorter = TableComparatorChooser.install(table, sortedIssues, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE);
        JScrollPane issuesTableScrollPane = new JScrollPane(table);

        final DefaultEventSelectionModel<Variable> selectionModel = new DefaultEventSelectionModel<>(textFilteredVariables);
        table.setSelectionModel(selectionModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int r = table.rowAtPoint(e.getPoint());
                if (r >= 0 && r < table.getRowCount()) {
                    table.setRowSelectionInterval(r, r);
                } else {
                    table.clearSelection();
                }

                int rowindex = table.getSelectedRow();
                if (rowindex < 0) {
                    return;
                }
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    JPopupMenu popup = createPopUp();
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            private JPopupMenu createPopUp() {
                 JPopupMenu popupMenu = new JPopupMenu();
                 
                 final JMenuItem editMenuItem = new JMenuItem("Edit");
                 editMenuItem.addActionListener((ActionEvent e) -> {
                     if (!selectionModel.isSelectionEmpty()) {
                         editPanel.addVariable(selectionModel.getSelected().get(0));
                     }
                 });
                 
                final JMenuItem removeMenuItem = new JMenuItem("Remove");
                removeMenuItem.addActionListener((ActionEvent e) -> {
                    if (!selectionModel.isSelectionEmpty()) {
                        eventList.removeAll(selectionModel.getSelected());
                    }
                 });
               
                popupMenu.add(editMenuItem);
                popupMenu.add(removeMenuItem);
                return popupMenu;
            }
        });

        final JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(filterEdit);
        this.add(filterPanel);
        this.add(issuesTableScrollPane);
    }

    public JTable getTable() {
        return table;
    } 
}
