
package com.laytonsmith.abstraction;

/**
 *
 * 
 */
public interface MCPlugin extends AbstractionObject {

    boolean isEnabled();

    boolean isInstanceOf(Class c);

    String getName();

    /**
     * This is intended to be a unique id
     *
     * @return
     */
    String getId();

    String getVersion();

}
