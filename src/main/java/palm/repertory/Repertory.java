package palm.repertory;

import palm.entity.AgeFace;
import palm.entity.Image;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Repertory {
    // maybe specified number, avoid memory error
    public static BlockingDeque<AgeFace> ageFaces = new LinkedBlockingDeque<AgeFace>();
    public static BlockingQueue<Image> images = new LinkedBlockingDeque<Image>();
}