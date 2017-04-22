package com.supergreenowl.blobables.model;


public abstract class Player {
	
	private Board b;
	private int cells = 0;
	private int stock = 0;
	private boolean isDead = false;
	
	public byte colour;
	
	public Player(byte colour) {
		this.colour = colour;
	}
	
	/**
	 * Gets the game board this player is playing on.
	 * @return Game board.
	 */
	protected Board getBoard() {
		return b;
	}
	
	/**
	 * Sets the board for this player.
	 * @param b
	 */
	public void setBoard(Board b) {
		this.b = b;
	}
	
	/**
	 * Gets the cell colour of this player.
	 * @return Cell colour value.
	 */
	@Deprecated
	public byte getColour() {
		return colour;
	}
	
	/**
	 * Signals to this player that it has been eliminated.
	 */
	public void kill() {
		isDead = true;
	}
	
	/**
	 * Checks whether the player has been eliminated or not.
	 * @return True if player has been eliminated, false otherwise.
	 */
	public boolean isDead() {
		return isDead;
	}
	
	/**
	 * Gets the number of cells on the board for this player.
	 * @return Number of cells on the game board.
	 */
	public int getCellsCount() {
		return cells;
	}
	
	/**
	 * Sets the number of cells on the game board.
	 * @param count Number of cells for this player.
	 */
	public void setCellsCount(int count) {
		cells = count;
	}
	
	/**
	 * Gets the current cell stock for this player.
	 * @return Number of cells in stock.
	 */
	public int getStock() {
		return stock;
	}
	
	/**
	 * Sets the stock level.
	 * @param stock
	 */
	public void setStock(int stock) {
		if(isDead) return;
		this.stock = stock;
	}
	
	/**
	 * Increases the cell stock for this player by 1.
	 */
	public void addStock() {
		if(isDead) return;
		stock++;
	}
	
	/**
	 * Decreases the number of cells in stock by 1.
	 */
	public void removeStock() {
		if(isDead) return;
		stock--;
	}
	
	/**
	 * Places a cell for the DroidPlayer at the specified location.
	 * Updates the stock level.
	 * @param x x-coordinate of location to place cell.
	 * @param y y-coordinate of location to place cell.
	 */
	protected void putCell(int x, int y) {
		// Cannot play if dead or no stock!
		if(isDead() || getStock() <= 0) return;
		
		getBoard().putBlob(x, y, colour);
		cells++;
		removeStock();
	}
	
	/**
	 * Resets the player to its initial state when the game began. Derived classes
	 * should override this method and call super.reset() to reset any of their own state.
	 */
	public void reset() {
		cells = 0;
		stock = 0;
		isDead = false;
	}
	
}