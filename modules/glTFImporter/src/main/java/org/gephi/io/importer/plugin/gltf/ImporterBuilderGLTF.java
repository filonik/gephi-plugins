/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.io.importer.plugin.gltf;

import org.gephi.io.importer.api.FileType;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.importer.spi.FileImporterBuilder;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Daniel Filonik
 */
@ServiceProvider(service = FileImporterBuilder.class)
public final class ImporterBuilderGLTF implements FileImporterBuilder {

    public static final String IDENTIFER = "gltf";
    public static final String[] EXTENSIONS = new String[]{".gltf"};

    @Override
    public FileImporter buildImporter() {
        return new ImporterGLTF();
    }

    @Override
    public String getName() {
        return IDENTIFER;
    }

    @Override
    public FileType[] getFileTypes() {
        return new FileType[]{
            new FileType(EXTENSIONS, "GL Transmission Format Files")
        };
    }

    @Override
    public boolean isMatchingImporter(FileObject fileObject) {
        for (String ext : EXTENSIONS) {
            if (fileObject.getExt().equalsIgnoreCase(ext.substring(1))) {
                return true;
            }
        }

        return false;
    }
}
