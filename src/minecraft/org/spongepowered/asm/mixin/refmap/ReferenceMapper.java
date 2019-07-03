/*
 * This file is part of Mixin, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.asm.mixin.refmap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * Stores runtime information allowing field, method and type references which
 * cannot be hard remapped by the reobfuscation process to be remapped in a
 * "soft" manner at runtime. Refmaps are generated by the <em>Annotation
 * Processor</em> at compile time and must be bundled with an obfuscated binary
 * to allow obfuscated references in injectors and other String-defined targets
 * to be remapped to the target obfsucation environment as appropriate. If the
 * refmap is absent the environment is assumed to be deobfuscated (eg. dev-time)
 * and injections and other transformations will fail if this is not the case. 
 */
public final class ReferenceMapper implements IReferenceMapper, Serializable {
    
    private static final long serialVersionUID = 2L;

    /**
     * Resource to attempt to load if no source is specified explicitly 
     */
    public static final String DEFAULT_RESOURCE = "mixin.refmap.json";
    
    /**
     * Passthrough mapper, used as failover 
     */
    public static final ReferenceMapper DEFAULT_MAPPER = new ReferenceMapper(true, "invalid");

    /**
     * "Default" mappings. The set of mappings to use as "default" is specified
     * by the AP. Each entry is keyed by the owning mixin, with the value map
     * containing the actual remappings for each owner
     */
    private final Map<String, Map<String, String>> mappings = Maps.newHashMap();
    
    /**
     * All mapping sets, keyed by environment type, eg. "notch", "searge". The
     * format of each map within this map is the same as for {@link #mappings}
     */
    private final Map<String, Map<String, Map<String, String>>> data = Maps.newHashMap();
    
    /**
     * True if this refmap cannot be written. Only true for the
     * {@link #DEFAULT_MAPPER}
     */
    private final transient boolean readOnly;
    
    /**
     * Current remapping context, used as the key into {@link data}
     */
    private transient String context = null;
    
    /**
     * Resource name this refmap was loaded from (if available) 
     */
    private transient String resource;
    
    /**
     * Create an empty refmap
     */
    public ReferenceMapper() {
        this(false, ReferenceMapper.DEFAULT_RESOURCE);
    }
    
    /**
     * Create a readonly refmap, only used by {@link #DEFAULT_MAPPER}
     * 
     * @param readOnly flag to indicate read-only
     */
    private ReferenceMapper(boolean readOnly, String resource) {
        this.readOnly = readOnly;
        this.resource = resource;
    }
    
    /* (non-Javadoc)
     * @see org.spongepowered.asm.mixin.refmap.IReferenceMapper#isDefault()
     */
    @Override
    public boolean isDefault() {
        return this.readOnly;
    }
    
    private void setResourceName(String resource) {
        if (!this.readOnly) {
            this.resource = resource != null ? resource : "<unknown resource>";
        }
    }
    
    /* (non-Javadoc)
     * @see org.spongepowered.asm.mixin.refmap.IReferenceMapper
     *      #getResourceName()
     */
    @Override
    public String getResourceName() {
        return this.resource;
    }

    /* (non-Javadoc)
     * @see org.spongepowered.asm.mixin.refmap.IReferenceMapper#getStatus()
     */
    @Override
    public String getStatus() {
        return this.isDefault() ? "No refMap loaded." : "Using refmap " + this.getResourceName();
    }

    /* (non-Javadoc)
     * @see org.spongepowered.asm.mixin.refmap.IReferenceMapper#getContext()
     */
    @Override
    public String getContext() {
        return this.context;
    }
    
    /* (non-Javadoc)
     * @see org.spongepowered.asm.mixin.refmap.IReferenceMapper#setContext(
     *      java.lang.String)
     */
    @Override
    public void setContext(String context) {
        this.context = context;
    }
    
    /* (non-Javadoc)
     * @see org.spongepowered.asm.mixin.refmap.IReferenceMapper#remap(
     *      java.lang.String, java.lang.String)
     */
    @Override
    public String remap(String className, String reference) {
        return this.remapWithContext(this.context, className, reference);
    }
    
    /* (non-Javadoc)
     * @see org.spongepowered.asm.mixin.refmap.IReferenceMapper
     *      #remapWithContext(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String remapWithContext(String context, String className, String reference) {
        Map<String, Map<String, String>> mappings = this.mappings;
        if (context != null) {
            mappings = this.data.get(context);
            if (mappings == null) {
                mappings = this.mappings;
            }
        }
        return this.remap(mappings, className, reference);
    }
    
    /**
     * Remap the things
     */
    private String remap(Map<String, Map<String, String>> mappings, String className, String reference) {
        if (className == null) {
            for (Map<String, String> mapping : mappings.values()) {
                if (mapping.containsKey(reference)) {
                    return mapping.get(reference);
                }
            }
        }
        
        Map<String, String> classMappings = mappings.get(className);
        if (classMappings == null) {
            return reference;
        }
        String remappedReference = classMappings.get(reference);
        return remappedReference != null ? remappedReference : reference;
    }
    
    /**
     * Add a mapping to this refmap
     * 
     * @param context Obfuscation context, can be null
     * @param className Class which owns this mapping, cannot be null
     * @param reference Reference to remap, cannot be null
     * @param newReference Remapped value, cannot be null
     * @return replaced value, per the contract of {@link Map#put}
     */
    public String addMapping(String context, String className, String reference, String newReference) {
        if (this.readOnly || reference == null || newReference == null || reference.equals(newReference)) {
            return null;
        }
        Map<String, Map<String, String>> mappings = this.mappings;
        if (context != null) {
            mappings = this.data.get(context);
            if (mappings == null) {
                mappings = Maps.newHashMap();
                this.data.put(context, mappings);
            }
        }
        Map<String, String> classMappings = mappings.get(className);
        if (classMappings == null) {
            classMappings = new HashMap<String, String>();
            mappings.put(className, classMappings);
        }
        return classMappings.put(reference, newReference);
    }
    
    /**
     * Write this refmap out to the specified writer
     * 
     * @param writer Writer to write to
     */
    public void write(Appendable writer) {
        new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
    }
    
    /**
     * Read a new refmap from the specified resource
     * 
     * @param resourcePath Resource to read from
     * @return new refmap or {@link #DEFAULT_MAPPER} if reading fails
     */
    public static ReferenceMapper read(String resourcePath) {
        Logger logger = LogManager.getLogger("mixin");
        Reader reader = null;
        try {
            IMixinService service = MixinService.getService();
            InputStream resource = service.getResourceAsStream(resourcePath);
            if (resource != null) {
                reader = new InputStreamReader(resource);
                ReferenceMapper mapper = ReferenceMapper.readJson(reader);
                mapper.setResourceName(resourcePath);
                return mapper;
            }
        } catch (JsonParseException ex) {
            logger.error("Invalid REFMAP JSON in " + resourcePath + ": " + ex.getClass().getName() + " " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("Failed reading REFMAP JSON from " + resourcePath + ": " + ex.getClass().getName() + " " + ex.getMessage());
        } finally {
            IOUtils.closeQuietly(reader);
        }
        
        return ReferenceMapper.DEFAULT_MAPPER;
    }
    
    /**
     * Read a new refmap instance from the specified reader 
     * 
     * @param reader Reader to read from
     * @param name Name of the resource being read from
     * @return new refmap
     */
    public static ReferenceMapper read(Reader reader, String name) {
        try {
            ReferenceMapper mapper = ReferenceMapper.readJson(reader);
            mapper.setResourceName(name);
            return mapper;
        } catch (Exception ex) {
            return ReferenceMapper.DEFAULT_MAPPER;
        }
    }

    private static ReferenceMapper readJson(Reader reader) {
        return new Gson().fromJson(reader, ReferenceMapper.class);
    }
    
}