package org.example.design;

public interface Command {
    void execute();
}

abstract class EditorCommand implements Command {
    protected Editor editor;

    public EditorCommand(Editor editor) {
        this.editor = editor;
    }
}

class CopyCommand extends EditorCommand {

    public CopyCommand(Editor editor) {
        super(editor);
    }

    @Override
    public void execute() {
        editor.copy();
    }
}

class PasteCommand extends EditorCommand {

    public PasteCommand(Editor editor) {
        super(editor);
    }

    @Override
    public void execute() {
        editor.paste();
    }
}

class Editor {
    public void copy() {
        System.out.println("copy");
    }

    public void paste() {
        System.out.println("paste");
    }
}
