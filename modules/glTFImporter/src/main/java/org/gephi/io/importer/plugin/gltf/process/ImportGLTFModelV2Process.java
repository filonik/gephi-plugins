/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.io.importer.plugin.gltf.process;

import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.model.AccessorData;
import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorIntData;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.GltfModel;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.vecmath.Vector3f;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.NodeDraft;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Daniel Filonik
 */
public class ImportGLTFModelV2Process extends AbstractImportProcess {
    private final int NODE_VERTEX_COUNT = 1;
    private final int EDGE_VERTEX_COUNT = 2;
    
    private GltfModelV2Configuration configuration;
    
    public ImportGLTFModelV2Process(GltfModelV2Configuration configuration, ContainerLoader container, ProgressTicket progressTicket) throws IOException {
        super(container, progressTicket);
        
        this.configuration = configuration;
    }
    
    public static AccessorModel getAccessor(List<AccessorModel> accessorModels, Map<String, Integer> attributes, String[] semantics) {
        for (String semantic : semantics) {
            if (attributes.containsKey(semantic)) {
                return accessorModels.get(attributes.get(semantic));
            }
        }
        return null;
    }
    
    public static AccessorModel getIdAccessor(List<AccessorModel> accessorModels, Map<String, Integer> attributes) {
        final String[] semantics = new String[] {"_ID"};
        return getAccessor(accessorModels, attributes, semantics);
    }
    
    public static AccessorModel getId0Accessor(List<AccessorModel> accessorModels, Map<String, Integer> attributes) {
        final String[] semantics = new String[] {"_ID_0"};
        return getAccessor(accessorModels, attributes, semantics);
    }
    
    public static AccessorModel getPositionAccessor(List<AccessorModel> accessorModels, Map<String, Integer> attributes) {
        final String[] semantics = new String[] {"POSITION"};
        return getAccessor(accessorModels, attributes, semantics);
    }
    
    public static AccessorModel getColorAccessor(List<AccessorModel> accessorModels, Map<String, Integer> attributes) {
        final String[] semantics = new String[] {"COLOR", "COLOR_0", "COLOR_1"};
        return getAccessor(accessorModels, attributes, semantics);
    }
    
    public static AccessorModel getSizeAccessor(List<AccessorModel> accessorModels, Map<String, Integer> attributes) {
        final String[] semantics = new String[] {"_SIZE"};
        return getAccessor(accessorModels, attributes, semantics);
    }
    
    public static AccessorModel getWeightAccessor(List<AccessorModel> accessorModels, Map<String, Integer> attributes) {
        final String[] semantics = new String[] {"_WEIGHT"};
        return getAccessor(accessorModels, attributes, semantics);
    }
    
    public static List<Integer> toListInt(AccessorModel accessor) {
        if (accessor != null) {
            AccessorData data = accessor.getAccessorData();
            if(data.getComponentType().equals(int.class) && data.getNumComponentsPerElement() > 0) {
                AccessorIntData _data = (AccessorIntData)data;
                IntStream range = IntStream.range(0, _data.getNumElements());
                return range.mapToObj(i -> _data.get(i, 0)).collect(Collectors.toList());
            }
        }
        return null;
    }
    
    public static List<Float> toListFloat(AccessorModel accessor) {
        if (accessor != null) {
            AccessorData data = accessor.getAccessorData();
            if(data.getComponentType().equals(float.class) && data.getNumComponentsPerElement() > 0) {
                AccessorFloatData _data = (AccessorFloatData)data;
                IntStream range = IntStream.range(0, _data.getNumElements());
                return range.mapToObj(i -> _data.get(i, 0)).collect(Collectors.toList());
            }
        }
        return null;
    }
    
    public static List<Vector3f> toListVector3f(AccessorModel accessor) {
        if (accessor != null) {
            AccessorData data = accessor.getAccessorData();
            if(data.getComponentType().equals(float.class) && data.getNumComponentsPerElement() > 2) {
                AccessorFloatData _data = (AccessorFloatData)data;
                IntStream range = IntStream.range(0, _data.getNumElements());
                return range.mapToObj(i -> new Vector3f(_data.get(i, 0), _data.get(i, 1), _data.get(i, 2))).collect(Collectors.toList());
            }
        }
        return null;
    }
    
    private static List<Integer> getIndices(GltfModel model, MeshPrimitive primitive, int n) {
        final List<AccessorModel> accessorModels = model.getAccessorModels();
        Integer _i = primitive.getIndices();
        List<Integer> indices = _i != null? toListInt(accessorModels.get(_i)): null;
        if (indices == null) {
            indices = IntStream.range(0, n).boxed().collect(Collectors.toList());
        }
        return indices;
    }
    
    @Override
    public boolean execute() {
        Progress.start(progressTicket);
        
        final GltfModel model = configuration.getModel();
        
        final MeshPrimitive nodes = configuration.getNodes();
        
        if (nodes != null) {
            List<AccessorModel> accessorModels = model.getAccessorModels();
            Map<String, Integer> attributes = nodes.getAttributes();
            
            List<Integer> ids = toListInt(getIdAccessor(accessorModels, attributes));
            
            List<Vector3f> positions = toListVector3f(getPositionAccessor(accessorModels, attributes));
            List<Vector3f> colors = toListVector3f(getColorAccessor(accessorModels, attributes));
            List<Float> sizes = toListFloat(getSizeAccessor(accessorModels, attributes));
            
            if (positions != null) {
                List<Integer> indices = getIndices(model, nodes, positions.size());
                int numRows = indices.size()/NODE_VERTEX_COUNT;
                for (int j=0; j<numRows; j++) {
                    int i0 = indices.get(j*NODE_VERTEX_COUNT + 0);
                    
                    String id = Integer.toString(i0/NODE_VERTEX_COUNT);
                    if (ids != null) {
                        id = Integer.toString(ids.get(i0));
                    }
                    
                    NodeDraft node = container.factory().newNodeDraft(id); // id != null ? container.factory().newNodeDraft(id) : container.factory().newNodeDraft();

                    if (positions != null) {
                        Vector3f position = positions.get(i0);
                        node.setX(position.x);
                        node.setY(position.y);
                        node.setZ(position.z);
                    }
                    
                    if (colors != null) {
                        Vector3f color = colors.get(i0);
                        node.setColor(color.x, color.y, color.z);
                    }
                    
                    if (sizes != null) {
                        float size = sizes.get(i0);
                        node.setSize(size);
                    }
                    
                    container.addNode(node);
                }
            }
        }
        
        final MeshPrimitive edges = configuration.getEdges();
        
        if (edges != null) {
            List<AccessorModel> accessorModels = model.getAccessorModels();
            Map<String, Integer> attributes = edges.getAttributes();
            
            List<Integer> ids = toListInt(getIdAccessor(accessorModels, attributes));
            List<Integer> id0s = toListInt(getId0Accessor(accessorModels, attributes));
            
            List<Vector3f> positions = toListVector3f(getPositionAccessor(accessorModels, attributes));
            List<Vector3f> colors = toListVector3f(getColorAccessor(accessorModels, attributes));
            List<Float> weights = toListFloat(getWeightAccessor(accessorModels, attributes));
            
            if (positions != null) {
                List<Integer> indices = getIndices(model, edges, positions.size());
                int numRows = indices.size()/EDGE_VERTEX_COUNT;
                for (int j = 0; j < numRows; j++) {
                    int i0 = indices.get(j*EDGE_VERTEX_COUNT + 0);
                    int i1 = indices.get(j*EDGE_VERTEX_COUNT + 1);
                    
                    String id = Integer.toString(i0/EDGE_VERTEX_COUNT);
                    if (ids != null) {
                        id = Integer.toString(ids.get(i0));
                    }
                    
                    String source = Integer.toString(id0s.get(i0));
                    String target = Integer.toString(id0s.get(i1));
                    
                    EdgeDraft edge = container.factory().newEdgeDraft(id);
                    
                    //if (!container.nodeExists(i0)) { throw new RuntimeException("Node: " + i0); }
                    //if (!container.nodeExists(i1)) { throw new RuntimeException("Node: " + i1); }
                    
                    edge.setSource(container.getNode(source));
                    edge.setTarget(container.getNode(target));
                    
                    if (colors != null) {
                        Vector3f color = colors.get(i0);
                        edge.setColor(color.x, color.y, color.z);
                    }
                    
                    if (weights != null) {
                        float weight = weights.get(i0);
                        edge.setWeight(weight);
                    }
                    
                    container.addEdge(edge);
                }
            }
        }
        
        Progress.finish(progressTicket);
        
        return !cancel;
    }
    
}
