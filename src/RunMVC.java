
public class RunMVC {
    
    RunMVC(){
        Database database = new Database();
        SongsTableComponent table = new SongsTableComponent(database.getSongsFromDatabase());
        MenuComponent menu = new MenuComponent();
        ButtonsComponent buttons = new ButtonsComponent();
        TextAreaComponent text = new TextAreaComponent();
        JTreeComponent tree = new JTreeComponent(database.getPlaylistsFromDatabase());
        JSliderComponent slider = new JSliderComponent();
        Neptune player = new Neptune(table, buttons, menu, text, tree, slider);
        
        
	//tell Model about View. 
        database.addObserver(table);

	//create Controller. tell it about Model and View, initialise model
	NeptuneController controller = new NeptuneController();
	controller.addPlayerView(player);
        controller.addDatabaseModel(database);
        controller.addButtonsView(buttons);
        controller.addMenuView(menu);
        controller.addTextView(text);
        controller.addTableModel(table);
        controller.addSlider(slider);

	//tell View about Controller 
	buttons.setController(controller);
        menu.setController(controller);
        table.addDropController(controller);
        table.addMouseController(controller);
        slider.addMouseController(controller);
    }
    
    public static void main(String args[]) {
        RunMVC spawn = new RunMVC();
    }
}
