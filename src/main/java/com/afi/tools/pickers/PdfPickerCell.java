package com.afi.tools.pickers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afi.data.TableData;
import com.afi.tools.ImageTools;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.FileChooser;

@Component
public class PdfPickerCell extends TableCell<Object, String> {

    private Button addPdfButton;
    private FileChooser fileChooser = new FileChooser();
    private String imagePath;
    
    @Autowired
    private TableData tableData;

    public PdfPickerCell() {}
    
    public PdfPickerCell(String imagePath) {
    	this.imagePath = imagePath;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(addPdfButton);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                setText(null);
                setGraphic(addPdfButton);
            } else {
                setText(getItem());
                setGraphic(null);
            }
        }
    }

    @SuppressWarnings("static-access")
	private void createDatePicker() {
    	addPdfButton = new Button();
    	addPdfButton.setText("Wybierz pdf");
    	addPdfButton.setOnAction((e) -> {
    		ImageTools.setPdfExtFilters(fileChooser);
    		File file = fileChooser.showOpenDialog(null);
    		File parentFolder = new File(imagePath + "\\" + tableData.PDF_FILES + "\\");
    		parentFolder.mkdirs();
    		if (file != null) {
    			try {
    				String[] spl = file.toString().replace("\\", "/").split("/");
    				Path pdfPath = Paths.get(parentFolder.toString() + "\\" + spl[spl.length - 1]);
    				Files.copy(file.toPath(), pdfPath, StandardCopyOption.REPLACE_EXISTING);
    				commitEdit(pdfPath.toString());
    			} catch (IOException e1) {
    				e1.printStackTrace();
    				return;
    			}
    		}
        });
    }
}