import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AudioPlayer extends Applet
{
    private ArrayList<Clip> clips;
    
    public AudioPlayer()
    {
        clips = new ArrayList<Clip>();
        
        try
        {
            AudioInputStream sample;
            sample = AudioSystem.getAudioInputStream(this.getClass().getResource("resource/bloop.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(sample);
            clips.add(clip);
            
            sample = AudioSystem.getAudioInputStream(this.getClass().getResource("resource/hit2.wav"));
            clip = AudioSystem.getClip();
            clip.open(sample);
            clips.add(clip);
            
            sample = AudioSystem.getAudioInputStream(this.getClass().getResource("resource/miss.wav"));
            clip = AudioSystem.getClip();
            clip.open(sample);
            clips.add(clip);
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void playSound(int sound)
    {
        clips.get(sound).stop();
        clips.get(sound).setFramePosition(0);
        clips.get(sound).start();
    }
}
