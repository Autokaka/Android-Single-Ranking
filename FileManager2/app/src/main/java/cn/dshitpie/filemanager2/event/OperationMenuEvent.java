package cn.dshitpie.filemanager2.event;

import java.io.File;

public class OperationMenuEvent {
    private File file;

    public OperationMenuEvent(File file) {
        this.file = file;
    }

    public File tellSelectFile() {
        return file;
    }
}
