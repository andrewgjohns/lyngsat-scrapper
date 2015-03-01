package org.andrew.scrapper.lyngsat.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;

import org.andrew.scrapper.lyngsat.models.ServiceDefinition;
import org.andrew.scrapper.lyngsat.models.Settings;
import org.andrew.scrapper.lyngsat.utilities.ResourceHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;

/**
 * Created by Andrew on 01/03/15.
 */

public class parser
{
    static final Logger logger = Logger.getLogger(parser.class);
    private ResourceHelper resourceHelper;

    public parser()
    {

    }

    public void parse()
    {
        logger.info("Parsing using lyngsat parser");
        Properties props = getProperties();
        logger.info("Building Settings");
        Settings settings = new Settings(props);
        try
        {
            resourceHelper = new ResourceHelper(this.getClass().getCanonicalName(),
                    settings.getRegion());
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
            return;
        }

        Set<ServiceDefinition> services = getLyngSatServices(settings);
    }



    private Set<ServiceDefinition> getLyngSatServices(Settings settings)
    {
        logger.info("Getting channel page from Lyngsat");
        Document channelPage = getHtmlPage(settings.getChannelUrl());
        logger.info("Getting channel page service tables from document");
        List<Element> serviceTables = channelPage
                .select("table").first()
                .select("td[width=728]").first()
                .select("table")
                .subList(8, 11);

        logger.info("Getting sid page from Lyngsat");
        Document sidPage = getHtmlPage(settings.getSidUrl());
        logger.info("Getting sid page service table from document");
        Element sidTable = sidPage
                .select("table").first()
                .select("td[width=728]").first()
                .select("table")
                .get(8);

        Map<String, ServiceDefinition> services =
                createLyngsatServices(serviceTables.get(0));

        logger.info("Add SID to Services");
        addSidToServices(services);

        Iterator<Map.Entry<String,ServiceDefinition>> it;
        it= services.entrySet().iterator();
        logger.info("Creating Multiplexes, this could take a while...");
        //while (it.hasNext())
        while (it.hasNext())
        {
            Map.Entry<String,ServiceDefinition> entry = it.next();
            ParserService service = entry.getValue();
            if (addMultiplexToService(service))
            {
                // Create final Service
                setServiceName(service);
                // Add Tags
                setServiceTags(service);

            }
            else
            {
                it.remove();
            }
        }

        logger.info("Done creating Multiplexes");

        return new HashSet(services.values());
    }

    private Map<String, ServiceDefinition> createLyngsatServices(Element table)
    {
        logger.info("createLyngsatServices");
        Elements allRows = table.select("tr");
        List<Element> rows = allRows.subList(2,allRows.size()-1);
        Map<String, ServiceDefinition> services = new HashMap();

        for (Element row : rows)
        {
            try
            {
                ServiceDefinition service = buildService(row);
                if (null != service)
                {
                    upsertServices(services, service);
                }
            }
            catch (Exception e)
            {
                logger.error("createLyngsatServices - Unable to build " +
                        "service.", e);
            }
        }

        return services;
    }

    private ServiceDefinition buildService(Element row)
    {
        logger.info("Building Service Definition");
        String name = row.select("td").get(3).text();
        String channel = row.select("td").get(0).text();
        if ((!validChannelNumber(channel)) || (excludeChannel(name)))
        {
            return null;
        }

        Element mutexElement = row.select("td").get(3).select("a").first();
        if (null == mutexElement)
        {
            return null;
        }

        String logoUrl = "";
        String mutexUrl = mutexElement.attr("href");
        channel = resourceHelper.channelMapValue(name, channel);
        String aditionalInfo = row.select("td").get(5).text();
        Element logoElement = row.select("td").get(2).select("img").first();
        boolean isHD = aditionalInfo.contains("HD");
        boolean isVideoguard = aditionalInfo.contains("Videoguard");

        if (null != logoElement)
        {
            logoUrl = logoElement.attr("src");
        }
        return ServiceDefinition.createInstance(
                channel, name, isHD, isVideoguard,
                logoUrl, mutexUrl);
    }

    private void setServiceName(ParserService service)
    {
        service.setName(renameChannel(service.getName()));
    }

    private void setServiceTags(ParserService service)
    {
        EnumSet<Tags.Tag> tags =
                Tags.ServiceTags(
                        Integer.parseInt(service.getNumber()),
                        service.getIsHD(), true);
        service.setTags(Tags.values(tags));
    }



    private void upsertServices(Map<String, ServiceDefinition> services,
                                ServiceDefinition service)
    {
        logger.debug("upsertServices");
        if (services.containsKey(service.getName()))
        {
            // contains service
            // is new SERVICE HD

            if (service.getIsHD())
            {
                if (service.getVideoguard())
                {
                    if (! resourceHelper.getUseHDForVideoGuard())
                    {
                        return;
                    }
                }

                ServiceDefinition existingService =  services.get(service.getName());
                String channel =
                        getLowestNumber(existingService.getNumber(),
                                service.getNumber());
                service.setNumber(channel);
            }
            else
            {
                return;
            }
        }
        services.put(service.getName(), service);
    }

    private Properties getProperties()
    {
        InputStream input = null;
        Properties properties = new Properties();

        try
        {
            input = new FileInputStream("scrapper.properties");
            properties.load(input);
            return properties;
        }
        catch (FileNotFoundException ex)
        {
            logger.error("Could not find Properties", ex);
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            logger.error("Could not load Properties", ex);
            ex.printStackTrace();
        }
        finally
        {
            if (null != input)
            {
                try
                {
                    input.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private Document getHtmlPage(String url)
    {
        Document doc;
        try
        {
            doc = Jsoup.connect(url).get();
            return doc;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
