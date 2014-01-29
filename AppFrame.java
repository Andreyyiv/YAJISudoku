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

// app frame with control panel on the left and sudoku panel on the right
// creates new sudoku and colors objects and passes them to both panels

package org.ailatovskiy.yajisudoku;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class AppFrame extends JFrame
{
	private Sudoku sudoku;
	private Colors colors;
	private ControlPanel controlPanel;
	private SudokuPanel sudokuPanel;
	private ImageIcon icon;

	// constructor
	AppFrame()
	{
		// set title and icon of the frame
		super("YAJI Sudoku");
		URL iconURL = getClass().getResource("icon.png");
		if (iconURL != null)
		{
			icon = new ImageIcon(iconURL);
			setIconImage(icon.getImage() );
		}
		else
		{
			icon = new ImageIcon();
		} // end if iconURL

		// primary objects that store program state
		sudoku = new Sudoku();
		colors = new Colors();

		// sudokuPanel needs access to controlPanel which is not defined yet
		// sudokuPanel will have access to controlPanel through this object
		sudokuPanel = new SudokuPanel(this, sudoku, colors);
		add(sudokuPanel, BorderLayout.CENTER);

		// control panel needs parent frame to display About dialog
		controlPanel = new ControlPanel(this, sudoku, colors, sudokuPanel, icon);
		add(controlPanel, BorderLayout.WEST);

		sudokuPanel.setControlPanel(); // now sudokuPanel has access to controlPanel
	} // end constructor

	public ControlPanel getControlPanel()
	{
		return controlPanel;
	} // end method getControlPanel

} // end class AppFrame
