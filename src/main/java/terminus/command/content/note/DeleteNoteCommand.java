package terminus.command.content.note;

import java.io.IOException;
import terminus.command.CommandResult;
import terminus.command.content.DeleteCommand;
import terminus.exception.InvalidArgumentException;
import terminus.module.ModuleManager;
import terminus.storage.ModuleStorage;
import terminus.ui.Ui;

public class DeleteNoteCommand extends DeleteCommand {

    /**
     * Creates a DeleteCommand object with referenced to the provided class type.
     *
     * @param type Content object type.
     */
    public DeleteNoteCommand(Class type) {
        super(type);
    }

    @Override
    public CommandResult execute(Ui ui, ModuleManager moduleManager) throws InvalidArgumentException, IOException {
        CommandResult result = super.execute(ui, moduleManager);
        // Update file accordingly
        ModuleStorage moduleStorage = ModuleStorage.getInstance();
        moduleStorage.saveNotesFromModule(moduleManager, getModuleName());
        return result;
    }
}
