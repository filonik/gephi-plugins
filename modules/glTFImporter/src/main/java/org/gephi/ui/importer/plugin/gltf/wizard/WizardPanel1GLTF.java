package org.gephi.ui.importer.plugin.gltf.wizard;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.gephi.io.importer.plugin.gltf.ImporterGLTF;
import org.gephi.io.importer.plugin.gltf.process.AbstractConfiguration;
import org.gephi.io.importer.plugin.gltf.process.GltfModelV2Configuration;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel Filonik
 */
public class WizardPanel1GLTF implements WizardDescriptor.Panel {
    private final ImporterGLTF importer;

    private WizardVisualPanel1GLTF component;
    
    public WizardPanel1GLTF(ImporterGLTF importer) {
        this.importer = importer;
}
    
    public Component getComponent() {
        if (component == null) {
            component = new WizardVisualPanel1GLTF(importer, this);
        }
        return component.getValidationPanel();
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        return component.isValidData();
}

    private final Set<ChangeListener> listeners = new HashSet<>(1); // or can use ChangeSupport in NB 6.0

    @Override
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
}
    
    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    @Override
    public void readSettings(Object settings) {
        component.refreshNodePreviewTable();
        component.refreshEdgePreviewTable();
    }

    @Override
    public void storeSettings(Object settings) {
        GltfModelV2Configuration configuration = (GltfModelV2Configuration) importer.getConfiguration();
        configuration.setDocument(component.getSelectedDocument());
        configuration.setMesh(component.getSelectedMesh());
        configuration.setNodes(component.getSelectedNodes());
        configuration.setEdges(component.getSelectedEdges());
    }

}
