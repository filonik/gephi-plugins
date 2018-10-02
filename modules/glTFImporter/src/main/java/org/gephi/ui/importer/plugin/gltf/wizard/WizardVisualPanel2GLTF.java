/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.importer.plugin.gltf.wizard;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Mesh;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorByteData;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import de.javagl.jgltf.model.v1.GltfModelV1;
import de.javagl.jgltf.model.v2.GltfModelV2;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.gephi.io.importer.plugin.gltf.ImporterGLTF;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.ValidationPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Daniel Filonik
 */
public class WizardVisualPanel2GLTF  extends javax.swing.JPanel {
    private final ImporterGLTF importer;

    private WizardPanel2GLTF wizard2;
    private ValidationPanel validationPanel;
    
    private boolean initialized = false;

    /**
     * Creates new form WizardVisualPanel2GLTF
     */
    public WizardVisualPanel2GLTF(ImporterGLTF importer, WizardPanel2GLTF wizard2) {
        initComponents();
        this.wizard2 = wizard2;
        this.importer = importer;
    }
    
    public ValidationPanel getValidationPanel() {
        if (validationPanel != null) {
            return validationPanel;
        }
        validationPanel = new ValidationPanel();
        validationPanel.setInnerComponent(WizardVisualPanel2GLTF.this);
        ValidationGroup validationGroup = validationPanel.getValidationGroup();

        validationPanel.setName(getName());

        return validationPanel;
    }

    public boolean isValidData() {
        return areValidColumnsForTable();
    }

    public boolean areValidColumnsForTable() {
        return true;
    }

    public boolean hasRowsMissingSourcesOrTargets() {
        return false;
    }
    
   
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName("Summary"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 743, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 346, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
