package engine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    private Clip clip;
    private File audioFile;
    private AudioInputStream audioInputStream;
    private boolean isLoop;

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

    public void start(){
        clip.setFramePosition(0);
        clip.start();
        if (isLoop) clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }

    public void increase() {
        if(!clip.isOpen()) // javaDocs - "Some Controls may only be available when the line(clip) is open."
            return;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float value = gainControl.getValue()+5;
        if(gainControl.getMaximum()>value)
            gainControl.setValue(value);
    }

    public void decrease() {
        if(!clip.isOpen())
            return;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float value = gainControl.getValue()-5;
        if(gainControl.getMinimum()<value)
            gainControl.setValue(value);
    }

    public boolean isRunning() {
        // return true from when call start() until invoke stop() or playback completes;
        return clip.isRunning();
    }


}
