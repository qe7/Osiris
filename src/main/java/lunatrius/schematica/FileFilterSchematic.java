package lunatrius.schematica;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FileFilterSchematic extends FileFilter implements java.io.FileFilter
{
    public FileFilterSchematic()
    {
    }

    public boolean accept(File file)
    {
        if (file.isDirectory())
        {
            return false;
        }
        else
        {
            return file.getPath().toLowerCase().endsWith(".schematic");
        }
    }

    public String getDescription()
    {
        return "Schematic files (*.schematic)";
    }
}
