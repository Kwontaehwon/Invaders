import engine.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import screen.GameScreen;
import screen.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class FileManagerTest {

    GameState gameState;
    int HEIGHT = 820;
    int WIDTH = 670;
    int FPS = 60;
    final int MAX_LIVES = 3;
    Frame frame;
    List<GameSettings> gameSettings;
    int EXTRA_LIFE_FREQUENCY = 3;
    GameScreen gameScreen;
    FileManager fileManager;

    @BeforeEach
    void setUp() {
        frame = new Frame(WIDTH, HEIGHT);
        int width = frame.getWidth();
        int height = frame.getHeight();
        GameSettings SETTINGS_LEVEL_1 =
                new GameSettings(5, 5, 2, 1000);
        DesignSetting designSetting = new DesignSetting(DrawManager.SpriteType.Ship);
        gameSettings = new ArrayList<GameSettings>();
        gameSettings.add(SETTINGS_LEVEL_1);
        gameState = new GameState(1, 0, MAX_LIVES, 0, 0,3, new int[]{15, 15, 15, 15},0);
        boolean bonusLife = gameState.getLevel()
                % EXTRA_LIFE_FREQUENCY == 0
                && gameState.getLivesRemaining() < MAX_LIVES;
        gameScreen = new GameScreen(gameState,
                gameSettings.get(gameState.getLevel() - 1),
                bonusLife, designSetting, width, height, FPS, frame);
        fileManager = Core.getFileManager();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveGame() throws IOException, ClassNotFoundException {
        fileManager.saveGame(gameScreen.getGameScreen());
        assertNotNull(fileManager.loadGame());
    }

    @Test
    void loadBackGroundTemplate(){
        assertNotNull(fileManager.loadBackgroundTemplate());
    }
}