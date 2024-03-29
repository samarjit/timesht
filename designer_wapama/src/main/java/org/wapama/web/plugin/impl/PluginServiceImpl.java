/***************************************
 * Copyright (c) Intalio, Inc 2010
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
****************************************/
package org.wapama.web.plugin.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleReference;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wapama.web.plugin.IDiagramPlugin;
import org.wapama.web.plugin.IDiagramPluginFactory;
import org.wapama.web.plugin.IDiagramPluginService;


/**
 * A service to manage plugins in the platform.
 * 
 * @author Antoine Toulme
 */
public class PluginServiceImpl implements IDiagramPluginService {
    
    private static PluginServiceImpl _instance = null;
    
    /**
     * @param context the context needed for initialization
     * @return the singleton of PluginServiceImpl
     */
    public static IDiagramPluginService getInstance(
                                    ServletContext context) {
        if (_instance == null) {
            _instance = new PluginServiceImpl(context);
        }
        return _instance;
    }
    
    /**
     * The default local plugins, available to the webapp 
     * so that the default profile can provision its plugins. 
     * Consumers through OSGi should use the service tracker
     * to get the plugins they need.
     */
    private static Map<String, IDiagramPlugin> LOCAL = null;
    
    /**
     * Initialize the local plugins registry
     * @param context the servlet context necessary to grab
     * the files inside the servlet.
     * @return the set of local plugins organized by name
     */
    public static Map<String, IDiagramPlugin> 
        getLocalPluginsRegistry(ServletContext context) {
        if (LOCAL == null) {
            LOCAL = initializeLocalPlugins(context);
        }
        return LOCAL;
    }

    private static Logger _logger = LoggerFactory.getLogger(PluginServiceImpl.class);

    private static Map<String, IDiagramPlugin> initializeLocalPlugins(ServletContext context) {
        Map<String, IDiagramPlugin> local = new HashMap<String, IDiagramPlugin>();
        //we read the plugins.xml file and make sense of it.
        FileInputStream fileStream = null;
        try {
            try {
                fileStream = new FileInputStream(new StringBuilder(context.getRealPath("/")).append("/").
                        append("js").append("/").append("Plugins").append("/").
                        append("plugins.xml").toString());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(fileStream);
            while(reader.hasNext()) {
                if (reader.next() == XMLStreamReader.START_ELEMENT) {
                    if ("plugin".equals(reader.getLocalName())) {
                        String source = null, name = null;
                        boolean core = false;
                        for (int i = 0 ; i < reader.getAttributeCount() ; i++) {
                            if ("source".equals(reader.getAttributeLocalName(i))) {
                                source = reader.getAttributeValue(i);
                            } else if ("name".equals(reader.getAttributeLocalName(i))) {
                                name = reader.getAttributeValue(i);
                            } else if ("core".equals(reader.getAttributeLocalName(i))) {
                                core = Boolean.parseBoolean(reader.getAttributeValue(i));
                            }
                        }
                        Map<String, Object> props = new HashMap<String, Object>();
                        while(reader.hasNext()) {
                            int ev = reader.next();
                            if (ev == XMLStreamReader.START_ELEMENT) {
                                if ("property".equals(reader.getLocalName())) {
                                    String key = null, value = null;
                                    for (int i = 0 ; i < reader.getAttributeCount() ; i++) {
                                        if ("name".equals(reader.getAttributeLocalName(i))) {
                                            key = reader.getAttributeValue(i);
                                        } else if ("value".equals(reader.getAttributeLocalName(i))) {
                                            value = reader.getAttributeValue(i);
                                        }
                                    }
                                    if(key != null & value != null)
                                        props.put(key, value);
                                }
                            } else if (ev == XMLStreamReader.END_ELEMENT) {
                                if ("plugin".equals(reader.getLocalName())) {
                                    break;
                                }
                            }
                        }
                        local.put(name, new LocalPluginImpl(name, source, context, core, props));
                    }
                }
            }
        } catch (XMLStreamException e) {
            _logger.error(e.getMessage(), e);
            throw new RuntimeException(e); // stop initialization
        } finally {
            if (fileStream != null) { try { fileStream.close(); } catch(IOException e) {}};
        }
        return local;
    }
    
    private Map<String, IDiagramPlugin> _registry = new HashMap<String, IDiagramPlugin>();
    private Set<IDiagramPluginFactory> _factories = new HashSet<IDiagramPluginFactory>();

    /**
     * Private constructor to make sure we respect the singleton
     * pattern.
     * @param context the servlet context
     */
    @SuppressWarnings("rawtypes")
	private PluginServiceImpl(ServletContext context) {
        _registry.putAll(getLocalPluginsRegistry(context));
        // if we are in the OSGi world:
        if (getClass().getClassLoader() instanceof BundleReference) {
            final BundleContext bundleContext = ((BundleReference) getClass().getClassLoader()).getBundle().getBundleContext();
            ServiceReference[] sRefs = null;
            try {
                sRefs = bundleContext.getServiceReferences(IDiagramPluginFactory.class.getName(), null);
            } catch (InvalidSyntaxException e) {
            }
            if (sRefs != null) {
                for (ServiceReference sRef : sRefs) {
                    IDiagramPluginFactory service = (IDiagramPluginFactory) bundleContext.getService(sRef);
                    _factories.add(service);
                }
            } 
            ServiceTrackerCustomizer cust = new ServiceTrackerCustomizer() {

                public void removedService(ServiceReference reference, Object service) {
                }

                public void modifiedService(ServiceReference reference, Object service) {
                }

                public Object addingService(ServiceReference reference) {
                    IDiagramPluginFactory service = (IDiagramPluginFactory) bundleContext.getService(reference);
                    _factories.add(service);
                    return service;
                }
            };
            ServiceTracker tracker = new ServiceTracker(bundleContext,
                    IDiagramPluginFactory.class.getName(), cust);
            tracker.open();
            //make the service available to consumers as well.
            bundleContext.registerService(IDiagramPluginService.class.getName(), this, 
                    new Hashtable());
        }
    }
    
    private Map<String, IDiagramPlugin> assemblePlugins(HttpServletRequest request) {
        Map<String, IDiagramPlugin> plugins = new HashMap<String, IDiagramPlugin>(_registry);
        for (IDiagramPluginFactory factory : _factories) {
            for (IDiagramPlugin  p : factory.getPlugins(request)) {
                plugins.put(p.getName(), p);
            }
        }
        return plugins;
    }

    
    public Collection<IDiagramPlugin> getRegisteredPlugins(HttpServletRequest request) {
        return assemblePlugins(request).values();
    }
    
    public IDiagramPlugin findPlugin(HttpServletRequest request, String name) {
        return assemblePlugins(request).get(name);
    }
}
