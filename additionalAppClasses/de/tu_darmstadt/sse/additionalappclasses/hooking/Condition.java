package de.tu_darmstadt.sse.additionalappclasses.hooking;

import com.morgoo.hook.zhook.MethodHook.MethodHookParam;


public interface Condition {
	boolean isConditionSatisfied(MethodHookParam param);
}
