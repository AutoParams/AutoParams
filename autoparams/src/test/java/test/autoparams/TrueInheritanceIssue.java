package test.autoparams;

public class TrueInheritanceIssue {
    
    public static class BaseWithProtectedSetter {
        private String protectedValue;
        
        public String getProtectedValue() {
            return protectedValue;
        }
        
        protected void setProtectedValue(String protectedValue) {
            this.protectedValue = protectedValue;
        }
    }
    
    public static class DerivedWithInheritedProtectedSetter extends BaseWithProtectedSetter {
        private String derivedValue;
        
        public String getDerivedValue() {
            return derivedValue;
        }
        
        public void setDerivedValue(String derivedValue) {
            this.derivedValue = derivedValue;
        }
        
        // Intentionally NOT overriding the protected setter
        // So it remains protected and should not be called by InstancePropertyWriter
    }
}