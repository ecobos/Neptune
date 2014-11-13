
public class RunMVC {
    
    RunMVC(boolean loadFromPlaylist, String playlistName){
        Database database = new Database();
        SongsTableComponent table;
        JTreeComponent tree = null;
        Neptune player = null;


        MenuComponent menu = new MenuComponent(loadFromPlaylist);
        ButtonsComponent buttons = new ButtonsComponent();
        TextAreaComponent text = new TextAreaComponent();
        JSliderComponent slider = new JSliderComponent();
        
        if(loadFromPlaylist == true){
            table = new SongsTableComponent(playlistName,database.getPlaylistSongsFromDatabase(playlistName), loadFromPlaylist);
            player = new Neptune(table, buttons, menu, text, slider);
        }else {
            table = new SongsTableComponent("Library",database.getSongsFromDatabase(), loadFromPlaylist);
            tree = new JTreeComponent(database.getPlaylistsFromDatabase());
            player = new Neptune(table, buttons, menu, text, tree, slider);
        }
        
        
        
	//tell Model about View. 
        database.addObserver(table);
        //database.addObserver();

	//create Controller. tell it about Model and View, initialise model
	NeptuneController controller = new NeptuneController(loadFromPlaylist);
	controller.addPlayerView(player);
        controller.addDatabaseModel(database);
        controller.addButtonsView(buttons);
        controller.addMenuView(menu);
        controller.addTextView(text);
        controller.addTableModel(table);
        controller.addSlider(slider);
        if(tree != null){
            controller.addTree(tree);
        }
        

	//tell View about Controller 
	buttons.setController(controller);
        menu.setController(controller);
        table.addDropController(controller);
        table.addMouseController(controller);
        slider.addMouseController(controller);
        if(tree != null){
            tree.setTreeMouseListener(controller);
        }
        
    }
    
    public static void main(String args[]) {
        RunMVC spawn = new RunMVC(false, "Pop");
    }
}
