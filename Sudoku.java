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

// current sudoku state

package org.ailatovskiy.yajisudoku;

public class Sudoku
{
	// constants for particular sudoku puzzle type
	// to change, also change:
	//+		- initial grid in SudokuGenerator,
	//+		- complexity levels in ActionsPanel,
	//+		- font scale factors in PossibilitiesPanel.
	public final static int NOFCOLS     = 3;
	public final static int NOFROWS     = NOFCOLS;
	public final static int NOFNUMBERS  = NOFCOLS * NOFROWS;
	// to define natural arrays
	public final static int NOFNUMBERS1 = NOFNUMBERS + 1;
	public final static int NOFTOTAL    = NOFNUMBERS * NOFNUMBERS;

	private SudokuCell[][] sudoku = new SudokuCell[NOFNUMBERS1][NOFNUMBERS1];
	private int[] usedSetColors = new int[Colors.NOFCOLORS1];
	private int solved;
	private MaskSetColor maskSetColor;

	// new sudoku
	public void reset()
	{
		solved = 0;

		for (int colorIndex = 1; colorIndex <= Colors.NOFCOLORS; colorIndex++)
		{
			usedSetColors[colorIndex] = 0;
		} // end for colorIndex
		maskSetColor = new MaskSetColor();

		for (int square = 1; square <= NOFNUMBERS; square++)
		{
			for (int cell = 1; cell <= NOFNUMBERS; cell++)
			{
				sudoku[square][cell] = new SudokuCell();
			} // end for cell
		} // end for square

	} // end method reset

	public boolean isSet(int square, int cell)
	{
		return sudoku[square][cell].isSet();
	} // end method isSet

	public boolean isConflict(int square, int cell)
	{
		return sudoku[square][cell].isConflict();
	} // end method isConflict

	public boolean isFixed(int square, int cell)
	{
		return sudoku[square][cell].isFixed();
	} // end method isFixed

	public boolean isPossible(int square, int cell, int number)
	{
		return sudoku[square][cell].isPossible(number);
	} // end method isPossible

	public boolean isMarked(int square, int cell, int number)
	{
		return sudoku[square][cell].isMarked(number);
	} // end method isMarked

	public boolean isSingle(int square, int cell)
	{
		return sudoku[square][cell].isSingle();
	} // end method isSingle

	public int getNumber(int square, int cell)
	{
		return sudoku[square][cell].getNumber();
	} // end method getNumber

	// for new sudoku grid only, used internally
	public void setNumber(int square, int cell, int number)
	{
		sudoku[square][cell].setNumber(number);
	} // end method setNumber(int, int, int)

	// set by user
	public void setNumber(int square, int cell, int number, int colorIndex)
	{
		sudoku[square][cell].setNumber(number);
		setColorIndex(square, cell, colorIndex);
		++usedSetColors[colorIndex];
		++solved;
		if (usedSetColors[colorIndex] == 1) // first number is set with this color
		{
			maskSetColor.set(square, cell, colorIndex);
		} // end if first number
	} // end method setNumber(int, int, int, int)

	public void unset(int square, int cell)
	{
		sudoku[square][cell].unset();
		--usedSetColors[getColorIndex(square, cell)];
		setColorIndex(square, cell, Colors.DEFAULT);
		--solved;
	} // end method unset

	// for new sudoku grid only, used internally
	public void fixNumber(int square, int cell)
	{
		sudoku[square][cell].fixNumber();
	} // end method fixNumber

	// fix partially solved sudoku, by user
	public void fixGrid()
	{
		for (int square = 1; square <= NOFNUMBERS; square++)
		{
			for (int cell = 1; cell <= NOFNUMBERS; cell++)
			{
				if (isSet(square, cell) && !isFixed(square, cell) )
				{
					--usedSetColors[getColorIndex(square, cell)];
					setColorIndex(square, cell, Colors.DEFAULT);
					sudoku[square][cell].fixNumber();
				} // end if isSet && !isFixed
			} // end for cell
		} // end for square
	} // end method fixGrid

	// set color index for set number
	private void setColorIndex(int square, int cell, int colorIndex)
	{
		sudoku[square][cell].setColorIndex(colorIndex);
	} // end method setColorIndex(int, int, int)

	// set color index for the possible number
	private void setColorIndex(int square, int cell, int number, int colorIndex)
	{
		sudoku[square][cell].setColorIndex(number, colorIndex);
	} // end method setColorIndex(int, int, int, int)

	// return color index for set number
	public int getColorIndex(int square, int cell)
	{
		return sudoku[square][cell].getColorIndex();
	} // end method getColorIndex(int, int)

	// return color index for the possible number
	public int getColorIndex(int square, int cell, int number)
	{
		return sudoku[square][cell].getColorIndex(number);
	} // end method getColorIndex(int, int, int)

	public void clearColorIndex(int colorIndex)
	{
		for (int square = 1; square <= NOFNUMBERS; square++)
		{
			for (int cell = 1; cell <= NOFNUMBERS; cell++)
			{
				clearColorIndex(square, cell, colorIndex);
			} // end for cell
		} // end for square
	} // end method clearColorIndex

	private void clearColorIndex(int square, int cell, int colorIndex)
	{
		if (isSet(square, cell) 
				&& (getColorIndex(square, cell) == colorIndex)
				&& !maskSetColor.isFirst(square, cell, colorIndex) )
		{
			unset(square, cell);
		} // end if isSet && colorIndex && !isFirst
		for (int number = 1; number <= NOFNUMBERS; number++)
		{
			if (isMarked(square, cell, number)
					&& (getColorIndex(square, cell, number) == colorIndex) )
			{
				unmarkImpossible(square, cell, number);
			} // end if isMarked && colorIndex
		} // end for number
	} // end method clearColorIndex

	public int getNofPossibles(int square, int cell)
	{
		return sudoku[square][cell].getNofPossibilities();
	} // end method getNofPossibles

	public int getFirstPossible(int square, int cell)
	{
		return sudoku[square][cell].getFirstPossibility();
	} // end method getFirstPossible

	public void exclude(int square, int cell, int number)
	{
		sudoku[square][cell].exclude(number);
	} // end method exclude

	public void include(int square, int cell, int number)
	{
		sudoku[square][cell].include(number);
	} // end method include

	public void markImpossible(int square, int cell, int number, int colorIndex)
	{
		sudoku[square][cell].markImpossible(number, true);
		setColorIndex(square, cell, number, colorIndex);
	} // end method markImpossible

	public void unmarkImpossible(int square, int cell, int number)
	{
		sudoku[square][cell].markImpossible(number, false);
		setColorIndex(square, cell, number, Colors.DEFAULT);
	} // end method unmarkImpossible

	// used while creating new sudoku grid
	public void setSolved(int solved_)
	{
		solved = solved_;
	} // end method setSolved

	public boolean isSolved()
	{
		return (solved == NOFTOTAL);
	} // end method isSolved

	public int getSolved()
	{
		return solved;
	} // end method getSolved

	public void updateSudoku()
	{
		// run updatePossibles() until there is no change
		while (updatePossibles() );
	} // end method updateSudoku

	private boolean updatePossibles()
	{
		boolean updated = false;

		for (int square = 1; square <= NOFNUMBERS; square++)
		{
			for (int cell = 1; cell <= NOFNUMBERS; cell++)
			{
				if (!isSet(square, cell) )
				{   // always call updatePossibles
					updated = updated | updatePossibles(square, cell);
				} // end if !isSet
			} // end for cell
		} // end for square

		return updated;
	} // end method updatePossibles

	private boolean updatePossibles(int square, int cell)
	{
		boolean updated = false, possible, set;

		for (int number = 1; number <= NOFNUMBERS; number++)
		{
			possible = isPossible(square, cell, number);
			set = isSetSquare(square, number) || isSetLine(square, cell, number);

			if (!isMarked(square, cell, number) )
			{ // not marked
				if (possible && set)
				{
					exclude(square, cell, number);
					updated = true;
				}
				else if (!possible && !set)
				{
					include(square, cell, number);
					updated = true;
				} // end if possible && set
			} // end if isMarked
		} // end for number

		return updated;
	} // end method updatePossibles

	// check if the number is set in the square
	private boolean isSetSquare(int square, int number)
	{
		boolean set = false;

		int cell = 1;
		while (!set && (cell <= NOFNUMBERS) )
		{
			if (isSet(square, cell) && (getNumber(square, cell) == number) )
			{
				set = true;
			} // end if isSet && number
			cell++;
		} // end while cell

		return set;
	} // end method isSetSquare

	// check if the number is set in the row or the column outside the square
	private boolean isSetLine(int square, int cell, int number)
	{
		boolean set = false;

		int squareRow = num2row(square);
		int squareCol = num2col(square);
		int cellRow = num2row(cell);
		int cellCol = num2col(cell);
		int square1, cell1; // numbers to check
		int squareRow1, squareCol1;
		int cellRow1, cellCol1;

		// scan the row outside the square
		squareCol1 = 1;
		while (!set && (squareCol1 <= NOFCOLS) )
		{	// scan every square of the same row
			square1 = rowcol2num(squareRow, squareCol1);
			if (square1 != square) // exclude the parent square
			{
				cellCol1 = 1;
				while (!set && (cellCol1 <= NOFCOLS) )
				{ 	// scan every cell in the same row
					cell1 = rowcol2num(cellRow, cellCol1);
					if (isSet(square1, cell1) && (getNumber(square1, cell1) == number) )
					{ // if number is set
						set = true;
					} // end if isSet && number
					cellCol1++;
				} // end while cellCol1
			} // end if !square
			squareCol1++;
		} // end while squareCol1

		// scan the column outside the square
		squareRow1 = 1;
		while (!set && (squareRow1 <= NOFROWS) )
		{	// scan every square of the same column
			square1 = rowcol2num(squareRow1, squareCol);
			if (square1 != square) // exclude the parent square
			{
				cellRow1 = 1;
				while (!set && (cellRow1 <= NOFROWS) )
				{ 	// scan every cell in the same column
					cell1 = rowcol2num(cellRow1, cellCol);
					if (isSet(square1, cell1) && (getNumber(square1, cell1) == number) )
					{ // if number is set
						set = true;
					} // end if isSet && number
					cellRow1++;
				} // end while cellRow1
			} // end if !square
			squareRow1++;
		} // end while squareRow1

		return set;
	} // end method isSetLine

	// convert square/cell number to row number
	private static int num2row(int number)
	{
		return (number - 1) / NOFCOLS + 1;
	} // end method num2row

	// convert square/cell number to column number
	private static int num2col(int number)
	{
		int col = number % NOFCOLS;
		if (col == 0)
		{
			col = NOFCOLS;
		} // end if 0
		return col;
	} // end method num2col

	// convert row and column numbers to square/cell number
	private static int rowcol2num(int row, int col)
	{
		return (row - 1) * NOFCOLS + col;
	} // end method rowcol2num

	// store coordinates of the first number \
	// that is set with particular color index
	private class MaskSetColor
	{
		private int[] squares = new int[NOFNUMBERS1];
		private int[] cells = new int[NOFNUMBERS1];

		public void set(int square, int cell, int colorIndex)
		{
			squares[colorIndex] = square;
			cells[colorIndex] = cell;
		} // end method set

		public boolean isFirst(int square, int cell, int colorIndex)
		{
			return (squares[colorIndex] == square) && (cells[colorIndex] == cell);
		} // end method isFirst

	} // end private inner class MaskSetColor

} // end class Sudoku
