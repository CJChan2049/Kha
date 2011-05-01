package com.kontechs.kje.backends.android;

import android.content.Context;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import com.kontechs.kje.Key;
import com.kontechs.kje.Loader;
import com.kontechs.kje.Painter;

public class GameThread extends Thread {
	private boolean running = false;
	private SurfaceHolder surface;
	private Context context;
	private com.kontechs.kje.Game game;

	public GameThread(SurfaceHolder surface, Context context) {
		this.surface = surface;
		this.context = context;
	}

	@Override
	public void run() {
		com.kontechs.kje.System.init(new AndroidSystem());
		Loader.init(new ResourceLoader(context));
		
		//game = new com.kontechs.sml.SuperMarioLand();
		//game = new com.kontechs.kje.zool.ZoolGame();
		game = new de.hsharz.beaver.BeaverGame("level1", "tiles");
		
		while (running) {
			Canvas c = null;
			try {
				c = surface.lockCanvas(null);
				Painter p = new CanvasPainter(c);
				synchronized (surface) {
					updateGame();
					doDraw(p);
				}
			}
			finally {
				if (c != null) surface.unlockCanvasAndPost(c);
			}
		}
	}

	public void setRunning(boolean b) {
		running = b;
	}

	public void setSurfaceSize(int width, int height) {
		synchronized (surface) {

		}
	}

	boolean keyDown(int keyCode) {
		synchronized (surface) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				game.key(new com.kontechs.kje.KeyEvent(Key.RIGHT, true));
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				game.key(new com.kontechs.kje.KeyEvent(Key.LEFT, true));
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				game.key(new com.kontechs.kje.KeyEvent(Key.UP, true));
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				game.key(new com.kontechs.kje.KeyEvent(Key.DOWN, true));
				break;
			default:
				return false;	
			}
			return true;
		}
	}

	boolean keyUp(int keyCode) {
		synchronized (surface) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				game.key(new com.kontechs.kje.KeyEvent(Key.RIGHT, false));
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				game.key(new com.kontechs.kje.KeyEvent(Key.LEFT, false));
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				game.key(new com.kontechs.kje.KeyEvent(Key.UP, false));
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				game.key(new com.kontechs.kje.KeyEvent(Key.DOWN, false));
				break;
			default:
				return false;	
			}
			return true;
		}
	}

	private void updateGame() {
		game.update();
	}

	private void doDraw(Painter painter) {
		game.render(painter);
	}
}