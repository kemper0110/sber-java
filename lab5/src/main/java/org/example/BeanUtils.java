package org.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class BeanUtils {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */
    public static void assign(Object to, Object from) {
        final Pattern getterPattern = Pattern.compile("^(get|is)(.+)$"),
                setterPattern = Pattern.compile("^set(.+)$");


        final var setters = new HashMap<String, Method>();
        for(final var setter : to.getClass().getDeclaredMethods()) {
            final var setterMatcher = setterPattern.matcher(setter.getName());
            if(!setterMatcher.find()) continue;

            final var name = setterMatcher.group(1);
            setters.put(name, setter);
            System.out.println("setter: " + name);
        }

        for (final var getter : from.getClass().getDeclaredMethods()) {
            final var getterMatcher = getterPattern.matcher(getter.getName());
            if(!getterMatcher.find()) continue;

            final var name = getterMatcher.group(2);
            System.out.println("getter: " + name);

            final var setter = setters.get(name);
            if(setter == null) continue;

            final var param = setter.getParameters()[0].getType();
            final var returnType = getter.getReturnType();
            if(!param.isAssignableFrom(returnType)) continue;

            try {
                setter.invoke(to, getter.invoke(from));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
