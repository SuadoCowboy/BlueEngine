package engine.core;

import engine.test.Game;

public class Launcher {
    public static void main(String[] args) {
        Engine engine = new Engine();
        Game game = new Game();
        game.init();

        engine.setWindow(game.getWindow());
        engine.setGameLogic(game);

        engine.run();

        engine.terminate();
    }
}
