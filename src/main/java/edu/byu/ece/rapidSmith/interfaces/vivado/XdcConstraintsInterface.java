/*
 * Copyright (c) 2018 Brigham Young University
 *
 * This file is part of the BYU RapidSmith Tools.
 *
 * BYU RapidSmith Tools is free software: you may redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * BYU RapidSmith Tools is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License is included with the BYU
 * RapidSmith Tools. It can be found at doc/LICENSE.GPL3.TXT. You may
 * also get a copy of the license at <http://www.gnu.org/licenses/>.
 */

package edu.byu.ece.rapidSmith.interfaces.vivado;

import edu.byu.ece.rapidSmith.design.subsite.*;
import edu.byu.ece.rapidSmith.device.*;

import java.io.*;
import java.time.LocalDateTime;

/**
 * This class is used for parsing XDC constraint files and adding them into a RS2 design.
 * 
 * @author Dallon Glick, Dr. Jeffrey Goeders, Thomas Townsend
 *
 */
public class XdcConstraintsInterface {
	private final CellDesign design;
	private final Device device;

	public XdcConstraintsInterface(CellDesign design, Device device) {
		this.design = design;
		this.device = device;
	}

    /**
     * Parses a single line from constraints.xdc, makes an XdcConstraint, and adds it to the design.
     * @param line the constraint line to parse
     */
	private void parseConstraintsLine(String line) {
        int optIdx = line.indexOf(" ");
        int cmntIdx = line.indexOf("#");
        String command = line.substring(0, optIdx);
        String options = (cmntIdx != -1) ? line.substring(optIdx + 1, cmntIdx).trim() : line.substring(optIdx + 1).trim();
        String comment = (cmntIdx != -1) ? line.substring(cmntIdx) : null;
        design.addVivadoConstraint(new XdcConstraint(command, options, comment));
	}

	/**
	 * Loads Vivado constraints into the specified {@link CellDesign}. For now, these constraints are
	 * loaded as two strings, a command and a list of arguments. For package pin constraints, the pin and corresponding
     * port is saved. Otherwise, there is no attempt right now to intelligently handle these constraints, and they are
     * included so the user has access to them.
	 * TODO: Update how we handle constraints files to make them easier to move
	 * @param xdcFile constraints.xdc file
	 * @throws IOException
	 */
	public void parseConstraintsXDC(String xdcFile) throws IOException {
		LineNumberReader br = new LineNumberReader(new BufferedReader(new FileReader(xdcFile)));
		String line;

		while ((line = br.readLine()) != null) {
			String trimmed = line.trim();

			// Skip empty and comment lines
			if (trimmed.length() < 1 || trimmed.startsWith("#"))
				continue;

			parseConstraintsLine(trimmed);
		}

		br.close();
	}

    /**
     * Reads the Vivado constraints from the {@link CellDesign} and creates a
     * constraints.xdc file representing the constraints. The file is written to the newly
     * created TINCR checkpoint.
     *
     * @param xdcOut Constraints.xdc file path
     * @throws IOException
     */
    public void writeConstraintsXdc(String xdcOut) throws IOException {
        try (BufferedWriter fileout = new BufferedWriter (new FileWriter(xdcOut))) {
            LocalDateTime time = LocalDateTime.now();

            fileout.write(String.format("##############################################################\n"
                            + "# Generated by RapidSmith v.2.0 on %02d/%02d/%02d at %02d:%02d:%02d\n"
                            + "##############################################################\n\n",
                    time.getMonthValue(), time.getDayOfMonth(), time.getYear(), time.getHour(), time.getMinute(), time.getSecond()));

            if (design.getVivadoConstraints() != null) {
                for (XdcConstraint constraint : design.getVivadoConstraints()) {
                    fileout.write(constraint + "\n");
                }
            }
        }
    }
}
