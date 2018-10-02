/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.io.importer.plugin.gltf.process;

import java.io.Closeable;
import java.io.IOException;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.Report;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Daniel Filonik
 */
public abstract class AbstractImportProcess implements Closeable {
    protected final ContainerLoader container;
    protected final Report report;
    protected final ProgressTicket progressTicket;

    protected boolean cancel = false;

    public AbstractImportProcess(ContainerLoader container, ProgressTicket progressTicket) {
        this.container = container;
        this.report = new Report();
        this.progressTicket = progressTicket;
    }
    
    public ContainerLoader getContainer() {
        return container;
    }
    
    public Report getReport() {
        return report;
    }
    
    public boolean execute() {
        return !cancel;
    }

    public void close() throws IOException {}
}
