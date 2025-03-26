package org.ainzson.oops.collections.queue;

import java.util.ArrayDeque;
import java.util.Deque;

public class UndoRedoManager {
    private Deque<String> undoStack = new ArrayDeque<>();
    private Deque<String> redoStack = new ArrayDeque<>();

    public void performAction(String action) {
        System.out.println("Performing: " + action);
        undoStack.push(action);
        redoStack.clear();
    }

    public void undo() {
        if(undoStack.isEmpty()) {
            System.out.println("Noting to Undo!");
            return;
        }
        String lastAction = undoStack.pop();
        redoStack.push(lastAction);
        System.out.println("Undo: " + lastAction);
    }

    public void redoAction() {
        if(redoStack.isEmpty()) {
            System.out.println("Nothing to Redo!");
            return;
        }
        String lastUndone = redoStack.pop();
        undoStack.push(lastUndone);
        System.out.println("Redo: " + lastUndone);
    }
}
