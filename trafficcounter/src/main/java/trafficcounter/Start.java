package trafficcounter;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by tobias on 25.08.17.
 */
public class Start {

    /* change this path to an image on your disk which you want to work with */
    public static final String IMAGE_PATH = "src/main/java/trafficcounter/Car2.jpg";
    public static final String IMAGE_TEST1_PATH = "src/main/java/trafficcounter/CarTest1.jpeg";
    public static final String IMAGE_TEST2_PATH = "src/main/java/trafficcounter/CarTest2.jpeg";
    public static final String IMAGE_TEST3_PATH = "src/main/java/trafficcounter/CarTest3.jpeg";
    public static final String CASCADE_PATH = "src/test/resources/haar_cascades/haarcascade_car.xml";

    /* window size */
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_WIDTH = 1600;

    public static void main(String[] args) {

    	
        // load the native library 
    	nu.pattern.OpenCV.loadShared();

        ImageHelper helper = new ImageHelper();
      //  Mat image = Mat.zeros( WINDOW_HEIGHT, WINDOW_WIDTH,  CV_8UC3 );

        // read an image to work with from disk 
        Mat input = Imgcodecs.imread(IMAGE_TEST3_PATH);

        // perform image processing on this image 
        Mat processedImage = processImageHaarCascade(input);

        // add the original and the processed image to the panel and show the window 
        helper.addImage(input);
        helper.addImage(processedImage);
        
        
        

    }


    /**
     * takes a mat and performs some image processing on it
     * @param input the image to process
     * @return the processed image
     */
    public static Mat processImage(Mat input) {

   	 Mat blur = new Mat();
   	 
       /* * * * * * * * * * * * * * * * * * * * * * * * * * * *
        *  This is your place to start.
        *  Do whatever you want with OpenCV here!
        *  For example: Convert colors to gray
        * * * * * * * * * * * * * * * * * * * * * * * * * * * */
       Imgproc.blur(input, blur, new Size(7, 7));
       Mat processed = new Mat(blur.size(),blur.type());
       Imgproc.cvtColor(blur, processed, Imgproc.COLOR_BGR2HSV);
       
       return processed;
   }
    
    /**
     * returns an image with rectangles for each car it identifies
     * @param input the image to process
     * @return the processed image
     */
    
    public static Mat processImageHaarCascade(Mat input) {

        Mat processed = input.clone();        
        
//        Imgproc.cvtColor(input, processed, Imgproc.COLOR_BGR2HSV);

        CascadeClassifier cascade = new CascadeClassifier(CASCADE_PATH);
        
        MatOfRect faces = new MatOfRect();
        cascade.detectMultiScale(processed, faces);
        
        Rect[] facesArray = faces.toArray();
        System.out.println(facesArray.length);
        
        for (Rect r :  facesArray) {
        	Imgproc.rectangle(processed, r.tl(), r.br(), new Scalar(0, 0, 0, 255), 3);			
		}
              
        return processed;
    }
    
}