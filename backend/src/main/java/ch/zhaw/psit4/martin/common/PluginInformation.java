package ch.zhaw.psit4.martin.common;

import ch.zhaw.psit4.martin.common.FunctionInformation;
import ch.zhaw.psit4.martin.pluginlib.db.function.Function;
import java.util.Set;

/**
 * 
 * Contains all the information of a plugin that is displayed in the MArtIn
 * frontend. Has the information about the plugin name, its description and all
 * of the available functions.
 * 
 * @version 0.0.1-SNAPSHOT
 */
public class PluginInformation {

    private String name;
    private String description;
    private Set<FunctionInformation> functionInformation;

    /**
     * @param name
     *            of the plugin
     * @param description
     *            of the plugin
     * @param all
     *            functions that the plugin offers
     */
    public PluginInformation(String name, String description,
            Set<Function> functions) {
        this.name = name;
        this.description = description;
        for (Function function : functions) {
            functionInformation
                    .add(new FunctionInformation(function.getName()));
        }
    }

}
