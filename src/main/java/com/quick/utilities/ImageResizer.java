/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.utilities;

/**
 *
 * @author rajkirans
 */
import com.quick.global.GlobalConstants;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

public class ImageResizer {

    public static byte[] resize(File icon, String topicFileName) {
        try 
        {
            String ext=topicFileName.substring(topicFileName.indexOf(GlobalConstants.FULL_STOP)+1);
           BufferedImage originalImage = ImageIO.read(icon);

           int imgWidth=originalImage.getWidth();
           int imgHeight = originalImage.getHeight();
           
           if(imgWidth>Integer.parseInt(GlobalConstants.getProperty(GlobalConstants.IMAGE_WIDTH)))
           {
               imgWidth=Integer.parseInt(GlobalConstants.getProperty(GlobalConstants.IMAGE_WIDTH));
           }
           
           if(imgHeight>Integer.parseInt(GlobalConstants.getProperty(GlobalConstants.IMAGE_HEIGHT)))
           {
               imgHeight=Integer.parseInt(GlobalConstants.getProperty(GlobalConstants.IMAGE_HEIGHT));
           }
           
           
           originalImage= Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, imgWidth, imgHeight);
            //To save with original ratio uncomment next line and comment the above.
            //originalImage= Scalr.resize(originalImage, 153, 128);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, ext, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {
        byte[] b = resize(new File("/home/rajkirans/Desktop/learnMore.png"),"");
        
        FileOutputStream fileOuputStream = new FileOutputStream("/home/rajkirans/Desktop/Learn.png");
	    fileOuputStream.write(b);
	    fileOuputStream.close();
            
        
    }
}
