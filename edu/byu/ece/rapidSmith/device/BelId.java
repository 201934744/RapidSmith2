/*
 * Copyright (c) 2010-2011 Brigham Young University
 *
 * This file is part of the BYU RapidSmith Tools.
 *
 * BYU RapidSmith Tools is free software: you may redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * BYU RapidSmith Tools is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License is included with the BYU
 * RapidSmith Tools. It can be found at doc/gpl2.txt. You may also
 * get a copy of the license at <http://www.gnu.org/licenses/>.
 *
 */

package edu.byu.ece.rapidSmith.device;

import java.io.Serializable;
import java.util.Objects;

/**
 *  Identifier for a unique BEL name (SLICEL/AFF, SLICEM/F7MUX, etc) in the device.
 *
 *  The identifier consists of a primitive type and the name of the BEL within the
 *  primitive site type.  BelId objects are immutable.
 */
public final class BelId implements Serializable {
	private PrimitiveType primitiveType;
	private String name;

	/**
	 * Constructs a new BelId object.
	 *
	 * @param primitiveType the primitive type of the BEL id
	 * @param name the name of the BEL within the site
	 */
	public BelId(PrimitiveType primitiveType, String name) {
		this.primitiveType = primitiveType;
		this.name = name;
	}

	/**
	 * Returns the primitive type portion of this id.
	 *
	 * @return the primitive type
	 */
	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	/**
	 * Returns the name portion of this id.
	 *
	 * @return the BEL name
	 */
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return primitiveType.hashCode() * 31 + name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final BelId other = (BelId) obj;
		return Objects.equals(this.primitiveType, other.primitiveType) && Objects.equals(this.name, other.name);
	}

	@Override
	public String toString() {
		return primitiveType + "/" + name ;
	}
}