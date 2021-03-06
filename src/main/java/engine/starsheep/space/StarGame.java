package engine.starsheep.space;

import engine.starsheep.space.controller.ItemsController;
import engine.starsheep.space.controller.MissionsController;
import engine.starsheep.space.job.TraitDependency;
import engine.starsheep.space.json.StarReader;
import engine.starsheep.space.trait.TraitManager;

import java.util.List;

public class StarGame {
    private TraitManager traitManager;
    private Mission currentMission;
    private Choice currentChoice;
    private StarGameView display;
    private StarPlayer player;
    private GameSoundManager soundManager;
    private StarController controller;

    /**
     * Need some way to read the game state from XML files. Possibly another class
     * to read data from save files, which would be xml files.
     * <p>
     * represents model in MVC
     *
     * @param display a display for the game, should implement StarGameView. swap it out for
     *                different enviornments.
     */
    public StarGame(StarGameView display, StarPlayer player, StarFileManager fileManager, GameSoundManager soundManager) {
        StarReader.setFileManager(fileManager); //give StarReader class the filemanager.
        StarController.setGame(this);
        this.soundManager = soundManager;
        this.traitManager = TraitManager.getInstance();
        ItemsManager.getInstance();
        MissionsController.getInstance();
        this.currentMission = null;
        this.currentChoice = null;
        this.display = display;
        this.player = player;
    }

    public void setMission(Mission m) {
        currentMission = m;
    }

    public void setChoice(Choice c) {
        currentChoice = c;
    }
    
    public MissionsController getMissionsController() {
        return MissionsController.getInstance();
    }

    //this method is used to expose the controller outside of the engine. to be connected with the view.
    public StarController getController() {
        return StarController.getInstance();
    }
    
    public ItemsController getItemsController() {
        return ItemsController.getInstance();
    }


    public boolean calculateSuccess(List<TraitDependency> traitDependencies) {

        /*
         * todo: computer calculations.
         */
        for (int i = 0; i < traitDependencies.size(); i++) {
            TraitDependency td = traitDependencies.get(i);
            int weight = td.getWeight();
        }
        return true;
    }

    public StarGameView getView() {
        return this.display;
    }
}

