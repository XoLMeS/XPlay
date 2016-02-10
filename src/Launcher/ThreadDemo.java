package Launcher;
import java.io.*;

import javax.sound.sampled.*;

import org.lwjgl.LWJGLException;

import javazoom.jl.decoder.JavaLayerException;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Core;

public class ThreadDemo extends Thread {

	private Thread t;
	private Thread t2;

	private String threadName;
	public String Tsong;
    
	boolean vol = true;
	boolean released = false;
	boolean playing = false;
	boolean iSplaying = true;
	public boolean paused = false;
	public boolean endOfSong = true;
	private boolean play;

	public int currentSong = 0;

	Playlist p = new Playlist();
	Clip clip = null;
	FloatControl volumeC = null;
	public static Core c;
	ThreadDemo(String name) throws IOException {

		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run() {
		c = new Core();
		
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	public void play() throws JavaLayerException, FileNotFoundException {
		if(vol==true){
			setVolume(0.5f);
			vol  = !vol;
		}
		float vol = (float) 1000/100;
		setVolume(vol);
		iSplaying = true;
		play2(true);
	}

	public void stopP() throws IOException {
		if (play) {

			play = false;
		}
	}

	public void pause() throws InterruptedException {
		if (!paused) {
			t.suspend();
			//clip.stop();

		} else {
			t.resume();
			//clip.start();
			// iSplaying = true;
		}
		paused = !paused;
	}

	public void nextSong() throws IOException, JavaLayerException,
			InterruptedException, LWJGLException {

		if (currentSong > p.getPlaylistLength() - 1) {
			currentSong = -1;
		}
		currentSong = currentSong + 1;
		Tsong = p.nextSong();
		t.stop();
		stop2();
		t = new Thread(this, threadName);
		t.start();
		
	}

	public void previousSong() throws IOException, JavaLayerException,
			InterruptedException, LWJGLException {

		if (currentSong < 0) {
			currentSong = p.getPlaylistLength() - 1;
		}
		currentSong = currentSong - 1;
		Tsong = p.previousSong();
		t.stop();
		stop2();
		t = new Thread(this, threadName);
		t.start();
	}

	public void changeSong() {

		t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Thread 2");

					nextSong();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JavaLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LWJGLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, "THREAD");
		System.out.println("Starting thread 2");
		t2.start();
		clip.start();
		
	}

	private void test(File f) throws LineUnavailableException,
			UnsupportedAudioFileException, IOException {

		AudioInputStream stream = AudioSystem.getAudioInputStream(f);
		AudioFormat baseFormat = stream.getFormat();
		AudioFormat decodedFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
				16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(), false);
		AudioInputStream stream2 = AudioSystem.getAudioInputStream(
				decodedFormat, stream);

		clip = AudioSystem.getClip();
		clip.open(stream2);
		clip.addLineListener(new Listener());
		volumeC = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		released = true;
	}

	public class Listener implements LineListener {
		public void update(LineEvent ev) {
			if (ev.getType() == LineEvent.Type.STOP) {
				iSplaying = false;
				playing = false;
				synchronized (clip) {
					clip.notify();
				}
			}
		}
	}

	public void join2() {
		if (!released)
			return;
		synchronized (clip) {
			try {
				while (playing)
					clip.wait();
			} catch (InterruptedException exc) {
			}
		}
	}

	public void setVolume(float x) {
		if (x < 0)
			x = 0;
		if (x > 1)
			x = 1;
		float min = volumeC.getMinimum();
		float max = volumeC.getMaximum();
		volumeC.setValue((max - min) * x + min);
	}

	public float getVolume() {
		float v = volumeC.getValue();
		float min = volumeC.getMinimum();
		float max = volumeC.getMaximum();
		return (v - min) / (max - min);
	}

	public void play2(boolean breakOld) {
		if (released) {
			if (breakOld) {
				clip.stop();
				clip.setFramePosition(0);
				clip.start();
				playing = true;
			} else if (!isPlaying()) {
				clip.setFramePosition(0);
				clip.start();
				playing = true;
			}
		}
	}

	public boolean isPlaying() {
		return playing;
	}

	public boolean isReleased() {
		return released;
	}

	public void stop2() {
		if (playing) {
			clip.stop();
		}
	}

}