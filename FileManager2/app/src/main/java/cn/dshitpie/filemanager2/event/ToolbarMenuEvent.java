package cn.dshitpie.filemanager2.event;

import java.io.File;
public class ToolbarMenuEvent {
    private File file;

    public ToolbarMenuEvent(File file) {
        this.file = file;
    }

    public File tellInFile() {
        return file;
    }
}
