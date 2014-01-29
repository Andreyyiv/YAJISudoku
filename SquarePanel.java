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

// one of the squares

package org.ailatovskiy.yajisudoku;

import java.awt.GridLayout;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SquarePanel extends JPanel
{
	private final CellPanel[] cells = new CellPanel[Sudoku.NOFNUMBERS1];

	// constructor
	SquarePanel(SudokuPanel sudokuPanel, Sudoku sudoku, int square, Colors colors)
	{	
		// 1px border between cells
		setLayout(new GridLayout(Sudoku.NOFROWS, Sudoku.NOFCOLS, 1, 1) );
		setBackground(colors.getColor(Colors.BGGRID) );

		// the cells
		for (int cell = 1; cell <= Sudoku.NOFNUMBERS; cell++)
		{
			cells[cell] = new CellPanel(sudokuPanel, sudoku, square, cell, colors);
			add(cells[cell]);
		} // end for cell		
	} // end constructor

	public void updatePanel()
	{
		for (int cell = 1; cell <= Sudoku.NOFNUMBERS; cell++)
		{
			cells[cell].updatePanel();
		} // end for cell
	} // end method updatePanel

} // end class SquarePanel
