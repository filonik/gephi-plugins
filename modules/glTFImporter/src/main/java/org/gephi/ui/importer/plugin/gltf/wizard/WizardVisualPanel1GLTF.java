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
import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import de.javagl.jgltf.model.v1.GltfModelV1;
import de.javagl.jgltf.model.v2.GltfModelV2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.gephi.io.importer.plugin.gltf.ImporterGLTF;
import org.gephi.io.importer.plugin.gltf.process.AbstractConfiguration;
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
public class WizardVisualPanel1GLTF  extends AbstractWizardVisualPanel1 {
    public static class ItemWrapper {
        private final String name;
        private final int index;

        public ItemWrapper(int index, String name) {
            this.index = index;
            this.name = name;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public String getName() {
            return this.name;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.index);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ItemWrapper other = (ItemWrapper) obj;
            if (this.index != other.index) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return this.name!=null? this.name: "#" + this.index;
        }
    }
    private final ImporterGLTF importer;

    private WizardPanel1GLTF wizard1;
    private ValidationPanel validationPanel;

    private de.javagl.jgltf.impl.v2.GlTF document = null;
    private de.javagl.jgltf.impl.v2.Mesh mesh = null;
    private de.javagl.jgltf.impl.v2.MeshPrimitive nodes = null;
    private de.javagl.jgltf.impl.v2.MeshPrimitive edges = null;
    
    private boolean initialized = false;

    /**
     * Creates new form WizardVisualPanel1GLTF
     */
    public WizardVisualPanel1GLTF(ImporterGLTF importer, WizardPanel1GLTF wizard1) {
        super(importer);
        initComponents();
        this.wizard1 = wizard1;
        this.importer = importer;
        
        final String filePath = importer.getFile().getAbsolutePath();
        pathTextField.setText(filePath);
        pathTextField.setToolTipText(filePath);
        
        initialized = true;
        
        GltfModelV2 model = (GltfModelV2) importer.getModel();
        if (model != null) {
            setSelectedDocument(model.getGltf());
        }
    }

    public final de.javagl.jgltf.impl.v2.GlTF getSelectedDocument() {
        return this.document;
    }

    public final void setSelectedDocument(de.javagl.jgltf.impl.v2.GlTF value) {
        this.document = value;
        this.onDocumentChanged();
    }

    public void onDocumentChanged() {
        meshComboBox.setSelectedIndex(-1);
        meshComboBox.removeAllItems();

        List<Mesh> meshes = document.getMeshes();
        for (int i = 0; i < meshes.size(); i++) {
            Mesh mesh = meshes.get(i);
            meshComboBox.addItem(new ItemWrapper(i, mesh.getName()));
        }

        if (meshComboBox.getItemCount()>0) {
            meshComboBox.setSelectedIndex(0);
        }
    }

    public final de.javagl.jgltf.impl.v2.Mesh getSelectedMesh() {
        return this.mesh;
    }

    public final void setSelectedMesh(de.javagl.jgltf.impl.v2.Mesh value) {
        this.mesh = value;
        this.onMeshChanged();
    }

    public void onMeshChanged() {
        nodeComboBox.setSelectedIndex(-1);
        edgeComboBox.setSelectedIndex(-1);

        nodeComboBox.removeAllItems();
        edgeComboBox.removeAllItems();

        List<MeshPrimitive> primitives = mesh.getPrimitives();
        for (int i = 0; i < primitives.size(); i++) {
            MeshPrimitive primitive = primitives.get(i);
            if (primitive.getMode() == 0) {
                nodeComboBox.addItem(new ItemWrapper(i, "Primitive #" + i));
            }
            if (primitive.getMode() == 1) {
                edgeComboBox.addItem(new ItemWrapper(i, "Primitive #" + i));
            }
        }
        
        if(nodeComboBox.getItemCount()>0) {
            nodeComboBox.setSelectedIndex(0);
        }
        if(edgeComboBox.getItemCount()>0) {
            edgeComboBox.setSelectedIndex(0);
        }
    }
    
    public final de.javagl.jgltf.impl.v2.MeshPrimitive getSelectedNodes() {
        return this.nodes;
    }

    public final void setSelectedNodes(de.javagl.jgltf.impl.v2.MeshPrimitive value) {
        this.nodes = value;
        this.onNodesChanged();
    }
    
    public void onNodesChanged() {
        refreshNodePreviewTable();
    }
    
    public final de.javagl.jgltf.impl.v2.MeshPrimitive getSelectedEdges() {
        return this.edges;
    }

    public final void setSelectedEdges(de.javagl.jgltf.impl.v2.MeshPrimitive value) {
        this.edges = value;
        this.onEdgesChanged();
    }
    
    public void onEdgesChanged() {
        refreshEdgePreviewTable();
    }
    
    @Override
    protected String[] getNodePreviewTableHeaders() {
        final String[] result = new String[0];
        if (nodes != null){
            Map<String, Integer> attributes = nodes.getAttributes();
            return attributes.keySet().toArray(result);
        }
        return result;
    }
    
    @Override
    protected String[] getEdgePreviewTableHeaders() {
        final String[] result = new String[0];
        if (edges != null){
            Map<String, Integer> attributes = edges.getAttributes();
            return attributes.keySet().toArray(result);
        }
        return result;
    }

    private static String AccessorByteDataToString(AccessorByteData data, int i) {
        if (i < data.getNumElements()) {
            int n = data.getNumComponentsPerElement();
            if (n == 1) {
                byte v0 = data.get(i, 0);
                return String.format("%d", v0);
            }
            if (n == 2) {
                byte v0 = data.get(i, 0);
                byte v1 = data.get(i, 1);
                return String.format("(%d, %d)", v0, v1);
            }
            if (n == 3) {
                byte v0 = data.get(i, 0);
                byte v1 = data.get(i, 1);
                byte v2 = data.get(i, 2);
                return String.format("(%d, %d, %d)", v0, v1, v2);
            }
            if (n == 4) {
                byte v0 = data.get(i, 0);
                byte v1 = data.get(i, 1);
                byte v2 = data.get(i, 2);
                byte v3 = data.get(i, 3);
                return String.format("(%d, %d, %d, %d)", v0, v1, v2, v3);
            }
        }
        return null;
    }
    
    private static String AccessorShortDataToString(AccessorShortData data, int i) {
        if (i < data.getNumElements()) {
            int n = data.getNumComponentsPerElement();
            if (n == 1) {
                short v0 = data.get(i, 0);
                return String.format("%d", v0);
            }
            if (n == 2) {
                short v0 = data.get(i, 0);
                short v1 = data.get(i, 1);
                return String.format("(%d, %d)", v0, v1);
            }
            if (n == 3) {
                short v0 = data.get(i, 0);
                short v1 = data.get(i, 1);
                short v2 = data.get(i, 2);
                return String.format("(%d, %d, %d)", v0, v1, v2);
            }
            if (n == 4) {
                short v0 = data.get(i, 0);
                short v1 = data.get(i, 1);
                short v2 = data.get(i, 2);
                short v3 = data.get(i, 3);
                return String.format("(%d, %d, %d, %d)", v0, v1, v2, v3);
            }
        }
        return null;
    }
    
    private static String AccessorIntDataToString(AccessorIntData data, int i) {
        if (i < data.getNumElements()) {
            int n = data.getNumComponentsPerElement();
            if (n == 1) {
                int v0 = data.get(i, 0);
                return String.format("%d", v0);
            }
            if (n == 2) {
                int v0 = data.get(i, 0);
                int v1 = data.get(i, 1);
                return String.format("(%d, %d)", v0, v1);
            }
            if (n == 3) {
                int v0 = data.get(i, 0);
                int v1 = data.get(i, 1);
                int v2 = data.get(i, 2);
                return String.format("(%d, %d, %d)", v0, v1, v2);
            }
            if (n == 4) {
                int v0 = data.get(i, 0);
                int v1 = data.get(i, 1);
                int v2 = data.get(i, 2);
                int v3 = data.get(i, 3);
                return String.format("(%d, %d, %d, %d)", v0, v1, v2, v3);
            }
        }
        return null;
    }
    private static String AccessorFloatDataToString(AccessorFloatData data, int i) {
        if (i < data.getNumElements()) {
            int n = data.getNumComponentsPerElement();
            if (n == 1) {
                float v0 = data.get(i, 0);
                return String.format("%.02f", v0);
            }
            if (n == 2) {
                float v0 = data.get(i, 0);
                float v1 = data.get(i, 1);
                return String.format("(%.02f, %.02f)", v0, v1);
            }
            if (n == 3) {
                float v0 = data.get(i, 0);
                float v1 = data.get(i, 1);
                float v2 = data.get(i, 2);
                return String.format("(%.02f, %.02f, %.02f)", v0, v1, v2);
            }
            if (n == 4) {
                float v0 = data.get(i, 0);
                float v1 = data.get(i, 1);
                float v2 = data.get(i, 2);
                float v3 = data.get(i, 3);
                return String.format("(%.02f, %.02f, %.02f, %.02f)", v0, v1, v2, v3);
            }
        }
        return null;
    }
    
    private static String AccessorDataToString(AccessorData data, int i) {
        Class<?> componentType = data.getComponentType();
        if (componentType.equals(byte.class)) {
            return AccessorByteDataToString((AccessorByteData) data, i);
        } else if (componentType.equals(short.class)) {
            return AccessorShortDataToString((AccessorShortData) data, i);
        } else if (componentType.equals(int.class)) {
            return AccessorIntDataToString((AccessorIntData) data, i);
        } else if (componentType.equals(float.class)) {
            return AccessorFloatDataToString((AccessorFloatData) data, i);
        }
        return null;
    }
    
    @Override
    protected String[][] getNodePreviewTableValues() {
        final GltfModel model = this.importer.getModel();
        final String[][] result = new String[0][];
        if (nodes != null){
            Map<String, Integer> attributes = nodes.getAttributes();
            List<AccessorData> accessors = attributes.values().stream().map((i) -> model.getAccessorModels().get(i).getAccessorData()).collect(Collectors.toList());
            int numCols = accessors.size();
            int numRows = accessors.stream().map((accessor) -> accessor.getNumElements()).reduce(MAX_ROWS_PREVIEW, (a,b) -> Math.min(a, b));
            
            List<String[]> rows = new ArrayList<>();
            for (int i=0; i<numRows; ++i) {
                String[] row = new String[numCols];
                for (int j=0; j<row.length; ++j) {
                    row[j] = AccessorDataToString(accessors.get(j), i);
                }
                rows.add(row);
            }
            
            return rows.toArray(result);
        }
        return result;
    }
    
    @Override
    protected String[][] getEdgePreviewTableValues() {
        final GltfModel model = this.importer.getModel();
        final String[][] result = new String[0][];
        if (edges != null){
            Map<String, Integer> attributes = edges.getAttributes();
            List<AccessorData> accessors = attributes.values().stream().map((i) -> model.getAccessorModels().get(i).getAccessorData()).collect(Collectors.toList());
            int numCols = accessors.size();
            int numRows = accessors.stream().map((accessor) -> accessor.getNumElements()).reduce(MAX_ROWS_PREVIEW, (a,b) -> Math.min(a, b));
            
            List<String[]> rows = new ArrayList<>();
            for (int i=0; i<numRows; ++i) {
                String[] row = new String[numCols];
                for (int j=0; j<row.length; ++j) {
                    row[j] = AccessorDataToString(accessors.get(j), i);
                }
                rows.add(row);
            }
            
            return rows.toArray(result);
        }
        return result;
    }

    public ValidationPanel getValidationPanel() {
        if (validationPanel != null) {
            return validationPanel;
        }
        validationPanel = new ValidationPanel();
        validationPanel.setInnerComponent(WizardVisualPanel1GLTF.this);
        ValidationGroup validationGroup = validationPanel.getValidationGroup();
        /*
        validationGroup.add(pathTextField, new Validator<String>() {
            @Override
            public boolean validate(Problems problems, String string, String t) {
                if (!areValidColumnsForTable()) {
                    //problems.add(getMessage("WizardVisualPanel1CSV.validation.edges.no-source-target-columns"));
                    return false;
                }
                if (hasRowsMissingSourcesOrTargets()) {
                    //problems.add(NbBundle.getMessage(WizardVisualPanel1CSV.class, "WizardVisualPanel1CSV.validation.edges.empty-sources-or-targets"), Severity.WARNING);
                }
                return true;
            }
        });
        */
        validationGroup.add(nodeComboBox, new Validator<String>() {
            @Override
            public boolean validate(Problems problems, String string, String t) {
                ItemWrapper item = (ItemWrapper)nodeComboBox.getSelectedItem();
                MeshPrimitive nodes = item!=null? mesh.getPrimitives().get(item.getIndex()): null;
                return hasVaildNodeAttributes(nodes, problems);
            }
        });
        validationGroup.add(edgeComboBox, new Validator<String>() {
            @Override
            public boolean validate(Problems problems, String string, String t) {
                ItemWrapper item = (ItemWrapper)edgeComboBox.getSelectedItem();
                MeshPrimitive edges = item!=null? mesh.getPrimitives().get(item.getIndex()): null;
                return hasVaildEdgeAttributes(edges, problems);
            }
        });
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
    
    public boolean hasVaildNodeAttributes(MeshPrimitive nodes, Problems problems) {
        final String[] requiredAttributes = new String[] {"POSITION"};
        if (nodes != null) {
            Map<String, Integer> attributes = nodes.getAttributes();
            for (String attribute: requiredAttributes) {
                if (!attributes.containsKey(attribute)) {
                    problems.add(String.format("Nodes missing \"%s\" attribute. (required)", attribute));
                    return false;
                }
            }
        } else {
            problems.add("Missing nodes.", Severity.WARNING);
        }
        return true;
    }
    
    public boolean hasVaildEdgeAttributes(MeshPrimitive edges, Problems problems) {
        final String[] requiredAttributes = new String[] {"POSITION", "_ID_0"};
        if (edges != null) {
            Map<String, Integer> attributes = edges.getAttributes();
            for (String attribute: requiredAttributes) {
                if (!attributes.containsKey(attribute)) {
                    problems.add(String.format("Edges missing \"%s\" attribute. (required)", attribute));
                    return false;
                }
            }
        } else {
            problems.add("Missing edges.", Severity.WARNING);
        }
        return true;
    }
    
    @Override
    protected JTable getNodePreviewTable() {
        return nodePreviewTable;
    }
    
    @Override
    protected JTable getEdgePreviewTable() {
        return edgePreviewTable;
    }

    @Override
    protected JScrollPane getNodePreviewTableScrollPane() {
        return nodePane;
    }
    
    @Override
    protected JScrollPane getEdgePreviewTableScrollPane() {
        return edgePane;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pathPanel = new javax.swing.JPanel();
        pathLabel = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();
        meshLabel = new javax.swing.JLabel();
        meshComboBox = new javax.swing.JComboBox<>();
        nodeLabel = new javax.swing.JLabel();
        nodeComboBox = new javax.swing.JComboBox<>();
        edgeLabel = new javax.swing.JLabel();
        edgeComboBox = new javax.swing.JComboBox<>();
        previewPane = new javax.swing.JTabbedPane();
        nodePane = new javax.swing.JScrollPane();
        nodePreviewTable = new javax.swing.JTable();
        edgePane = new javax.swing.JScrollPane();
        edgePreviewTable = new javax.swing.JTable();

        setName("Options"); // NOI18N

        pathPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pathLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pathLabel.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1GLTF.class, "WizardVisualPanel1GLTF.pathLabel.text_1")); // NOI18N

        pathTextField.setEditable(false);
        pathTextField.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1GLTF.class, "WizardVisualPanel1GLTF.pathTextField.text")); // NOI18N

        meshLabel.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1GLTF.class, "WizardVisualPanel1GLTF.meshLabel.text")); // NOI18N

        meshComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                meshComboBoxItemStateChanged(evt);
            }
        });

        nodeLabel.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1GLTF.class, "WizardVisualPanel1GLTF.nodeLabel.text")); // NOI18N

        nodeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nodeComboBoxItemStateChanged(evt);
            }
        });

        edgeLabel.setText(org.openide.util.NbBundle.getMessage(WizardVisualPanel1GLTF.class, "WizardVisualPanel1GLTF.edgeLabel.text")); // NOI18N

        edgeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                edgeComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pathPanelLayout = new javax.swing.GroupLayout(pathPanel);
        pathPanel.setLayout(pathPanelLayout);
        pathPanelLayout.setHorizontalGroup(
            pathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pathPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pathTextField)
                    .addComponent(pathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pathPanelLayout.createSequentialGroup()
                        .addGroup(pathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(meshLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                            .addComponent(meshComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                            .addComponent(nodeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(edgeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edgeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pathPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {edgeLabel, meshLabel, nodeLabel});

        pathPanelLayout.setVerticalGroup(
            pathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pathPanelLayout.createSequentialGroup()
                .addComponent(pathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nodeLabel)
                    .addComponent(edgeLabel)
                    .addComponent(meshLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pathPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(meshComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edgeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        nodePreviewTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        nodePane.setViewportView(nodePreviewTable);

        previewPane.addTab(org.openide.util.NbBundle.getMessage(WizardVisualPanel1GLTF.class, "WizardVisualPanel1GLTF.nodePane.TabConstraints.tabTitle"), nodePane); // NOI18N

        edgePreviewTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        edgePane.setViewportView(edgePreviewTable);

        previewPane.addTab(org.openide.util.NbBundle.getMessage(WizardVisualPanel1GLTF.class, "WizardVisualPanel1GLTF.edgePane.TabConstraints.tabTitle"), edgePane); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pathPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(previewPane))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pathPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previewPane, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void edgeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_edgeComboBoxItemStateChanged
        if (initialized) {
            ItemWrapper item = (ItemWrapper)edgeComboBox.getSelectedItem();
            MeshPrimitive edges = item!=null? mesh.getPrimitives().get(item.getIndex()): null;
            setSelectedEdges(edges);
        }
    }//GEN-LAST:event_edgeComboBoxItemStateChanged

    private void nodeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nodeComboBoxItemStateChanged
        if (initialized) {
            ItemWrapper item = (ItemWrapper)nodeComboBox.getSelectedItem();
            MeshPrimitive nodes = item!=null? mesh.getPrimitives().get(item.getIndex()): null;
            setSelectedNodes(nodes);
        }
    }//GEN-LAST:event_nodeComboBoxItemStateChanged

    private void meshComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_meshComboBoxItemStateChanged
        if (initialized) {
            ItemWrapper item = (ItemWrapper)meshComboBox.getSelectedItem();
            Mesh mesh = item!=null? document.getMeshes().get(item.getIndex()): null;
            setSelectedMesh(mesh);
        }
    }//GEN-LAST:event_meshComboBoxItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<ItemWrapper> edgeComboBox;
    private javax.swing.JLabel edgeLabel;
    private javax.swing.JScrollPane edgePane;
    private javax.swing.JTable edgePreviewTable;
    private javax.swing.JComboBox<ItemWrapper> meshComboBox;
    private javax.swing.JLabel meshLabel;
    private javax.swing.JComboBox<ItemWrapper> nodeComboBox;
    private javax.swing.JLabel nodeLabel;
    private javax.swing.JScrollPane nodePane;
    private javax.swing.JTable nodePreviewTable;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JPanel pathPanel;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JTabbedPane previewPane;
    // End of variables declaration//GEN-END:variables
}
