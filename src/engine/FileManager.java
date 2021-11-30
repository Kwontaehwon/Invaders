package engine;

import java.awt.*;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import engine.DrawManager.SpriteType;
import entity.Bullet;
import entity.BulletPool;
import entity.EnemyShipFormation;
import screen.GameScreen;
import screen.Screen;

/**
 * Manages files used in the application.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class FileManager {

	/** Singleton instance of the class. */
	private static FileManager instance;
	/** Application logger. */
	private static Logger logger;
	/** Max number of high scores. */
	private static final int MAX_SCORES = 7;

	/**
	 * private constructor.
	 */
	private FileManager() {
		logger = Core.getLogger();
	}

	/**
	 * Returns shared instance of FileManager.
	 * 
	 * @return Shared instance of FileManager.
	 */
	protected static FileManager getInstance() {
		if (instance == null)
			instance = new FileManager();
		return instance;
	}

	/**
	 * Loads sprites from disk.
	 * 
	 * @param spriteMap
	 *            Mapping of sprite type and empty boolean matrix that will
	 *            contain the image.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public void loadSprite(final Map<SpriteType, Color[][]> spriteMap)
			throws IOException {
		InputStream inputStream = null;

		try {
			inputStream = DrawManager.class.getClassLoader()
					.getResourceAsStream("graphics");
			char c;
//			byte[] buffer = new byte[8];
			// Sprite loading.
			for (Map.Entry<SpriteType, Color[][]> sprite : spriteMap
					.entrySet()) {
				for (int i = 0; i < sprite.getValue().length; i++)
					for (int j = 0; j < sprite.getValue()[i].length; j++) {
						String rgbHex = "";
						for(int k = 0 ; k < 8 ; k++){
							do
								c = (char) inputStream.read();
							while (!(c>=97 && c<=122) && !(c>=48 && c<=57) ); // 변경필요
							rgbHex += c;
						}
						if(rgbHex.equals("0x000000"))sprite.getValue()[i][j] = Color.BLACK;
						else sprite.getValue()[i][j] = Color.decode(rgbHex);

					}
				logger.fine("Sprite " + sprite.getKey() + " loaded.");
			}
			if (inputStream != null)
				inputStream.close();
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	/**
	 * Loads a font of a given size.
	 * 
	 * @param size
	 *            Point size of the font.
	 * @return New font.
	 * @throws IOException
	 *             In case of loading problems.
	 * @throws FontFormatException
	 *             In case of incorrect font format.
	 */
	public Font loadFont(final float size) throws IOException,
			FontFormatException {
		InputStream inputStream = null;
		Font font;

		try {
			// Font loading.
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("font.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(
					size);
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return font;
	}

	/**
	 * Save adjusted image for background.
	 * @param img is adjusted image.
	 */
	public void saveImage(RenderedImage img) {
		try {
			ImageIO.write(img, "PNG", new File("res/gameBackground.PNG"));
			logger.info("save new image.");
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 *	load Template Image of background on play.
	 *	@return Background Image.
	 */
	public Image loadBackgroundTemplate() {
		ImageIcon imageIcon;
		Image img;
		// Image loading.
		imageIcon = new ImageIcon("res/backgroundTemplate.png");
		img = imageIcon.getImage();
		if(img != null) {
			logger.info("Background Image("+ img.getWidth(null) + "x" + img.getHeight(null) +") loaded.");
		}
		return img;
	}

	/**
	 * Returns the application default scores if there is no user high scores
	 * file.
	 * 
	 * @return Default high scores.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	private List<Score> loadDefaultHighScores() throws IOException {
		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader reader = null;

		try {
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("scores");
			reader = new BufferedReader(new InputStreamReader(inputStream));

			Score highScore = null;
			String name = reader.readLine();
			String score = reader.readLine();

			while ((name != null) && (score != null)) {
				highScore = new Score(name, Integer.parseInt(score));
				highScores.add(highScore);
				name = reader.readLine();
				score = reader.readLine();
			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return highScores;
	}

	/**
	 * Loads high scores from file, and returns a sorted list of pairs score -
	 * value.
	 * 
	 * @return Sorted list of scores - players.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public List<Score> loadHighScores() throws IOException {

		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "scores";

			File scoresFile = new File(scoresPath);
			inputStream = new FileInputStream(scoresFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			logger.info("Loading user high scores.");

			Score highScore = null;
			String name = bufferedReader.readLine();
			String score = bufferedReader.readLine();

			while ((name != null) && (score != null)) {
				highScore = new Score(name, Integer.parseInt(score));
				highScores.add(highScore);
				name = bufferedReader.readLine();
				score = bufferedReader.readLine();
			}

		} catch (FileNotFoundException e) {
			// loads default if there's no user scores.
			logger.info("Loading default high scores.");
			highScores = loadDefaultHighScores();
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
		}

		Collections.sort(highScores);
		return highScores;
	}

	/**
	 * Saves user high scores to disk.
	 * 
	 * @param highScores
	 *            High scores to save.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public void saveHighScores(final List<Score> highScores) 
			throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "scores";

			File scoresFile = new File(scoresPath);

			if (!scoresFile.exists())
				scoresFile.createNewFile();

			outputStream = new FileOutputStream(scoresFile);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user high scores.");

			// Saves 7 or less scores.
			int savedCount = 0;
			for (Score score : highScores) {
				if (savedCount >= MAX_SCORES)
					break;
				bufferedWriter.write(score.getName());
				bufferedWriter.newLine();
				bufferedWriter.write(Integer.toString(score.getScore()));
				bufferedWriter.newLine();
				savedCount++;
			}

		} finally {
			if (bufferedWriter != null)
				bufferedWriter.close();
		}
	}

	// 추가한 부분 (Save 기능)
	/**
	 * Saves game to disk.
	 *
	 * @param save
	 * 			Save list to save.
	 * @throws IOException
	 */
	public void saveSaves(final List<String> save)
			throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String savesPath = new File(jarPath).getParent();
			savesPath += File.separator;
			savesPath += "saves";

			File savesFile = new File(savesPath);

			if (!savesFile.exists())
				savesFile.createNewFile();

			outputStream = new FileOutputStream(savesFile);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user's game.");

			bufferedWriter.write(save.get(0));
			bufferedWriter.newLine();
			bufferedWriter.write(save.get(1));
			bufferedWriter.newLine();
			bufferedWriter.write(save.get(2));


		} finally {
			if (bufferedWriter != null)
				bufferedWriter.close();
		}
	}

	// 추가한 부분 (생성된 saves 파일 없을시,, 기본 Saves 파일 불러오기)
	/**
	 * Returns the application default saves if there is no user saves file.
	 *
	 * @return Default game status
	 * @throws IOException
	 */
	public GameStatus loadDefaultSaves() throws IOException {

		GameState gameStates;
		GameSettings gameSettings;
		GameStatus gameStatus = null;

		InputStream inputStream = null;
		BufferedReader reader = null;

		try {
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("saves");
			reader = new BufferedReader(new InputStreamReader(inputStream));

			String gameStatesl = reader.readLine();
			String gameSettingsl = reader.readLine();
			String bonusl = reader.readLine();

			if ((gameStatesl != null) && (gameSettingsl != null) && (bonusl != null)) {
				String[] gameStatesArr = gameStatesl.split(", ");
				String[] gameSettingslArr = gameSettingsl.split(", ");

				gameStates = new GameState(Integer.parseInt(gameStatesArr[0]),
						Integer.parseInt(gameStatesArr[1]),
						Integer.parseInt(gameStatesArr[2]),
						Integer.parseInt(gameStatesArr[3]),
						Integer.parseInt(gameStatesArr[4]),
						Integer.parseInt(gameStatesArr[5]),
						new int[] {Integer.parseInt(gameStatesArr[6]),
								Integer.parseInt(gameStatesArr[7]),
								Integer.parseInt(gameStatesArr[8]),
								Integer.parseInt(gameStatesArr[9])},
						Integer.parseInt(gameStatesArr[10]));

				gameSettings = new GameSettings(Integer.parseInt(gameSettingslArr[0]),
						Integer.parseInt(gameSettingslArr[1]),
						Integer.parseInt(gameSettingslArr[2]),
						Integer.parseInt(gameSettingslArr[3]));

				gameStatus = new GameStatus(gameStates, gameSettings, Boolean.parseBoolean(bonusl));

			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return gameStatus;
	}

	// 추가한 부분 (Saves 파일 불러오기)
	/**
	 * Loads saves from file, and returns a game status.
	 *
	 * @return game status
	 * @throws IOException
	 */
	public GameStatus loadSaves() throws IOException {

		GameState gameStates;
		GameSettings gameSettings;
		GameStatus gameStatus = null;

		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String savesPath = new File(jarPath).getParent();
			savesPath += File.separator;
			savesPath += "saves";
			File savesFile = new File(savesPath);
			inputStream = new FileInputStream(savesFile);

			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));
			logger.info("Loading user saves.");
			String gameStatesl = bufferedReader.readLine();
			String gameSettingsl = bufferedReader.readLine();
			String bonusl = bufferedReader.readLine();

			if ((gameStatesl != null) && (gameSettingsl != null) && (bonusl != null)) {
				String[] gameStatesArr = gameStatesl.split(", ");
				String[] gameSettingslArr = gameSettingsl.split(", ");
				gameStates = new GameState(Integer.parseInt(gameStatesArr[0]),
						Integer.parseInt(gameStatesArr[1]),
						Integer.parseInt(gameStatesArr[2]),
						Integer.parseInt(gameStatesArr[3]),
						Integer.parseInt(gameStatesArr[4]),
						Integer.parseInt(gameStatesArr[5]),
						new int[] {Integer.parseInt(gameStatesArr[6]),
								Integer.parseInt(gameStatesArr[7]),
								Integer.parseInt(gameStatesArr[8]),
								Integer.parseInt(gameStatesArr[9])},
						Integer.parseInt(gameStatesArr[10]));

				gameSettings = new GameSettings(Integer.parseInt(gameSettingslArr[0]),
						Integer.parseInt(gameSettingslArr[1]),
						Integer.parseInt(gameSettingslArr[2]),
						Integer.parseInt(gameSettingslArr[3]));

				gameStatus = new GameStatus(gameStates, gameSettings, Boolean.parseBoolean(bonusl));

			}

		} catch (FileNotFoundException e) {
			// loads default if there's no user scores.
			logger.info("Loading default saves.");
			gameStatus = loadDefaultSaves();
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
		}

		Collections.sort(highScores);
		return gameStatus;
	}

	public void saveGame(GameScreen gameScreen) throws IOException, ClassNotFoundException {
		FileOutputStream fos = new FileOutputStream("res/gameScreen.bin");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(gameScreen);
		oos.close();
	}

	public GameScreen loadGame() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream("res/gameScreen.bin");
		ObjectInputStream ois = new ObjectInputStream(fis);
		GameScreen gameScreen = (GameScreen)ois.readObject();
		return gameScreen;

	}

}
