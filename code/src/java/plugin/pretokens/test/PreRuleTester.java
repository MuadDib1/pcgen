/*
 *
 * Copyright 2003 (C) Chris Ward <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	   See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package plugin.pretokens.test;

import pcgen.cdom.base.CDOMObject;
import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.core.prereq.PrerequisiteTestFactory;


public class PreRuleTester extends AbstractPrerequisiteTest implements PrerequisiteTest
{

	/**
	 * Get the type of prerequisite handled by this token.
	 * @return the type of prerequisite handled by this token.
	 */
    @Override
	public String kindHandled()
	{
		return "RULE"; //$NON-NLS-1$
	}

	/**
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.prereq.Prerequisite, pcgen.core.Equipment)
	 */
	@Override
	public int passes(final Prerequisite prereq, final Equipment equipment,
		PlayerCharacter aPC) throws PrerequisiteException
	{
		int runningTotal = 0;
		int targetNumber;

		try
		{
			targetNumber = Integer.parseInt(prereq.getOperand());
		}
		catch (NumberFormatException ne)
		{
			// Not an error, just a string
			targetNumber = 1;
		}

		if (Globals.checkRule(prereq.getKey()))
		{
			runningTotal = 1;
		}

		for (Prerequisite element : prereq.getPrerequisites())
		{
			final PrerequisiteTestFactory factory =
					PrerequisiteTestFactory.getInstance();
			final PrerequisiteTest test = factory.getTest(element.getKind());
			if (test != null)
			{
				runningTotal += test.passes(element, equipment, aPC);
			}
		}
		return countedTotal(prereq, prereq.getOperator().compare(runningTotal,
			targetNumber));
	}

	/**
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.prereq.Prerequisite, pcgen.core.PlayerCharacter)
	 */
	@Override
	public int passes(final Prerequisite prereq, final PlayerCharacter character, CDOMObject source)
		throws PrerequisiteException
	{
		int runningTotal = 0;
		int targetNumber;

		try
		{
			targetNumber = Integer.parseInt(prereq.getOperand());
		}
		catch (NumberFormatException ne)
		{
			// Not an error, just a string
			targetNumber = 1;
		}

		if (Globals.checkRule(prereq.getKey()))
		{
			runningTotal = 1;
		}

		final PrerequisiteTestFactory factory =
				PrerequisiteTestFactory.getInstance();
		for (Prerequisite element : prereq.getPrerequisites())
		{
			final PrerequisiteTest test = factory.getTest(element.getKind());
			if (test != null)
			{
				runningTotal += test.passes(element, character, source);
			}
		}
		return countedTotal(prereq, prereq.getOperator().compare(runningTotal,
			targetNumber));
	}

}
