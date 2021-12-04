package engine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    /** Audio sound */
    private Clip clip;
    /** Audio file */
    private File audioFile;
    /** Audio input stream */
    private AudioInputStream audioInputStream;
    /** Audio is looping or not */
    private boolean isLoop;

    /**
     * Turn on the audio sound.
     * @param pathName path of audio file
     * @param isLoop check the audio is loop
     */
    public Audio(String pathName, boolean isLoop){
        try{
            this.isLoop = isLoop;
            clip = AudioSystem.getClip();
            audioFile = new File(pathName);
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioInputStream);
        } catch (LineUnavailableException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the music clip
     */
    public void start(){
        clip.setFramePosition(0);
        clip.start();
        if (isLoop) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    /**
     * Stop the music clip
     */
    public void stop(){
        clip.stop();
    }

    /**
     * Increase the music clip volume
     */
    public void increase() {
        if(!clip.isOpen()) // javaDocs - "Some Controls may only be available when the line(clip) is open."
            return;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float value = gainControl.getValue()+5;
        if(gainControl.getMaximum()>value)
            gainControl.setValue(value);
    }

    /**
     * Decrease the music clip volume
     */
    public void decrease() {
        if(!clip.isOpen())
            return;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float value = gainControl.getValue()-5;
        if(gainControl.getMinimum()<value)
            gainControl.setValue(value);
    }

    /**
     * Check clip is running or not
     * @return Ture if clip is running
     */
    public boolean isRunning() {
        // return true from when call start() until invoke stop() or playback completes;
        return clip.isRunning();
    }


}
