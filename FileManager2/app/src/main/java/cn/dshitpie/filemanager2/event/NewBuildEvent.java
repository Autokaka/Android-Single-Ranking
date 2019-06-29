package cn.dshitpie.filemanager2.event;

import java.io.File;

public class NewBuildEvent {
    private File file;

    public NewBuildEvent(File file) {
        this.file = file;
    }

    public File tellHighlightFile() {
        return file;
    }
}
