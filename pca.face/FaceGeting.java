package pca.face;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_highgui.*;  
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import pca.util.ForderOpenning;
import pca.util.ImageProcessing;

/*This function is to get the faces from many images and save them to  files 
 * We can using the got faces for PCA training.
 * And one Point we must care is that the path or file name cannot be chinese*/

public class FaceGeting implements ActionListener {
  private JFrame faceWindow; // Definite a new frame
	private JButton faceGetting;
	private String path = "E:\\faces";  //Save location folder,cannot be Chinese

	public FaceGeting() {
		faceWindow = new JFrame("FaceGetting from Files of Faceimages");
		faceGetting = new JButton("faceGetting");
		faceGetting.addActionListener(this);
		Container pane = faceWindow.getContentPane();
		faceWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // The action
																	// when the frame close
		pane.add(faceGetting);

		// Set the ui layout
		faceWindow.setLocation(600, 300);
		faceWindow.setSize(200, 70);
		faceWindow.setVisible(true);
	}

	public static void main(String[] args) {
		new FaceGeting();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == faceGetting) {
			ForderOpenning forderOpenning = new ForderOpenning();
			Vector<String> filesImage = forderOpenning.getFiles();  //Get the image files from Forder
			for(int i = 0; i < filesImage.size(); ++i){
				System.out.println(filesImage.get(i));
				IplImage image = cvLoadImage(filesImage.get(i),0);
				Vector<IplImage> faces = ImageProcessing.GetFaces(image);
				for(int j = 0;j < faces.size();++j){
				
					IplImage face = IplImage.create(128, 128, IPL_DEPTH_8U, 1); //Sava the size 100 * 100
					cvResize(faces.get(j), face, CV_INTER_LINEAR);
					String name = path + "\\" + filesImage.get(i).replace("\\", "").replace(":", "").replace(".", "") + j + ".jpg";
					System.out.println(name);
					cvSaveImage(name,face); 
				}
			}
		}
	}
}
