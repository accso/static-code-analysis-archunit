package de.accso.library.util;

import java.util.UUID;

/**
 * Use this to generate IDs
 */
public class UUIDGenerator {

	public static Long getUUID() {
		return UUID.randomUUID().getMostSignificantBits();
	}

}
