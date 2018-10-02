/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.importer.plugin.gltf;

import javax.swing.JPanel;
import org.gephi.io.importer.plugin.gltf.ImporterBuilderGLTF;
import org.gephi.io.importer.plugin.gltf.ImporterGLTF;
import org.gephi.io.importer.spi.Importer;
import org.gephi.io.importer.spi.ImporterUI;
import org.gephi.ui.importer.plugin.gltf.wizard.ImportGLTFUIWizard;
import org.openide.WizardDescriptor;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Daniel Filonik
 */
@ServiceProvider(service = ImporterUI.class)
public class ImporterGLTFUI implements ImporterUI, ImporterUI.WithWizard {
    private ImporterGLTF[] importers;
    private ImportGLTFUIWizard wizard;

    @Override
    public void setup(Importer[] importers) {
        this.importers = (ImporterGLTF[])importers;
        
        for (ImporterGLTF importer : this.importers) {
            importer.loadModel();
        }
    }

    @Override
    public JPanel getPanel() {
        return null;
    }

    @Override
    public void unsetup(boolean update) {}

    @Override
    public String getDisplayName() {
        return "Import (GLTF)";
    }

    public String getIdentifier() {
        return ImporterBuilderGLTF.IDENTIFER;
}

    @Override
    public boolean isUIForImporter(Importer importer) {
        return importer instanceof ImporterGLTF;
    }

    @Override
    public WizardDescriptor getWizardDescriptor() {
        this.wizard = new ImportGLTFUIWizard(importers);
        return wizard.getDescriptor();
    }
    
}
