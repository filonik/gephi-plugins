/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.io.importer.plugin.gltf.process;

import de.javagl.jgltf.model.v1.GltfModelV1;
import java.io.IOException;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Daniel Filonik
 */
public class ImportGLTFModelV1Process extends AbstractImportProcess {
    private GltfModelV1 model;
    
    public ImportGLTFModelV1Process(GltfModelV1Configuration configuration, ContainerLoader container, ProgressTicket progressTicket) throws IOException {
        super(container, progressTicket);
        
        this.model = model;
    }
    
    @Override
    public boolean execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}