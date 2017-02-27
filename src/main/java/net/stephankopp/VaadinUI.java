package net.stephankopp;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {
	private Grid<DocumentData> dataGrid;
	
	@Autowired
	private DatabaseConnector databaseConnector;
	
	@Override
	protected void init(VaadinRequest request) {
		//Create a layout to contain all our components
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		//Create a label as Header and add it to the layout
		Label headerLabel = new Label("Notes Modernization Demo App");
		headerLabel.setStyleName("h1");
		layout.addComponent(headerLabel);
		
		//Create a separate layout containing the buttons and add it to the layout
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		layout.addComponent(buttonLayout);
		
		//Add a button to create new entries and add it to the layout
		Button create = new Button("create");
		create.addClickListener(event -> {
			//Create a new empty DocumentData object
			DocumentData documentData = new DocumentData();
			//open the new item in a window
			DocumentDataView window = new DocumentDataView(documentData);
			getUI().addWindow(window);
			//add a listener, which will be executed when the window will be closed
			window.addCloseListener(closeEvent -> {
				//check if "changed" property of DocumentData object is true, which means user hit the save button
				if (documentData.isChanged()) {
					//add the newly created object
					databaseConnector.add(documentData);
				}
				
				//refresh grid to show any changes
				updateGrid(); 
			});
		});
		buttonLayout.addComponent(create);
		
		//Add a button to delete selected entries and add it to the layout
		Button delete = new Button("delete");
		delete.addClickListener(event -> {
			//Get a list of selected documentData objects
			Set<DocumentData> selected = dataGrid.getSelectedItems();
			//loop through all selected objects and delete them
			for (DocumentData d : selected) {
				databaseConnector.delete(d);
			}
			//refresh grid
			updateGrid();
		});
		buttonLayout.addComponent(delete);
		
		//Add a grid, which displays all our data and add it to the layout
		dataGrid = new Grid<DocumentData>();
		dataGrid.setWidth("100%");
		dataGrid.setHeight("100%");
		layout.addComponent(dataGrid);
		
		//Change grid to allow multi-selections
		dataGrid.setSelectionMode(SelectionMode.MULTI);
		
		//Add a double-click listener
		dataGrid.addItemClickListener(event -> {
			//Check, if it is a double-click event
			if (event.getMouseEventDetails().isDoubleClick()) {
				//get the item which has been clicked
				DocumentData documentData = event.getItem();
				//open the item in a window
				DocumentDataView window = new DocumentDataView(documentData);
				getUI().addWindow(window);
				//add a listender, which will be executed when the window will be closed
				window.addCloseListener(closeEvent -> {
					updateGrid(); //refresh grid to show any changes
					
					//check if "changed" property of DocumentData object is true, which means user hit the save button
					if (documentData.isChanged()) {
						//save the documentData object to the Domino database
						databaseConnector.save(documentData);
					}
				});
			}
		});
		
		//only the layout has to be set as content, it contains all other components
		setContent(layout);
		
		//Configure each column
		dataGrid.addColumn(DocumentData::getStatus).setCaption("Status"); 
		dataGrid.addColumn(DocumentData::getTopic).setCaption("Topic"); 
		dataGrid.addColumn(DocumentData::getDueDate).setCaption("Due date"); 
		
		//update grid
		updateGrid();
	}

	/**
	 * Load data from database connector and update the grid
	 */
	private void updateGrid() {
		dataGrid.setItems(databaseConnector.getCachedData()); //Add all objects from the cache to the grid
		

	}
}
