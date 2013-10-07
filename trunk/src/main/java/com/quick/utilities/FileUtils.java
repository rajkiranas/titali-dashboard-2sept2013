/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.utilities;


import com.quick.global.GlobalConstants;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
//import com.vaadin.terminal.DownloadStream;
//import com.vaadin.terminal.FileResource;
//import com.vaadin.terminal.StreamResource;
import java.io.*;
import java.util.*;


/**
 *
 * @author suyogn
 */
public class FileUtils {
      
///**
// * This method is used to download all type of files 
// * @param app
// * @param filePath
// * @param filename
// * @return 
// */
//     public static FileResource DownLoadFile(MyApplication app,String filePath ,String filename) {
//         final File sourceFile = new File(GlobalConstants.getProperty(filePath),filename);
//       
//                final FileResource stream = new FileResource(sourceFile, app) {
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public DownloadStream getStream() {
//                        try {
//                            final DownloadStream ds = new DownloadStream(new FileInputStream(sourceFile), getMIMEType(), getFilename());
//                            ds.setParameter("Content-Length", String.valueOf(sourceFile.length()));
//                            ds.setCacheTime(0);
//                            return ds;
//                        } catch (final FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                stream.setCacheTime(0);
//                return stream;
//                //getWindow().open(stream);
//            }
     
     
     
     /**
      * This method check file is exist or not exist
      * @param filePath
      * @param fileName
      * @return 
      */  
     public static boolean isFileExist(String filePath, String fileName)
     {
         File source = new File(GlobalConstants.getProperty(filePath)+fileName);
         return source.exists();
     }
    
 public static byte[] getFileIntoByteArray(File file) {
        byte[] filebyte = new byte[(int) file.length()];
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(filebyte);
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
        return filebyte;
    }
 
 
//  public static void dwnldAttachment(byte[] data,MyApplication app,String str) {
//        try 
//        {
//            //getWindow().open(FileUtils.DownLoadFile(app, GlobalConstants.EXCEL_FILE_DIRECTORY,"Incentive_NewEngland_Q4.xls"));
//            File dwnfile=new File(GlobalConstants.FILE_DOWNLOAD_PATH+app.getEmpLoginProfile().getLoginId() + str);
//            
////           // String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
////            ServletContext context =((WebApplicationContext)app.getContext()).getHttpSession().getServletContext();
//            FileOutputStream fos = new FileOutputStream(dwnfile);
//            fos.write(data);
//            
//            fos.flush();
//            fos.close();
//                    
////            getWindow().open(FileUtils.DownLoadFile(application,GlobalConstants.FILE_DOWNLOAD_PATH,dwnfile.getName()),"Download file",100,100,0);
//            
//        } catch (FileNotFoundException ex) {
//          //  Logger.getLogger(ReadMail.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//               // Logger.getLogger(ReadMail.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        finally
//        {
//            
//        }       
//        
//    }   
  
  public static boolean validateFileExtension(String fileName){
      
      String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (ext.equals("gif") || ext.equals("GIF") || ext.equals("JPEG") || ext.equals("jpeg") || ext.equals("jpg") || ext.equals("JPG") || ext.equals("bmp")||ext.equals("BMP")||ext.equals("png")||ext.equals("PNG")) {
            return true;
        } else {
            return false;
        }
    }
  
//   public static boolean validateFileExtensionForWorkspece(String fileName){
//      
//      String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
//       if(MyApplication.getFileExtensions().contains(ext.toLowerCase())) {
//            return true;
//        } else {
//            return false;
//        }
//    }
  
//   public static StreamResource readFileByte(String filename,String strFileType,String folderName,String folderType ,MyApplication  app) 
//    {
//         File file=new File(GlobalConstants.FILE_UPLOAD_PATH+folderName+"/"+folderType+"/"+filename); 
//         byte[] encrptedFileByte = com.saterisystems.common.utils.FileUtils.getImageIntoByteArray(file);
//         byte[] decryptedFilebyte=Encryption.aesDecrypt(encrptedFileByte,GlobalConstants.KEY.getBytes(), GlobalConstants.IV.getBytes());
//         return  showFile(decryptedFilebyte,filename,strFileType,app);
//    }
//    
//     public static StreamResource showFile(final byte[] decryptedFilebyte,final String filename,final String strFileType,MyApplication  app) 
//     {
//        StreamResource imageResource = null;
//        
//        StreamResource.StreamSource source = new StreamResource.StreamSource() 
//        {
//            @Override
//            public InputStream getStream() 
//            {
//                return new ByteArrayInputStream(decryptedFilebyte);
//            }
//        };
//        
//        imageResource = new StreamResource(source, filename, app);
//        imageResource.setCacheTime(0);
//        //imageResource = new StreamResource(source, "Narkhede.pdf", app);
//        String strMimeType=GlobalConstants.getProperty("MIME_TYPE");
//        String[] mimeTypeArr=strMimeType.split(",");
//        for(int t=0;t<mimeTypeArr.length;t++)
//        {
//            String[] TypeArr=mimeTypeArr[t].split("#");
//            if(TypeArr[1].equalsIgnoreCase(strFileType))
//            {
//                imageResource.setMIMEType(TypeArr[0]);
//                break;
//            }
//        }
//        
//        return imageResource;
//    }
}
