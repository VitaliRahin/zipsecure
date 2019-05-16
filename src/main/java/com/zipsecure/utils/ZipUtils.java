package com.zipsecure.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;


public final class ZipUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

    public static ZipFile createSplitZipFile(String pathFileName, String pathArc, char[] password, int splitLength) throws ZipException {
        deleteFileIfExist(pathArc);
        ZipFile zipFile = new ZipFile(pathArc);
        ArrayList<File> filesToAdd = new ArrayList<>();
        filesToAdd.add(new File(pathFileName));
        LOGGER.info("Creating archive " + pathArc + "from file " + pathFileName);

        ZipParameters parameters = new ZipParameters();

        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        parameters.setPassword(password);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

        zipFile.createZipFile(filesToAdd, parameters, true, splitLength);
        LOGGER.info("Archiving completed");
        return zipFile;
    }

    private static void deleteFileIfExist(String filePath){
        new File(filePath).deleteOnExit();
    }
}
