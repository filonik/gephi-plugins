/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.io.importer.plugin.gltf.process;

import de.javagl.jgltf.model.v1.GltfModelV1;
import java.io.IOException;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Daniel Filonik
 */
public class GltfModelV1Configuration extends AbstractConfiguration {
    public GltfModelV1Configuration(GltfModelV1 model) {
        super(model);
    }
    
    @Override
    public AbstractImportProcess createProcess(ContainerLoader container, ProgressTicket progressTicket) throws IOException {
        return new ImportGLTFModelV1Process(this, container, progressTicket);
    }
}
