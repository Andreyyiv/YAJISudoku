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


// possibilities panel for individual sudoku cell GUI element

package org.ailatovskiy.yajisudoku;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PossibilitiesPanel extends JPanel
{
	// refs
	private final SudokuPanel sudokuPanel;
	private final Sudoku sudoku;
	private final int square;
	private final int cell;
	private final Colors colors;
	private final CellPanel cellPanel;
	
	private JLabel[] possibilities = new JLabel[Sudoku.NOFNUMBERS1];
	private final MouseHandler[] mouseHandlers =
			new MouseHandler[Sudoku.NOFNUMBERS1];
	private int single = 0;

	// font scale factors
	private final static float HSCALE = 0.333f;
	private final static float WSCALE = 0.266f;

	// constructor
	public PossibilitiesPanel(SudokuPanel sudokuPanel_, Sudoku sudoku_,
			int square_, int cell_, Colors colors_, CellPanel cellPanel_)
	{
		sudokuPanel = sudokuPanel_;
		sudoku = sudoku_;
		square = square_;
		cell = cell_;
		colors = colors_;
		cellPanel = cellPanel_;
		
		// no border between possibilities
		setLayout(new GridLayout(Sudoku.NOFROWS, Sudoku.NOFCOLS, 0, 0) );
		setBackground(colors.getColor(Colors.BGPOSSIBLE) );

		for (int number = 1; number <= Sudoku.NOFNUMBERS; number++)
		{
			possibilities[number] =
				new JLabel(String.format("%1d", number), SwingConstants.CENTER);
			possibilities[number].setOpaque(true);
			possibilities[number].setBackground(colors.getColor(Colors.BGPOSSIBLE) );
			possibilities[number].setForeground(colors.getColor(Colors.FGPOSSIBLE) );

			mouseHandlers[number] = new MouseHandler(number);
			possibilities[number].addMouseListener(mouseHandlers[number]);

			add(possibilities[number]);
		} // end for number

		resizeLabels();

		addComponentListener(
				new ComponentAdapter()
				{
					@Override
					public void componentResized(ComponentEvent e)
					{
						resizeLabels();
					} // end method componentResized
				} // end anonymous inner class
			); // end call to addComponentListener method

	} // end constructor

	// adjust labels font size
	public void resizeLabels()
	{
		Font fontOld, fontNew;
		// use current panel size
		float size = Math.min(getHeight() * HSCALE, getWidth() * WSCALE);
		// all the fonts are the same, use the first one
		fontOld = possibilities[1].getFont();
		fontNew = fontOld.deriveFont(size);

		for (int number = 1; number <= Sudoku.NOFNUMBERS; number++)
		{
			possibilities[number].setFont(fontNew);
		} // end for number
	} // end method resizeLabels

	// return true if number color index is the same
	//+		as selected color on the color panel
	public boolean isColor(int number)
	{
		return (sudoku.getColorIndex(square, cell, number)
					== colors.getColorIndex() );
	} // end method isColor

	public void updatePanel()
	{
		String label;
		boolean isConflict = sudoku.isConflict(square, cell);
		boolean isMarked;

		// label text
		for (int number = 1; number <= Sudoku.NOFNUMBERS; number++)
		{
			if (sudoku.isPossible(square, cell, number) )
			{ // possible
				label = String.format("%1d", number);
			}
			else if (sudoku.isMarked(square, cell, number) )
			{ // not possible, marked
				label = "-";
			}
			else
			{ // not possible, excluded
				label = "";
			} // end if isPossible

			possibilities[number].setText(label);
		} // end for number

		// highlight conflict on the Panel level
		if (isConflict)
		{
			setBackground(colors.getColor(Colors.CONFLICT) );
		}
		else
		{
			setBackground(colors.getColor(Colors.BGPOSSIBLE) );
		} // end if isConflict

		// update possibilities colors
		for (int number = 1; number <= Sudoku.NOFNUMBERS; number++)
		{
			isMarked = sudoku.isMarked(square, cell, number);
			if (isConflict && !isMarked)
			{ // conflict
				possibilities[number].setBackground(colors.getColor(Colors.CONFLICT) );
			}
			else if (isMarked)
			{ // marked
				possibilities[number].setBackground(
					colors.getColor(
						sudoku.getColorIndex(square, cell, number) ) );
			}
			else
			{ // default
				possibilities[number].setBackground(colors.getColor(Colors.BGPOSSIBLE) );
			} // end if isConflict && !isMarked
			possibilities[number].setForeground(colors.getColor(Colors.FGPOSSIBLE) );
		} // end for number

		if (sudoku.isSingle(square, cell) )
		{ // single
			single = sudoku.getFirstPossible(square, cell);
			possibilities[single].setForeground(colors.getColor(Colors.FGSINGLE) );
		}
		else
		{ // not single
			single = 0;
		} // end if isSingle

		// activate mouse listeners
		for (int number = 1; number <= Sudoku.NOFNUMBERS; number++)
		{   // activate if not solved
			mouseHandlers[number].activate(!sudoku.isSolved() );
		} // end for number

	} // end method updatePanel

	public void select(int number)
	{
		if (sudoku.isPossible(square, cell, number) ||
				(sudoku.isMarked(square, cell, number) && isColor(number) )
			)
		{
			possibilities[number].setBackground(colors.getColor(Colors.BGSELECTED) );
			setBackground(colors.getColor(Colors.BGSELECTED) );
			if (sudoku.isConflict(square, cell) )
			{
				possibilities[number].setForeground(colors.getColor(Colors.CONFLICT) );
			}
			else
			{
				possibilities[number].setForeground(colors.getColor(Colors.FGSELECTED) );
			} // end if isConflict
		} // end if isPossible || (isMarked && isColor)

		cellPanel.select();
	} // end method select

	public void deselect(int number)
	{
		boolean isConflict = sudoku.isConflict(square, cell);
		boolean isMarked = sudoku.isMarked(square, cell, number);

		if (isConflict && !isMarked)
		{ // conflict
			setBackground(colors.getColor(Colors.CONFLICT) );
			possibilities[number].setBackground(colors.getColor(Colors.CONFLICT) );
		}
		else if (isMarked)
		{ // marked
			possibilities[number].setBackground(
				colors.getColor(sudoku.getColorIndex(square, cell, number) ) );
		}
		else
		{ // default
			setBackground(colors.getColor(Colors.BGPOSSIBLE) );
			possibilities[number].setBackground(colors.getColor(Colors.BGPOSSIBLE) );
		} // end if isConflict && !isMarked

		if (number != single)
		{ // not single
			possibilities[number].setForeground(colors.getColor(Colors.FGPOSSIBLE) );
		}
		else
		{ // single
			possibilities[number].setForeground(colors.getColor(Colors.FGSINGLE) );
		} // end if !single

		cellPanel.deselect();
	} // end method deselect

	public void setNumber(int number)
	{
		if (sudoku.isPossible(square, cell, number) )
		{
			sudoku.setNumber(square, cell, number, colors.getColorIndex() );
			sudoku.updateSudoku();
			sudokuPanel.updatePanel();
		} // end if isPossible
	} // end method setNumber

	public void toggleMarked(int number)
	{
		if (sudoku.isPossible(square, cell, number) )
		{
			sudoku.markImpossible(square, cell, number, colors.getColorIndex() );
			sudoku.updateSudoku();
			sudokuPanel.updatePanel();
		}
		else if (sudoku.isMarked(square, cell, number) && isColor(number) )
		{
			sudoku.unmarkImpossible(square, cell, number);
			sudoku.updateSudoku();
			sudokuPanel.updatePanel();
		} // end if isPossible
	} // end method toggleMarked

	private class MouseHandler extends MouseAdapter
	{
		private final int number;
		private boolean active = false;

		// constructor
		MouseHandler(int number_)
		{
			number = number_;
		} // end constructor

		public void activate(boolean activate)
		{
			active = activate;
		} // end method activate

		@Override
		public void mouseEntered(MouseEvent e)
		{
			if (active)
			{
				select(number);
			} // end if active
		} // end method mouseEntered

		@Override
		public void mouseExited(MouseEvent e)
		{
			if (active)
			{
				deselect(number);
			} // end if active
		} // end method mouseEntered

		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (active)
			{
				if (e.isMetaDown() )
				{ // right mouse button
					toggleMarked(number);
				}
				else
				{ // other mouse button
					setNumber(number);
				} // end if isMetaDown
			} // end if active
		} // end method mouseClicked

	} // end inner class MouseHandler

} // end class PossibilitiesPanel
