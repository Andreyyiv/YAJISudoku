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

// panel with the main menu

package org.ailatovskiy.yajisudoku;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ActionsPanel extends JPanel
{
	// refs
	private final AppFrame appFrame;
	private final Sudoku sudoku;
	private final SudokuPanel sudokuPanel;
	private final StatusPanel statusPanel;
	private final ColorsPanel colorsPanel;
	private final ImageIcon icon;

	// created in class
	private JPopupMenu actionsMenu;
	private JButton actionsButton;
	private JSliderMenuItem itemNewCustomSlider;
	private JMenuItem itemFixGrid;

	private static final String CUSTOM = "custom";
	private static final String MANUAL = "manual";
	private static final int COMPLEXITYDEFAULT = -1; // special cases
	// complexity levels depend on sudoku parameters defined in Sudoku
	private static final int COMPLEXITYEASY    = 40;
	private static final int COMPLEXITYMEDIUM  = 50;
	private static final int COMPLEXITYHARD    = 56;
	private static final int COMPLEXITYMAX     = 72;

	// constructor
	ActionsPanel(AppFrame appFrame_, Sudoku sudoku_,
					SudokuPanel sudokuPanel_, StatusPanel statusPanel_, 
					ColorsPanel colorsPanel_, ImageIcon icon_)
	{
		// refs
		appFrame = appFrame_;
		sudoku = sudoku_;
		sudokuPanel = sudokuPanel_;
		statusPanel = statusPanel_;
		colorsPanel = colorsPanel_;
		icon = icon_;

		// main menu Actions
		actionsMenu = new JPopupMenu();

		// Actions > New game
    	JMenu menuNew = new JMenu("New game...");
    	actionsMenu.add(menuNew);

    	// Actions > New game > Easy
    	JMenuItem itemNewEasy = new JMenuItem("Easy");
    	itemNewEasy.addActionListener(
    			new newHandler("easy", COMPLEXITYEASY)
    			); // end call to addActionListener method
    	menuNew.add(itemNewEasy);

    	// Actions > New game > Medium
    	JMenuItem itemNewMedium = new JMenuItem("Medium");
    	itemNewMedium.addActionListener(
    			new newHandler("medium", COMPLEXITYMEDIUM)
    			); // end call to addActionListener method
    	menuNew.add(itemNewMedium);

    	// Actions > New game > Hard
    	JMenuItem itemNewHard = new JMenuItem("Hard");
    	itemNewHard.addActionListener(
    			new newHandler("hard", COMPLEXITYHARD)
    			); // end call to addActionListener method
    	menuNew.add(itemNewHard);

    	// Actions > New game > Custom
    	JMenu menuNewCustom = new JMenu("Custom...");
    	menuNew.add(menuNewCustom);

    	// Actions > New game > Custom > slider
    	itemNewCustomSlider = new JSliderMenuItem();
    	menuNewCustom.add(itemNewCustomSlider);

    	// Actions > New game > Custom > OK
    	JMenuItem itemNewCustomOK = new JMenuItem("OK");
    	itemNewCustomOK.addActionListener(
    			new newHandler(CUSTOM, COMPLEXITYDEFAULT)
    			); // end call to addActionListener method
    	menuNewCustom.add(itemNewCustomOK);

    	menuNew.addSeparator();

    	// Actions > New game > Manual
    	JMenuItem itemNewManual = new JMenuItem("Manual");
    	itemNewManual.addActionListener(
    			new newHandler("manual", COMPLEXITYDEFAULT)
    			); // end call to addActionListener method
    	menuNew.add(itemNewManual);

    	// Actions > Fix grid
    	itemFixGrid = new JMenuItem("Fix grid");
    	itemFixGrid.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						sudoku.fixGrid();
						sudokuPanel.updatePanel();
					} // end method actionPerformed
				} // end anonymous inner class
			); // end call to addActionListener method
    	itemFixGrid.setEnabled(false); // disabled
    	actionsMenu.add(itemFixGrid);

    	actionsMenu.addSeparator();

    	// Actions > About
    	JMenuItem itemAbout = new JMenuItem("About...");
    	itemAbout.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JOptionPane.showMessageDialog(appFrame,
						    "YAJI Sudoku\n" +
						    	"Copyright (C) 2014 Andrey Ilatovskiy\n" +
						    	"Distributed under GNU GPLv2+",
						    "About YAJI Sudoku",
						    JOptionPane.INFORMATION_MESSAGE,
						    icon);
					} // end method actionPerformed
				} // end anonymous inner class
			); // end call to addActionListener method
    	actionsMenu.add(itemAbout);

    	actionsMenu.addSeparator();

    	// Actions > Exit
    	JMenuItem itemExit = new JMenuItem("Exit");
    	itemExit.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						System.exit(0);
					} // end method actionPerformed
				} // end anonymous inner class
			); // end call to addActionListener method
    	actionsMenu.add(itemExit);

    	// main button Actions to show menu Actions
		actionsButton = new JButton("Actions");
		actionsButton.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						actionsMenu.show(actionsButton, 0,
							actionsButton.getHeight() ); // just below the button
					} // end method actionPerformed
				} // end anonymous inner class
			); // end call to addActionListener method
		add(actionsButton);

	} // end constructor

	public void updatePanel()
	{
		itemFixGrid.setEnabled(!sudoku.isSolved() ); // disable if solved
	} // end method updatePanel

	// creates new game
	private class newHandler implements ActionListener
	{
		private final String level;
		private int complexity;

		// constructor
		newHandler(String level_, int complexity_)
		{
			level = level_;
			complexity = complexity_;
		} // end constructor

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int solved = 0;

			// get custom complexity from slider
			if (level.equals(CUSTOM) )
			{
				complexity = itemNewCustomSlider.getValue();
			} // end if custom

			// generate new sudoku grid
			sudoku.reset();
			if (!level.equals(MANUAL) )
			{ // autogenerate
				solved = Sudoku.NOFTOTAL - complexity;
				SudokuGenerator sudokuGenerator = new SudokuGenerator(complexity);
				// copy generated grid into main sudoku object
				for (int square = 1; square <= Sudoku.NOFNUMBERS; square++)
				{
					for (int cell = 1; cell <= Sudoku.NOFNUMBERS; cell++)
					{
						sudoku.setNumber(square, cell, sudokuGenerator.getNumber(square, cell) );
						sudoku.fixNumber(square, cell);
					} // end for cell
				} // end for square
				sudoku.setSolved(solved);
			} // end if !manual
			sudoku.updateSudoku();

			// update panels' state
			statusPanel.setLevel(level);
			colorsPanel.select(ColorsPanel.PRESELECTED);
			sudokuPanel.updatePanel(); // updates all panels
		} // end method actionPerformed

	} // end private inner class newHadler


	// menu element with JSlider to choose sudoku complexity
	private class JSliderMenuItem extends JSlider implements MenuElement
	{
		// constructor
		JSliderMenuItem()
		{
			super(SwingConstants.HORIZONTAL, COMPLEXITYEASY, COMPLEXITYMAX,
					COMPLEXITYMEDIUM); // medium level is preselected
			// the label table
			Hashtable<Integer, JLabel> labelTable = // size of 3 elements
					new Hashtable<Integer, JLabel>(3, 1.f);
			labelTable.put(new Integer(COMPLEXITYEASY), new JLabel("easy") );
			labelTable.put(new Integer(COMPLEXITYHARD), new JLabel("hard") );
			labelTable.put(new Integer(COMPLEXITYMAX),  new JLabel("extreme") );
			setLabelTable(labelTable);
			setPaintLabels(true);
		} // end constructor

		@Override // MenuElement
		public Component getComponent()
		{
			return this;
		} // end method getComponent

		@Override // MenuElement
		public MenuElement[] getSubElements()
		{
			return new MenuElement[0];
		} // end method getSubElements

		@Override // MenuElement
		public void menuSelectionChanged(boolean isIncluded)
		{
		} // end method menuSelectionChanged

		@Override // MenuElement
		public void processKeyEvent(KeyEvent e, MenuElement[] path,
										MenuSelectionManager manager)
		{
		} // end method processKeyEvent

		@Override // MenuElement
		public void processMouseEvent(MouseEvent e, MenuElement[] path,
										MenuSelectionManager manager)
		{
		} // end method processMouseEvent

	} // end private inner class JSliderMenuItem

} // end class ActionsPanel
