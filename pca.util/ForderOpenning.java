package pca.util;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;


public class ForderOpenning {
  private Vector<String> files = new Vector<String>();

	public Vector<String> getFiles() {
		return files;
	}

	public ForderOpenning() {
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Choose the Files Direction");
		int ret = fileChooser.showOpenDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION) {
			// Fodder location
			String path = fileChooser.getSelectedFile().getAbsolutePath();
			File folderPath = new File(fileChooser.getSelectedFile()
					.getAbsolutePath());
			String[] temp = folderPath.list();
			
			for (int i = 0; i < temp.length; ++i) {
				if (temp[i].contains(".jpg") || temp[i].contains(".bmp")
						|| temp[i].contains(".JPG") || temp[i].contains(".png")) {
					files.add(path + "\\" + temp[i]);
				}

			}
		}
	}

}
