package org.wings_lab.safedrive;

import org.wings_lab.safedrive.parsers.M2XValueObject;

/**
 * Created by rAYMOND on 2/20/2016.
 */
public abstract class CallBack {

    public abstract void onFiveSecondEyeClosed();

    public void onAccessingObject(M2XValueObject object){};
}
