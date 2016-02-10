package Launcher;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;


public class Playlist {

	
	private int currentSong = -1;
	ArrayList<String> playlist = new ArrayList<String>();
	
	BuffReader br = new BuffReader();
	int num_songs = br.num_songs;

	public Playlist() throws IOException {
		writePlaylistToArray();

	}

	public String nextSong() {
		currentSong = currentSong + 1;
		if (currentSong == playlist.size()) {
			currentSong = 0;
		}
		return playlist.get(currentSong);
	}

	public String previousSong() {
		currentSong = currentSong - 1;
		if (currentSong < 0) {
			currentSong = playlist.size()-1;
		}
		return playlist.get(currentSong);
	}
	
	public String getCurrentSong(){
		return playlist.get(currentSong);
	}

	public void getPlaylist() throws IOException {
		for (String song : playlist) {
			System.out.println(song);
		}

	}

	private void writePlaylistToArray() throws IOException {
		for (int i = 0; i < num_songs; i++) {
			addSong();
		}

	}

	private void addSong() throws IOException {
		String song = br.getNextString("playlist.txt");
		playlist.add(song);
	
	}

	public int getPlaylistLength() {
		return playlist.size();
	}


	public void addSongToPlaylist() throws IOException {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("C:/Users/"));
		int result = fileChooser.showOpenDialog(new JPanel());
		if (result != JFileChooser.APPROVE_OPTION) {

		}
		File file = fileChooser.getSelectedFile();
		if (file != null) {
			String audio = file.getPath();
			FileUtils f = new FileUtils();
			f.playlistWriter(audio);
			playlist.add(audio);
			System.out.println("Song added to playlist " + audio);
		
		}
	}
}
