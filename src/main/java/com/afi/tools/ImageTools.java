package com.afi.tools;

import javafx.stage.FileChooser;

public class ImageTools {
	public static void setExtFilters(FileChooser chooser){
        chooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
    }
}
