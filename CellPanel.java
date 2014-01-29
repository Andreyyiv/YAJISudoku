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

// individual sudoku cell GUI element

package org.ailatovskiy.yajisudoku;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class CellPanel extends JPanel
{
	// refs
	private final SudokuPanel sudokuPanel;
	private final Sudoku sudoku;
	private final int square;
	private final int cell;
	private final Colors colors;

	private final GridBagConstraints constraints;
	private final JLabel numberLabel;
	private final PossibilitiesPanel possibilitiesPanel;
	private final Border borderEmpty;
	private final Border borderSelected;
	private final Border borderUnselected;
	private final Border borderConflict;
	private final MouseHandler mouseHandler;
	private final ComponentHandler componentHandler;

	// font scale factors
	private final static float HSCALE = 0.7f;
	private final static float WSCALE = 0.5f;

	// default: both elements are not shown
	private boolean numberIsShown     = false;
	private boolean possibilitiesAreShown = false;

	// constructor
	CellPanel(SudokuPanel sudokuPanel_, Sudoku sudoku_,
				int square_, int cell_, Colors colors_)
	{
		// refs
		sudokuPanel = sudokuPanel_;
		sudoku = sudoku_;
		square = square_;
		cell = cell_;
		colors = colors_;

		// number label (shown if number is set)
		// default is 0, that is never shown
		numberLabel = new JLabel("0", SwingConstants.CENTER);
		// possibilities panel (shown if number is not set)
		possibilitiesPanel = new PossibilitiesPanel(sudokuPanel,
										sudoku, square, cell, colors, this);

		setLayout(new GridBagLayout() );

		// one xy-resizable element in the center
		constraints = new GridBagConstraints();
		constraints.fill       = GridBagConstraints.BOTH;
		constraints.anchor     = GridBagConstraints.CENTER;
		constraints.gridx      = 0;
		constraints.gridy      = 0;
		constraints.gridwidth  = 1;
		constraints.gridheight = 1;
		constraints.weightx    = 1.0;
		constraints.weighty    = 1.0;

		// all possible borders
		borderEmpty      = BorderFactory.createEmptyBorder();
		borderConflict   = BorderFactory.createLineBorder(
										colors.getColor(Colors.CONFLICT) );
		borderSelected   = BorderFactory.createLineBorder(
										colors.getColor(Colors.BGSELECTED) );
		borderUnselected = BorderFactory.createLineBorder(
										colors.getColor(Colors.BGPOSSIBLE) );

		// event handlers
		componentHandler = new ComponentHandler();
		addComponentListener(componentHandler);
		mouseHandler = new MouseHandler();
		addMouseListener(mouseHandler);
	} // end constructor

	// return true if cell color index is the same
	//+		as of selected color on the color panel
	private boolean isColor()
	{
		return (sudoku.getColorIndex(square, cell) == colors.getColorIndex() );
	} // end method isColor

	// show number label or possibilities panel in the cell
	public void updatePanel()
	{
		if (sudoku.isSet(square, cell) )
		{ // number is set

			// hide possibilities if shown
			if (possibilitiesAreShown)
			{
				remove(possibilitiesPanel);
				possibilitiesAreShown = false;
			} // end if possibilitiesAreShown

			// show number if not shown
			if (!numberIsShown)
			{
				add(numberLabel, constraints);
				numberIsShown = true;
				resizeNumberLabel();
			} // end if !numberIsShown

			// set number
			numberLabel.setText(String.format("%1d", sudoku.getNumber(square, cell) ) );

			// set color
			if (sudoku.isFixed(square, cell) )
			{ // number is fixed
				numberLabel.setForeground(colors.getColor(Colors.FGFIXED) );
				setBackground(colors.getColor(Colors.BGFIXED) );
			}
			else
			{ // number is set but not fixed
				numberLabel.setForeground(colors.getColor(Colors.FGSET) );
				if (sudoku.isConflict(square, cell) )
				{ // highlight conflicts
					setBackground(colors.getColor(Colors.CONFLICT) );
				}
				else
				{ // use stored color
					setBackground(colors.getColor(sudoku.getColorIndex(square, cell) ) );
				} // end if isConflict
			} // end if isFixed

			// no border
			setBorder(borderEmpty);
		}
		else
		{ // number is not set

			// hide number if shown
			if (numberIsShown)
			{
				remove(numberLabel);
				numberIsShown = false;
			} // end if numberIsShown

			// display possibilities if not shown
			if (!possibilitiesAreShown)
			{
				add(possibilitiesPanel, constraints);
				possibilitiesAreShown = true;
			} // end if !possibilitiesAreShown

			possibilitiesPanel.updatePanel();

			// set border
			if (sudoku.isConflict(square, cell) )
			{ // highlight conflict on the cell panel level
				setBorder(borderConflict);
			}
			else
			{
				setBorder(borderUnselected);
			} // end if isConflict

		} // end if isSet

		mouseHandler.activate(!sudoku.isSolved() ); // activate if not solved
		componentHandler.activate(); // active if cell panel elements are shown
	} // end method updatePanel

	// adjust font size of number label
	public void resizeNumberLabel()
	{
		Font font = numberLabel.getFont();
		// use current size of the cell panel
		float size = Math.min(getHeight() * HSCALE, getWidth() * WSCALE);
		numberLabel.setFont(font.deriveFont(size) );
	} // end method resizeNumberLabel

	public void select()
	{
		setBorder(borderSelected);
	} // end method select

	public void deselect()
	{
		if (sudoku.isSet(square, cell) )
		{ // number is set
			setBorder(borderEmpty); // no border for number
		}
		else if (!sudoku.isConflict(square, cell) )
		{ // default
			setBorder(borderUnselected); // default for possibilities
		}
		else
		{ // conflict
			setBorder(borderConflict); // if isConflict
		} // end if isSet
	} // end method deselect

	// unset number
	public void unset()
	{
		sudoku.unset(square, cell);
		sudoku.updateSudoku();
		sudokuPanel.updatePanel();
	} // end method unset

	private class MouseHandler extends MouseAdapter
	{
		// do nothing until activated
		private boolean active = false;
		private boolean selected = false;

		public void activate(boolean activate)
		{
			active = activate;
		} // end method activate

		// select cell on mouse hovering
		@Override
		public void mouseEntered(MouseEvent e)
		{
			if (active && sudoku.isSet(square, cell) 
					&& !sudoku.isFixed(square, cell) && isColor() )
			{
				select();
				selected = true;
			} // end if active && isSet && !isFixed && isColor
		} // end method mouseEntered

		// unset number on mouse click (any button)
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (active && selected)
			{
				unset();
			} // end if active && selected
		} // end method mouseClicked

		// deselect cell when mouse exits
		@Override
		public void mouseExited(MouseEvent e)
		{
			if (active && selected)
			{
				deselect();
				selected = false;
			} // end if active && selected
		} // end method mouseEntered

	} // end inner class MouseHandler

	// adjust font size of the number label if the panel is resized
	private class ComponentHandler extends ComponentAdapter
	{
		// do nothing until activated
		private boolean active = false;

		public void activate()
		{
			active = true;
		} // end method activate

		@Override
		public void componentResized(ComponentEvent e)
		{
			if (active && sudoku.isSet(square, cell) )
			{
				resizeNumberLabel();
			} // end if active && isSet
		} // end method componentResized

	} // end inner class ComponentHandler

} // end class CellPanel
