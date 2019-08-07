package com.amaglovany.nsd.net.protocol;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@IntDef({ConfirmationType.DECLINED, ConfirmationType.ACCEPTED})
public @interface ConfirmationType {
    int DECLINED = 0;
    int ACCEPTED = 1;
}
