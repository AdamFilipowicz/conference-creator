package com.afi;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.afi.data.TableData;
import com.afi.data.TableMapObject;
import com.afi.model.Conference;
import com.afi.service.ConferenceService;
import com.afi.service.ConfigService;
import com.afi.service.NamingService;
import com.afi.tools.FXDialogs;
import com.afi.tools.Pager;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;

public class NonFXController {
	@Autowired
	protected NamingService namingService;

	@Autowired
	protected ConferenceService conferenceService;

	@Autowired
	protected ConfigService configService;

	@Autowired
	protected TableData tableData;

	@Autowired
	protected FXDialogs dialogs;
	
	@SuppressWarnings("rawtypes")
	protected TableColumn tab;
	protected List<Object> list;
	protected Set<Integer> edited = new TreeSet<Integer>();
	protected Map<String, TableMapObject> tableDataMap;
	protected Object exampleObject;
	protected Pager pager;
	protected int pageSize = 5;
	protected int buttonsToShow = 7;
	protected final Button firstPageButton = new Button("<-");
	protected final Button lastPageButton = new Button("->");
	protected Conference selectedConference;
	protected FileChooser fileChooser = new FileChooser();
	protected GoogleMap map;
	protected MapOptions mapOptions;
	protected String mapCoordinates;
	
	@SuppressWarnings("static-access")
	protected boolean checkRow(int rowNo) {
		for (Map.Entry<String, TableMapObject> entry : tableDataMap.entrySet()) {
			TableMapObject properties = entry.getValue();
			Method[] methods = list.get(0).getClass().getMethods();
			for (Method method : methods) {
				if (method.getName().equals("get" + properties.getTableObjectName().substring(0, 1).toUpperCase()
						+ properties.getTableObjectName().substring(1))) {
					try {
						if (!checkField(method.invoke(list.get(rowNo)).toString(), properties)) {
							return false;
						}
					} catch (Exception e) {
						if (!properties.isNullable()) {
							dialogs.showError("Błąd", tableData.EMPTY_VALUE);
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
	protected boolean checkField(String value, TableMapObject properties) {
		if (!properties.isNullable()) {
			if (value.equals("") || value == null) {
				dialogs.showError("Błąd", tableData.EMPTY_VALUE);
				return false;
			}
		}
		if ("data".equals(properties.getType()) && !value.isEmpty()) {
			try {
				new Timestamp((new SimpleDateFormat(tableData.DATE_FORMAT).parse(value)).getTime());
			} catch (Exception e) {
				dialogs.showError("Błąd", tableData.DATE_ERROR);
				return false;
			}
		}
		if ("time".equals(properties.getType()) && !value.isEmpty()) {
			try {
				new Timestamp((new SimpleDateFormat(tableData.TIME_FORMAT).parse(value)).getTime());
				String[] time = value.split(":");
				if (Integer.parseInt(time[0]) > 23 || Integer.parseInt(time[0]) < 0 || Integer.parseInt(time[1]) > 60
						|| Integer.parseInt(time[1]) < 0) {
					dialogs.showError("Błąd", tableData.TIME_VALUE_ERROR);
					return false;
				}
			} catch (Exception e) {
				dialogs.showError("Błąd", tableData.TIME_ERROR);
				return false;
			}
		} else if ("int".equals(properties.getType()) && !value.isEmpty()) {
			try {
				Integer.parseInt(value);
			} catch (Exception e) {
				dialogs.showError("Błąd", tableData.VALUE_ERROR);
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("static-access")
	public boolean checkTableSave() {
		if (!edited.isEmpty()) {
			dialogs.showError("Błąd", tableData.SAVE_CHANGES);
			return false;
		}
		return true;
	}
	
	public Conference getSelectedConference() {
		return selectedConference;
	}
}
