package pca.util;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.JFileChooser;

public class ImagfileChoosed {

  private String path;

	public String getPath() {
		return path;
	}

	public ImagfileChoosed() {
		JFileChooser fileChooser = new JFileChooser();
		// Load the image
		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			showMessageDialog(null, "Cannot open image file: " + path,
					"PCA EigenFace", ERROR_MESSAGE);
			path = null;
			//System.exit(0);
		}
		String pathTemp = fileChooser.getSelectedFile().getAbsolutePath();
		if (pathTemp.contains(".jpg") || pathTemp.contains(".bmp")
				|| pathTemp.contains(".png") || pathTemp.contains(".JPG")) {
			path = pathTemp;
		} else
			showMessageDialog(null, "Cannot open image file: " + path,
					"PCA EigenFace", ERROR_MESSAGE);
	}

}
