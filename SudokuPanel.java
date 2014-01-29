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

// sudoku panel

package org.ailatovskiy.yajisudoku;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;


@SuppressWarnings("serial")
public class SudokuPanel extends JLayeredPane
{
	//refs
	private final AppFrame appFrame;
	private final Sudoku sudoku;

	private ControlPanel controlPanel;
	private JPanel panel;
	private JLabel label;
	private boolean labelIsShown = false;
	private ComponentHandler componentHandler;
	private SquarePanel[] squares = new SquarePanel[Sudoku.NOFNUMBERS1];

	// label font scale factor
	private final static double SCALE = 0.2;

	// constructor
	SudokuPanel(AppFrame appFrame_, Sudoku sudoku_, Colors colors)
	{
		appFrame = appFrame_;
		sudoku = sudoku_;

		panel = new JPanel();
		GridLayout gridLayout =  // 2px for borders between squares
				new GridLayout(Sudoku.NOFROWS, Sudoku.NOFCOLS, 2, 2);
		panel.setLayout(gridLayout);
		panel.setBackground(colors.getColor(Colors.BGGRID) );
		for (int square = 1; square <= Sudoku.NOFNUMBERS; square++)
		{
			squares[square] = new SquarePanel(this, sudoku, square, colors);
			panel.add(squares[square]);
		} // end for square

		label = new JLabel("solved");
		label.setOpaque(false);
        label.setForeground(colors.getColor(Colors.BGSELECTED) );
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentY(CENTER_ALIGNMENT);

        OverlayLayout overlay = new OverlayLayout(this);
        setLayout(overlay);
		add(panel);

		componentHandler = new ComponentHandler();
		addComponentListener(componentHandler);
	} // end constructor

	public void updatePanel()
	{
		for (int square = 1; square <= Sudoku.NOFNUMBERS; square++)
		{
			squares[square].updatePanel();
		} // end for square

		controlPanel.updatePanel();

		if (sudoku.isSolved() )
		{
			showLabel();
		}
		else
		{
			hideLabel();
		} // end if isSolved

		revalidate();
		repaint();
	} // end method updatePanel

	public void resizeLabel()
	{
		Font font = label.getFont();
		int size = (int) Math.min(getHeight() * SCALE, getWidth() * SCALE);
		label.setFont(new Font(font.getFontName(), Font.BOLD + Font.ITALIC, size) );
	} // end method resizeLabel

	public void showLabel()
	{
		if (!labelIsShown)
		{
			resizeLabel();
			add(label);
			moveToFront(label);
			labelIsShown = true;
		} // end if
	} // end method showLabel

	public void hideLabel()
	{
		if (labelIsShown)
		{
			remove(label);
			labelIsShown = false;
		} // end if
	} // end method hideLabel

	public void setControlPanel()
	{
		controlPanel = appFrame.getControlPanel();
	} // end method setControlPanel

	// resize label font when the panel is resized
	private class ComponentHandler extends ComponentAdapter
	{
		@Override
		public void componentResized(ComponentEvent e)
		{
			if (labelIsShown)
			{
				resizeLabel();
			} // end if labelIsShown
		} // end method componentResized

	} // end inner class ComponentHandler

} // end class SudokuPanel
