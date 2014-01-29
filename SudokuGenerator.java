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

// tools to generate initial sudoku grid of the specified complexity
// generation starts from solved sudoku with randomly shuffled numbers
// Sudoku.NOFNUMBERS different numbers are selected and fixed
//+		to exclude cyclic ambiguity
// remaining numbers are unfixed in accordance with the specified complexity
// complexity is Sudoku.NOFTOTAL - number of fixed numbers
// complexity range:
//+		from 0 (all numbers are fixed)
//+		to Sudoku.NOFTOTAL - Sudoku.NOFNUMBERS

package org.ailatovskiy.yajisudoku;

import java.util.Random;

public class SudokuGenerator
{
	// initial solved sudoku grid
	// depends on sudoku parameters defined in Sudoku
	private int[][] grid = { // int[10][10]
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, // arbitrary
			{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, // square #1
			{0, 4, 5, 6, 7, 8, 9, 1, 2, 3}, // square #2
			{0, 7, 8, 9, 1, 2, 3, 4, 5, 6}, // square #3
			{0, 2, 1, 4, 3, 6, 5, 8, 9, 7}, // square #4
			{0, 3, 6, 5, 8, 9, 7, 2, 1, 4}, // square #5
			{0, 8, 9, 7, 2, 1, 4, 3, 6, 5}, // square #6
			{0, 5, 3, 1, 6, 4, 2, 9, 7, 8}, // square #7
			{0, 6, 4, 2, 9, 7, 8, 5, 3, 1}, // square #8
			{0, 9, 7, 8, 5, 3, 1, 6, 4, 2}, // square #9
		}; // end grid
	// fixed mask
	private boolean[][] fixed =
			new boolean[Sudoku.NOFNUMBERS1][Sudoku.NOFNUMBERS1];
	// random number generator
	private static final Random randomNum = new Random();
	// max number of shuffles
	private static final int SHUFFLEMAX = 1000; // 9! = 362880

	// constructor
	SudokuGenerator(int complexity)
	{
		// randomize grid
		randomize();

		// set complexity
		setComplexity(complexity);

	} // end constructor

	// shuffle the grid
	private void randomize()
	{
		int number1 = 0, number2 = 0;

		for (int shuffle = 0; shuffle < SHUFFLEMAX; shuffle++)
		{
			// select two different numbers
			do
			{
				number1 = 1 + randomNum.nextInt(Sudoku.NOFNUMBERS);
				number2 = 1 + randomNum.nextInt(Sudoku.NOFNUMBERS);
			} while (number1 == number2); // end do...while
			// swap selected numbers
			swapNumbers(number1, number2);
		} // end for shuffle

	} // end method randomize

	// swap two specified numbers
	private void swapNumbers(int number1, int number2)
	{
		for (int square = 1; square <= Sudoku.NOFNUMBERS; square++)
		{
			for (int cell = 1; cell <= Sudoku.NOFNUMBERS; cell++)
			{
				int number = grid[square][cell];
				if (number == number1)
				{
					grid[square][cell] = number2;
				}
				else if (number == number2)
				{
					grid[square][cell] = number1;
				} // end if number
			} // end for cell
		} // end for square
	} // end method swapNumbers

	// unfix cells in accordance with the complexity
	private void setComplexity(int complexity)
	{
		Mask mask;
		int square, cell;
		int number;

		// select all different numbers
		mask = new Mask();
		while (!mask.isFull() )
		{
			// pick random cell
			square = 1 + randomNum.nextInt(Sudoku.NOFNUMBERS);
			cell = 1 + randomNum.nextInt(Sudoku.NOFNUMBERS);
			// check if the cell contains number to select
			number = grid[square][cell];
			if (!mask.isSelected(number) )
			{ // fix cell and select number
				fixed[square][cell] = true;
				mask.select(number);
			} // end if !isSelected
		} // end while !isFull

		// select remaining numbers in accordance with the specified complexity
		// Sudoku.NOFNUMBERS cells are already fixed
		for (int i = Sudoku.NOFNUMBERS + 1;
				i <= Sudoku.NOFTOTAL - complexity; i++)
		{
			do // find unfixed cell
			{
				// pick random cell
				square = 1 + randomNum.nextInt(Sudoku.NOFNUMBERS);
				cell = 1 + randomNum.nextInt(Sudoku.NOFNUMBERS);
				// check the cell is not fixed
			} while (fixed[square][cell]); // end do...while
			// fix the cell
			fixed[square][cell] = true;
		} // end for i

		// unfix the remaining cells (set to 0)
		for (square = 1; square <= Sudoku.NOFNUMBERS; square++)
		{
			for (cell = 1; cell <= Sudoku.NOFNUMBERS; cell++)
			{
				if (!fixed[square][cell])
				{
					grid[square][cell] = 0;
				} // end !fixed
			} // end for cell
		} // end for square

	} // end method setComplexity

	public int getNumber(int square, int cell)
	{ // 0 means unfixed cell
		return grid[square][cell];
	} // end method getNumber

	// keep track of fixed numbers to fix all different numbers first
	private class Mask
	{
		 private boolean[] mask = new boolean[Sudoku.NOFNUMBERS1]; // default: false

		 public boolean isFull()
		 {
			 boolean full = true;
			 int number = 1;
			 while (full && (number <= Sudoku.NOFNUMBERS) )
			 {
				full = full & mask[number++];
			 } // end while full && number
			 return full; // true only if all mask elements are true
		 } // end method isFull

		 public boolean isSelected(int number)
		 {
			 return mask[number];
		 } // end method isSelected

		 public void select(int number)
		 {
			 mask[number] = true;
		 } // end method select

	} // end inner private class Mask

} // end class SudokuGenerator
