package com.amaglovany.nsd.net.protocol;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@IntDef({Type.IMAGE, Type.VIDEO, Type.TEXT, Type.FILE})
public @interface Type {
    int IMAGE = 0;
    int VIDEO = 1;
    int TEXT = 2;
    int FILE = 3;
}