package pca.face;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import pca.algorithm.SangerRule;
import pca.math.Matrix;
import pca.util.ForderOpenning;
import pca.util.ImageProcessing;

public class ParaTrainning implements ActionListener {
  private JFrame Para;
	private JButton ParaTrainning;
	private static int Dem = 64;
	private static double error = 0.01;
	private static SangerRule faceLearning = new SangerRule(Dem * Dem, 121);
	private Matrix W_before = new Matrix(faceLearning.getW().getArrayCopy());

	public ParaTrainning() {
		Para = new JFrame("ParaTrainning from  Faceimages");
		ParaTrainning = new JButton("faceGetting");
		ParaTrainning.addActionListener(this);
		Container pane = Para.getContentPane();
		Para.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // The action
																// when the
																// frame close
		pane.add(ParaTrainning);

		// Set the ui layout
		Para.setLocation(600, 300);
		Para.setSize(200, 70);
		Para.setVisible(true);

	}

	public static void main(String[] args) {
		new ParaTrainning();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		ForderOpenning forderFaces = new ForderOpenning();
		Vector<String> filesFace = forderFaces.getFiles();
		try {
			learning(filesFace);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void learning(Vector<String> filesFace) throws FileNotFoundException {
		int breakCount = 0;
		Random rnd = new Random();
		for (int i = 0; i < filesFace.size(); ++i) {
			breakCount++;
			int num = rnd.nextInt(filesFace.size() - 1);
			System.out.println(breakCount + "Face loacation:"
					+ filesFace.get(num));
			IplImage image = cvLoadImage(filesFace.get(num), 0);
			IplImage face = IplImage.create(Dem, Dem, IPL_DEPTH_8U, 1);

			cvResize(image, face, CV_INTER_LINEAR);
			/* Here to get the data for training */
			CvScalar s;
			double[] data = new double[Dem * Dem];
			for (int k = 0; k < Dem; ++k) {
				for (int l = 0; l < Dem; ++l) {
					s = cvGet2D(face, l, k);
					data[Dem * k + l] = ((double) s.getVal(0)) / 256;
				}
			}
			faceLearning.update(data); // Update the data

			/* Here to compute the error */
			if (breakCount % 3 == 0) {
				Matrix sub = W_before.minus(faceLearning.getW());
				int temp = sub.getColumnDimension() - 1;
				Matrix tempMatrix = sub.getMatrix(0, 0, 0, temp);
				double error_now = tempMatrix.normF();
				W_before = new Matrix(faceLearning.getW().getArrayCopy());
				System.out.println("The error now is " + error_now);

				if (error_now < error) {
					System.out.println("We are out the circulation!");
					break;
				}
			}
		}
		PrintWriter FILE = new PrintWriter(new FileOutputStream("E:\\123.txt"));
		faceLearning.getW().print(FILE, 10, 4);
		FILE.close();
		// faceLearning.getW().print(10, 4);
	}
}
