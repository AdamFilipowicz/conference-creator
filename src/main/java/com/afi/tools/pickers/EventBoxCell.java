package com.afi.tools.pickers;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;

public class EventBoxCell extends TableCell<Object, String> {

	private ComboBox<String> comboBox;
	private ObservableList<String> events;

	public EventBoxCell() {
	}

	public EventBoxCell(ObservableList<String> events) {
		this.events = events;
	}

	@Override
	public void startEdit() {
		if (!isEmpty()) {
			super.startEdit();
			createComboBox();
			setText(null);
			setGraphic(comboBox);
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
				if (comboBox != null) {
					comboBox.setValue(getItem());
				}
				setText(getItem());
				setGraphic(comboBox);
			} else {
				setText(getItem());
				setGraphic(null);
			}
		}
	}

	private void createComboBox() {
		comboBox = new ComboBox<>(events);
		comboBoxConverter(comboBox);
		comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		comboBox.setOnAction((e) -> {
			commitEdit(comboBox.getValue().toString());
		});
	}

	private void comboBoxConverter(ComboBox<String> comboBox) {
		comboBox.setCellFactory((c) -> {
			return new ListCell<String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item);
					}
				}
			};
		});
	}
}