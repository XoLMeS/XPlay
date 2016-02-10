package kz.kuzhagaliyev.TimboKZ.ActiveVisualiser;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.RenderModes.*;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.OnePoleFilter;
import net.beadsproject.beads.ugens.SamplePlayer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import Launcher.Playlist;
import Parts.XButton;
import Parts.XImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Timur Kuzhagaliyev
 * @since 05-12-2014
 */

public class Core {

	public static boolean fullscreen = false;
	public static int width = 1280;
	public static int height = 720;
	public static int framerate = 60;
	public static boolean active = false;
	public static String audio = "";
	public static boolean fps = false;

	public static RenderMode currentRenderMode;
	public static ArrayList<RenderMode> renderModes;

	public static float controlsAdjustment = 0.0f;
	public static boolean controlsReady = false;

	private static long lastFrame;

	private static AudioContext audioContext;
	private static UGen mainInput;
	private static int[] channels = new int[2048];
	private static float volumeLimit;
	public static Integer[] frequencies;
	public static HashMap<Integer, Gain> gainHashMap;
	public static HashMap<Integer, Float[]> valuesHashMap;
	public static HashMap<Integer, Float> meansHashMap;
	public static HashMap<Integer, Float> absoluteMeansHashMap;

	public static UnicodeFont font;

	static int delta_click = 100;
	static long firstClick;
	static long secondClick;
	static boolean first_click = false;
	static boolean paused = false;

	static XButton a;
	static XImage b;
	static XButton play;
	static XButton pause;
	static XButton next;
	static XButton pre;
	
	static Playlist playlist = null;

	public static void main(String[] args) {
		
		try {
			playlist = new Playlist();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		int steps = 5;
		int range = 100;
		frequencies = new Integer[steps];
		for (int i = 0; i < steps; i++) {
			frequencies[i] = range / steps * i;
		}

		audio = new File("dion_the_wanderer.mp3").getPath();
		initAudio();

		lastFrame = getTime();

		initDisplay();
		initRenderModes();
		initGL();

		loadFonts();

		loop();

		cleanUp();

	}

	private static void keyboars() {
		if (!first_click) {
			firstClick = System.currentTimeMillis();
			first_click = !first_click;
		} else {
			secondClick = System.currentTimeMillis();
		}
		if (secondClick - firstClick > delta_click) {
			first_click = !first_click;
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {

				if (!paused) {
					paused = !paused;
					audioContext.stop();
				} else {
					audioContext.start();
					paused = !paused;
				}
			}

			if (Mouse.isButtonDown(Mouse.getEventButton())) {
				if (play.isSelected()) {
					play.action();
				}
				if (pause.isSelected()) {
					pause.action();
				}
				if(next.isSelected()){
					next.action();
				}
				if(pre.isSelected()){
					pre.action();
				}
			}
		}
	}

	private static void renderButtons() {
		next.render();
		pre.render();
		if (paused) {
			play.hidden(false);
			play.render();
			pause.hidden(true);
		} else {
			pause.hidden(false);
			pause.render();
			play.hidden(true);
		}
	}

	private static void initButtons() {
		next = new XButton("btn_next.png", "png", 674, 600);
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				play_the_song(playlist.nextSong());
			}
		});
		pre = new XButton("btn_pre.png", "png", 526, 600);
		pre.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				play_the_song(playlist.previousSong());
			}
		});

		pause = new XButton("btn_pause.png", "png", 600, 600);
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				audioContext.stop();
				paused = !paused;
			}
		});

		play = new XButton("btn_play.png", "png", 600, 600);
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				audioContext.start();
				paused = !paused;
			}
		});
	}

	private static void loop() {
		// Creating buttons
		initButtons();

		int frames = 0;
		int currentFramerate = 0;
		double frameCounter = 0;
		double lastTime = (double) System.nanoTime() / (double) 1000000000L;
		double unprocessedTime = 0;
		// Launch l = new Launch();
		a = new XButton("kot.jpg", "jpg", -1000, -1000);
		b = new XImage("back_04.jpg", "jpg", 0, 0, 2048, 1024);

		while (!Display.isCloseRequested()
				&& !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {

			// Controls
			keyboars();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glLoadIdentity();
			float delta = getDelta();
			generateData();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			glColor3f(1.0f, 1.0f, 1.0f);

			// Background
			b.render();

			// Buttons
			renderButtons();

			// DONT TOUCH
			a.render();

			initGL();

			if (!paused) {
				currentRenderMode.render(delta);
			} else {
				currentRenderMode.render(-1);
			}
			initGL();

			for (RenderMode renderMode : renderModes) {
				if (Keyboard.isKeyDown(renderMode.getKey())) {
					currentRenderMode = renderMode;
				}
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_F))
				fps = !fps;
			if (fps) {
				initGL();
				font.drawString(0, 0, currentFramerate + " "
						+ currentRenderMode.getName());
				// glDisable(GL_TEXTURE_2D);
			}

			if (!active) {
				initGL();
				drawControls();
			}

			Display.update();
			Display.sync(framerate);

			double startTime = (double) System.nanoTime()
					/ (double) 1000000000L;
			double passedTime = startTime - lastTime;
			lastTime = startTime;
			unprocessedTime += passedTime;
			frameCounter += passedTime;
			frames++;
			while (unprocessedTime > 1.0 / framerate) {
				unprocessedTime -= 1.0 / framerate;
				if (frameCounter >= 1.0) {
					currentFramerate = frames;
					frames = 0;
					frameCounter = 0;
				}
			}

		}

	}

	private static void drawControls() {

		float indent = 20.0f;
		float height = 20.0f;
		float displacement = 40.0f;
		Sample sample = ((SamplePlayer) mainInput).getSample();
		SamplePlayer samplePlayer = (SamplePlayer) mainInput;

		glPushMatrix();
		glTranslatef(0, displacement - controlsAdjustment, 0);
		Util.drawRect(indent, Display.getHeight() - indent - height,
				Display.getWidth() - 2 * indent, height, 0.6f, 0.6f, 1.0f);
		Util.drawRect(
				indent,
				Display.getHeight() - indent - height,
				(float) ((Display.getWidth() - 2 * indent)
						* samplePlayer.getPosition() / sample.getLength()),
				height, 0.0f, 0.0f, 1.0f);
		glPopMatrix();

		if (Util.isInRange(Display.getHeight() - (float) Mouse.getY(),
				Display.getHeight() - indent - height, Display.getHeight())
				&& Util.isInRange(Mouse.getX(), 0, Display.getWidth())) {
			if (controlsReady) {
				if (Mouse.isButtonDown(0)
						&& Util.isInRange(
								Display.getHeight() - (float) Mouse.getY(),
								Display.getHeight() - indent - height,
								Display.getHeight() - indent)
						&& Util.isInRange(Mouse.getX(), indent,
								Display.getWidth() - indent)) {
					samplePlayer
							.setPosition((double) (((float) Mouse.getX() - indent) / (Display
									.getWidth() - 2 * indent))
									* sample.getLength());
				}
			} else {
				if (controlsAdjustment <= displacement) {
					controlsAdjustment += 2;
				} else {
					controlsReady = true;
				}
			}
		} else {
			controlsReady = false;
			if (controlsAdjustment >= 0) {
				controlsAdjustment -= 2;
			}
		}

	}

	private static void generateData() {

		if (valuesHashMap == null)
			valuesHashMap = new HashMap<Integer, Float[]>();
		if (meansHashMap == null)
			meansHashMap = new HashMap<Integer, Float>();
		if (absoluteMeansHashMap == null)
			absoluteMeansHashMap = new HashMap<Integer, Float>();

		float cumulativeMean = 0.0f;
		float cumulativeAbsoluteMean = 0.0f;

		for (Integer frequency : frequencies) {

			Float[] values = new Float[512];
			Float mean = 0.0f;
			Float absoluteMean = 0.0f;
			Gain gain = gainHashMap.get(frequency);

			for (int i = 0; i < 512; i++) {
				Float value = gain.getValue(0, i) / volumeLimit;
				values[i] = value;
				mean += value;
				absoluteMean += Math.abs(value);
			}
			mean /= 512.0f;
			absoluteMean /= 512.0f;

			if (frequency != 0) {
				mean -= cumulativeMean;
				cumulativeMean += mean;
				absoluteMean -= cumulativeAbsoluteMean;
				cumulativeAbsoluteMean += absoluteMean;
			}

			valuesHashMap.put(frequency, values);
			meansHashMap.put(frequency, mean);
			absoluteMeansHashMap.put(frequency, absoluteMean);

		}

	}

	public static float[] getValues(Integer frequency) {
		return Util.parseFloatArray(valuesHashMap.get(Util.parseFrequency(
				frequencies, frequency)));
	}

	public static float getMean(Integer frequency) {
		return meansHashMap.get(Util.parseFrequency(frequencies, frequency));
	}

	public static float getAbsoluteMean(Integer frequency) {
		return absoluteMeansHashMap.get(Util.parseFrequency(frequencies,
				frequency));
	}

	private static void initDisplay() {

		// Create display
		try {
			DisplayMode displayMode = null;
			DisplayMode[] modes = Display.getAvailableDisplayModes();

			if (fullscreen) {
				for (DisplayMode mode : modes) {
					if (mode.getWidth() == 1920 && mode.getHeight() == 1080
							&& mode.isFullscreenCapable()) {
						displayMode = mode;
					}
				}
			} else {
				displayMode = new DisplayMode(width, height);
			}

			Display.setDisplayMode(displayMode);
			Display.setFullscreen(true);
			Display.setResizable(false);
			Display.create();

			Display.setTitle("Active Visualiser (" + Display.getWidth() + "x"
					+ Display.getHeight() + ")");
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		Mouse.setCursorPosition(0, Display.getHeight());

	}

	private static void initRenderModes() {
		renderModes = new ArrayList<RenderMode>();

		// Registering new render modes

		renderModes.add(new Flow("Flow", Keyboard.KEY_2));

		if (renderModes.size() == 0) {
			System.out.println("No render modes available.");
			System.exit(1);
		}

		currentRenderMode = renderModes.get(0);
	}

	public static void initGL() {
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

	}

	private static void initAudio() {

		
			audioContext = new AudioContext();
			mainInput = new SamplePlayer(audioContext,
					SampleManager.sample(audio));
			volumeLimit = 1.0f;
		

		gainHashMap = new HashMap<Integer, Gain>();
		for (Integer frequency : frequencies) {

			Gain gain;
			if (frequency == 0) {
				gain = new Gain(audioContext, 1, volumeLimit);
				gain.addInput(mainInput);
			} else {
				OnePoleFilter filter = new OnePoleFilter(audioContext,
						(float) frequency);
				filter.addInput(mainInput);
				gain = new Gain(audioContext, 2, volumeLimit);
				gain.addInput(filter);
			}
			audioContext.out.addInput(gain);
			gainHashMap.put(frequency, gain);

		}

		audioContext.start();

	}

	private static void loadFonts() {
		font = new UnicodeFont(new java.awt.Font("Times New Roman",
				java.awt.Font.PLAIN, 24));
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		font.addAsciiGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private static void cleanUp() {

		Display.destroy();
		if (audioContext != null)
			audioContext.stop();

	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static float getDelta() {
		long currentTime = getTime();
		float delta = (float) currentTime - (float) lastFrame;
		lastFrame = getTime();
		return delta;
	}

	public static void echo(String string) {
		System.out.println(string);
	}

	private static void myBestButton() {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("JPG",
					ResourceLoader.getResourceAsStream("geralt.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int x = 100;
		int y = 100;
		texture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(x + texture.getTextureWidth(), y);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x + texture.getTextureWidth(),
				y + texture.getTextureHeight());
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x, y + texture.getTextureHeight());
		GL11.glEnd();
	}
	
	public static void play_the_song(String song){
		audioContext.stop();
		
		audio = song;
		initAudio();
	}

}
