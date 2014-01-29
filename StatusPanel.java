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

// status panel

package org.ailatovskiy.yajisudoku;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel
{
	private final Sudoku sudoku;
	private final JLabel labelLevel, labelSolved;
	private String level;

	// constructor
	StatusPanel(Sudoku sudoku_)
	{
		sudoku = sudoku_;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS) );

		labelLevel = new JLabel("");
		add(labelLevel);

		labelSolved = new JLabel("");
		add(labelSolved);

		labelLevel.setAlignmentX(LEFT_ALIGNMENT);
		labelSolved.setAlignmentX(LEFT_ALIGNMENT);

	} // end constructor

	public void setLevel(String level_)
	{
		level = level_;
	} // end method setLevel

	public void updatePanel()
	{
		labelLevel.setText("level: " + String.format("%s", level) );

		if (!sudoku.isSolved() )
		{ // not solved
			labelSolved.setText("solved: " + String.format("%2d", sudoku.getSolved() ) );
		}
		else
		{ // solved
			labelSolved.setText("");
		} // end if !isSolved
	} // end method updatePanel

} // end class StatusPanel
