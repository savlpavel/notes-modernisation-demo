package net.stephankopp;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class DocumentDataView extends Window {
	
	private DocumentData documentData;
	
	/**
	 * Constructor
	 * @param documentData The data object we want to display
	 */
	public DocumentDataView(DocumentData documentData) {
		this.documentData = documentData;
		
		//Create a form layout which holds all components
		FormLayout layout = new FormLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		//Status combobox
		ComboBox<String> status = new ComboBox<String>();
		status.setCaption("Status");
		List<String> statusList = new ArrayList<String>();
		statusList.add("OPEN");
		statusList.add("IN PROGRESS");
		statusList.add("CLOSED");
		status.setItems(statusList); //configure the possible options (usually, this is not a manually defined list...)
		if (documentData.getStatus() != null) status.setValue(documentData.getStatus()); //load the value of given data object
		layout.addComponent(status);
		
		//Due Date DatePicker
		DateField dueDate = new DateField();
		if (documentData.getDueDate() != null) dueDate.setValue(documentData.getDueDate());
		dueDate.setCaption("Due date");
		layout.addComponent(dueDate);
				
		//Topic TextField
		TextField topic = new TextField();
		topic.setCaption("Topic");
		if (documentData.getTopic() != null) topic.setValue(documentData.getTopic());
		layout.addComponent(topic);
		
		//Description TextArea
		TextArea description = new TextArea();
		description.setCaption("Description");
		if (documentData.getDescription() != null) description.setValue(documentData.getDescription());
		layout.addComponent(description);
		
		//Add save and cancel buttons
		HorizontalLayout buttonLayout = new HorizontalLayout();
		Button save = new Button("save");
		save.setStyleName("friendly");
		buttonLayout.addComponent(save);
		
		Button cancel = new Button("cancel");
		cancel.addClickListener(event -> this.close());
		buttonLayout.addComponent(cancel);
		layout.addComponent(buttonLayout);
		
		//save click listener
		save.addClickListener(event -> {
			//TODO: Do some validations here
			
			//Write your changes back to the object
			documentData.setStatus(status.getValue());
			documentData.setDescription(description.getValue());
			documentData.setDueDate(dueDate.getValue());
			documentData.setTopic(topic.getValue());
			
			//Mark the object as changed
			documentData.setChanged(true);
			
			//Close window
			close();
		});
		
		//Set layout size to undefined to let it fit to its content
		layout.setSizeUndefined();
		
		//Set the layout as content of the hole window
		setContent(layout);
		
		//Configure position and caption of the window
		setCaption("Entry Details");
		center();
	}
}
