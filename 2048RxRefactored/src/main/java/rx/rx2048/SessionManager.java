package rx.rx2048;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 *
 * @author José Pereda
 * @date 22-abr-2014 - 12:11:11
 */
public class SessionManager {

    public final String SESSION_PROPERTIES_FILENAME;
    private final Properties props = new Properties();
    private final int grid_size;

    public SessionManager(int grid_size) {
        this.grid_size = grid_size;
        this.SESSION_PROPERTIES_FILENAME = "game2048_" + grid_size + ".properties";
    }

    public void saveSession(Map<Location, Tile> gameGrid, Integer score) {
        try {
            IntStream.range(0, this.grid_size).boxed().forEach(t_x -> {
                IntStream.range(0, this.grid_size).boxed().forEach(t_y -> {
                    Tile t = gameGrid.get(new Location(t_x, t_y));
                    this.props.setProperty("Location_" + t_x.toString() + "_" + t_y.toString(),
                            t != null ? t.getValue().toString() : "0");
                });
            });
            this.props.setProperty("score", score.toString());
            this.props.store(new FileWriter(this.SESSION_PROPERTIES_FILENAME), this.SESSION_PROPERTIES_FILENAME);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int restoreSession(Map<Location, Tile> gameGrid) {
        try (Reader reader = new FileReader(this.SESSION_PROPERTIES_FILENAME)) {
            this.props.load(reader);
        } catch (FileNotFoundException ignored) {
            return -1;
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

        Observable.range(0, this.grid_size)
        		.flatMap(x -> Observable.range(0, this.grid_size)
        				.<Location>map(y -> new Location(x, y)))
        		.map(loc -> {
					String val = this.props.getProperty("Location_" + loc.getX() + "_" + loc.getY());
					Tile t = Tile.newTile(new Integer(val));
					t.setLocation(loc);
					return t;
				})
        		.filter(tile -> !tile.getValue().equals(Integer.valueOf(0)))
        		.observeOn(Schedulers.computation())
        		.subscribe(tile -> gameGrid.put(tile.getLocation(), tile));

        String score = this.props.getProperty("score");
        if (score != null) {
            return new Integer(score);
        }
        return 0;
    }

}
