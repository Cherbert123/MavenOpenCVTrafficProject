package trafficcounter;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageHelper {

    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(panel);
    private JLabel label = new JLabel();

    public ImageHelper() {
        panel.setLayout(new FlowLayout());
        frame.setSize(Start.WINDOW_WIDTH, Start.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(scrollPane);
        panel.removeAll();
        panel.add(label);
        frame.setVisible(true);
    }

    public void updateFrame(Mat m)
    {
    	int type = BufferedImage.TYPE_BYTE_GRAY;

        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];

        m.get(0, 0, b);
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        ImageIcon imageIcon = new ImageIcon(image);
        
        label.setIcon(imageIcon);
        label.repaint();
    }

}