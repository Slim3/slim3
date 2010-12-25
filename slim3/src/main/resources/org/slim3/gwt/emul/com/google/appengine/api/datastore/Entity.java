package com.google.appengine.api.datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class Entity
    implements Cloneable, Serializable
{
    static final class UnindexedValue
        implements Serializable
    {

        private final Object value;

        UnindexedValue(Object value)
        {
            this.value = value;
        }

        @Override
		public boolean equals(Object that)
        {
            if(that instanceof UnindexedValue)
            {
                UnindexedValue uv = (UnindexedValue)that;
                return value != null ? value.equals(uv.value) : uv.value == null;
            } else
            {
                return false;
            }
        }

        public Object getValue()
        {
            return value;
        }

        @Override
		public int hashCode()
        {
            return value != null ? value.hashCode() : 0;
        }

        @Override
		public String toString()
        {
            return (new StringBuilder()).append(value).append(" (unindexed)").toString();
        }
    }


    static final long serialVersionUID = 1570217081L;

    public static final String KEY_RESERVED_PROPERTY = "__key__";

    static Object unwrapValue(Object obj)
    {
        if(obj instanceof UnindexedValue)
            return ((UnindexedValue)obj).getValue();
        else
            return obj;
    }

    private final Key key;

    @SuppressWarnings("rawtypes")
	private final Map propertyMap;

    public Entity(Key key)
    {
        this.key = key;
        propertyMap = new HashMap<String, Object>();
    }

    public Entity(String kind)
    {
        this(kind, (Key)null);
    }

    public Entity(String kind, Key parent)
    {
        this(new Key(kind, parent));
    }

    public Entity(String kind, String keyName)
    {
        this(KeyFactory.createKey(kind, keyName));
    }

    public Entity(String kind, String keyName, Key parent)
    {
        this(parent != null ? parent.getChild(kind, keyName) : KeyFactory.createKey(kind, keyName));
    }

	public Entity clone()
    {
        Entity entity = new Entity(key);
        entity.setPropertiesFrom(this);
        return entity;
    }

    private Object cloneIfMutable(Object obj)
    {
        if(obj instanceof Date)
            return ((Date)obj).clone();
        else
            return obj;
    }

    @Override
	public boolean equals(Object object)
    {
        if(object instanceof Entity)
        {
            Entity otherEntity = (Entity)object;
            return key.equals(otherEntity.key);
        } else
        {
            return false;
        }
    }

    public String getAppId()
    {
        return key.getAppId();
    }

    AppIdNamespace getAppIdNamespace()
    {
        return key.getAppIdNamespace();
    }

    public Key getKey()
    {
        return key;
    }

    public String getKind()
    {
        return key.getKind();
    }

    public String getNamespace()
    {
        return key.getNamespace();
    }

    public Key getParent()
    {
        return key.getParent();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getProperties()
    {
        Map properties = new HashMap(propertyMap.size());
        java.util.Map.Entry entry;
        for(Iterator i$ = propertyMap.entrySet().iterator(); i$.hasNext(); properties.put(entry.getKey(), unwrapValue(entry.getValue())))
            entry = (java.util.Map.Entry)i$.next();

        return Collections.unmodifiableMap(properties);
    }

    public Object getProperty(String propertyName)
    {
        return unwrapValue(propertyMap.get(propertyName));
    }

    @SuppressWarnings("rawtypes")
	Map getPropertyMap()
    {
        return propertyMap;
    }

    @Override
	public int hashCode()
    {
        return key.hashCode();
    }

    public boolean hasProperty(String propertyName)
    {
        return propertyMap.containsKey(propertyName);
    }

    public boolean isUnindexedProperty(String propertyName)
    {
        Object value = propertyMap.get(propertyName);
        return (value instanceof UnindexedValue) || (value instanceof Text) || (value instanceof Blob);
    }

    public void removeProperty(String propertyName)
    {
        propertyMap.remove(propertyName);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void setPropertiesFrom(Entity src)
    {
        String name;
        Object valueToAdd;
        for(Iterator n = src.propertyMap.entrySet().iterator(); n.hasNext(); propertyMap.put(name, valueToAdd))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)n.next();
            name = (String)entry.getKey();
            Object entryValue = entry.getValue();
            boolean indexed = entryValue instanceof UnindexedValue;
            valueToAdd = unwrapValue(entryValue);
            if(valueToAdd instanceof Collection)
            {
                Collection srcColl = (Collection)valueToAdd;
                Collection destColl = new ArrayList(srcColl.size());
                valueToAdd = destColl;
                Object element;
                for(Iterator i = srcColl.iterator(); i.hasNext(); destColl.add(cloneIfMutable(element)))
                    element = i.next();

            } else
            {
                valueToAdd = cloneIfMutable(valueToAdd);
            }
            if(indexed)
                valueToAdd = new UnindexedValue(valueToAdd);
        }

    }
    @SuppressWarnings("unchecked")
	public void setProperty(String propertyName, Object value)
    {
        // DataTypeUtils.checkSupportedValue(propertyName, value); TODO Delete or try to emulate
        propertyMap.put(propertyName, value);
    }
    @SuppressWarnings("unchecked")
	public void setUnindexedProperty(String propertyName, Object value)
    {
        // DataTypeUtils.checkSupportedValue(propertyName, value); TODO Delete or try to emulate
        propertyMap.put(propertyName, new UnindexedValue(value));
    }
    @SuppressWarnings("rawtypes")
	@Override
	public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append((new StringBuilder()).append("<Entity [").append(key).append("]:\n").toString());
        java.util.Map.Entry entry;
        for(Iterator i$ = propertyMap.entrySet().iterator(); i$.hasNext(); buffer.append((new StringBuilder()).append("\t").append((String)entry.getKey()).append(" = ").append(entry.getValue()).append("\n").toString()))
            entry = (java.util.Map.Entry)i$.next();

        buffer.append(">\n");
        return buffer.toString();
    }
}