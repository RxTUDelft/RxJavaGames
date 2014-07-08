package rx.rx2048.ui;

public enum Constants {

	CELL_SIZE(128),
	DEFAULT_GRID_SIZE(4),
	FINAL_VALUE_TO_WIN(2048),
	BORDER_WIDTH(8),
	// grid_width=4*cell_size + 2*cell_stroke/2d (14px css)+2*grid_stroke/2d (2 px css)
	GRID_WIDTH(CELL_SIZE.get() * DEFAULT_GRID_SIZE.get() + BORDER_WIDTH.get() * 2),
	TOP_HEIGHT(92);

	private final int constant;

	private Constants(int constant) {
		this.constant = constant;
	}

	public int get() {
		return this.constant;
	}
}
