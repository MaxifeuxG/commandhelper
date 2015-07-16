package com.laytonsmith.PureUtilities.ClassLoading;

import com.laytonsmith.abstraction.MCEntity;

import java.util.Map;

/**
 *
 */
public abstract class DynamicEnum<Abstracted extends Enum,Concrete> {

	Abstracted abstracted;
	Concrete concrete;

	public abstract String name();

	public Abstracted getAbstracted() {
		return abstracted;
	}

	public Concrete getConcrete() {
		return concrete;
	}

}
