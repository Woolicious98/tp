package terminus.command.link;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import terminus.command.Command;
import terminus.command.CommandResult;
import terminus.common.CommonUtils;
import terminus.content.Link;
import terminus.exception.InvalidArgumentException;
import terminus.exception.InvalidCommandException;
import terminus.module.ModuleManager;
import terminus.module.NusModule;
import terminus.parser.LinkCommandParser;
import terminus.ui.Ui;

public class AddLinkCommandTest {

    private LinkCommandParser linkCommandParser;
    private ModuleManager moduleManager;
    private Ui ui;

    private String tempModule = "test";

    Class<Link> type = Link.class;

    @BeforeEach
    void setUp() {
        this.moduleManager = new ModuleManager();
        moduleManager.setModule(tempModule);
        this.linkCommandParser = LinkCommandParser.getInstance();
        this.linkCommandParser.setModuleName(tempModule);
        this.ui = new Ui();
    }

    @Test
    void parseArguments_addLinkCommand_success() {
        String addLinkInput = "add \"test\" \"Thursday\" \"00:00\" \"https://zoom.us/test\"";
        ArrayList<String> parsedArguments = CommonUtils.findArguments(addLinkInput);
        assertEquals("test", parsedArguments.get(0));
        assertEquals("Thursday", parsedArguments.get(1));
        assertEquals("00:00", parsedArguments.get(2));
        assertEquals("https://zoom.us/test", parsedArguments.get(3));
    }

    @Test
    void execute_addLinkCommand_success() throws InvalidCommandException, InvalidArgumentException {
        Command addLinkCommand = linkCommandParser.parseCommand("add \"test\" \"Monday\" \"00:00\" \"https://zoom.us/test\"");
        CommandResult addResult = addLinkCommand.execute(ui, moduleManager);
        assertTrue(addResult.isOk());
        assertEquals(1, moduleManager.getModule(tempModule).getContentManager(type).getTotalContents());
        assertTrue(moduleManager.getModule(tempModule).getContentManager(type).getContentData(1).contains("test"));
        assertTrue(moduleManager.getModule(tempModule).getContentManager(type).getContentData(1).contains("Monday"));
        assertTrue(moduleManager.getModule(tempModule).getContentManager(type).getContentData(1).contains("00:00"));
        assertTrue(moduleManager.getModule(tempModule).getContentManager(type).getContentData(1).contains("https://zoom.us/test"));

        for (int i = 0; i < 5; i++) {
            addLinkCommand = linkCommandParser.parseCommand(
                    "add \"test\" \"Saturday\" \"00:00\" \"https://zoom.us/test\"");
            addResult = addLinkCommand.execute(ui, moduleManager);
            assertTrue(addResult.isOk());
        }
        assertEquals(6, moduleManager.getModule(tempModule).getContentManager(type).getTotalContents());
    }
}