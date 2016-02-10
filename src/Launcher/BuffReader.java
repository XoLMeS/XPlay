package Launcher;
import java.io.*;

public class BuffReader {
	FileReader fr;
	BufferedReader br;
	private boolean openThread = false;
	int num_songs = 0;

	public BuffReader() throws IOException {
		countSongs();
	}

	public void readFilfe(String filename) throws IOException {

		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			String s;
			do {
				s = br.readLine();
				if (s != null) {
					System.out.println(s);

				}
			} while (s != null);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	public String getNextString(String filename) throws IOException {
		if (!openThread) {
			openThread(filename);
			openThread = !openThread;
		}
		String s;
		s = br.readLine();
		if (s != null) {
			return s;
		} else
			return null;
	}

	private void openThread(String filename) throws FileNotFoundException {
		fr = new FileReader(filename);
		br = new BufferedReader(fr);
	}

	public void countSongs() throws IOException {

		openThread("playlist.txt");
		String s = "";
		{
			do {
				s = br.readLine();
				if (s != null && !s.isEmpty()) {
					num_songs++;
				}
			} while (s != null);
			fr.close();
			br.close();

		}
	}
}