package org.model;

public class Progress {
    private final int completed;
    private final int toRead;
    private final int inProgress;

    public Progress(int completed, int toRead, int inProgress) {
        this.completed = completed;
        this.toRead = toRead;
        this.inProgress = inProgress;
    }

    public int getCompleted() {
        return completed;
    }

    public int getToRead() {
        return toRead;
    }

    public int getInProgress() {
        return inProgress;
    }
}
