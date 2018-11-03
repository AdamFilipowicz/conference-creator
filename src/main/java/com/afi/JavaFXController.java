package com.afi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import com.afi.data.TableData;
import com.afi.data.TableMapObject;
import com.afi.dto.*;
import com.afi.model.Conference;
import com.afi.model.Config;
import com.afi.service.ConferenceService;
import com.afi.service.ConfigService;
import com.afi.service.NamingService;
import com.afi.tools.DatePickerCell;
import com.afi.tools.FXDialogs;
import com.afi.tools.ImageTools;
import com.afi.tools.Pager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

@Controller
public class JavaFXController implements Initializable {

	@Autowired
	private NamingService namingService;
	
	@Autowired
	private ConferenceService conferenceService;
	
	@Autowired
	private ConfigService configService;

	@Autowired
	private TableData tableData;
	
	@Autowired
	private FXDialogs dialogs;

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
	private Label infoTableText;
	
	@FXML
	private Label chosenConferenceLabel;
	
	@FXML
	private GridPane pageButtons;
	
	@FXML
	private FlowPane imagePane;
	
	@FXML
	private TextField pathTextField;

	@SuppressWarnings("rawtypes")
	private TableColumn tab;
	private List<Object> list;
	private Set<Integer> edited = new TreeSet<Integer>();
	private Map<String, TableMapObject> tableDataMap;
	private Object exampleObject;
	private Pager pager;
	private int pageSize = 5;
	private int buttonsToShow = 7;
	private final Button firstPageButton = new Button("<-");
    private final Button lastPageButton = new Button("->");
    private Conference selectedConference;
    private FileChooser fileChooser = new FileChooser();
    private TilePane tile = new TilePane();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		mainTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(final KeyEvent keyEvent) {
				Object selectedItem = mainTable.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					if (keyEvent.getCode().equals(KeyCode.DELETE)) {
						if(!checkTableSave()) {
							return;
						}
						namingService.deleteObject(selectedItem);
						list.remove(selectedItem);
						refreshTable(pager.getCurrentPage());
					}
				}
			}
		});
		firstPageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(!checkTableSave()) {
					return;
				}
            	goToFirstPage();
            }
        });
		lastPageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(!checkTableSave()) {
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
		if(exampleObject instanceof Config) {
			if(!edited.add(-10)) {
				list = new ArrayList<Object>();
				list.add(pathTextField.getText());
				configService.saveConfig(list, selectedConference);
				edited.clear();
				dialogs.showConfirm("Sukces", "Pomyślnie zmieniono konfigurację");
			}
			return;
		}
		edited.remove(-10);
		for (Integer elementToChange : edited) {
			if(checkRow(elementToChange)) {
				String message = namingService.saveObject(list.get(elementToChange), selectedConference);
				if(message != null) {
					dialogs.showError("Błąd", message);
					return;
				}
			}
		}
		refreshTable(pager.getCurrentPage());
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void initializeAbstractTable(ActionEvent e) {
		if(selectedConference == null) {
			dialogs.showError("Błąd", "Wybierz konferencję");
			return;
		}
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setTableView();
		infoTableText.setText("Edytuj abstrakty");
		exampleObject = new TableAbstract();
		tableDataMap = tableData.getAbstractMap();
		setTable();
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void initializeAlarmTable(ActionEvent e) {
		if(selectedConference == null) {
			dialogs.showError("Błąd", "Wybierz konferencję");
			return;
		}
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setTableView();
		infoTableText.setText("Edytuj alarmy");
		exampleObject = new TableAlarm();
		tableDataMap = tableData.getAlarmMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeConferenceTable(ActionEvent e) {
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setConferenceView();
		infoTableText.setText("Edytuj konferencje");
		exampleObject = new TableConference();
		tableDataMap = tableData.getConferenceMap();
		setTable();
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void initializeEventTable(ActionEvent e) {
		if(selectedConference == null) {
			dialogs.showError("Błąd", "Wybierz konferencję");
			return;
		}
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setTableView();
		infoTableText.setText("Edytuj wydarzenia");
		exampleObject = new TableEvent();
		tableDataMap = tableData.getEventMap();
		setTable();
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void initializeGradeTable(ActionEvent e) {
		if(selectedConference == null) {
			dialogs.showError("Błąd", "Wybierz konferencję");
			return;
		}
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setTableView();
		infoTableText.setText("Edytuj oceny");
		exampleObject = new TableGrade();
		tableDataMap = tableData.getGradeMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializePrelegentTable(ActionEvent e) {
		if(selectedConference == null) {
			dialogs.showError("Błąd", "Wybierz konferencję");
			return;
		}
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setTableView();
		infoTableText.setText("Edytuj prelegentów");
		exampleObject = new TablePrelegent();
		tableDataMap = tableData.getPrelegentMap();
		setTable();
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void initializeLecturerTable(ActionEvent e) {
		if(selectedConference == null) {
			dialogs.showError("Błąd", "Wybierz konferencję");
			return;
		}
		pickConferenceButton.setVisible(false);
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setTableView();
		infoTableText.setText("Edytuj wykładowców");
		exampleObject = new TableLecturer();
		tableDataMap = tableData.getLecturerMap();
		setTable();
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void initializePhotos(ActionEvent e) {
		if(selectedConference == null) {
			dialogs.showError("Błąd", "Wybierz konferencję");
			return;
		}
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setImageView();
		infoTableText.setText("Edytuj zdjęcia konferencji");
		setImagePane();
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void initializeContact(ActionEvent e) {
		if(selectedConference == null) {
			dialogs.showError("Błąd", "Wybierz konferencję");
			return;
		}
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		setConfigView();
		infoTableText.setText("Edytuj kontakt konferencji");
		exampleObject = new Config();
		setConfig();
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void pickConference(ActionEvent e) {
		if(mainTable.getSelectionModel().getSelectedItem() != null) {
			Method[] methods = list.get(0).getClass().getMethods();
			String conferenceName = null;
			for (Method method : methods) {
				if (method.getName().equals("getName")) {
					try {
						conferenceName = method.invoke(mainTable.getSelectionModel().getSelectedItem()).toString();
					} catch (Exception ex) {
						dialogs.showError("Błąd", "Zapisz najpierw konferencję");
					}
				}
			}
			if(conferenceName != null && !"".equals(conferenceName)) {
				chosenConferenceLabel.setText("Wybrana konferencja: " + conferenceName);
				selectedConference = conferenceService.findConferenceByName(conferenceName);
			}
		}
	}
	
	@FXML
	private void setEdited(KeyEvent e){
		edited.add(-10);
	}
	
	@SuppressWarnings("static-access")
	@FXML
	private void addPhotos(ActionEvent e) {
		ImageTools.setExtFilters(fileChooser);
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        File parentFolder = new File(configService.findValueByKeyAndConference("image_path", selectedConference) + "\\" + tableData.CONFERENCE_IMAGES + "\\");
        parentFolder.mkdirs();
        for(File f: files) {
    	    try {
    	    	String[] spl = f.toString().replace("\\", "/").split("/");
				Files.copy(f.toPath(), Paths.get(parentFolder.toString() + "\\" + spl[spl.length - 1]), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e1) {
				e1.printStackTrace();
				dialogs.showError("Błąd", "Błąd dodawania zdjęć");
				return;
			}
        }
        dialogs.showConfirm("Sukces", "Pomyślnie dodano zdjęcia");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setTableItems() {
		Callback<TableColumn<Object, String>, TableCell<Object, String>> dateCellFactory = (TableColumn<Object, String> param) -> new DatePickerCell();
		mainTable.getColumns().clear();
		mainTable.sort();
		for (Map.Entry<String, TableMapObject> entry : tableDataMap.entrySet()) {
			tab = new TableColumn(entry.getKey());
			tab.setSortable(false);
			tab.setPrefWidth(calculatePrefColumnWidth(entry.getKey().length(), entry.getValue().getLength()));
			if(entry.getValue().getType().equals("data")) {
				tab.setCellFactory(dateCellFactory);
			}
			else {
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
						if (method.getName().equals("set" + properties.getTableObjectName().substring(0, 1).toUpperCase() + properties.getTableObjectName().substring(1))) {
							setter = method;
							try {
								method.invoke(list.get(editedRow), t.getNewValue());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else if (method.getName().equals("get" + properties.getTableObjectName().substring(0, 1).toUpperCase() + properties.getTableObjectName().substring(1))) {
							getter = method;
						}
					}
					try {
						if(!checkField(getter.invoke(list.get(editedRow)).toString(), properties)) {
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
		pager = namingService.findAll(exampleObject, pager, selectedConference, pageSize, buttonsToShow, PageRequest.of(0, pageSize));
		list = pager.getList();
		pager.setCurrentPage(0);
		setTableItems();
	}
	
	private void setConfig() {
		edited.clear();
		list = new ArrayList<Object>(configService.findConfig(selectedConference));
		pathTextField.setText(list.get(0).toString());
	}
	
	private void refreshTable(int page) {
		edited.clear();
		pager = namingService.findAll(exampleObject, pager, selectedConference, pageSize, buttonsToShow, PageRequest.of(page, pageSize));
		list = pager.getList();
		pager.setCurrentPage(page);
		setTabItems();
	}

	private void setTabItems() {
		if (!list.isEmpty()) {
			mainTable.setItems(FXCollections.observableArrayList(list));
		} 
		else {
			mainTable.setItems(FXCollections.observableArrayList(new ArrayList<>()));
		}
		refreshPageButtons();
	}
	
	@SuppressWarnings("static-access")
	private boolean checkRow(int rowNo) {
		for (Map.Entry<String, TableMapObject> entry : tableDataMap.entrySet()) {
			TableMapObject properties = entry.getValue();
			Method[] methods = list.get(0).getClass().getMethods();
			for (Method method : methods) {
				if (method.getName().equals("get" + properties.getTableObjectName().substring(0, 1).toUpperCase() + properties.getTableObjectName().substring(1))) {
					try {
						if(!checkField(method.invoke(list.get(rowNo)).toString(), properties)) {
							return false;
						}
					} catch (Exception e) {
						if(!properties.isNullable()) {
							dialogs.showError("Błąd", "Wartość nie może być pusta");
							return false;
						}
						return true;
					}
					break;
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings("static-access")
	private void setImagePane() {
		File parentFolder = new File(configService.findValueByKeyAndConference("image_path", selectedConference) + "\\" + tableData.CONFERENCE_IMAGES + "\\");
        File[] files = parentFolder.listFiles();

        for (final File file : files) {
            ImageView imageView;
            imageView = ImageTools.createImageView(file);
            tile.getChildren().addAll(imageView);
        }
        for(File f: files) {
        	Image image;
			try {
				image = new Image(new FileInputStream(f), 500, 0, true, true);
				ImageView imageView = new ImageView(image);
				imagePane.getChildren().add(imageView);
			} catch (FileNotFoundException e) {
				dialogs.showError("Błąd", "Nie wczytano zdjęcia");
			}
//        	try {
//				images.add(ImageIO.read(f));
//			} catch (IOException e) {
//				dialogs.showError("Błąd", "Nie wczytano zdjęcia");
//			}
        }
	}
	
	@SuppressWarnings("static-access")
	private boolean checkField(String value, TableMapObject properties) {
		if(!properties.isNullable()) {
			if(value.equals("") || value == null) {
				dialogs.showError("Błąd", "Wartość nie może być pusta");
				return false;
			}
		}
		if("data".equals(properties.getType()) && !value.isEmpty()) {
			try {
				new Timestamp((new SimpleDateFormat("yyyy-MM-dd").parse(value)).getTime());
			}
			catch(Exception e) {
				dialogs.showError("Błąd", "Data musi być postaci yyyy-MM-dd.\nPrzykładowa data: 2018-08-30");
				return false;
			}
		}
		if("time".equals(properties.getType()) && !value.isEmpty()) {
			try {
				new Timestamp((new SimpleDateFormat("hh:mm").parse(value)).getTime());
				String[] time = value.split(":");
				if(Integer.parseInt(time[0]) > 23 || Integer.parseInt(time[0]) < 0 || Integer.parseInt(time[1]) > 60 || Integer.parseInt(time[1]) < 0) {
					dialogs.showError("Błąd", "Błędna wartość godzinna lub minutowa");
					return false;
				}
			}
			catch(Exception e) {
				dialogs.showError("Błąd", "Czas musi być postaci hh:mm.\nPrzykładowe czasy: 0:00, 8:45, 15:20");
				return false;
			}
		}
		else if("int".equals(properties.getType()) && !value.isEmpty()) {
			try {
				Integer.parseInt(value);
			}
			catch(Exception e) {
				dialogs.showError("Błąd", "Podaj poprawną wartość liczbową");
				return false;
			}
		}
		return true;
	}
	
	private void refreshPageButtons() {
		pageButtons.getChildren().clear();
		if(pager.getTotalPages() > 1) {
			int buttonIndex = 0;
			if(pager.getCurrentPage() <= 0) {
				firstPageButton.setDisable(true);
			}
			else {
				firstPageButton.setDisable(false);
			}
			firstPageButton.setPrefWidth(50.0);
	        pageButtons.add(firstPageButton, buttonIndex, 0);
	        
	        for (int index = pager.getStartPage(); index <= pager.getEndPage(); index++) {
	        	buttonIndex++;
	            Button pageButton = new Button(Integer.toString(index));
	            if(pager.getCurrentPage() == index - 1) {
	    			pageButton.setDisable(true);
	    		}
	            pageButton.setPrefWidth(50.0);
	            pageButtons.add(pageButton, buttonIndex, 0);
	            int page = index;
	            pageButton.setOnAction(new EventHandler<ActionEvent>() {
	                @Override public void handle(ActionEvent e) {
	                	if(!checkTableSave()) {
							return;
						}
	                	refreshTable(page - 1);
	                }
	            });
	        }
	        buttonIndex++;
	        if(pager.getCurrentPage() >= pager.getTotalPages() - 1) {
				lastPageButton.setDisable(true);
			}
	        else {
	        	lastPageButton.setDisable(false);
	        }
	        lastPageButton.setPrefWidth(50.0);
	        pageButtons.add(lastPageButton, buttonIndex, 0);
		}
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
    
    @SuppressWarnings("static-access")
	public boolean checkTableSave() {
    	if(!edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return false;
		}
    	return true;
    }
    
    private void setConferenceView() {
    	mainTable.setVisible(true);
		pickConferenceButton.setVisible(true);
		saveButton.setVisible(true);
		pickImagesButton.setVisible(false);
		configPane.setVisible(false);
		imagePane.setVisible(false);
    }
    
    private void setTableView() {
    	mainTable.setVisible(true);
		pickConferenceButton.setVisible(false);
		saveButton.setVisible(true);
		pickImagesButton.setVisible(false);
		configPane.setVisible(false);
		imagePane.setVisible(false);
    }
    
    private void setImageView() {
    	mainTable.setVisible(false);
		pickConferenceButton.setVisible(false);
		saveButton.setVisible(false);
		pickImagesButton.setVisible(true);
		configPane.setVisible(false);
		imagePane.setVisible(true);
    }
    
    private void setConfigView() {
    	mainTable.setVisible(false);
		pickConferenceButton.setVisible(false);
		saveButton.setVisible(true);
		pickImagesButton.setVisible(false);
		configPane.setVisible(true);
		imagePane.setVisible(false);
    }

	private double calculatePrefColumnWidth(int columnLength, int tableMapLength) {
		return Math.max(columnLength, tableMapLength) * 10.0;
	}
}
