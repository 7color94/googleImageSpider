package palm.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringEscapeUtils;
import org.omg.CORBA.IMP_LIMIT;
import palm.entity.Image;
import palm.repertory.Repertory;
import palm.util.Configuration;

import java.io.*;
import java.net.URL;

public class ImageSaveThread implements Runnable {

    private static Logger logger = LogManager.getLogger(ImageSaveThread.class.getSimpleName());

    public Image takeAgeFaceFromDeque() {
        Image image = null;
        try {
            image = Repertory.images.take();
        } catch (Exception e) {
            logger.error("Take image from Deque Error." + e.getMessage());
        }
        return image;
    }

    public void putAgeFaceToDeque(Image img) {
        Image image = null;
        try {
            Repertory.images.put(img);
        } catch (Exception e) {
            logger.error("Put image to Deque Error." + e.getMessage());
        }
    }

    public boolean saveImage(String imgUrl, String destinationFile) {
        URL url;
        InputStream is = null;
        OutputStream os = null;
        boolean res = false;
        try {
            url = new URL(imgUrl);
            is = url.openStream();
            os = new FileOutputStream(destinationFile);
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            logger.info("Save image " + imgUrl + " Success.");
            res = true;
        } catch (IOException e) {
            logger.error("Save image " + imgUrl + "Error." + e.getMessage());
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                logger.error("Close IO Error." + e.getMessage());
            }
        }
        return res;
    }

    public boolean createDirectory(String dirName) {
        File theDir = new File(dirName);
        boolean result = false;
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            logger.info("Creating directory: " + dirName);
            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                logger.error("Creating directory Error.");
            }
            if (result) {
                logger.info("Dir created");
            }
        } else {
            result = true;
        }
        return result;
    }

    public void run() {
        while (true) {
            Image img = takeAgeFaceFromDeque();
            if (img == null) {
                continue;
            }
            String imgUrl = StringEscapeUtils.unescapeJava(img.getUrl());
            String destDir = Configuration.imageSavePath + img.getAge();
            String destFile = Configuration.imageSavePath + img.getAge() + "/" + img.getId() + ".jpg";
            logger.info("Save img " + imgUrl + " To " + destFile);
            boolean fileCreated = createDirectory(destDir);
            if (fileCreated == false) {
                logger.error("Create Dir " + destDir + " Error.");
                putAgeFaceToDeque(img);
                continue;
            }
            if (!saveImage(imgUrl, destFile)) {
                putAgeFaceToDeque(img);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //String imageUrl = "https://encrypted-tbn3.gstatic.com//images?q=tbn:ANd9GcQMr9AN_w4zTK9eSBF8i-szC_ubSKX5LrlXsTCNTRRhjjeJltgocA";
        String imageUrl = "https:\\/\\/encrypted-tbn3.gstatic.com\\/images?q=tbn:ANd9GcQMr9AN_w4zTK9eSBF8i-szC_ubSKX5LrlXsTCNTRRhjjeJltgocA";
        imageUrl = StringEscapeUtils.unescapeJava(imageUrl);
        Image img = new Image(imageUrl.hashCode(), 20, imageUrl);
        String destinationFile = "/Users/sukai/Desktop/savedImage.jpg";
        ImageSaveThread i = new ImageSaveThread();
//        i.saveImage(imageUrl, destinationFile);
        i.createDirectory("10");
    }
}
