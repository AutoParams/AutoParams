package test.autoparams;

public class InheritanceVisibilityTest {
    
    public static class BaseWithProtectedSetter {
        private String protectedValue;
        
        public String getProtectedValue() {
            return protectedValue;
        }
        
        protected void setProtectedValue(String protectedValue) {
            this.protectedValue = protectedValue;
        }
    }
    
    public static class DerivedFromProtectedSetter extends BaseWithProtectedSetter {
        private String derivedValue;
        
        public String getDerivedValue() {
            return derivedValue;
        }
        
        public void setDerivedValue(String derivedValue) {
            this.derivedValue = derivedValue;
        }
        
        // Make the inherited setter public
        @Override
        public void setProtectedValue(String protectedValue) {
            super.setProtectedValue(protectedValue);
        }
    }
    
    public static class BaseWithPackagePrivateSetter {
        private String packageValue;
        
        public String getPackageValue() {
            return packageValue;
        }
        
        void setPackageValue(String packageValue) {
            this.packageValue = packageValue;
        }
    }
    
    public static class DerivedFromPackagePrivateSetter extends BaseWithPackagePrivateSetter {
        private String derivedValue;
        
        public String getDerivedValue() {
            return derivedValue;
        }
        
        public void setDerivedValue(String derivedValue) {
            this.derivedValue = derivedValue;
        }
    }
}