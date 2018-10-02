/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.importer.plugin.gltf.wizard;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import org.gephi.io.importer.plugin.gltf.ImporterGLTF;

/**
 *
 * @author Daniel Filonik
 */
public abstract class AbstractWizardVisualPanel1 extends javax.swing.JPanel {

    protected static final int MAX_ROWS_PREVIEW = 50;

    public static final Color VERY_LIGHT_GRAY = new Color(225,225,225);
    
    private final ImporterGLTF importer;

    public AbstractWizardVisualPanel1(ImporterGLTF importer) {
        this.importer = importer;
    }
    
    protected abstract String[] getNodePreviewTableHeaders();
    protected abstract String[] getEdgePreviewTableHeaders();
    
    protected abstract String[][] getNodePreviewTableValues();
    protected abstract String[][] getEdgePreviewTableValues();
    
    public void refreshNodePreviewTable() {
        final String[] headers = getNodePreviewTableHeaders();
        final String[][] values = getNodePreviewTableValues();
        final int rowSize = headers.length;
        
        final JTable nodeTable = getNodePreviewTable();
        nodeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground((row/1)%2==0 ? Color.WHITE : VERY_LIGHT_GRAY);                        
                return c;
            };
        });
        
        nodeTable.setModel(new TableModel() {
            
            @Override
            public int getRowCount() {
                return values.length;
            }
            
            @Override
            public int getColumnCount() {
                return rowSize;
            }
            
            @Override
            public String getColumnName(int columnIndex) {
                if (columnIndex > headers.length - 1) {
                    return null;
                }
                return headers[columnIndex];
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
            
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (values[rowIndex].length > columnIndex) {
                    return values[rowIndex][columnIndex];
                } else {
                    return null;
                }
            }
            
            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            }
            
            @Override
            public void addTableModelListener(TableModelListener l) {
            }
            
            @Override
            public void removeTableModelListener(TableModelListener l) {
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boolean needsHeader = headers.length > 0;
                getNodePreviewTableScrollPane().setColumnHeaderView(needsHeader ? nodeTable.getTableHeader() : null);
            }
        });
    }

    public void refreshEdgePreviewTable() {
        final String[] headers = getEdgePreviewTableHeaders();
        final String[][] values = getEdgePreviewTableValues();
        final int rowSize = headers.length;
        
        final JTable edgeTable = getEdgePreviewTable();
        edgeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground((row/2)%2==0 ? Color.WHITE : VERY_LIGHT_GRAY);                        
                return c;
            };
        });
        
        edgeTable.setModel(new TableModel() {
            
            @Override
            public int getRowCount() {
                return values.length;
            }
            
            @Override
            public int getColumnCount() {
                return rowSize;
            }
            
            @Override
            public String getColumnName(int columnIndex) {
                if (columnIndex > headers.length - 1) {
                    return null;
                }
                return headers[columnIndex];
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
            
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (values[rowIndex].length > columnIndex) {
                    return values[rowIndex][columnIndex];
                } else {
                    return null;
                }
            }
            
            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            }
            
            @Override
            public void addTableModelListener(TableModelListener l) {
            }
            
            @Override
            public void removeTableModelListener(TableModelListener l) {
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                boolean needsHeader = headers.length > 0;
                getEdgePreviewTableScrollPane().setColumnHeaderView(needsHeader ? edgeTable.getTableHeader() : null);
            }
        });
    }

    protected abstract JTable getNodePreviewTable();
    protected abstract JTable getEdgePreviewTable();

    protected abstract JScrollPane getNodePreviewTableScrollPane();
    protected abstract JScrollPane getEdgePreviewTableScrollPane();
}
