package trafficcounter;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by tobias on 25.08.17.
 */
public class Start {

    /* change this path to an image on your disk which you want to work with */
    public static final String IMAGE_PATH = "src/main/java/trafficcounter/Car2.jpg";

    /* window size */
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_WIDTH = 1600;

    public static void main(String[] args) {

    	
        // load the native library 
    	nu.pattern.OpenCV.loadShared();

        ImageHelper helper = new ImageHelper();
      //  Mat image = Mat.zeros( WINDOW_HEIGHT, WINDOW_WIDTH,  CV_8UC3 );

        // read an image to work with from disk 
        Mat input = Imgcodecs.imread(IMAGE_PATH);
        helper.addImage(input);
        // perform image processing on this image 
        Mat processedImage = processImage(input);
      Mat processedImage2 = playWithHue(processedImage);
        Mat morphImage = morph(processedImage2);
     
        Mat contours = findContours(input, morphImage);

        // add the original and the processed image to the panel and show the window 
       
        helper.addImage(contours);
        
        
        

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
    
    public static Mat morph(Mat mask) {

        Mat morphOutput = new Mat();

    	 Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
    	 Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));

    	 Imgproc.erode(mask, morphOutput, erodeElement);
    	 Imgproc.erode(mask, morphOutput, erodeElement);

    	 Imgproc.dilate(mask, morphOutput, dilateElement);
    	 Imgproc.dilate(mask, morphOutput, dilateElement);
    	 return morphOutput;
    }
    
    public static Mat findContours(Mat frame , Mat maskedImage) {
    	// init
    	List<MatOfPoint> contours = new ArrayList<>();
    	Mat hierarchy = new Mat();
 
    	//Imgproc.cvtColor(maskedImage, blackAndWhite, Imgproc.COLOR_BGR2GRAY);
    	// find contours
    	
    	Imgproc.findContours(maskedImage, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

    	// if any contour exist...
    	if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
    	{
    	        // for each contour, display it in blue
    			
    	        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
    	        {
    	                Imgproc.drawContours(frame, contours, idx, new Scalar(0, 255, 255));
    	        }
    	}
    	
    	return frame;
    }
    
    public static Mat playWithHue(Mat image) {
    	Mat mask = new Mat(image.size(), image.type());
    	// get thresholding values from the UI
    	// remember: H ranges 0-180, S and V range 0-255
    	Scalar minValues = new Scalar(20, 80,
    	20);
    	Scalar maxValues = new Scalar(180, 255,
    	255);



    	// threshold HSV image to select tennis balls
    	Core.inRange(image, minValues, maxValues, mask);
    	return mask;
    }
}
