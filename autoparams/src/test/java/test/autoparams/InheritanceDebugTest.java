package test.autoparams;

import java.beans.PropertyDescriptor;
import java.beans.Introspector;
import java.beans.BeanInfo;
import java.lang.reflect.Method;

public class InheritanceDebugTest {
    
    public static class BaseClass {
        private String baseField;
        
        public String getBaseField() {
            System.out.println("BaseClass.getBaseField() called");
            return baseField;
        }
        
        public void setBaseField(String baseField) {
            System.out.println("BaseClass.setBaseField() called with: " + baseField);
            this.baseField = baseField;
        }
    }
    
    public static class DerivedClass extends BaseClass {
        private String derivedField;
        
        public String getDerivedField() {
            System.out.println("DerivedClass.getDerivedField() called");
            return derivedField;
        }
        
        public void setDerivedField(String derivedField) {
            System.out.println("DerivedClass.setDerivedField() called with: " + derivedField);
            this.derivedField = derivedField;
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Checking JavaBeans introspection ===");
        BeanInfo beanInfo = Introspector.getBeanInfo(DerivedClass.class);
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
        
        for (PropertyDescriptor prop : properties) {
            if (!prop.getName().equals("class")) {
                System.out.println("Property: " + prop.getName());
                Method writeMethod = prop.getWriteMethod();
                if (writeMethod != null) {
                    System.out.println("  Setter: " + writeMethod.getName() + " from " + writeMethod.getDeclaringClass().getSimpleName());
                }
            }
        }
        
        System.out.println("\n=== Testing direct setter calls ===");
        DerivedClass instance = new DerivedClass();
        
        // Call inherited setter directly
        instance.setBaseField("test-base");
        instance.setDerivedField("test-derived");
        
        System.out.println("Base field value: " + instance.getBaseField()); 
        System.out.println("Derived field value: " + instance.getDerivedField());
    }
}