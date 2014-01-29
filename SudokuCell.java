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

// individual sudoku cell state

package org.ailatovskiy.yajisudoku;

public class SudokuCell
{
	// set number, 0 if not set
	private int number;
	// color indices
	private int[] colors = new int[Sudoku.NOFNUMBERS1];
	// possibilities mask (true if possible)
	private boolean[] possibilities = new boolean[Sudoku.NOFNUMBERS1];
	// possibilities count
	private int possibilitiesTotal;
	// conflict cell flag, true if no possibility remains
	private boolean conflict;
	// fixed cell flag
	private boolean fixed;
	// marked mask
	private boolean[] marked = new boolean[Sudoku.NOFNUMBERS1];

	// constructor
	SudokuCell()
	{
		// number
		number = 0; // not set
		colors[0] = Colors.DEFAULT;
		// possibilities
		for (int number = 1; number <= Sudoku.NOFNUMBERS; number++)
		{
			colors[number] = Colors.DEFAULT;
			possibilities[number] = true;
			marked[number] = false;
		} // end for number
		possibilitiesTotal = Sudoku.NOFNUMBERS; // all are possible in the beginning
		// cell flags
		conflict = false; // no conflict
		fixed = false; // not fixed
	} // end constructor

	public boolean isSet()
	{
		return number != 0; // 0 is not set
	} // end method isSet

	public boolean isConflict()
	{
		return conflict;
	} // end method isConflict

	public boolean isFixed()
	{
		return fixed;
	} // end method isFixed

	public boolean isPossible(int number)
	{
		return possibilities[number];
	} // end method isPossible

	public boolean isMarked(int number)
	{
		return marked[number];
	} // end method isMarked

	// return true if there is only one possibility
	public boolean isSingle()
	{
		return (possibilitiesTotal == 1);
	} // end method isSingle

	public int getNumber()
	{
		return number;
	} // end method getNumber

	public void setNumber(int number_)
	{
		number = number_;
	} // end method setNumber

	public void unset()
	{
		number = 0; // 0 is not set
	} // end method unset

	public void fixNumber()
	{
		fixed = (number != 0); // fix number if set
	} // end method fixNumber

	// return color index for set number
	public int getColorIndex()
	{
		return colors[0];
	} // end method getColorIndex()

	// return color index for the possibility
	public int getColorIndex(int number)
	{
		return colors[number];
	} // end method getColorIndex(int)

	// set color for set number
	public void setColorIndex(int colorIndex)
	{
		colors[0] = colorIndex;
	} // end method setColorIndex(int)

	// set color for the possibility
	public void setColorIndex(int number, int colorIndex)
	{
		colors[number] = colorIndex;
	} // end method setColorIndex(int, int)

	public void clearColorIndex(int colorIndex)
	{
		// reset color index for set number and unset
		if (isSet() && (getColorIndex() == colorIndex) )
		{
			setColorIndex(Colors.DEFAULT);
			unset();
		}
		// reset color index for marked possibility and unmark
		for (int number = 1; number <= Sudoku.NOFNUMBERS; number++)
		{
			if (isMarked(number) && (getColorIndex(number) == colorIndex) )
			{
				setColorIndex(number, Colors.DEFAULT);
				markImpossible(number, false);
			} // end if isMarked && colorIndex
		} // end for number
	} // end method clearColorIndex

	public void exclude(int number)
	{
		possibilities[number] = false;
		--possibilitiesTotal;
		if (possibilitiesTotal < 1)
		{
			conflict = true;
		} // end if
	} // end method exclude

	public void include(int number)
	{
		possibilities[number] = true;
		++possibilitiesTotal;
		if (possibilitiesTotal > 0)
		{
			conflict = false;
		} // end if
	} // end method include

	public int getNofPossibilities()
	{
		return possibilitiesTotal;
	} // end method getNofPossibilities

	// return the first possibility
	public int getFirstPossibility()
	{
		boolean found = false;
		int first = 0;

		int number = 1;
		while (!found && (number <= Sudoku.NOFNUMBERS) )
		{
			if (possibilities[number])
			{
				first = number;
				found = true;
			} // end if possible
			number++;
		} // end while !found && number

		return first;
	} // end method getFirstPossibility

	public void markImpossible(int number, boolean mark)
	{
		marked[number] = mark;
		if (mark)
		{ // exclude marked
			exclude(number);
		}
		else
		{ // include unmarked
			include(number);
		} // end if mark
	} // end method markImpossible

} // end class SudokuCell
