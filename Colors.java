/* YAJI Sudoku (Yet Another Java Implementation of Sudoku puzzle)
 * Copyright (C) 2014 Andrey Ilatovskiy
 *
 * This file is part of YAJI Sudoku.
 *
 * YAJI Sudoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * YAJI Sudoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with YAJI Sudoku.  If not, see <http://www.gnu.org/licenses/>.
 */

// colors for different GUI elements

package org.ailatovskiy.yajisudoku;

import java.awt.Color;

public class Colors
{
	public static final int DEFAULT    = -1; // used as a flag only
	// number of numbered colors
	// to change, also change NOFCOLS and NOFROWS defined in ColorsPanel
	public static final int NOFCOLORS  = 9;
	public static final int NOFCOLORS1 = NOFCOLORS + 1; // to define natural array
	public static final int FGFIXED    = NOFCOLORS1;
	public static final int BGFIXED    = NOFCOLORS1 + 1;
	public static final int FGSELECTED = NOFCOLORS1 + 2;
	public static final int BGSELECTED = NOFCOLORS1 + 3;
	public static final int BGGRID     = NOFCOLORS1 + 4;
	public static final int FGSET      = NOFCOLORS1 + 5;
	public static final int FGPOSSIBLE = NOFCOLORS1 + 6;
	public static final int BGPOSSIBLE = NOFCOLORS1 + 7;
	public static final int FGSINGLE   = NOFCOLORS1 + 8;
	public static final int CONFLICT   = NOFCOLORS1 + 9;

	// use the last defined color index + 1
	private static final Color[] colors = new Color[CONFLICT + 1];
	private int currentColorIndex;

	// constructor
	Colors()
	{
		// numbered colors
		colors[1] = new Color(255, 255, 255); // white
		colors[2] = new Color(170, 204, 255); // pale blue
		colors[3] = new Color(128, 229, 255); // pale cyan
		colors[4] = new Color(128, 255, 179); // pale green
		colors[5] = new Color(209, 255, 146); // pale green-yellow
		colors[6] = new Color(255, 255, 113); // pale yellow
		colors[7] = new Color(255, 204, 127); // pale orange
		colors[8] = new Color(255, 170, 170); // pale pink
		colors[9] = new Color(255, 170, 238); // pale magenta

		// preselected
		currentColorIndex = ColorsPanel.PRESELECTED;

		// fixed number
		colors[FGFIXED] = Color.BLACK;
		colors[BGFIXED] = new Color(214, 217, 223); // light gray

		// selections
		colors[FGSELECTED] = Color.WHITE;
		colors[BGSELECTED] = Color.BLUE;

		// sudoku panel grid
		colors[BGGRID] = Color.BLACK;

		// set numbers
		colors[FGSET] = Color.BLUE;

		// possibilities
		colors[FGPOSSIBLE] = Color.BLACK;
		colors[BGPOSSIBLE] = Color.WHITE;

		// single possibility
		colors[FGSINGLE] = Color.BLUE;

		// conflict
		colors[CONFLICT] = Color.RED;

	} // end constructor

	// get current color index
	public int getColorIndex()
	{
		return currentColorIndex;
	} // end method getColorIndex

	// set current color index
	public void setColorIndex(int colorIndex)
	{
		currentColorIndex = colorIndex;
	} // end method setColorIndex

	// get current color
	public Color getColor()
	{
		return colors[currentColorIndex];
	} // end method getColor()

	// get color by color index
	public Color getColor(int colorIndex)
	{
		return colors[colorIndex];
	} // end method getColor(int)

	public void setColor(int colorIndex, Color color)
	{
		colors[colorIndex] = color;
	} // end method setColor

} // end class Colors
