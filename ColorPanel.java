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

// individual color panel

package org.ailatovskiy.yajisudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ColorPanel extends JPanel
{
	private final Colors colors;
	private final Color colorInternal, colorExternal;
	private boolean selected;
	private static final int SIZE = 25;
	private static final int SIZEARC = 10;

	// constructor
	ColorPanel(Colors colors_, Color color, boolean isSelected)
	{
		colors = colors_;
		colorInternal = color;
		selected = isSelected;

		colorExternal = colors.getColor(Colors.BGSELECTED);
	} // end constructor

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.setColor(colorInternal);
		g.fillRoundRect(0, 0, SIZE, SIZE, SIZEARC, SIZEARC);

		if (selected)
		{ // highlight selected panel
			g.setColor(colorExternal);
			g.drawRoundRect(0, 0, SIZE, SIZE, SIZEARC, SIZEARC);
		} // end if selected

	} // end method paintComponent

	public void select(boolean select)
	{
		selected = select;
		repaint();
	} // end method select

	public boolean isSelected()
	{
		return selected;
	} // end method isSelected

	@Override
	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	} // end method getMinimumSize

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(SIZE + 1, SIZE + 1);
	} // end method getPreferredSize

} // end class ColorPanel
