package com.afi;

import java.lang.reflect.Method;
import java.net.URL;
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
import com.afi.dto.TableConference;
import com.afi.model.Prelegent;
import com.afi.service.NamingService;
import com.afi.tools.FXDialogs;
import com.afi.tools.Pager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

@Controller
public class JavaFXController implements Initializable {

	@Autowired
	private NamingService namingService;

	@Autowired
	private TableData tableData;
	
	@Autowired
	private FXDialogs dialogs;

	@FXML
	private VBox box;

	@FXML
	private TableView<Object> mainTable;

	@FXML
	private Button saveButton;

	@FXML
	private Label infoTableText;
	
	@FXML
	private GridPane pageButtons;

	@SuppressWarnings("rawtypes")
	private TableColumn tab;
	private List<Object> list;
	private Set<Integer> edited = new TreeSet<Integer>();
	private Map<String, TableMapObject> tableDataMap;
	private Object exampleObject;
	private Pager pager;
	private int pageSize = 10;
	private int buttonsToShow = 7;
	private final Button firstPageButton = new Button("<-");
    private final Button lastPageButton = new Button("->");

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		mainTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(final KeyEvent keyEvent) {
				Object selectedItem = mainTable.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					if (keyEvent.getCode().equals(KeyCode.DELETE)) {
						if(!edited.isEmpty()) {
							dialogs.showError("Błąd", "Zapisz najpierw zmiany");
							return;
						}
						namingService.deleteObject(selectedItem);
						list.remove(selectedItem);
						setTabItems();
					}
				}
			}
		});
		firstPageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	goToFirstPage();
            }
        });
		lastPageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	goToLastPage();
            }
        });
		edited.clear();
		
		initializeConferenceTable(null);
	}

	@FXML
	private void saveTable(ActionEvent e) {
		for (Integer elementToChange : edited) {
			if(checkRow(elementToChange)) {
				namingService.saveObject(list.get(elementToChange));
			}
		}
		refreshTable(pager.getCurrentPage());
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializeConferenceTable(ActionEvent e) {
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		infoTableText.setText("Edytuj konferencje");
		exampleObject = new TableConference();
		tableDataMap = tableData.getConferenceMap();
		setTable();
	}

	@SuppressWarnings("static-access")
	@FXML
	private void initializePrelegentTable(ActionEvent e) {
		if(e != null && !edited.isEmpty()) {
			dialogs.showError("Błąd", "Zapisz najpierw zmiany");
			return;
		}
		infoTableText.setText("Edytuj prelegentów");
		exampleObject = new Prelegent();
		tableDataMap = tableData.getPrelegentMap();
		setTable();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setTableItems() {
		mainTable.getColumns().clear();
		for (Map.Entry<String, TableMapObject> entry : tableDataMap.entrySet()) {
			tab = new TableColumn(entry.getKey());
			tab.setSortable(false);
			tab.setPrefWidth(calculatePrefColumnWidth(entry.getKey().length(), entry.getValue().getLength()));
			tab.setCellFactory(TextFieldTableCell.forTableColumn());
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
		pager = namingService.findAll(exampleObject, pager, pageSize, buttonsToShow, PageRequest.of(0, pageSize));
		list = pager.getList();
		setTableItems();
	}
	
	private void refreshTable(int page) {
		edited.clear();
		pager = namingService.findAll(exampleObject, pager, pageSize, buttonsToShow, PageRequest.of(page, pageSize));
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
						dialogs.showError("Błąd", "Wartość nie może być pusta");
						return false;
					}
					break;
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings("static-access")
	private boolean checkField(String value, TableMapObject properties) {
		if(!properties.isNullable()) {
			if(value.equals("") || value == null) {
				dialogs.showError("Błąd", "Wartość nie może być pusta");
				return false;
			}
		}
		if("data".equals(properties.getType())) {
			try {
				new Timestamp((new SimpleDateFormat("yyyy-MM-dd").parse(value)).getTime());
			}
			catch(Exception e) {
				dialogs.showError("Błąd", "Data musi być postaci yyyy-MM-dd.\nPrzykładowa data: 2018-08-30");
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

	private double calculatePrefColumnWidth(int columnLength, int tableMapLength) {
		return Math.max(columnLength, tableMapLength) * 10.0;
	}
}
