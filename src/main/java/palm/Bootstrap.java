package palm;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import palm.entity.AgeFace;
import palm.repertory.Repertory;
import palm.thread.ImageSaveThread;
import palm.thread.ImageUrlThread;
import palm.util.Configuration;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bootstrap {

    private static Logger logger = LogManager.getLogger(Bootstrap.class.getSimpleName());
    public ExecutorService exec = Executors.newFixedThreadPool(30);

    public static void main(String[] args) throws IOException {

        // read config info from file
        new Configuration("project.properties");

        // init ages Deque
        for (int age = Configuration.startAge; age <= Configuration.endAge; age += Configuration.stepAge) {
            try {
                AgeFace ageFace = new AgeFace(age, 0);
                Repertory.ageFaces.put(ageFace);
            } catch (Exception e) {
                logger.error("Add "+ age + "to ages Deque Error." + e.getMessage());
            }
        }

        logger.info("Init ages Deque Success.");

        // run thread in thread pools
        Bootstrap bootstrap = new Bootstrap();
        // run imageUrlThread
        bootstrap.imageUrlThreadMain();
        // run imageSaveThread
        bootstrap.imageSaveThreadMain();
    }

    public void imageUrlThreadMain() {
        for(int i = 0; i < Configuration.imageUrlThreadNum; i++) {
            ImageUrlThread imageUrlThread = new ImageUrlThread();
            this.exec.execute(imageUrlThread);
        }
    }

    public void imageSaveThreadMain() {
        for(int i = 0; i < Configuration.imageUrlThreadNum; i++) {
            ImageSaveThread imageSaveThread = new ImageSaveThread();
            this.exec.execute(imageSaveThread);
        }
    }
}
