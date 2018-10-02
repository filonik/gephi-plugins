/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.io.importer.plugin.gltf;

import java.io.Reader;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.Report;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.ProgressTicket;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import de.javagl.jgltf.model.v1.GltfModelV1;
import de.javagl.jgltf.model.v2.GltfModelV2;
import java.io.File;
import java.io.IOException;
import org.gephi.io.importer.plugin.gltf.process.AbstractConfiguration;
import org.gephi.io.importer.plugin.gltf.process.AbstractImportProcess;
import org.gephi.io.importer.plugin.gltf.process.ImportGLTFModelV1Process;
import org.gephi.io.importer.plugin.gltf.process.ImportGLTFModelV2Process;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

/**
 *
 * @author Daniel Filonik
 */
public class ImporterGLTF implements FileImporter, FileImporter.FileAware, LongTask  {
    private ContainerLoader container;
    private Report report;
    private ProgressTicket progressTicket;
    private boolean cancel = false;
    
    protected AbstractConfiguration generalConfig = null;
    protected AbstractImportProcess importer = null;
    
    protected File file;
    
    @Override
    public ContainerLoader getContainer() {
        return container;
    }

    @Override
    public Report getReport() {
        return report;
    }
    
    @Override
    public void setReader(Reader reader) {}
    
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File value) {
        this.file = value;
    }
    
    public AbstractConfiguration getConfiguration() {
        return generalConfig;
    }

    public void setConfiguration(AbstractConfiguration value) {
        generalConfig = value;
    }
    
    public GltfModel getModel() {
        return generalConfig.getModel();
    }

    public void setModel(GltfModel value) {
        generalConfig.setModel(value);
    }
    
    public void loadModel() {
        try {
            GltfModelReader gltfModelReader = new GltfModelReader();
            GltfModel model = gltfModelReader.read(Utilities.toURI(getFile()));
            setConfiguration(AbstractConfiguration.FromModel(model));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    @Override
    public boolean execute(ContainerLoader container) {
        this.container = container;
        this.report = new Report();
        
        try {
            //GltfModelReader gltfModelReader = new GltfModelReader();
            //this.model = gltfModelReader.read(file.toURI());
            
            importer = generalConfig.createProcess(container, progressTicket);
            
            importer.execute();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if (importer != null) {
                report.append(importer.getReport());
                importer = null;
            }
        }
        
        return !cancel;
    }

    @Override
    public boolean cancel() {
        /*
        if (importer != null) {
            importer.cancel();
            importer = null;
        }
        */
        return cancel = true;
    }
    
    @Override
    public void setProgressTicket(ProgressTicket progressTicket) {
        this.progressTicket = progressTicket;
    }

}
