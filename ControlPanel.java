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

// control panel is a tool bar with actions panel,
//+		color selection panel, status panel

package org.ailatovskiy.yajisudoku;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class ControlPanel extends JToolBar
{
	private ActionsPanel actionsPanel;
	private ColorsPanel colorsPanel;
	private StatusPanel statusPanel;

	// constructor
	ControlPanel(AppFrame appFrame, Sudoku sudoku, Colors colors,
					SudokuPanel sudokuPanel, ImageIcon icon)
	{
		setOrientation(VERTICAL);

		statusPanel = new StatusPanel(sudoku);
		colorsPanel = new ColorsPanel(sudokuPanel, sudoku, colors);
		actionsPanel =	new ActionsPanel(appFrame, sudoku,
								sudokuPanel, statusPanel, colorsPanel, icon);

		add(actionsPanel);
		addSeparator();
		add(colorsPanel);
		addSeparator();
		add(statusPanel);
	} // end constructor

	public void updatePanel()
	{
		actionsPanel.updatePanel();
		statusPanel.updatePanel();
		colorsPanel.updatePanel();
	} // end method updatePanel

} // end class ControlPanel
