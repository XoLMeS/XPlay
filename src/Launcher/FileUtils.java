package Launcher;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class FileUtils {

	public void playlistWriter(String audio) throws IOException {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream("playlist.txt", true);
			PrintStream ps = new PrintStream(fos, true, "UTF-8");
			ps.println(audio);
			ps.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
