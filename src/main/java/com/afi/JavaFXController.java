package com.afi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import com.afi.data.TableMapObject;
import com.afi.dto.*;
import com.afi.model.Config;
import com.afi.tools.CalculationTools;
import com.afi.tools.ImageTools;
import com.afi.tools.pickers.DatePickerCell;
import com.afi.tools.pickers.EventBoxCell;
import com.afi.tools.pickers.PdfPickerCell;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

@Controller
public class JavaFXController extends NonFXController implements Initializable, MapComponentInitializedListener {

	@FXML
	private AnchorPane configPane;

	@FXML
	private VBox box;

	@FXML
	private TableView<Object> mainTable;

	@FXML
	private Button saveButton;

	@FXML
	private Button pickConferenceButton;

	@FXML
	private Button pickImagesButton;

	@FXML
	private Button pickLogoButton;

	@FXML
	private Button pickPhotoButton;

	@FXML
	private Label infoTableText;

	@FXML
	private Label chosenConferenceLabel;

	@FXML
	private GridPane pageButtons;

	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab conferenceTab;
	
	@FXML
	private ScrollPane imagePane;

	@FXML
	private VBox verticalImages;

	@FXML
	private TextField pathTextField;

	@FXML
	private TextField logoTextField;

	@FXML
	private TextField photoTextField;

	@FXML
	private TextField emailTextField;

	@FXML
	private TextField phoneTextField;
	
	@FXML
	private TextField address1TextField;
	
	@FXML
	private TextField address2TextField;
	
	@FXML
	private GoogleMapView googleMapView;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	    googleMapView.addMapInializedListener(this);
		mainTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(final KeyEvent keyEvent) {
				Object selectedItem = mainTable.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					if (keyEvent.getCode().equals(KeyCode.DELETE)) {
						if (!checkTableSave()) {
							return;
						}
						if(exampleObject instanceof TableConference) {
							if("Nie".equals(dialogs.showConfirm(tableData.CONFERENCE_DELETE_TITLE, tableData.CONFERENCE_DELETE, "Tak", "Nie"))){
								return;
							}
						}
						namingService.deleteObject(selectedItem);
						list.remove(selectedItem);
						refreshTable(pager.getCurrentPage());
					}
				}
			}
		});
		firstPageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!checkTableSave()) {
					return;
				}
				goToFirstPage();
			}
		});
		lastPageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!checkTableSave()) {
					return;
				}
				goToLastPage();
			}
		});
		edited.clear();

		initializeConferenceTable(null);
	}

	@SuppressWarnings("static-access")
	@FXML
	private void saveTable(ActionEvent e) {
		if (exampleObject instanceof Config) {
			if (!edited.add(-10)) {
				list = new ArrayList<Object>();
				list.add(pathTextField.getText());
				list.add(logoTextField.getText());
				list.add(photoTextField.getText());
				list.add(emailTextField.getText());
				list.add(phoneTextField.getText());
				list.add(address1TextField.getText());
				list.add(address2TextField.getText());
				list.add(mapCoordinates);
				configService.saveConfig(list, selectedConference);
				edited.clear();
				dialogs.showConfirm("Sukces", tableData.CONFIG_CHANGED);
			}
			return;
		}
		edited.remove(-10);
		for (Integer elementToChange : edited) {
			if (checkRow(elementToChange)) {
				String message = namingService.saveObject(list.get(elementToChange), selectedConference);
				if (message != null) {
					dialogs.showError("Błąd", message);
					return;
				}
			}
		}
		refreshTable(pager.getCurrentPage());
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeAbstractTable(Event e) {
		if (selectedConference == null) {
			dialogs.showError("Błąd", tableData.SELECT_CONFERENCE);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		setTableView();
		infoTableText.setText(tableData.ABSTRACTS_TEXT);
		exampleObject = new TableAbstract();
		tableDataMap = tableData.getAbstractMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeAlarmTable(Event e) {
		if (selectedConference == null) {
			dialogs.showError("Błąd", tableData.SELECT_CONFERENCE);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		setTableView();
		infoTableText.setText(tableData.ALARM_TEXT);
		exampleObject = new TableAlarm();
		tableDataMap = tableData.getAlarmMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeConferenceTable(Event e) {
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			return;
		}
		setConferenceView();
		infoTableText.setText(tableData.CONFERENCE_TEXT);
		exampleObject = new TableConference();
		tableDataMap = tableData.getConferenceMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeEventTable(Event e) {
		if (selectedConference == null) {
			dialogs.showError("Błąd", tableData.SELECT_CONFERENCE);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		setTableView();
		infoTableText.setText(tableData.EVENT_TEXT);
		exampleObject = new TableEvent();
		tableDataMap = tableData.getEventMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeGradeTable(Event e) {
		if (selectedConference == null) {
			dialogs.showError("Błąd", tableData.SELECT_CONFERENCE);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		setTableView();
		infoTableText.setText(tableData.GRADE_TEXT);
		exampleObject = new TableGrade();
		tableDataMap = tableData.getGradeMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializePrelegentTable(Event e) {
		if (selectedConference == null) {
			dialogs.showError("Błąd", tableData.SELECT_CONFERENCE);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		setTableView();
		infoTableText.setText(tableData.PRELEGENT_TEXT);
		exampleObject = new TablePrelegent();
		tableDataMap = tableData.getPrelegentMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeLecturerTable(Event e) {
		if (selectedConference == null) {
			dialogs.showError("Błąd", tableData.SELECT_CONFERENCE);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		pickConferenceButton.setVisible(false);
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		setTableView();
		infoTableText.setText(tableData.LECTURER_TEXT);
		exampleObject = new TableLecturer();
		tableDataMap = tableData.getLecturerMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializePhotos(ActionEvent e) {
		if (selectedConference == null) {
			dialogs.showError("Błąd", tableData.SELECT_CONFERENCE);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		setImageView();
		infoTableText.setText(tableData.PHOTOS_TEXT);
		setImagePane();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeContact(ActionEvent e) {
		if (selectedConference == null) {
			dialogs.showError("Błąd", tableData.SELECT_CONFERENCE);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		if (e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			tabPane.getSelectionModel().select(conferenceTab);
			return;
		}
		String[] coordinations = configService.findValueByKeyAndConference("map", selectedConference).split(";");
		System.out.println();
		mapOptions.center(new LatLong(Double.parseDouble(coordinations[0]), Double.parseDouble(coordinations[1])))
        .overviewMapControl(false)
        .panControl(false)
        .rotateControl(false)
        .scaleControl(false)
        .streetViewControl(false)
        .zoomControl(false)
        .zoom(12)
        .mapType(MapTypeIdEnum.ROADMAP);

		map = googleMapView.createMap(mapOptions);
		
		map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
    	   LatLong latLong = event.getLatLong();
    	   if("Tak".equals(dialogs.showConfirm(tableData.CONFERENCE_PLACE_TITLE, tableData.CONFERENCE_PLACE, "Tak", "Nie"))) {
    		   mapCoordinates = Double.toString(latLong.getLatitude())+";"+Double.toString(latLong.getLongitude());
    		   edited.add(-10);
    		   saveTable(null);
    		   initializeContact(null);
    	   }
    	});
		
		MarkerOptions markerOptions = new MarkerOptions();
	    markerOptions.position( new LatLong(Double.parseDouble(coordinations[0]), Double.parseDouble(coordinations[1])) )
	                .visible(Boolean.TRUE)
	                .title(tableData.MAP_TEXT);

	    Marker marker = new Marker( markerOptions );

	    map.addMarker(marker);
		setConfigView();
		infoTableText.setText(tableData.CONTACT_TEXT);
		exampleObject = new Config();
		setConfig();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void pickConference(ActionEvent e) {
		if (mainTable.getSelectionModel().getSelectedItem() != null) {
			Method[] methods = list.get(0).getClass().getMethods();
			String conferenceName = null;
			for (Method method : methods) {
				if (method.getName().equals("getName")) {
					try {
						conferenceName = method.invoke(mainTable.getSelectionModel().getSelectedItem()).toString();
					} catch (Exception ex) {
						dialogs.showError("Błąd", tableData.SAVE_CONFERENCE);
					}
				}
			}
			if (conferenceName != null && !"".equals(conferenceName)) {
				chosenConferenceLabel.setText("Wybrana konferencja: " + conferenceName);
				selectedConference = conferenceService.findConferenceByName(conferenceName);
			}
		}
	}

	@FXML
	private void setEdited(KeyEvent e) {
		edited.add(-10);
	}

	@SuppressWarnings("static-access")
	@FXML
	private void addPhotos(ActionEvent e) {
		ImageTools.setImageExtFilters(fileChooser);
		List<File> files = fileChooser.showOpenMultipleDialog(null);
		File parentFolder = new File(configService.findValueByKeyAndConference("image_path", selectedConference) + "\\"
				+ tableData.CONFERENCE_IMAGES + "\\");
		parentFolder.mkdirs();
		if (files != null && !files.isEmpty()) {
			for (File f : files) {
				try {
					String[] spl = f.toString().replace("\\", "/").split("/");
					Files.copy(f.toPath(), Paths.get(parentFolder.toString() + "\\" + spl[spl.length - 1]),
							StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					e1.printStackTrace();
					dialogs.showError("Błąd", tableData.IMAGES_ERROR);
					return;
				}
			}
			dialogs.showConfirm("Sukces", tableData.IMAGES_ADD_INFO);
			setImagePane();
		}
	}

	@SuppressWarnings("static-access")
	@FXML
	private void addLogo(ActionEvent e) {
		ImageTools.setImageExtFilters(fileChooser);
		File file = fileChooser.showOpenDialog(null);
		File parentFolder = new File(configService.findValueByKeyAndConference("image_path", selectedConference) + "\\"
				+ tableData.CONFERENCE_IMAGES + "\\" + tableData.LOGO + "\\");
		parentFolder.mkdirs();
		if (file != null) {
			edited.add(-10);
			try {
				String[] spl = file.toString().replace("\\", "/").split("/");
				Path logoPath = Paths.get(parentFolder.toString() + "\\" + spl[spl.length - 1]);
				Files.copy(file.toPath(), logoPath, StandardCopyOption.REPLACE_EXISTING);
				logoTextField.setText(logoPath.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
				dialogs.showError("Błąd", tableData.LOGO_ERROR);
				return;
			}
			dialogs.showConfirm("Sukces", tableData.LOGO_ADD_INFO);
		}
	}

	@SuppressWarnings("static-access")
	@FXML
	private void addMainPhoto(ActionEvent e) {
		ImageTools.setImageExtFilters(fileChooser);
		File file = fileChooser.showOpenDialog(null);
		File parentFolder = new File(configService.findValueByKeyAndConference("image_path", selectedConference) + "\\"
				+ tableData.CONFERENCE_IMAGES + "\\" + tableData.MAIN_PHOTO + "\\");
		parentFolder.mkdirs();
		if (file != null) {
			edited.add(-10);
			try {
				String[] spl = file.toString().replace("\\", "/").split("/");
				Path logoPath = Paths.get(parentFolder.toString() + "\\" + spl[spl.length - 1]);
				Files.copy(file.toPath(), logoPath, StandardCopyOption.REPLACE_EXISTING);
				photoTextField.setText(logoPath.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
				dialogs.showError("Błąd", tableData.IMAGE_ERROR);
				return;
			}
			dialogs.showConfirm("Sukces", tableData.IMAGE_ADD_INFO);
		}
	}
	
	private void setConferenceView() {
		mainTable.setVisible(true);
		pickConferenceButton.setVisible(true);
		saveButton.setVisible(true);
		pickImagesButton.setVisible(false);
		configPane.setVisible(false);
		imagePane.setVisible(false);
		tabPane.setVisible(true);
	}

	private void setTableView() {
		mainTable.setVisible(true);
		pickConferenceButton.setVisible(false);
		saveButton.setVisible(true);
		pickImagesButton.setVisible(false);
		configPane.setVisible(false);
		imagePane.setVisible(false);
		tabPane.setVisible(true);
	}

	private void setImageView() {
		mainTable.setVisible(false);
		pickConferenceButton.setVisible(false);
		saveButton.setVisible(false);
		pickImagesButton.setVisible(true);
		configPane.setVisible(false);
		imagePane.setVisible(true);
		tabPane.setVisible(false);
	}

	private void setConfigView() {
		mainTable.setVisible(false);
		pickConferenceButton.setVisible(false);
		saveButton.setVisible(true);
		pickImagesButton.setVisible(false);
		configPane.setVisible(true);
		imagePane.setVisible(false);
		tabPane.setVisible(false);
	}

	@SuppressWarnings({ "static-access" })
	@Override
	public void mapInitialized() {
		String[] coordinations = configService.findValueByKeyAndConference("map", selectedConference).split(";");
		
		mapOptions = new MapOptions();
		
		mapOptions.center(new LatLong(Double.parseDouble(coordinations[0]), Double.parseDouble(coordinations[1])))
        .overviewMapControl(false)
        .panControl(false)
        .rotateControl(false)
        .scaleControl(false)
        .streetViewControl(false)
        .zoomControl(false)
        .zoom(12)
        .mapType(MapTypeIdEnum.ROADMAP);

		map = googleMapView.createMap(mapOptions);
		
		map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
    	   LatLong latLong = event.getLatLong();
    	   if("Tak".equals(dialogs.showConfirm(tableData.CONFERENCE_PLACE_TITLE, tableData.CONFERENCE_PLACE, "Tak", "Nie"))) {
    		   mapCoordinates = Double.toString(latLong.getLatitude())+";"+Double.toString(latLong.getLongitude());
    		   edited.add(-10);
    		   saveTable(null);
    		   initializeContact(null);
    	   }
    	});
		
		MarkerOptions markerOptions = new MarkerOptions();
	    markerOptions.position( new LatLong(Double.parseDouble(coordinations[0]), Double.parseDouble(coordinations[1])) )
	                .visible(Boolean.TRUE)
	                .title(tableData.MAP_TEXT);

	    Marker marker = new Marker( markerOptions );

	    map.addMarker(marker);
	}
	
	private void refreshPageButtons() {
		pageButtons.getChildren().clear();
		if (pager.getTotalPages() > 1) {
			int buttonIndex = 0;
			if (pager.getCurrentPage() <= 0) {
				firstPageButton.setDisable(true);
			} else {
				firstPageButton.setDisable(false);
			}
			firstPageButton.setPrefWidth(50.0);
			pageButtons.add(firstPageButton, buttonIndex, 0);

			for (int index = pager.getStartPage(); index <= pager.getEndPage(); index++) {
				buttonIndex++;
				Button pageButton = new Button(Integer.toString(index));
				if (pager.getCurrentPage() == index - 1) {
					pageButton.setDisable(true);
				}
				pageButton.setPrefWidth(50.0);
				pageButtons.add(pageButton, buttonIndex, 0);
				int page = index;
				pageButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						if (!checkTableSave()) {
							return;
						}
						refreshTable(page - 1);
					}
				});
			}
			buttonIndex++;
			if (pager.getCurrentPage() >= pager.getTotalPages() - 1) {
				lastPageButton.setDisable(true);
			} else {
				lastPageButton.setDisable(false);
			}
			lastPageButton.setPrefWidth(50.0);
			pageButtons.add(lastPageButton, buttonIndex, 0);
		}
	}
	
	private void setConfig() {
		edited.clear();
		list = new ArrayList<Object>(configService.findConfig(selectedConference));
		pathTextField.setText(list.get(0).toString());
		logoTextField.setText(list.get(1).toString());
		photoTextField.setText(list.get(2).toString());
		emailTextField.setText(list.get(3).toString());
		phoneTextField.setText(list.get(4).toString());
		address1TextField.setText(list.get(5).toString());
		address2TextField.setText(list.get(6).toString());
		mapCoordinates = list.get(7).toString();
	}

	private void refreshTable(int page) {
		edited.clear();
		pager = namingService.findAll(exampleObject, pager, selectedConference, pageSize, buttonsToShow,
				PageRequest.of(page, pageSize));
		list = pager.getList();
		pager.setCurrentPage(page);
		setTabItems();
	}
	
	private void setTabItems() {
		if (!list.isEmpty()) {
			mainTable.setItems(FXCollections.observableArrayList(list));
		} else {
			mainTable.setItems(FXCollections.observableArrayList(new ArrayList<>()));
		}
		refreshPageButtons();
	}

	@SuppressWarnings("static-access")
	private void setImagePane() {
		File parentFolder = new File(configService.findValueByKeyAndConference("image_path", selectedConference) + "\\"
				+ tableData.CONFERENCE_IMAGES + "\\");
		File[] files = parentFolder.listFiles();
		verticalImages.getChildren().clear();
		int imageCounter = 0;
		int hboxesCounter = 0;
		List<HBox> hboxes = new ArrayList<HBox>();
		if (files != null && files.length != 0) {
			for (File f : files) {
				if (!f.isDirectory()) {
					imageCounter++;
					if (imageCounter % 2 != 0) {
						hboxesCounter++;
						hboxes.add(new HBox());
						Image image;
						try {
							image = new Image(new FileInputStream(f), 500, 0, true, true);
							ImageView imageView = new ImageView(image);
							hboxes.get(hboxesCounter - 1).getChildren().add(imageView);
						} catch (FileNotFoundException e) {
							dialogs.showError("Błąd", tableData.IMAGE_READ_ERROR);
						}
					} else {
						Image image;
						try {
							image = new Image(new FileInputStream(f), 500, 0, true, true);
							ImageView imageView = new ImageView(image);
							hboxes.get(hboxesCounter - 1).getChildren().add(imageView);
							verticalImages.getChildren().add(hboxes.get(hboxesCounter - 1));
						} catch (FileNotFoundException e) {
							dialogs.showError("Błąd", tableData.IMAGE_READ_ERROR);
						}
					}
				}
			}
			if (!hboxes.isEmpty() && hboxes.get(hboxesCounter - 1).getChildren().size() == 1) {
				verticalImages.getChildren().add(hboxes.get(hboxesCounter - 1));
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setTableItems() {
		Callback<TableColumn<Object, String>, TableCell<Object, String>> dateCellFactory = (
				TableColumn<Object, String> param) -> new DatePickerCell();
		Callback<TableColumn<Object, String>, TableCell<Object, String>> pdfCellFactory = (
				TableColumn<Object, String> param) -> new PdfPickerCell(
						configService.findValueByKeyAndConference("image_path", selectedConference));
		Callback<TableColumn<Object, String>, TableCell<Object, String>> eventCellFactory = (
				TableColumn<Object, String> param) -> new EventBoxCell(
						namingService.findEventNames(selectedConference));
		Callback<TableColumn<Object, String>, TableCell<Object, String>> lecturerPrelegentCellFactory = (
				TableColumn<Object, String> param) -> new EventBoxCell(
						namingService.findLecturerPrelegentNames(selectedConference));
		mainTable.getColumns().clear();
		mainTable.sort();
		for (Map.Entry<String, TableMapObject> entry : tableDataMap.entrySet()) {
			tab = new TableColumn(entry.getKey());
			tab.setSortable(false);
			tab.setPrefWidth(CalculationTools.calculatePrefColumnWidth(entry.getKey().length(), entry.getValue().getLength()));
			if (entry.getValue().getType().equals("data")) {
				tab.setCellFactory(dateCellFactory);
			} else if (entry.getValue().getType().equals("event")) {
				tab.setCellFactory(eventCellFactory);
			} else if (entry.getValue().getType().equals("lecturerPrelegent")) {
				tab.setCellFactory(lecturerPrelegentCellFactory);
			} else if (entry.getValue().getType().equals("pdffile")) {
				tab.setCellFactory(pdfCellFactory);
			}else {
				tab.setCellFactory(TextFieldTableCell.forTableColumn());
			}
			tab.setCellValueFactory(new PropertyValueFactory<Object, String>(entry.getValue().getTableObjectName()));
			tab.setOnEditCommit(new EventHandler<CellEditEvent<Object, String>>() {
				@Override
				public void handle(CellEditEvent<Object, String> t) {
					int editedRow = t.getTablePosition().getRow();
					TableMapObject properties = tableDataMap.get(t.getTableColumn().getText());
					Method[] methods = list.get(0).getClass().getMethods();
					Method getter = null;
					Method setter = null;
					String oldValue = t.getOldValue();
					edited.add(editedRow);
					for (Method method : methods) {
						if (method.getName()
								.equals("set" + properties.getTableObjectName().substring(0, 1).toUpperCase()
										+ properties.getTableObjectName().substring(1))) {
							setter = method;
							try {
								method.invoke(list.get(editedRow), t.getNewValue());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (method.getName()
								.equals("get" + properties.getTableObjectName().substring(0, 1).toUpperCase()
										+ properties.getTableObjectName().substring(1))) {
							getter = method;
						}
					}
					try {
						if (!checkField(getter.invoke(list.get(editedRow)).toString(), properties)) {
							setter.invoke(list.get(editedRow), oldValue);
							t.getTableView().refresh();
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			mainTable.getColumns().add(tab);
		}
		setTabItems();
	}
	
	private void setTable() {
		edited.clear();
		pager = namingService.findAll(exampleObject, pager, selectedConference, pageSize, buttonsToShow,
				PageRequest.of(0, pageSize));
		list = pager.getList();
		pager.setCurrentPage(0);
		setTableItems();
	}

	private void goToFirstPage() {
		if (pager.getTotalPages() == 0) {
			return;
		}
		refreshTable(0);
	}

	private void goToLastPage() {
		if (pager.getTotalPages() == 0) {
			return;
		}
		refreshTable(pager.getTotalPages() - 1);
	}
}
