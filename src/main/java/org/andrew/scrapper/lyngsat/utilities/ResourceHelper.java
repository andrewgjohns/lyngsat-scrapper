package org.andrew.scrapper.lyngsat.utilities;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 09/02/14.
 */
public class ResourceHelper
{
    static final Logger logger = Logger.getLogger(ResourceHelper.class);

    private final String[] _excludeExpressions;
    private final String[] _channelParseExpressions;
    private final Map<String,String> _channelRemap;

    public ResourceHelper(String parserClass, String region) throws
            IOException,
            URISyntaxException
    {
        String excludeResource =
                String.format("/%s_%s_ExcludeChannel.txt", parserClass, region);
        String remapResource =
                String.format("/%s_%s_RemapChannel.txt", parserClass, region);
        String parseResource =
                String.format("/%s_%s_ParseChannel.txt", parserClass, region);

        String[] channelMapLines = resourceToArray(remapResource);
        this._channelRemap = getChannelRemapResourceMap(channelMapLines);

        this._excludeExpressions = resourceToArray(excludeResource);
        this._channelParseExpressions = resourceToArray(parseResource);
    }

    public String[] getExcludeExpressions()
    {
        return _excludeExpressions;
    }

    public String[] getChannelParseExpressions()
    {
        return  _channelParseExpressions;
    }

    private Map<String, String> getChannelRemapResourceMap(String[] lines)
    {
        Map<String, String> channels = new HashMap<String, String>();

        for(String channel : lines)
        {
            String[] c = channel.split("\\|");
            channels.put(c[1],c[0]);
        } return channels;
    }

    public String channelMapValue(String key, String defaultValue)
    {
        if (_channelRemap.containsKey(key))
        {
            return _channelRemap.get(key);
        }
        return defaultValue;
    }

    private String[] resourceToArray(String resourceName)
            throws URISyntaxException, IOException
    {
        logger.info("Loading Resource: " + resourceName);

        URL location = this.getClass().getResource(resourceName);
        Path fullPath = Paths.get(location.toURI());

        logger.info("File path: " + fullPath);

        String file = ReadFileToString.readFile(fullPath.toString(),
                Charset.forName("UTF-8"));

        return file.split(System.getProperty("line.separator"));
    }
}
