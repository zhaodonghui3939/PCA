package pca.face;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import pca.algorithm.SangerRule;
import pca.math.Matrix;
import pca.util.ImageProcessing;
import pca.util.ImagfileChoosed;

public class EigenFace implements ActionListener {
	private JFrame Eigen;
	private JButton EigenFace;
	private Matrix R;

	public EigenFace() throws FileNotFoundException, IOException {
		R = Matrix.read(new BufferedReader(new FileReader("E:\\123.txt"))); // Open
																			// the
																			// PCA
																			// parameters

		Eigen = new JFrame("Convert a image to EigenFace");
		EigenFace = new JButton("EigenFace");
		EigenFace.addActionListener(this);
		Container pane = Eigen.getContentPane();
		Eigen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // The action
																// when the
																// frame close
		pane.add(EigenFace);

		// Set the ui layout
		Eigen.setLocation(600, 300);
		Eigen.setSize(200, 70);
		Eigen.setVisible(true);

	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		new EigenFace();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == EigenFace) {
			ImagfileChoosed file = new ImagfileChoosed();
			String name = file.getPath();
			IplImage image = cvLoadImage(name, 0);
			Vector<IplImage> faces = ImageProcessing.GetFaces(image);

			int column = R.getColumnDimension();
			int row = R.getRowDimension();

			IplImage faceTemplate = IplImage.create((int) Math.sqrt(column),
					(int) Math.sqrt(column), IPL_DEPTH_8U, 1);
			for (int i = 0; i < faces.size(); ++i) {

				cvResize(faces.get(i), faceTemplate, CV_INTER_LINEAR);
				CanvasFrame canvasFrame = new CanvasFrame("Face Original");
				canvasFrame.setCanvasSize(400, 400);
				canvasFrame.showImage(faceTemplate);

				CvScalar s;
				IplImage faceEigen = IplImage.create((int) Math.sqrt(row),
						(int) Math.sqrt(row), IPL_DEPTH_8U, 1);
				double[] tempArray = new double[row];
				for (int w = 0; w < row; ++w) {
					double temp = 0;
					for (int k = 0; k < (int) Math.sqrt(column); ++k)
						for (int l = 0; l < (int) Math.sqrt(column); ++l) {
							s = cvGet2D(faceTemplate, k, l);
							temp = temp + s.getVal(0) / 256
									* R.get(w, (int) Math.sqrt(column) * k + l);
						}
					tempArray[w] = temp;
		
				}
				double[] tempArray2 = tempArray;
				Arrays.sort(tempArray2);
				double Dvalue =  tempArray2[row -1] - tempArray2[0];
				System.out.println(Dvalue);
				for (int n = 0;n < tempArray2.length;++n){
					tempArray[n] = (tempArray[n] - tempArray2[0])/Dvalue * 256;
					System.out.println(tempArray2[n]);
				}
				for (int x = 0; x < (int) Math.sqrt(row); ++x)
					for (int j = 0; j < (int) Math.sqrt(row); ++j) {
						s = cvGet2D(faceEigen, x, j);
						s.setVal(0, tempArray[(int) Math.sqrt(row) * x +j]);
					}
				CanvasFrame canvasFrame2 = new CanvasFrame("Face Eigen");
				canvasFrame2.setCanvasSize(400, 400);
				canvasFrame2.showImage(faceEigen);

			}

		}

	}
}
