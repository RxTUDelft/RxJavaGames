package rx.rx2048;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        	Observable.range(0, this.grid_size)
        			.flatMap(x -> Observable.range(0, this.grid_size)
        					.<Location>map(y -> new Location(x, y)))
        			.map(gameGrid::get)
        			.filter(Objects::nonNull)
        			.observeOn(Schedulers.computation())
        			.subscribe(tile -> {
        				Location l = tile.getLocation();
        				this.props.setProperty("Location_" + l.getX() + "_" + l.getY(), tile.getValue().toString());
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
					if (val != null) {
						Tile t = Tile.newTile(new Integer(val));
						t.setLocation(loc);
						return t;
					}
					return null;
				})
				.filter(Objects::nonNull)
        		.observeOn(Schedulers.computation())
        		.subscribe(tile -> gameGrid.put(tile.getLocation(), tile));

        String score = this.props.getProperty("score");
        if (score != null) {
            return new Integer(score);
        }
        return 0;
    }

}
