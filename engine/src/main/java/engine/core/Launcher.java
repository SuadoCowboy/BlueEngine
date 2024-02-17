package engine.core;

public class Launcher {
    public static void run(IGameLogic game) {
        Engine.init();
        game.init();

        game.run();

        game.getWindow().terminate();
        Engine.terminate();
    }
}
