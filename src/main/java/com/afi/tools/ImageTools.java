package com.afi.tools;

import javafx.stage.FileChooser;

public class ImageTools {
	public static void setImageExtFilters(FileChooser chooser){
        chooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
    }
	
	public static void setPdfExtFilters(FileChooser chooser){
        chooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("Images", "*.pdf")
        );
    }
}
