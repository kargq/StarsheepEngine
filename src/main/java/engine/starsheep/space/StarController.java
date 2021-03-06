package engine.starsheep.space;

import java.util.List;


public class StarController {

    private static StarController instance;
    private static StarGame game;

    private StarController() {

    }

    static StarController getInstance() {
        if (instance == null)
            instance = new StarController();
        return instance;
    }

    static void setGame(StarGame g) {
        game = g;
    }

    public void changeToTab(StarTab t) {
        game.getView().changeToTab(t);
    }
}
