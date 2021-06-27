package trafficcounter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;

import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;


public class ImageDetection {
	static ImageHelper ih = new ImageHelper();
	public static void main(String[] args) {
        // Load the native OpenCV library
		nu.pattern.OpenCV.loadShared();
        new ImageDetection().run(args);
    }
	
	public void detectAndDisplay(Mat frame, CascadeClassifier carCascade) {
        Mat frameGray = new Mat();
        
        //Image processing
        Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(frameGray, frameGray);
        // --
        
        //Detect Cars
        MatOfRect cars = new MatOfRect();

        carCascade.detectMultiScale(frameGray, cars);

        List<Rect> listOfCars = new ArrayList<Rect>(cars.toList());
        for(Rect r : listOfCars)
        {
        	Imgproc.rectangle(frame, r.tl(), r.br(), new Scalar(255,255,255));
        }
        ih.updateFrame(frame);
    }
	
    public void run(String[] args) {
        String CarHaarXMLPath = args.length > 2 ? args[0] : "src/main/java/trafficcounter/resources/cars.xml";
        
        String VideoPath = new File("TrafficVideoSet.mp4").getAbsolutePath(); //Broken, because opencv is bad
    
        CascadeClassifier carCascade = new CascadeClassifier();
        
        if (!carCascade.load(CarHaarXMLPath)) {
            System.err.println("--(!)Error loading car classifier cascade: " + CarHaarXMLPath);
            System.exit(0);
        }

        VideoCapture vc = new VideoCapture();
        if(!vc.open(0))
        {
        	 System.err.println("--(!)Error loading video input: " + VideoPath);
             System.exit(0);
        }
        
       // vc.open("src/main/java/trafficcounter/TraafficVideoSet.mp4");
        while(vc.isOpened())
        {
        	 Mat frame = new Mat();
        	 if(vc.read(frame)){
        	 detectAndDisplay(frame, carCascade);
        	 } else {
        		 break;
        	 }
        }
        vc.release();
        //Mat frame = Imgcodecs.imread("src/main/java/trafficcounter/Traffic2.png");
       // detectAndDisplay(frame, carCascade);
       // System.exit(0);
    }
}

