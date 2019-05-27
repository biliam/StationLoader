package net.modificationstation.classloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Some reflection hacks for more control over Minecraft
 * 
 * @author mine_diver
 *
 */
public class ReflectionHelper
{
    @SuppressWarnings("serial")
	public static class UnableToFindMethodException extends RuntimeException
    {
        @SuppressWarnings("unused")
		private String[] methodNames;

        public UnableToFindMethodException(String[] methodNames, Exception failed)
        {
            super(failed);
            this.methodNames = methodNames;
        }

    }

    @SuppressWarnings("serial")
	public static class UnableToFindClassException extends RuntimeException
    {
        @SuppressWarnings("unused")
		private String[] classNames;

        public UnableToFindClassException(String[] classNames, Exception err)
        {
            super(err);
            this.classNames = classNames;
        }

    }

    @SuppressWarnings("serial")
	public static class UnableToAccessFieldException extends RuntimeException
    {

        @SuppressWarnings("unused")
		private String[] fieldNameList;

        public UnableToAccessFieldException(String[] fieldNames, Exception e)
        {
            super(e);
            this.fieldNameList = fieldNames;
        }
    }

    @SuppressWarnings("serial")
	public static class UnableToFindFieldException extends RuntimeException
    {
        @SuppressWarnings("unused")
		private String[] fieldNameList;
        public UnableToFindFieldException(String[] fieldNameList, Exception e)
        {
            super(e);
            this.fieldNameList = fieldNameList;
        }
    }
    
    /**
     * Returns a field that matches a name in given list
     * 
     * @param clazz
     * @param fieldNames
     * @return
     */
    public static Field findField(Class<?> clazz, String... fieldNames)
    {
        Exception failed = null;
        for (String fieldName : fieldNames)
        {
            try
            {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindFieldException(fieldNames, failed);
    }
    
    /**
     * Returns the value of a private field by its index
     * 
     * @param classToAccess
     * @param instance
     * @param fieldIndex
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class <? super E > classToAccess, E instance, int fieldIndex)
    {
        try
        {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            return (T) f.get(instance);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(new String[0], e);
        }
    }
    
    /**
     * Returns the value of a private field by its name(s)
     * 
     * @param classToAccess
     * @param instance
     * @param fieldNames
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class <? super E > classToAccess, E instance, String... fieldNames)
    {
        try
        {
            return (T) findField(classToAccess, fieldNames).get(instance);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }
    
    /**
     * Sets the value of a private field by its index
     * 
     * @param classToAccess
     * @param instance
     * @param value
     * @param fieldIndex
     */
    public static <T, E> void setPrivateValue(Class <? super T > classToAccess, T instance, E value, int fieldIndex)
    {
        try
        {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            f.set(instance, value);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(new String[0] , e);
        }
    }
    
    /**
     * Sets the value of a private field by its name(s)
     * 
     * @param classToAccess
     * @param instance
     * @param value
     * @param fieldNames
     */
    public static <T, E> void setPrivateValue(Class <? super T > classToAccess, T instance, E value, String... fieldNames)
    {
        try
        {
            findField(classToAccess, fieldNames).set(instance, value);
        }
        catch (Exception e)
        {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }
    
    /**
     * Searches for a class by a list of names and loads with given classloader
     * 
     * @param loader
     * @param classNames
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class<? super Object> getClass(ClassLoader loader, String... classNames)
    {
        Exception err = null;
        for (String className : classNames)
        {
            try
            {
                return (Class<? super Object>) Class.forName(className, false, loader);
            }
            catch (Exception e)
            {
                err = e;
            }
        }

        throw new UnableToFindClassException(classNames, err);
    }
    
    /**
     * Searches for a method by a list of names and parameter types
     * 
     * @param clazz
     * @param instance
     * @param methodNames
     * @param methodTypes
     * @return
     */
    public static <E> Method findMethod(Class<? super E> clazz, E instance, String[] methodNames, Class<?>... methodTypes)
    {
        Exception failed = null;
        for (String methodName : methodNames)
        {
            try
            {
                Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                m.setAccessible(true);
                return m;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindMethodException(methodNames, failed);
    }
    
    /**
     * Returns a list of methods that have given annotation and parameter types
     * 
     * @param clazz
     * @param annotation
     * @param parameterTypes
     * @return
     */
    public static Method[] getMethodsAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotation, final Class<?>...parameterTypes) {
        List<Method> methods = new ArrayList<Method>();
        for (Method m : clazz.getDeclaredMethods()) {
            for (Annotation a : m.getAnnotations()) {
                if (a.annotationType().equals(annotation) && m.getParameterTypes().length == parameterTypes.length && Arrays.asList(m.getParameterTypes()).equals(Arrays.asList(parameterTypes))){
                    methods.add(m);
                }
            }
        }
        return (Method[])methods.toArray(new Method[methods.size()]);
    }
    
    /**
     * Returns an array of fields that have given annotation
     * 
     * @param clazz
     * @param annotation
     * @return
     */
    public static Field[] getFieldsAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotation) {
        List<Field> fields = new ArrayList<Field>();
        for (Field f : clazz.getDeclaredFields()) {
            for (Annotation a : f.getAnnotations()) {
                if (a.annotationType().equals(annotation)){
                    fields.add(f);
                }
            }
        }
        return (Field[])fields.toArray(new Field[fields.size()]);
    }
    
    /**
     * Sets the value of a final field by its index
     * 
     * @param classToAccess
     * @param instance
     * @param value
     * @param fieldIndex
     */
    public static <T, E> void setFinalValue(Class <? super T > classToAccess, T instance, E value, int fieldIndex) {
    	try {
    		Field f = classToAccess.getDeclaredFields()[fieldIndex];
    		publicField(f);
    		f.set(instance, value);
    	} catch (Exception e) {
    		throw new UnableToAccessFieldException(new String[0], e);
    	}
    }
    
    /**
     * Sets the value of a final field by its name(s)
     * 
     * @param classToAccess
     * @param instance
     * @param value
     * @param fieldNames
     */
    public static <T, E> void setFinalValue(Class <? super T > classToAccess, T instance, E value, String... fieldNames) {
    	try {
    		Field f = findField(classToAccess, fieldNames);
    		publicField(f);
    		f.set(instance, value);
    	} catch (Exception e) {
    		throw new UnableToAccessFieldException(new String[0], e);
    	}
    }
    
    /**
     * Makes a field public and removes "final" modifier
     * 
     * @param field
     */
    public static void publicField(Field field) {
        try {
            field.setAccessible(true);
            modifiersField.set(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    private static final Field modifiersField;
    
    static {
        Field field = null;
        try {
            field = Field.class.getDeclaredField("modifiers");
            field.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        modifiersField = field;
    }
}
