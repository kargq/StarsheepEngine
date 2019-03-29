package engine.starsheep.space;

import engine.starsheep.space.Job.Job;
import engine.starsheep.space.Job.TraitDependency;
import engine.starsheep.space.Trait.TraitManager;

import java.util.List;

public class StarGame implements Runnable {
    private MissionsManager missionsManager;
    private TraitManager traitManager;
    private Mission currentMission;
    private Choice currentChoice;

    private StarGameView display;
    private StarPlayer player;
    private StarSaveFilesReader fileManager;
    private GameSoundManager soundManager;

    /**
     * Need some way to read the game state from XML files. Possibly another class
     * to read data from save files, which would be xml files.
     * <p>
     * represents model in MVC
     *
     * @param display a display for the game, should implement StarGameView. swap it out for
     *                different enviornments.
     */
    public StarGame(StarGameView display, StarPlayer player, StarSaveFilesReader saveFilesReader, GameSoundManager soundManager) {
        this.fileManager = saveFilesReader;
        StarReader.setFileManager(fileManager); //give StarReader class the filemanager.
        Controller.setGame(this);
        this.soundManager = soundManager;
        this.traitManager = TraitManager.getInstance();
        this.missionsManager = MissionsManager.getInstance();
        this.currentMission = null;
        this.currentChoice = null;
        this.display = display;
        this.player = player;
        Job j = StarReader.readJob(999);
        System.out.println(j.toString());
    }

    public void setMission(Mission m) {
        currentMission = m;
    }

    public void setChoice(Choice c) {
        currentChoice = c;
    }

    public List<Mission> getMissions() {
        return this.missionsManager.getMissions();
    }

    //this method is used to expose the controller outside of the engine. to be connected with the view.
    public Controller getController() {
        return Controller.getInstance();
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

    @Override
    public void run() {
        String name = "StarSheep";
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(name + ": " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(name + "Interrupted");
        }
        System.out.println(name + " exiting.");
    }
}

