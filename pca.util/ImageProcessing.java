package pca.util;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvSet2D;
//import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
//import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import java.util.Vector;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

public class ImageProcessing {
  private static String CASCADE_FILE = "C:\\opencv\\data\\haarcascades\\haarcascade_frontalface_alt.xml";																																																	// recogination
	private static CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
			cvLoad(CASCADE_FILE));
	private static CvMemStorage storage = CvMemStorage.create();
	
	public static Vector<IplImage> GetFaces(IplImage image) {
		Vector<IplImage> facesImage = new Vector<IplImage>(); // Definite the face vector for a image
		CvSeq faces = cvHaarDetectObjects(image, cascade, storage, 1.1, 3, 0);
		CvScalar s;
		for (int i = 0; i < faces.total(); i++) {
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			IplImage face = IplImage.create(r.width(), r.height(),
					IPL_DEPTH_8U, 1);

			for (int k = 0; k < r.height(); ++k)
				for (int l = 0; l < r.width(); ++l) {
					s = cvGet2D(image, k + r.y(), l + r.x());
					cvSet2D(face, k, l, s);
				}
			facesImage.add(face);
		}
		return facesImage;

	}

}
