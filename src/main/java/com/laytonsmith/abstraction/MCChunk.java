
package com.laytonsmith.abstraction;

import java.util.List;

/**
 *
 * @author import
 */
public interface MCChunk extends AbstractionObject {
	boolean isLoaded();

	int getX();

	int getZ();

	MCWorld getWorld();

	List<MCEntity> getEntities();
}
