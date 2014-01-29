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

// main

package org.ailatovskiy.yajisudoku;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main
{
	public static void main(String[] args)
	{
		// set Nimbus look&feel
		try
		{
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
		    {
		        if ("Nimbus".equals(info.getName() ) )
		        {
		            UIManager.setLookAndFeel(info.getClassName() );
		            break;
		        } // end if Nimbus
		    } // end for info
		}
		catch (Exception e)
		{ // use default look&feel
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName() );
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			} // end try default
		} // end try Nimbus

		AppFrame appFrame = new AppFrame();
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.setSize(600, 536); // near square shape of the sudoku panel
		appFrame.setVisible(true);
	} // end main

} // end class Main
