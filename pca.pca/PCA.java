package pca.pca;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvSet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import pca.math.EigenvalueDecomposition;
import pca.math.Matrix;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/*Training the Data to get the main component
 * Here the method is computing covariance Matrix */

public class PCA {

  private static int Dem = 64; // The data size
	private Matrix matrix_data = new Matrix(200, Dem * Dem);
	private Matrix base;
	private Matrix matrix_mean;
	private Matrix PCAMatrix;

	public PCA() throws FileNotFoundException {
		data_pre(); // prepare the data
		compute();
		// Write the base Matrix
		PrintWriter FILE = new PrintWriter(new FileOutputStream(
				"E:\\baseMatrix.txt"));
		base.print(FILE, 10, 4);
		FILE.close();

		// Write the PCA Matrix
		FILE = new PrintWriter(new FileOutputStream("E:\\PCAMatrix.txt"));
		PCAMatrix.print(FILE, 10, 4);
		FILE.close();
		
		//Here draw the eigenFace,51 eigenfaces
		drawEigenfaces(51);

	}

	public static void main(String[] args) throws FileNotFoundException {
		new PCA();
	}

	public void drawEigenfaces(int num) {
		CvScalar s;
		for(int k = 0;k < num;++k){
			Matrix temp = base.getMatrix(0, Dem * Dem -1, k, k);
			temp.ReNorm();
			String name = ""+k;
			CanvasFrame canvasFrame = new CanvasFrame(name);
			canvasFrame.setCanvasSize(200, 200);
			IplImage eigenface = IplImage.create(Dem,Dem, IPL_DEPTH_8U, 1);
			for(int i = 0;i < Dem;++i){
				for(int j = 0; j < Dem;++j){
					s = cvGet2D(eigenface, j, i);
					s.setVal(0, temp.get(Dem * i +j, 0));
					cvSet2D(eigenface, j, i,s);
				}
			}
			canvasFrame.showImage(eigenface);
		}

	}

	/*
	 * Here are 200 data for training to get the main component. This is data
	 * preparing for data Matrix in order to compute
	 */
	public void data_pre() {
		for (int i = 0; i < 40; ++i)
			for (int j = 5; j < 10; ++j) {
				String path = "E:\\faces\\s" + (i + 1) + "\\" + "Hdatas" // here
						+ (i + 1) + (j + 1) + "pgm0.jpg";
				IplImage image = cvLoadImage(path, 0);
				IplImage face = IplImage.create(Dem, Dem, IPL_DEPTH_8U, 1);
				cvResize(image, face, CV_INTER_LINEAR);

				CvScalar s;
				for (int k = 0; k < Dem; ++k) {
					for (int l = 0; l < Dem; ++l) {
						s = cvGet2D(face, l, k); // from top to down ;from left
													// to right
						matrix_data
								.set(5 * i + j - 5, Dem * k + l, s.getVal(0));
					}
				}
			}
	}

	/*
	 * This is core: to get the base Matrix through computing To why compute
	 * liake this ,We can find many papers in the Internet
	 */
	public void compute() {
		matrix_mean = matrix_data.meanPCACopy(); // Get the mean Matrix
		Matrix sigmaMatrix = matrix_mean.times(matrix_mean.transpose()); // to
																			// get
																			// singular
																			// matrix

		EigenvalueDecomposition para = sigmaMatrix.eig();
		double[] s = para.getRealEigenvalues();
		Matrix eigenMatrix = para.getV();

		reverse(s); // reverse the s to make it increase progressively
		eigenMatrix.turnhorizon();// reverse the eigenMatrix to fit the
									// eigenvalue
		int p = pcaNum(s, 0.9); // Get the number of PCA ,here they contribute
								// 90%

		getBaseMatrix(eigenMatrix, p, s); // Compute the baseMatrix
		PCAMatrix = matrix_data.times(base);
		System.out.print(p);

	}

	/* The process compute the base */
	public void getBaseMatrix(Matrix eigenMatrix, int p, double[] s) {
		base = new Matrix(Dem * Dem, p);
		Matrix eigenMatrixP = eigenMatrix.getMatrix(0,
				eigenMatrix.getRowDimension() - 1, 0, p - 1);
		base = matrix_mean.transpose().times(eigenMatrixP);
		for (int i = 0; i < base.getRowDimension(); ++i)
			for (int j = 0; j < base.getColumnDimension(); ++j) {
				base.set(i, j, base.get(i, j) / (Math.sqrt(s[j])));
			}
	}

	public double sum(double[] X) {
		double sum = 0;
		for (int i = 0; i < X.length; ++i) {
			sum = sum + X[i];
		}
		return sum;
	}

	public double[] reverse(double[] s) {
		double temp = 0;
		for (int i = 0; i <= s.length / 2; ++i) {
			temp = s[i];
			s[i] = s[s.length - 1 - i];
			s[s.length - 1 - i] = temp;
		}
		return s;
	}

	public int pcaNum(double[] X, double energyvalue) {
		double sum = sum(X), temp = 0;
		int p = 0;
		while ((temp / sum) < energyvalue && X[p] > 0) {
			temp = temp + X[p];
			++p;
		}
		return p;
	}

}
