package com.afi;

import java.lang.reflect.Method;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.afi.data.TableData;
import com.afi.data.TableMapObject;
import com.afi.dto.TableConference;
import com.afi.model.Prelegent;
import com.afi.service.NamingService;
import com.afi.tools.FXDialogs;

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

	@SuppressWarnings("rawtypes")
	private TableColumn tab;
	private List<Object> list;
	private Set<Integer> edited = new TreeSet<Integer>();
	private Map<String, TableMapObject> tableDataMap;
	private int tableLength = 10;
	private Object exampleObject;

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
		edited.clear();
		initializeConferenceTable(null);
	}

	@FXML
	private void saveTable(ActionEvent e) {
		for (Integer elementToChange : edited) {
			namingService.saveObject(list.get(elementToChange));
		}
		refreshTable();
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
			tab.setPrefWidth(calculatePrefColumnWidth(entry.getKey().length(), entry.getValue().getLength()));
			tab.setCellFactory(TextFieldTableCell.forTableColumn());
			tab.setCellValueFactory(new PropertyValueFactory<Object, String>(entry.getValue().getTableObjectName()));
			tab.setOnEditCommit(new EventHandler<CellEditEvent<Object, String>>() {
				@Override
				public void handle(CellEditEvent<Object, String> t) {
					int editedRow = t.getTablePosition().getRow();
					String columnName = tableDataMap.get(t.getTableColumn().getText()).getTableObjectName();
					String columnType = tableDataMap.get(t.getTableColumn().getText()).getType();
					Method[] methods = list.get(0).getClass().getMethods();
					Method getter = null;
					Method setter = null;
					edited.add(editedRow);
					for (Method method : methods) {
						if (method.getName().equals("set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1))) {
							setter = method;
							try {
								method.invoke(list.get(editedRow), t.getNewValue());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else if (method.getName().equals("get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1))) {
							getter = method;
						}
					}
					try {
						if(!checkField(getter.invoke(list.get(editedRow)).toString(), columnType)) {
							setter.invoke(list.get(editedRow), "");
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
		list = namingService.findAll(exampleObject, tableLength);
		setTableItems();
	}
	
	private void refreshTable() {
		edited.clear();
		list = namingService.findAll(exampleObject, tableLength);
		setTabItems();
	}

	private void setTabItems() {
		if (!list.isEmpty()) {
			mainTable.setItems(FXCollections.observableArrayList(list));
		} 
		else {
			mainTable.setItems(FXCollections.observableArrayList(new ArrayList<>()));
		}
	}
	
	private boolean checkField(String value, String columnType) {
		System.out.println("\n\n"+value+"\n\n"+columnType+"\n\n");
		return false;
	}

	private double calculatePrefColumnWidth(int columnLength, int tableMapLength) {
		return Math.max(columnLength, tableMapLength) * 10.0;
	}
}
