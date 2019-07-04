package cn.dshitpie.filemanager2.event;

import java.io.File;

public class CopyEvent {
    private File file;

    public CopyEvent(File file) {
        this.file = file;
    }

    public File tellCopyFile() {
        return file;
    }
}
