package terminus.command.content.note;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import terminus.TestFilePath;
import terminus.command.Command;
import terminus.command.CommandResult;
import terminus.common.CommonFormat;
import terminus.content.Note;
import terminus.exception.InvalidArgumentException;
import terminus.exception.InvalidCommandException;
import terminus.exception.InvalidFileException;
import terminus.module.ModuleManager;
import terminus.parser.NoteCommandParser;
import terminus.storage.StorageActionEnum;
import terminus.storage.StorageManager;
import terminus.storage.StorageTypeEnum;
import terminus.ui.Ui;

public class ExportNoteCommandTest {

    Class<Note> type = Note.class;
    private NoteCommandParser commandParser;
    private ModuleManager moduleManager;
    private Ui ui;
    private String tempModule = "test1";

    private StorageManager storageManager;

    @BeforeEach
    void setUp() {
        this.moduleManager = new ModuleManager();
        moduleManager.addModule(tempModule);
        this.commandParser = NoteCommandParser.getInstance();
        this.commandParser.setModuleName(tempModule);
        this.storageManager = new StorageManager(TestFilePath.RESOURCE_FOLDER, TestFilePath.SAVE_FILE);
    }

    @Test
    void execute_success() throws InvalidCommandException, InvalidArgumentException, InvalidFileException {
        for (int i = 0; i < 5; i++) {
            Command addCommand = commandParser.parseCommand("add \"test" + i + "\" \"test" + i + "\"");
            CommandResult addResult = addCommand.execute(moduleManager);
            assertTrue(addResult.isOk());
        }

        assertEquals(5, moduleManager.getModule(tempModule).getContentManager(type).getTotalContents());

        Command exportCommand = commandParser.parseCommand("export");
        CommandResult exportResult = exportCommand.execute(moduleManager);
        String affectedModule = exportResult.getModule();
        StorageActionEnum storageAction = exportResult.getStorageAction();
        StorageTypeEnum storageType = exportResult.getStorageType();
        String deletedItemName = exportResult.getDeletedItemName();
        storageManager.execute(moduleManager, affectedModule, deletedItemName, storageAction, storageType);
        assertTrue(exportResult.isOk());
        File pdf = new File(Paths.get(TestFilePath.RESOURCE_FOLDER.toString(), tempModule + CommonFormat.PDF_FORMAT)
                .toString());
        assertTrue(pdf.exists());
        pdf.delete();
    }
}
