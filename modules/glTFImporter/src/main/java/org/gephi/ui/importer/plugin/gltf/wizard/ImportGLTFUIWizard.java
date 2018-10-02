/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.importer.plugin.gltf.wizard;

import java.awt.Component;
import javax.swing.JComponent;
import org.gephi.io.importer.plugin.gltf.ImporterGLTF;
import org.openide.WizardDescriptor;

/**
 *
 * @author Daniel Filonik
 */
public final class ImportGLTFUIWizard {

    private final ImporterGLTF[] importers;
    private WizardDescriptor.Panel[] panels;
    private WizardDescriptor wizardDescriptor;
    
    public ImportGLTFUIWizard(ImporterGLTF[] importers) {
        this.importers = importers;
        initDescriptor();
    }
    
    public WizardDescriptor getDescriptor() {
        return wizardDescriptor;
    }
    
    public void initDescriptor() {
        buildPanels();
        wizardDescriptor = new WizardDescriptor(panels);
    }
    
    private void buildPanels() {
        panels = new WizardDescriptor.Panel[importers.length * 2];

        for (int i = 0; i < importers.length; i++) {
            ImporterGLTF importer = importers[i];
            WizardPanel1GLTF step1 = new WizardPanel1GLTF(importer);
            WizardPanel2GLTF step2 = new WizardPanel2GLTF(importer);

            panels[i * 2 + 0] = step1;
            panels[i * 2 + 1] = step2;
        }
        
        String[] steps = new String[panels.length];
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            steps[i] = c.getName();
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                // Sets step number of a component
                jc.putClientProperty("WizardPanel_contentSelectedIndex", i);
                // Sets steps names for a panel
                jc.putClientProperty("WizardPanel_contentData", steps);
                // Turn on subtitle creation on each step
                jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                // Show steps on the left side with the image on the background
                jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                // Turn on numbering of all steps
                jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
            }
        }
    }
}
