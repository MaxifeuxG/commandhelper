
package com.laytonsmith.abstraction;

import com.laytonsmith.PureUtilities.Vector3D;
import com.laytonsmith.abstraction.blocks.MCBlock;

/**
 *
 * 
 */
public interface MCLocation extends AbstractionObject {

	double getX();

	double getY();

	double getZ();

	double distance(MCLocation o);

	double distanceSquared(MCLocation o);

	float getYaw();

	float getPitch();

	double getYawD();

	double getPitchD();

	int getBlockX();

	int getBlockY();

	int getBlockZ();

	MCBlock getBlock();

	MCChunk getChunk();

	MCWorld getWorld();

	Vector3D toVector();

	MCLocation add(MCLocation vec);

	MCLocation add(Vector3D vec);

	MCLocation add(double x, double y, double z);

	MCLocation multiply(double m);

	MCLocation subtract(MCLocation vec);

	MCLocation subtract(Vector3D vec);

	MCLocation subtract(double x, double y, double z);

	void setX(double x);

	void setY(double y);

	void setZ(double z);

	void setPitch(float p);

	void setYaw(float y);

	void breakBlock();

	Vector3D getDirection();

	MCLocation clone();
}
