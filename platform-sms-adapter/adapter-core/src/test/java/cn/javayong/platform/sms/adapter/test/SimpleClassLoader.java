package cn.javayong.platform.sms.adapter.test;

import java.io.IOException;
import java.io.InputStream;

public class SimpleClassLoader
extends ClassLoader
{
    private static final String CLASS_FILE_EXTENSION = ".class";
 
    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve)
    throws ClassNotFoundException 
    {
        Class<?> clazz = findLoadedClass(name);
        if(clazz != null)
            return clazz;
 
        String clazzPackageName = parsePackageName(name);
        String clazzSimpleName = parseSimpleName(name);
         
        System.out.println("Loading '" + clazzPackageName + clazzSimpleName + "'");
         
        if(clazzPackageName.startsWith("java"))
            return this.getParent().loadClass(name);
         
        String resourceName = name;
        resourceName = resourceName.replaceAll("\\.", "/");
        InputStream classInStream = this.getParent().getResourceAsStream(resourceName + CLASS_FILE_EXTENSION);
        if(classInStream == null)
            throw new ClassNotFoundException("Unable to load class '" + name + "' from '" + resourceName + "'.");
         
        byte[] buffy = new byte[1024];
        byte[] classBytes = new byte[buffy.length];
         
        int totalBytesRead = 0;
        try
        {
            for(int bytesRead = 0 ; (bytesRead = classInStream.read(buffy)) > 0 ; )
            {
                // grow the buffer as needed, always 1K at a time so we're not constantly
                // reallocating on small reads
                if(totalBytesRead + bytesRead > classBytes.length)
                {
                    byte temp[] = new byte[totalBytesRead + buffy.length];
                    System.arraycopy(classBytes, 0, temp, 0, totalBytesRead);
                    classBytes = temp;
                }
                System.arraycopy(buffy, 0, classBytes, totalBytesRead, bytesRead);
                totalBytesRead += bytesRead;
            }
        } 
        catch (IOException e)
        {
            throw new ClassNotFoundException("Unable to read class '" + name + "' from '" + resourceName + "'.");
        }
         
        return defineClass(name, classBytes, 0, totalBytesRead);
    }
 
    private String parseSimpleName(String name) 
    {
        int lastDot = name.lastIndexOf('.');
        if(lastDot < 0)
            return name;
         
        return name.substring(lastDot);
    }
 
    private String parsePackageName(String name) 
    {
        int lastDot = name.lastIndexOf('.');
        if(lastDot < 0)
            return null;
         
        return name.substring(0, lastDot);
    }
}
 

