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

// color selection panel with color chooser and clear button

package org.ailatovskiy.yajisudoku;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ColorsPanel extends JPanel
{
	private final SudokuPanel sudokuPanel;
	private final Sudoku sudoku;
	private final Colors colors;
	private final ColorPanel[] colorPanels = new ColorPanel[Colors.NOFCOLORS1];
	private final ClickHandler[] clickHandlers =
			new ClickHandler[Colors.NOFCOLORS1];
	public static final int PRESELECTED = 1;

	// depend on Colors.NOFCOLORS
	public static final int NOFROWS = 3;
	public static final int NOFCOLS = 3;

	private int selected;
	private final JPanel panel;
	private final JButton button;

	// constructor
	ColorsPanel(SudokuPanel sudokuPanel_, Sudoku sudoku_, Colors colors_)
	{
		sudokuPanel = sudokuPanel_;
		sudoku = sudoku_;
		colors = colors_;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS) );

		// color chooser
		panel = new JPanel();
		// 2px between color panels
		panel.setLayout(new GridLayout(NOFROWS, NOFCOLS, 2, 2) );
		for (int colorIndex = 1; colorIndex <= Colors.NOFCOLORS; colorIndex++)
		{
			colorPanels[colorIndex] = 
				new ColorPanel(colors, colors.getColor(colorIndex),
								(colorIndex == PRESELECTED) );
			clickHandlers[colorIndex] = new ClickHandler(colorIndex);
			colorPanels[colorIndex].addMouseListener(clickHandlers[colorIndex]);
			panel.add(colorPanels[colorIndex]);
		} // end for colorIndex
		selected = PRESELECTED;
		add(panel);

		// clear button
		button = new JButton("clear");
		button.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						sudoku.clearColorIndex(colors.getColorIndex() );
						sudoku.updateSudoku();
						sudokuPanel.updatePanel();
					} // end method actionPerformed
				} // end anonymous inner class
			); // end call to addActionListener method
		button.setEnabled(false); // disable before sudoku grid is created
		add(button);

		panel.setAlignmentX(CENTER_ALIGNMENT);
		button.setAlignmentX(CENTER_ALIGNMENT);

	} // end constructor

	public void select(int colorIndex)
	{
		if (selected != colorIndex)
		{
			colorPanels[selected].select(false);
			colorPanels[colorIndex].select(true);
			selected = colorIndex;
			colors.setColorIndex(selected);
		} // end if
	} // end method select

	public void updatePanel()
	{
		button.setEnabled(!sudoku.isSolved() ); // disable if solved
	} // end method updatePanel

	// select color index upon mouse click
	private class ClickHandler extends MouseAdapter
	{
		private int colorIndex;

		// constructor
		ClickHandler(int colorIndex_)
		{
			colorIndex = colorIndex_;
		} // end constructor

		public void mouseClicked(MouseEvent e)
		{
			select(colorIndex);
		} // end method mouseClicked

	} // end inner class ClickHandler

} // end class ColorsPanel
