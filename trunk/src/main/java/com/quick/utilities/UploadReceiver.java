/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.utilities;

/**
 *
 * @author sahil
 */
import com.quick.global.GlobalConstants;
import com.vaadin.ui.Upload.Receiver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UploadReceiver implements Receiver {

    private static final long serialVersionUID = 1L;
    private String fileName;
    private String mtype;
    private int counter;
    private File file;
    FileOutputStream fos = null;
    /**
     * return an OutputStream that simply counts line ends
     */
    OutputStream outputFile = null;

    @Override
    public OutputStream receiveUpload(String filename, String MIMEType) {
        counter = 0;
        fileName = filename;
        mtype = MIMEType;



        try {
            ////System.out.println("GlobalConstants.FILE_UPLOAD_PATH="+GlobalConstants.FILE_UPLOAD_PATH);
            file = new File(GlobalConstants.getProperty(GlobalConstants.FILE_UPLOAD_PATH) + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputFile = new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    protected void finalize() {
        try {
            super.finalize();
            if (outputFile != null) {
                outputFile.flush();
                outputFile.close();
                
            }
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mtype;
    }

    public int getLineBreakCount() {
        return counter;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    public void clearResources(){
                this.fos=null;
                this.file =null;
                this.fileName = null;
    }
}
