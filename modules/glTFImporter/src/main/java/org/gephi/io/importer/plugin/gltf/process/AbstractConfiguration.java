/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.io.importer.plugin.gltf.process;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.v1.GltfModelV1;
import de.javagl.jgltf.model.v2.GltfModelV2;
import java.io.IOException;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Daniel Filonik
 */
public abstract class AbstractConfiguration {
    protected GltfModel model;
    
    public static AbstractConfiguration FromModel(GltfModel model) {
        if (model instanceof GltfModelV1) {
            return new GltfModelV1Configuration((GltfModelV1)model);
        } 
        if (model instanceof GltfModelV2) {
            return new GltfModelV2Configuration((GltfModelV2)model);
        } 
        return null;
    }
    
    public AbstractConfiguration(GltfModel model) {
        this.model = model;
    }
    
    public GltfModel getModel() {
        return model;
    }
    
    public void setModel(GltfModel value) {
        this.model = value;
    }
    
    public abstract AbstractImportProcess createProcess(ContainerLoader container, ProgressTicket progressTicket) throws IOException;
}
