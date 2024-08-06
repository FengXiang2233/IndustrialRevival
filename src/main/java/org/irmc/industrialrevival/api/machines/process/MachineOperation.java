package org.irmc.industrialrevival.api.machines.process;

public interface MachineOperation {
    void addProgress(int progress);
    int getCurrentProgress();
    int getTotalDuration();
    default int getRemainingDuration() {
        return getTotalDuration() - getCurrentProgress();
    }
    default boolean isDone() {
        return getCurrentProgress() >= getTotalDuration();
    }
    default void onCancel() {}
}
