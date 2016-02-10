package Launcher;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.opengl.ImageData;

import tools.Color4f;
import tools.FontTT;
import tools.Texture;
import tools.TextureLoader;

public class Launch {

	static final int WIDTH = 1280;
	static final int HEIGHT = 800;
	static Texture back;
	static Texture t1;
	static Texture t2;
	static Texture t3;
	static Texture btn_play;
	static Texture btn_pause;
	static Texture btn_pre;
	static Texture btn_next;
	static boolean paused = true;
	static boolean first_click = false;
	int delta_click = 50;
	static long firstClick;
	static long secondClick;
	static String target = "back_01";
	static String song = "E:/eclipse/X1/XPlay/Nickelback_-_She_Keeps_Me_Up.mp3";
	static ThreadDemo T1 ;
	public static void main(String[] args) throws LWJGLException,
			FontFormatException, IOException, JavaLayerException, InterruptedException {

		initGL2(WIDTH, HEIGHT);
		T1 = new ThreadDemo("qq");
		T1.start();
		init();

		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();

			render();

			Display.update();
			Display.sync(60);
		}

		Display.destroy();
		System.exit(0);
	}

	static FontTT myFont;

	private static void initGL2(int width, int height) throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.create();

		// setup the 2D view
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		// set up textures + transparency
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private static void init() throws FontFormatException, IOException {

		// Load resources!
		TextureLoader loader = new TextureLoader();

		back = loader.getTexture("back_01.jpg", false);
		t1 = loader.getTexture("back_01.jpg", false);
		t2 = loader.getTexture("back_02.jpg", false);
		t3 = loader.getTexture("back_03.jpg", false);
		btn_play = loader.getTexture("btn_play.png", false);
		btn_pause = loader.getTexture("btn_pause.png", false);
		btn_pre = loader.getTexture("btn_pre.png", false);
		btn_next = loader.getTexture("btn_next.png", false);

		int fontResolution = 16; // larger number = better quality = more RAM
									// required
		myFont = new FontTT(Font.createFont(Font.TRUETYPE_FONT, new File(
				"kenvector_future.ttf")), fontResolution, 0);

	}

	private static void render() throws FileNotFoundException, JavaLayerException, InterruptedException {

		mouseControl();
		// draw things
		GL11.glColor3f(1, 1, 1);
		back.bind(); // use the texture
		drawQuad(0, 0, 2050, 1080); // draw a square

		// draw some text
		myFont.drawText("hello, world!", 22, 20, 680, 0, Color4f.YELLOW, 0, 0,
				0, false);
		myFont.drawText("here is some centered text!", 22,
				Display.getWidth() / 2f, 680, 0, new Color4f(0.3f, 0.3f, 1.0f),
				0, 0, 0, true);
		t1.bind();
		drawQuad(50, 50, 100, 100);
		t2.bind();
		drawQuad(150, 50, 200, 100);
		t3.bind();
		drawQuad(250, 50, 300, 100);
		btn_pre.bind();
		drawQuad(350, 50, 400, 100);
		if (paused) {
			btn_play.bind();
		} else {
			btn_pause.bind();
		}
		drawQuad(450, 50, 500, 100);
		btn_next.bind();
		drawQuad(550, 50, 600, 100);

	}

	public static void drawQuad(float x1, float y1, float x2, float y2) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x1, y1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(x2, y1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x2, y2);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x1, y2);
		GL11.glEnd();
	}

	private static void setBack(Texture t) {
		back = t;
	}

	public static void mouseControl() throws FileNotFoundException, JavaLayerException, InterruptedException {
		if(!first_click){
			firstClick = System.currentTimeMillis();
			first_click = !first_click;
		}
		else {
			secondClick = System.currentTimeMillis();
		}
		if(secondClick - firstClick>50){
			first_click = !first_click;
		if (Mouse.isButtonDown(Mouse.getEventButton())) {
			
			switch (target) {
			case "back_01":
				setBack(t1);
				break;
			case "back_02":
				setBack(t2);
				break;
			case "back_03":
				setBack(t3);
				break;
			case "play":
				paused = !paused;
				T1.pause();
				break;
			case "pause":
				paused = !paused;
				T1.pause();
				break;
			case "pre":
				break;
			case "next":
				break;
			default:
				break;
			}
		}
		}
		if (Mouse.getX() > 50 && Mouse.getX() < 100 && Mouse.getY() > 50
				&& Mouse.getY() < 100) {
			target = "back_01";
		}
		else if (Mouse.getX() > 150 && Mouse.getX() < 200 && Mouse.getY() > 50
				&& Mouse.getY() < 100) {
			target = "back_02";
		}
		else if (Mouse.getX() > 250 && Mouse.getX() < 300 && Mouse.getY() > 50
				&& Mouse.getY() < 100) {
			target = "back_03";
		}
		else if (Mouse.getX() > 350 && Mouse.getX() < 400 && Mouse.getY() > 50
			 	&& Mouse.getY() < 100) {
			target = "pre";
		}
		else if (Mouse.getX() > 450 && Mouse.getX() < 500 && Mouse.getY() > 50
				&& Mouse.getY() < 100) {
			if (paused) {
				target = "play";
			} else {
				target = "pause";
			}
		}
		else if (Mouse.getX() > 550 && Mouse.getX() < 600 && Mouse.getY() > 50
				&& Mouse.getY() < 100) {
			target = "next";
		}
		else {target = "";}

	}

}
