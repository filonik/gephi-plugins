/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.io.importer.plugin.gltf.process;

import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Mesh;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.model.v2.GltfModelV2;
import java.io.IOException;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Daniel Filonik
 */
public class GltfModelV2Configuration extends AbstractConfiguration {
    private de.javagl.jgltf.impl.v2.GlTF document = null;
    private de.javagl.jgltf.impl.v2.Mesh mesh = null;
    private de.javagl.jgltf.impl.v2.MeshPrimitive nodes = null;
    private de.javagl.jgltf.impl.v2.MeshPrimitive edges = null;
    
    public GltfModelV2Configuration(GltfModelV2 model) {
        super(model);
    }
    
    public GlTF getDocument() {
        return document;
    }
    
    public void setDocument(GlTF document) {
        this.document = document;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
    
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    
    public MeshPrimitive getNodes() {
        return nodes;
    }
    
    public void setNodes(MeshPrimitive nodes) {
        this.nodes = nodes;
    }

    public MeshPrimitive getEdges() {
        return edges;
    }

    public void setEdges(MeshPrimitive edges) {
        this.edges = edges;
    }

    @Override
    public AbstractImportProcess createProcess(ContainerLoader container, ProgressTicket progressTicket) throws IOException {
        return new ImportGLTFModelV2Process(this, container, progressTicket);
    }
}
