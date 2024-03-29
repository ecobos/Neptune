
public class RunMVC {
    private Database database;
    private NeptuneController parentControl;
    /**
     * Class constructor
     * 
     * @param loadFromPlaylist True means that its a playlist view
     * @param playlistName Only used if first parameter is true
     */
    RunMVC(boolean loadFromPlaylist, String playlistName, NeptuneController theParent){
        database = new Database();
        SongsTableComponent table;
        JTreeComponent tree = null;
        Neptune player = null;
        NeptuneController parent = null;
        
        
        MenuComponent menu = new MenuComponent(loadFromPlaylist);
        ButtonsComponent buttons = new ButtonsComponent();
        TextAreaComponent text = new TextAreaComponent();
        JSliderComponent slider = new JSliderComponent();
        JProgressBarComponent progress = new JProgressBarComponent();
        
        if(loadFromPlaylist == true){
            table = new SongsTableComponent(playlistName,database.getPlaylistSongsFromDatabase(playlistName), loadFromPlaylist, database.getPlayerSettings());
            player = new Neptune(table, buttons, menu, text, slider, progress);
            parent = theParent;
        }else {
            table = new SongsTableComponent("Library",database.getSongsFromDatabase(), loadFromPlaylist, database.getPlayerSettings());
            tree = new JTreeComponent(database.getPlaylistsFromDatabase());
            player = new Neptune(table, buttons, menu, text, tree, slider, progress);
        }
                
	//tell Model about View. 
        database.addObserver(table);
        //database.addObserver();

	//create Controller. tell it about Model and View, initialise model
	NeptuneController controller = new NeptuneController(loadFromPlaylist, parent);
	controller.addPlayerView(player);
        controller.addDatabaseModel(database);
        controller.addButtonsView(buttons);
        controller.addMenuView(menu);
        controller.addTextView(text); 
        controller.addSlider(slider);
        if(tree != null){
            controller.addTree(tree);
        }
        controller.addTableModel(table);
        controller.addProgressBar(progress);

	//tell View about Controller 
	buttons.setController(controller);
        menu.setController(controller);
        table.addDropController(controller);
        table.addMouseController(controller);
        slider.addMouseController(controller);
        player.setWindowListerner(controller);
        if(tree != null){
            tree.setTreeMouseListener(controller);
        }    
    }
    
    
    public void updateLibraryData(){
        parentControl.updateLibraryData();
    }
    
    
    /**
     * Main class. Just runs the program
     * 
     * @param args none
     */
    public static void main(String args[]) {
        RunMVC spawn = new RunMVC(false, "Pop", null);
    }
}
